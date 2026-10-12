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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // =========================================================
    // LOAD ngos EVENTS
    // =========================================================

    public static List<Object[]> getEventsForNGO(int ngoUserID) {

        List<Object[]> events = new ArrayList<>();

        String sql =
                "SELECT opportunity_id, title, date, status "
                + "FROM opportunities "
                + "WHERE ngo_user_id = ? "
                + "ORDER BY date DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, ngoUserID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                events.add(
                        new Object[]{
                                rs.getInt("opportunity_id"),
                                rs.getString("title"),
                                rs.getDate("date"),
                                rs.getString("status")
                        }
                );
            }

        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AttendanceDAO", e);
        }

        return events;
    }


    // =========================================================
    // LOAD APPROVED VOLUNTEERS FOR EVENT
    // =========================================================

    public static List<Object[]> getApprovedVolunteersForEvent(
            int ngoUserID,
            int opportunity_id
    ) {

        List<Object[]> volunteers = new ArrayList<>();

        String sql =
                "SELECT "
                + "oa.application_id, "
                + "v.volunteer_id, "
                + "u.full_name, "
                + "vo.title, "
                + "oa.status "
                + "FROM opportunity_applications oa "
                + "JOIN volunteers v "
                + "ON oa.volunteer_id = v.volunteer_id "
                + "JOIN users u "
                + "ON v.user_id = u.user_id "
                + "JOIN opportunities vo "
                + "ON oa.opportunity_id = vo.opportunity_id "
                + "WHERE vo.ngo_user_id = ? "
                + "AND vo.opportunity_id = ? "
                + "AND oa.status = 'Approved' "
                + "ORDER BY u.full_name";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, ngoUserID);
            ps.setInt(2, opportunity_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                volunteers.add(
                        new Object[]{
                                rs.getInt("application_id"),
                                rs.getInt("volunteer_id"),
                                rs.getString("full_name"),
                                rs.getString("title"),
                                rs.getString("status")
                        }
                );
            }

        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AttendanceDAO", e);
        }

        return volunteers;
    }


    // =========================================================
    // CHECK IN volunteers
    // =========================================================

    public static boolean checkInVolunteer(
            int volunteer_id,
            int opportunity_id,
            int reviewed_by
    ) {

        String checkSql =
                "SELECT attendance_id "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ? "
                + "AND opportunity_id = ?";

        String insertSql =
                "INSERT INTO attendance_records "
                + "(volunteer_id, opportunity_id, check_in_time, status, hours_awarded, reviewed_by) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP, 'Present', 0, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection()
        ) {

            try (
                    PreparedStatement checkPs =
                            conn.prepareStatement(checkSql)
            ) {

                checkPs.setInt(1, volunteer_id);
                checkPs.setInt(2, opportunity_id);

                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    return false;
                }
            }

            try (
                    PreparedStatement insertPs =
                            conn.prepareStatement(insertSql)
            ) {

                insertPs.setInt(1, volunteer_id);
                insertPs.setInt(2, opportunity_id);
                insertPs.setInt(3, reviewed_by);

                return insertPs.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AttendanceDAO", e);
        }

        return false;
    }


    // =========================================================
    // GET CHECK-IN RECORD
    // =========================================================

    public static Object[] getAttendanceRecord(
            int volunteer_id,
            int opportunity_id
    ) {

        String sql =
                "SELECT attendance_id, check_in_time, status, hours_awarded "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ? "
                + "AND opportunity_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, volunteer_id);
            ps.setInt(2, opportunity_id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Object[]{
                        rs.getInt("attendance_id"),
                        rs.getTimestamp("check_in_time"),
                        rs.getString("status"),
                        rs.getDouble("hours_awarded")
                };
            }

        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AttendanceDAO", e);
        }

        return null;
    }



    public static double getEventDurationHours(int ngoUserID, int opportunityId) {
        String sql = "SELECT start_time, end_time FROM opportunities WHERE opportunity_id = ? AND ngo_user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, opportunityId);
            ps.setInt(2, ngoUserID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getTime("start_time") != null && rs.getTime("end_time") != null) {
                    long minutes = java.time.Duration.between(
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime()).toMinutes();
                    if (minutes < 0) minutes += 24 * 60;
                    return minutes / 60.0;
                }
            }
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Failed to fetch event duration", e);
        }
        return 0;
    }

    // =========================================================
    // COMPLETE ATTENDANCE / AWARD HOURS
    // =========================================================

    public static boolean completeAttendance(
            int volunteer_id,
            int opportunity_id,
            double hours_awarded,
            int reviewed_by
    ) {

        String attendanceSql =
                "UPDATE attendance_records "
                + "SET status = 'Completed', "
                + "hours_awarded = ?, "
                + "reviewed_by = ? "
                + "WHERE volunteer_id = ? "
                + "AND opportunity_id = ?";

        String volunteerSql =
                "UPDATE volunteers "
                + "SET total_hours = total_hours + ?, "
                + "completed_events_count = completed_events_count + 1 "
                + "WHERE volunteer_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection()
        ) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement attendancePs =
                            conn.prepareStatement(attendanceSql);

                    PreparedStatement volunteerPs =
                            conn.prepareStatement(volunteerSql)
            ) {

                attendancePs.setDouble(1, hours_awarded);
                attendancePs.setInt(2, reviewed_by);
                attendancePs.setInt(3, volunteer_id);
                attendancePs.setInt(4, opportunity_id);

                int attendanceUpdated =
                        attendancePs.executeUpdate();

                if (attendanceUpdated == 0) {

                    conn.rollback();
                    return false;
                }

                volunteerPs.setDouble(1, hours_awarded);
                volunteerPs.setInt(2, volunteer_id);

                int volunteerUpdated =
                        volunteerPs.executeUpdate();

                if (volunteerUpdated == 0) {

                    conn.rollback();
                    return false;
                }

                conn.commit();

                return true;

            } catch (SQLException e) {

                conn.rollback();

                throw e;
            }

        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AttendanceDAO", e);
        }

        return false;
    }
}