package com.communityservice;

import com.communityservice.utils.AppLogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * Provides pooled connections to the MySQL database.
 *
 * Every DAO method already does {@code try (Connection conn = DatabaseConnection.getConnection()) { ... }},
 * which calls conn.close() automatically at the end of the block. Rather than
 * rewrite every one of those call sites, connections handed out here are
 * wrapped in a dynamic proxy: calling close() on the proxy returns the real
 * connection to the pool instead of physically closing it, so connections are
 * reused across calls instead of being opened and torn down every time.
 */
public class DatabaseConnection {

    // Credentials are read from environment variables so they never need to be
    // hardcoded or committed to source control. Fallback defaults below are only
    // for convenience during local development.
    //   DB_URL      e.g. jdbc:mysql://localhost:3306/community_service_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa%2FJohannesburg&autoReconnect=true
    //   DB_USERNAME e.g. root
    //   DB_PASSWORD e.g. your-mysql-password
    private static final String URL = getEnvOrDefault(
            "DB_URL", "jdbc:mysql://localhost:3306/community_service_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa%2FJohannesburg&autoReconnect=true");
    private static final String USERNAME = getEnvOrDefault("DB_USERNAME", "root");
    private static final String PASSWORD = getEnvOrDefault("DB_PASSWORD", "");

    private static final int POOL_SIZE = 5;
    private static final int BORROW_TIMEOUT_SECONDS = 10;

    private static final BlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
    /** Tracks the real (unwrapped) connections so the pool can be shut down cleanly. */
    private static final List<Connection> rawConnections = new ArrayList<>();
    private static volatile boolean initialized = false;

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key); // fallback: -Dkey=value in NetBeans VM Options
        }
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private static synchronized void initPool() {
        if (initialized) {
            return;
        }
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Class.forName("com.mysql.jdbc.Driver");
            }
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection real = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                // South Africa uses SAST (UTC+02:00) year-round. Pin every MySQL
                // session as well as the JDBC conversion zone so CURRENT_TIMESTAMP,
                // DATE_ADD and TIMESTAMP reads/writes agree with the application.
                try (java.sql.Statement tz = real.createStatement()) {
                    tz.execute("SET time_zone = '+02:00'");
                }
                rawConnections.add(real);
                pool.offer(wrap(real));
            }
            com.communityservice.utils.SchemaUpdater.ensureUpToDate(rawConnections.get(0));
            AppLogger.info("Database connection pool initialized with " + POOL_SIZE + " connections.");
            initialized = true;
        } catch (ClassNotFoundException e) {
            showError("MySQL Driver not found!\nPlease add mysql-connector-j jar to your project classpath.", "Driver Error");
            AppLogger.error("MySQL driver not found", e);
        } catch (SQLException e) {
            showError("Database connection failed: " + e.getMessage(), "Database Error");
            AppLogger.error("Failed to initialize database connection pool", e);
        }
    }

    /**
     * Borrows a connection from the pool. The caller should use it in a
     * try-with-resources block as usual — closing it returns it to the pool
     * rather than tearing down the underlying connection.
     */
    public static Connection getConnection() {
        if (!initialized) {
            initPool();
        }
        if (pool.isEmpty() && rawConnections.isEmpty()) {
            // initPool() failed entirely (e.g. DB unreachable) — nothing to hand out.
            return null;
        }
        try {
            Connection conn = pool.poll(BORROW_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (conn == null) {
                showError("All database connections are currently busy. Please try again.", "Database Busy");
                AppLogger.warn("Timed out waiting for a free pooled connection.");
                return null;
            }
            return conn;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /** Wraps a real connection so close() returns it to the pool instead of physically closing it. */
    private static Connection wrap(Connection real) {
        InvocationHandler handler = (proxy, method, args) -> {
            switch (method.getName()) {
                case "close":
                    // Only recycle genuinely healthy connections.
                    if (!real.isClosed()) {
                        pool.offer((Connection) proxy);
                    }
                    return null;
                case "isClosed":
                    return real.isClosed();
                default:
                    try {
                        return method.invoke(real, args);
                    } catch (InvocationTargetException ite) {
                        throw ite.getCause() != null ? ite.getCause() : ite;
                    }
            }
        };
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(), new Class<?>[]{Connection.class}, handler);
    }

    /** Physically closes every pooled connection. Call this once, on application shutdown. */
    public static synchronized void closeConnection() {
        pool.clear();
        for (Connection real : rawConnections) {
            try {
                if (!real.isClosed()) {
                    real.close();
                }
            } catch (SQLException e) {
                AppLogger.error("Failed to close a pooled database connection", e);
            }
        }
        rawConnections.clear();
        initialized = false;
        AppLogger.info("Database connection pool shut down.");
    }

    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        try {
            boolean valid = conn.isValid(2);
            conn.close(); // returns it to the pool
            return valid;
        } catch (SQLException e) {
            AppLogger.error("Connection test failed", e);
            return false;
        }
    }

    private static void showError(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
