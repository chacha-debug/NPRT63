package com.communityservice.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Brings an existing database up to the schema the code expects, so upgrading
 * the app never requires running SQL by hand. Every step checks first and only
 * changes what is missing, so it is safe to run on every startup and works on
 * both MySQL and MariaDB.
 */
public final class SchemaUpdater {

    private SchemaUpdater() {
    }

    public static void ensureUpToDate(Connection conn) {
        try {
            addColumnIfMissing(conn, "users", "is_deleted", "TINYINT(1) NOT NULL DEFAULT 0");
            addColumnIfMissing(conn, "users", "deleted_at", "DATETIME NULL");
            addColumnIfMissing(conn, "users", "deleted_by", "INT NULL");
            addColumnIfMissing(conn, "users", "deletion_reason", "VARCHAR(255) NULL");
            addColumnIfMissing(conn, "users", "verification_attempts", "INT NOT NULL DEFAULT 0");
            addColumnIfMissing(conn, "users", "reset_attempts", "INT NOT NULL DEFAULT 0");
            addColumnIfMissing(conn, "users", "verification_resend_available_at", "DATETIME NULL");
            addColumnIfMissing(conn, "users", "reset_resend_available_at", "DATETIME NULL");
            addColumnIfMissing(conn, "users", "admin_approval_status", "VARCHAR(20) NOT NULL DEFAULT 'APPROVED'");
            addColumnIfMissing(conn, "ngos", "operating_region", "VARCHAR(150) NULL");
            addColumnIfMissing(conn, "ngos", "org_scale", "VARCHAR(100) NULL");
            addColumnIfMissing(conn, "ngos", "mission_statement", "TEXT NULL");
            addColumnIfMissing(conn, "ngos", "focus_areas", "TEXT NULL");
            addColumnIfMissing(conn, "ngos", "preferred_skills", "TEXT NULL");
            addColumnIfMissing(conn, "volunteers", "race", "VARCHAR(50) NULL");
            addColumnIfMissing(conn, "volunteers", "gender", "VARCHAR(50) NULL");
        } catch (SQLException e) {
            AppLogger.error("Schema update failed for users table", e);
        }
        try {
            seedPolicy(conn, "Account Removal Policy",
                    "Administrators may deactivate an account temporarily (the user is blocked from signing in but "
                    + "the account and its data are kept) or remove it permanently (the account is closed and the "
                    + "user can no longer sign in). Where a reason is given, the user sees it the next time they "
                    + "try to sign in.");
            seedPolicy(conn, "Code of Conduct",
                    "Volunteers and NGOs are expected to communicate respectfully, honor confirmed commitments, and "
                    + "report attendance and hours accurately. Violations may result in a warning, temporary "
                    + "deactivation, or permanent removal from the platform at an administrator's discretion.");
        } catch (SQLException e) {
            AppLogger.warn("Could not seed default system policies: " + e.getMessage());
        }
    }

    private static void addColumnIfMissing(Connection conn, String table, String column, String definition)
            throws SQLException {
        String check = "SELECT COUNT(*) FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return;
                }
            }
        }
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
            AppLogger.info("Schema updated: added " + table + "." + column);
        }
    }

    private static void seedPolicy(Connection conn, String title, String content) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM system_policies WHERE policy_title = ?")) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return;
                }
            }
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO system_policies (policy_title, policy_content, version, effective_date, is_active) "
                + "VALUES (?, ?, '1.0', CURRENT_DATE, TRUE)")) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.executeUpdate();
        }
    }
}
