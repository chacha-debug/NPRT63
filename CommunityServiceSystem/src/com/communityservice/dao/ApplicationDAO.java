/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.utils.AppLogger;import com.communityservice.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public static List<Object[]> getApplicationsForNGO(int ngoUserID) {

        List<Object[]> applications = new ArrayList<>();

        String sql =
                "SELECT "
                + "oa.application_id, "
                + "u.full_name, "
                + "vo.title, "
                + "oa.application_date, "
                + "oa.status "
                + "FROM opportunity_applications oa "
                + "JOIN volunteers v "
                + "ON oa.volunteer_id = v.volunteer_id "
                + "JOIN users u "
                + "ON v.user_id = u.user_id "
                + "JOIN opportunities vo "
                + "ON oa.opportunity_id = vo.opportunity_id "
                + "WHERE vo.ngo_user_id = ? "
                + "ORDER BY oa.application_date DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {

            statement.setInt(1, ngoUserID);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Object[] row = {
                        rs.getInt("application_id"),
                        rs.getString("full_name"),
                        rs.getString("title"),
                        rs.getTimestamp("application_date"),
                        rs.getString("status")
                };

                applications.add(row);
            }

        } catch (SQLException e) {

            AppLogger.error("Database operation failed in ApplicationDAO", e);
        }

        return applications;
    }
    public static boolean updateApplicationStatus(
        int application_id,
        String status,
        String rejection_reason,
        int reviewed_by
) {

    String sql =
            "UPDATE opportunity_applications "
            + "SET status = ?, "
            + "rejection_reason = ?, "
            + "reviewed_by = ?, "
            + "reviewed_date = CURRENT_TIMESTAMP "
            + "WHERE application_id = ?";

    try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    conn.prepareStatement(sql)
    ) {

        statement.setString(
                1,
                status
        );

        statement.setString(
                2,
                rejection_reason
        );

        statement.setInt(
                3,
                reviewed_by
        );

        statement.setInt(
                4,
                application_id
        );

        return statement.executeUpdate() > 0;

    } catch (SQLException e) {

        AppLogger.error("Database operation failed in ApplicationDAO", e);

        return false;
    }
}
}