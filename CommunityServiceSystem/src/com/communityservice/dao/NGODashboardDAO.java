/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.utils.AppLogger;import com.communityservice.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class NGODashboardDAO {

    // Total number of events created by this ngos
    public static int getTotalEvents(int ngoUserID) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunities " +
                "WHERE ngo_user_id = ?";

        return getCount(sql, ngoUserID);
    }


    // Number of open events
    public static int getOpenEvents(int ngoUserID) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunities " +
                "WHERE ngo_user_id = ? AND status = 'Open'";

        return getCount(sql, ngoUserID);
    }


    // Total applications received for this ngos's events
    public static int getTotalApplications(int ngoUserID) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunity_applications oa " +
                "JOIN opportunities vo " +
                "ON oa.opportunity_id = vo.opportunity_id " +
                "WHERE vo.ngo_user_id = ?";

        return getCount(sql, ngoUserID);
    }


    // Pending applications
    public static int getPendingApplications(int ngoUserID) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunity_applications oa " +
                "JOIN opportunities vo " +
                "ON oa.opportunity_id = vo.opportunity_id " +
                "WHERE vo.ngo_user_id = ? " +
                "AND oa.status = 'Pending'";

        return getCount(sql, ngoUserID);
    }


    private static int getCount(String sql, int ngoUserID) {

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, ngoUserID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            AppLogger.error("Database operation failed in NGODashboardDAO", e);
        }

        return 0;
    }
    public static List<Object[]> getRecentEvents(int ngoUserID) {

    List<Object[]> events = new ArrayList<>();

    String sql = """
        SELECT opportunity_id, title, date, status
        FROM opportunities
        WHERE ngo_user_id = ?
        ORDER BY opportunity_id DESC
        LIMIT 3
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, ngoUserID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            events.add(new Object[]{
                rs.getInt("opportunity_id"),
                rs.getString("title"),
                rs.getDate("date"),
                rs.getString("status")
            });
        }

    } catch (SQLException e) {
        AppLogger.error("Database operation failed in NGODashboardDAO", e);
    }

    return events;
}
    public static List<Object[]> getRecentApplications(int ngoUserID) {

    List<Object[]> applications = new ArrayList<>();

    String sql = """
        SELECT
            oa.application_id,
            u.full_name,
            vo.title,
            oa.application_date,
            oa.status
        FROM opportunity_applications oa
        JOIN volunteers v
            ON oa.volunteer_id = v.volunteer_id
        JOIN users u
            ON v.user_id = u.user_id
        JOIN opportunities vo
            ON oa.opportunity_id = vo.opportunity_id
        WHERE vo.ngo_user_id = ?
        ORDER BY oa.application_date DESC
        LIMIT 3
        """;

    try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
    ) {

        ps.setInt(1, ngoUserID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            applications.add(
                    new Object[]{
                            rs.getInt("application_id"),
                            rs.getString("full_name"),
                            rs.getString("title"),
                            rs.getTimestamp("application_date"),
                            rs.getString("status")
                    }
            );
        }

    } catch (SQLException e) {
        AppLogger.error("Database operation failed in NGODashboardDAO", e);
    }

    return applications;
}
}