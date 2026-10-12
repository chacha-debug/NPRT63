/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.AuditLog;

import java.sql.*;

public class AuditLogDAO {

    public boolean addAuditLog(AuditLog log) {

        String sql = "INSERT INTO audit_logs "
                   + "(user_id, action, entity_type, entity_id, "
                   + "ip_address, details, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, log.getUserID());
            statement.setString(2, log.getAction());
            statement.setString(3, log.getEntityType());
            statement.setInt(4, log.getEntityID());
            statement.setString(5, log.getIpAddress());
            statement.setString(6, log.getDetails());
            statement.setString(7, log.getStatus());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                try (ResultSet keys =
                             statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        log.setLogID(keys.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding audit log: "
                    + e.getMessage()
            );
        }

        return false;
    }

    public java.util.List<AuditLog> getAllAuditLogs() {

        java.util.List<AuditLog> logs = new java.util.ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY `timestamp` DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setLogID(rs.getInt("audit_log_id"));
                log.setUserID(rs.getInt("user_id"));
                log.setAction(rs.getString("action"));
                log.setEntityType(rs.getString("entity_type"));
                log.setEntityID(rs.getInt("entity_id"));
                log.setIpAddress(rs.getString("ip_address"));

                Timestamp timestamp = rs.getTimestamp("timestamp");
                if (timestamp != null) {
                    log.setTimestamp(timestamp.toLocalDateTime());
                }

                log.setDetails(rs.getString("details"));
                log.setStatus(rs.getString("status"));
                logs.add(log);
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error listing audit logs: " + e.getMessage());
        }

        return logs;
    }

    public AuditLog getAuditLogByID(int audit_log_id) {

        String sql = "SELECT * FROM audit_logs "
                   + "WHERE audit_log_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, audit_log_id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                AuditLog log = new AuditLog();

                log.setLogID(rs.getInt("audit_log_id"));
                log.setUserID(rs.getInt("user_id"));
                log.setAction(rs.getString("action"));
                log.setEntityType(rs.getString("entity_type"));
                log.setEntityID(rs.getInt("entity_id"));
                log.setIpAddress(rs.getString("ip_address"));

                Timestamp timestamp =
                        rs.getTimestamp("timestamp");

                if (timestamp != null) {

                    log.setTimestamp(
                            timestamp.toLocalDateTime()
                    );
                }

                log.setDetails(rs.getString("details"));
                log.setStatus(rs.getString("status"));

                return log;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding audit log: "
                    + e.getMessage()
            );
        }

        return null;
    }

    public boolean updateAuditLog(AuditLog log) {

        String sql = "UPDATE audit_logs SET "
                   + "action = ?, "
                   + "entity_type = ?, "
                   + "entity_id = ?, "
                   + "ip_address = ?, "
                   + "details = ?, "
                   + "status = ? "
                   + "WHERE audit_log_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, log.getAction());
            statement.setString(2, log.getEntityType());
            statement.setInt(3, log.getEntityID());
            statement.setString(4, log.getIpAddress());
            statement.setString(5, log.getDetails());
            statement.setString(6, log.getStatus());
            statement.setInt(7, log.getLogID());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating audit log: "
                    + e.getMessage()
            );
        }

        return false;
    }

    public boolean deleteAuditLog(int audit_log_id) {

        String sql = "DELETE FROM audit_logs "
                   + "WHERE audit_log_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, audit_log_id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting audit log: "
                    + e.getMessage()
            );
        }

        return false;
    }
}
