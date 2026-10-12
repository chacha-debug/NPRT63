package com.communityservice.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimal application logger. Writes timestamped messages to both the
 * console and a rolling log file (logs/app.log, relative to the working
 * directory), so problems can still be diagnosed after the app has left
 * a developer's machine — printStackTrace() alone only helps if someone
 * is watching the console at the exact moment something breaks.
 */
public final class AppLogger {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Path LOG_DIR = Paths.get("logs");
    private static final Path LOG_FILE = LOG_DIR.resolve("app.log");

    private AppLogger() {}

    public static void info(String message) {
        write("INFO", message, null);
    }

    public static void warn(String message) {
        write("WARN", message, null);
    }

    public static void error(String message, Throwable t) {
        write("ERROR", message, t);
    }

    public static void error(String message) {
        write("ERROR", message, null);
    }

    private static synchronized void write(String level, String message, Throwable t) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP);
        String line = "[" + timestamp + "] [" + level + "] " + message;

        if ("ERROR".equals(level)) {
            System.err.println(line);
            if (t != null) {
                t.printStackTrace(System.err);
            }
        } else {
            System.out.println(line);
        }

        StringBuilder fileEntry = new StringBuilder(line).append(System.lineSeparator());
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            fileEntry.append(sw);
        }

        try {
            Files.createDirectories(LOG_DIR);
            Files.writeString(LOG_FILE, fileEntry.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioException) {
            // Last resort: if we can't even write the log file, at least
            // don't let logging itself crash the application.
            System.err.println("[AppLogger] Could not write to log file: " + ioException.getMessage());
        }
    }
}
