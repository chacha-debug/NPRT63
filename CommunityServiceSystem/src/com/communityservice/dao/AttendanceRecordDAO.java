/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.AttendanceRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordDAO {

    // =========================================================
    // ADD ATTENDANCE RECORD
    // =========================================================

    public AttendanceRecord addAttendanceRecord(
            AttendanceRecord attendance) {

        if (attendance == null) {
            return null;
        }

        String sql =
                "INSERT INTO attendance_records "
                + "(volunteer_id, opportunity_id, check_in_time, status, "
                + "hours_awarded, reviewed_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    attendance.getVolunteerID()
            );

            statement.setInt(
                    2,
                    attendance.getOppID()
            );

            // Check-in time
            if (attendance.getCheckInTime() != null) {

                statement.setTimestamp(
                        3,
                        Timestamp.valueOf(
                                attendance.getCheckInTime()
                        )
                );

            } else {

                statement.setNull(
                        3,
                        Types.TIMESTAMP
                );
            }

            // Status
            statement.setString(
                    4,
                    attendance.getStatus()
            );

            // Hours awarded
            if (attendance.getHoursAwarded() != null) {

                statement.setBigDecimal(
                        5,
                        attendance.getHoursAwarded()
                );

            } else {

                statement.setBigDecimal(
                        5,
                        BigDecimal.ZERO
                );
            }

            // Reviewed by
            if (attendance.getReviewedBy() != null) {

                statement.setInt(
                        6,
                        attendance.getReviewedBy()
                );

            } else {

                statement.setNull(
                        6,
                        Types.INTEGER
                );
            }

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted > 0) {

                try (
                        ResultSet generatedKeys =
                                statement.getGeneratedKeys()
                ) {

                    if (generatedKeys.next()) {

                        attendance.setAttendanceID(
                                generatedKeys.getInt(1)
                        );

                        return attendance;
                    }
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding attendance record: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // GET ATTENDANCE RECORD BY ID
    // =========================================================

    public AttendanceRecord getAttendanceRecordByID(
            int attendance_id) {

        String sql =
                "SELECT * "
                + "FROM attendance_records "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    attendance_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToAttendanceRecord(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding attendance record: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // GET ATTENDANCE RECORDS BY VOLUNTEER
    // =========================================================

    public List<AttendanceRecord>
            getAttendanceRecordsByVolunteer(
                    int volunteer_id) {

        List<AttendanceRecord> records =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ? "
                + "ORDER BY check_in_time DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    records.add(
                            mapResultSetToAttendanceRecord(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting volunteers attendance: "
                    + e.getMessage()
            );
        }

        return records;
    }


    // =========================================================
    // GET ATTENDANCE BY VOLUNTEER
    // =========================================================
    // Alias method.
    // Some panels may call this name instead.
    // =========================================================

    public List<AttendanceRecord>
            getAttendanceByVolunteer(
                    int volunteer_id) {

        return getAttendanceRecordsByVolunteer(
                volunteer_id
        );
    }


    // =========================================================
    // GET ATTENDANCE RECORDS BY OPPORTUNITY
    // =========================================================

    public List<AttendanceRecord>
            getAttendanceRecordsByOpportunity(
                    int opportunity_id) {

        List<AttendanceRecord> records =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM attendance_records "
                + "WHERE opportunity_id = ? "
                + "ORDER BY check_in_time DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    opportunity_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    records.add(
                            mapResultSetToAttendanceRecord(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting opportunity attendance: "
                    + e.getMessage()
            );
        }

        return records;
    }


    // =========================================================
    // GET ATTENDANCE BY VOLUNTEER AND OPPORTUNITY
    // =========================================================

    public AttendanceRecord
            getAttendanceByVolunteerAndOpportunity(
                    int volunteer_id,
                    int opportunity_id) {

        String sql =
                "SELECT * "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ? "
                + "AND opportunity_id = ? "
                + "LIMIT 1";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            statement.setInt(
                    2,
                    opportunity_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToAttendanceRecord(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting attendance record: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // CHECK WHETHER ATTENDANCE RECORD EXISTS
    // =========================================================

    public boolean hasAttendanceRecord(
            int volunteer_id,
            int opportunity_id) {

        return getAttendanceByVolunteerAndOpportunity(
                volunteer_id,
                opportunity_id
        ) != null;
    }


    // =========================================================
    // GET TOTAL HOURS BY VOLUNTEER
    // =========================================================

    public BigDecimal getTotalHoursByVolunteer(
            int volunteer_id) {

        String sql =
                "SELECT COALESCE(SUM(hours_awarded), 0) "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    BigDecimal total_hours =
                            resultSet.getBigDecimal(1);

                    if (total_hours != null) {
                        return total_hours;
                    }
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting total volunteers hours: "
                    + e.getMessage()
            );
        }

        return BigDecimal.ZERO;
    }


    // =========================================================
    // GET COMPLETED EVENTS BY VOLUNTEER
    // =========================================================

    public int getCompletedEventsByVolunteer(
            int volunteer_id) {

        String sql =
                "SELECT COUNT(*) "
                + "FROM attendance_records "
                + "WHERE volunteer_id = ? "
                + "AND status = 'Completed'";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting completed events: "
                    + e.getMessage()
            );
        }

        return 0;
    }


    // =========================================================
    // UPDATE ATTENDANCE RECORD
    // =========================================================

    public boolean updateAttendanceRecord(
            AttendanceRecord attendance) {

        if (attendance == null) {
            return false;
        }

        String sql =
                "UPDATE attendance_records SET "
                + "volunteer_id = ?, "
                + "opportunity_id = ?, "
                + "check_in_time = ?, "
                + "status = ?, "
                + "hours_awarded = ?, "
                + "reviewed_by = ? "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    attendance.getVolunteerID()
            );

            statement.setInt(
                    2,
                    attendance.getOppID()
            );

            if (attendance.getCheckInTime() != null) {

                statement.setTimestamp(
                        3,
                        Timestamp.valueOf(
                                attendance.getCheckInTime()
                        )
                );

            } else {

                statement.setNull(
                        3,
                        Types.TIMESTAMP
                );
            }

            statement.setString(
                    4,
                    attendance.getStatus()
            );

            if (attendance.getHoursAwarded() != null) {

                statement.setBigDecimal(
                        5,
                        attendance.getHoursAwarded()
                );

            } else {

                statement.setBigDecimal(
                        5,
                        BigDecimal.ZERO
                );
            }

            if (attendance.getReviewedBy() != null) {

                statement.setInt(
                        6,
                        attendance.getReviewedBy()
                );

            } else {

                statement.setNull(
                        6,
                        Types.INTEGER
                );
            }

            statement.setInt(
                    7,
                    attendance.getAttendanceID()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating attendance record: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // UPDATE ATTENDANCE STATUS
    // =========================================================

    public boolean updateAttendanceStatus(
            int attendance_id,
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return false;
        }

        String sql =
                "UPDATE attendance_records SET "
                + "status = ? "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            statement.setInt(
                    2,
                    attendance_id
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating attendance status: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // UPDATE ATTENDANCE HOURS
    // =========================================================

    public boolean updateAttendanceHours(
            int attendance_id,
            BigDecimal hours_awarded) {

        String sql =
                "UPDATE attendance_records SET "
                + "hours_awarded = ? "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            if (hours_awarded != null) {

                statement.setBigDecimal(
                        1,
                        hours_awarded
                );

            } else {

                statement.setBigDecimal(
                        1,
                        BigDecimal.ZERO
                );
            }

            statement.setInt(
                    2,
                    attendance_id
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating attendance hours: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // UPDATE REVIEWER
    // =========================================================

    public boolean updateReviewedBy(
            int attendance_id,
            Integer reviewed_by) {

        String sql =
                "UPDATE attendance_records SET "
                + "reviewed_by = ? "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            if (reviewed_by != null) {

                statement.setInt(
                        1,
                        reviewed_by
                );

            } else {

                statement.setNull(
                        1,
                        Types.INTEGER
                );
            }

            statement.setInt(
                    2,
                    attendance_id
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating attendance reviewer: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // DELETE ATTENDANCE RECORD
    // =========================================================

    public boolean deleteAttendanceRecord(
            int attendance_id) {

        String sql =
                "DELETE FROM attendance_records "
                + "WHERE attendance_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    attendance_id
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting attendance record: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // MAP RESULT SET TO ATTENDANCE MODEL
    // =========================================================

    private AttendanceRecord
            mapResultSetToAttendanceRecord(
                    ResultSet resultSet)
            throws SQLException {

        AttendanceRecord attendance =
                new AttendanceRecord();

        attendance.setAttendanceID(
                resultSet.getInt(
                        "attendance_id"
                )
        );

        attendance.setVolunteerID(
                resultSet.getInt(
                        "volunteer_id"
                )
        );

        attendance.setOppID(
                resultSet.getInt(
                        "opportunity_id"
                )
        );

        Timestamp check_in_time =
                resultSet.getTimestamp(
                        "check_in_time"
                );

        if (check_in_time != null) {

            attendance.setCheckInTime(
                    check_in_time.toLocalDateTime()
            );
        }

        attendance.setStatus(
                resultSet.getString(
                        "status"
                )
        );

        attendance.setHoursAwarded(
                resultSet.getBigDecimal(
                        "hours_awarded"
                )
        );

        int reviewed_by =
                resultSet.getInt(
                        "reviewed_by"
                );

        if (!resultSet.wasNull()) {

            attendance.setReviewedBy(
                    reviewed_by
            );
        }

        return attendance;
    }
}