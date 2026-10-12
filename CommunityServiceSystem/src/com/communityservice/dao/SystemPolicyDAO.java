/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.SystemPolicy;

import java.sql.*;

public class SystemPolicyDAO {

    public boolean addSystemPolicy(SystemPolicy policy) {

        String sql = "INSERT INTO system_policies "
                   + "(policy_title, policy_content, version, "
                   + "effective_date, last_updated_by, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    policy.getPolicyTitle()
            );

            statement.setString(
                    2,
                    policy.getPolicyContent()
            );

            statement.setString(
                    3,
                    policy.getVersion()
            );

            if (policy.getEffectiveDate() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                policy.getEffectiveDate()
                        )
                );

            } else {

                statement.setNull(4, Types.DATE);
            }

            if (policy.getLastUpdatedBy() > 0) {

                statement.setInt(
                        5,
                        policy.getLastUpdatedBy()
                );

            } else {

                statement.setNull(5, Types.INTEGER);
            }

            statement.setBoolean(
                    6,
                    policy.is_active()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                try (ResultSet keys =
                             statement.getGeneratedKeys()) {

                    if (keys.next()) {

                        policy.setPolicyID(
                                keys.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding system policy: "
                    + e.getMessage()
            );
        }

        return false;
    }

    public java.util.List<SystemPolicy> getAllSystemPolicies() {

        java.util.List<SystemPolicy> policies = new java.util.ArrayList<>();
        String sql = "SELECT * FROM system_policies ORDER BY system_policy_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                SystemPolicy policy = new SystemPolicy();
                policy.setPolicyID(rs.getInt("system_policy_id"));
                policy.setPolicyTitle(rs.getString("policy_title"));
                policy.setPolicyContent(rs.getString("policy_content"));
                policy.setVersion(rs.getString("version"));

                Date effective_date = rs.getDate("effective_date");
                if (effective_date != null) {
                    policy.setEffectiveDate(effective_date.toLocalDate());
                }

                policy.setLastUpdatedBy(rs.getInt("last_updated_by"));

                Timestamp timestamp = rs.getTimestamp("last_update_date");
                if (timestamp != null) {
                    policy.setLastUpdateDate(timestamp.toLocalDateTime());
                }

                policy.setActive(rs.getBoolean("is_active"));
                policies.add(policy);
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error listing system policies: " + e.getMessage());
        }

        return policies;
    }

    public SystemPolicy getSystemPolicyByID(int system_policy_id) {

        String sql = "SELECT * FROM system_policies "
                   + "WHERE system_policy_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, system_policy_id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                SystemPolicy policy =
                        new SystemPolicy();

                policy.setPolicyID(
                        rs.getInt("system_policy_id")
                );

                policy.setPolicyTitle(
                        rs.getString("policy_title")
                );

                policy.setPolicyContent(
                        rs.getString("policy_content")
                );

                policy.setVersion(
                        rs.getString("version")
                );

                Date effective_date =
                        rs.getDate("effective_date");

                if (effective_date != null) {

                    policy.setEffectiveDate(
                            effective_date.toLocalDate()
                    );
                }

                policy.setLastUpdatedBy(
                        rs.getInt("last_updated_by")
                );

                Timestamp timestamp =
                        rs.getTimestamp("last_update_date");

                if (timestamp != null) {

                    policy.setLastUpdateDate(
                            timestamp.toLocalDateTime()
                    );
                }

                policy.setActive(
                        rs.getBoolean("is_active")
                );

                return policy;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding system policy: "
                    + e.getMessage()
            );
        }

        return null;
    }

    public boolean updateSystemPolicy(
            SystemPolicy policy) {

        String sql = "UPDATE system_policies SET "
                   + "policy_title = ?, "
                   + "policy_content = ?, "
                   + "version = ?, "
                   + "effective_date = ?, "
                   + "last_updated_by = ?, "
                   + "is_active = ? "
                   + "WHERE system_policy_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    policy.getPolicyTitle()
            );

            statement.setString(
                    2,
                    policy.getPolicyContent()
            );

            statement.setString(
                    3,
                    policy.getVersion()
            );

            if (policy.getEffectiveDate() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                policy.getEffectiveDate()
                        )
                );

            } else {

                statement.setNull(4, Types.DATE);
            }

            if (policy.getLastUpdatedBy() > 0) {

                statement.setInt(
                        5,
                        policy.getLastUpdatedBy()
                );

            } else {

                statement.setNull(5, Types.INTEGER);
            }

            statement.setBoolean(
                    6,
                    policy.is_active()
            );

            statement.setInt(
                    7,
                    policy.getPolicyID()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating system policy: "
                    + e.getMessage()
            );
        }

        return false;
    }
}