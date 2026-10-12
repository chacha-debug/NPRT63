/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.Report;

import java.sql.*;

public class ReportDAO {

    public boolean addReport(Report reports) {

        String sql = "INSERT INTO reports "
                   + "(generated_by, report_type, title, "
                   + "start_date, end_date, file_format, "
                   + "file_url, data_summary) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            if (reports.getGeneratedBy() > 0) {

                statement.setInt(
                        1,
                        reports.getGeneratedBy()
                );

            } else {

                statement.setNull(1, Types.INTEGER);
            }

            statement.setString(
                    2,
                    reports.getReportType()
            );

            statement.setString(
                    3,
                    reports.getTitle()
            );

            if (reports.getStartDate() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                reports.getStartDate()
                        )
                );

            } else {

                statement.setNull(4, Types.DATE);
            }

            if (reports.getEndDate() != null) {

                statement.setDate(
                        5,
                        Date.valueOf(
                                reports.getEndDate()
                        )
                );

            } else {

                statement.setNull(5, Types.DATE);
            }

            statement.setString(
                    6,
                    reports.getFileFormat()
            );

            statement.setString(
                    7,
                    reports.getFileURL()
            );

            statement.setString(
                    8,
                    reports.getDataSummary()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                try (ResultSet keys =
                             statement.getGeneratedKeys()) {

                    if (keys.next()) {

                        reports.setReportID(
                                keys.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding reports: "
                    + e.getMessage()
            );
        }

        return false;
    }

    public java.util.List<Report> getAllReports() {

        java.util.List<Report> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY generated_date DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Report reports = new Report();
                reports.setReportID(rs.getInt("report_id"));
                reports.setGeneratedBy(rs.getInt("generated_by"));
                reports.setReportType(rs.getString("report_type"));
                reports.setTitle(rs.getString("title"));

                Timestamp timestamp = rs.getTimestamp("generated_date");
                if (timestamp != null) {
                    reports.setGeneratedDate(timestamp.toLocalDateTime());
                }

                Date start_date = rs.getDate("start_date");
                if (start_date != null) {
                    reports.setStartDate(start_date.toLocalDate());
                }

                Date end_date = rs.getDate("end_date");
                if (end_date != null) {
                    reports.setEndDate(end_date.toLocalDate());
                }

                reports.setFileFormat(rs.getString("file_format"));
                reports.setFileURL(rs.getString("file_url"));
                reports.setDataSummary(rs.getString("data_summary"));
                list.add(reports);
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error listing reports: " + e.getMessage());
        }

        return list;
    }

    public boolean deleteReport(int report_id) {

        String sql = "DELETE FROM reports WHERE report_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, report_id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error deleting report: " + e.getMessage());
        }

        return false;
    }

    public Report getReportByID(int report_id) {

        String sql = "SELECT * FROM reports "
                   + "WHERE report_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, report_id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                Report reports = new Report();

                reports.setReportID(
                        rs.getInt("report_id")
                );

                reports.setGeneratedBy(
                        rs.getInt("generated_by")
                );

                reports.setReportType(
                        rs.getString("report_type")
                );

                reports.setTitle(
                        rs.getString("title")
                );

                Timestamp timestamp =
                        rs.getTimestamp("generated_date");

                if (timestamp != null) {

                    reports.setGeneratedDate(
                            timestamp.toLocalDateTime()
                    );
                }

                Date start_date =
                        rs.getDate("start_date");

                if (start_date != null) {

                    reports.setStartDate(
                            start_date.toLocalDate()
                    );
                }

                Date end_date =
                        rs.getDate("end_date");

                if (end_date != null) {

                    reports.setEndDate(
                            end_date.toLocalDate()
                    );
                }

                reports.setFileFormat(
                        rs.getString("file_format")
                );

                reports.setFileURL(
                        rs.getString("file_url")
                );

                reports.setDataSummary(
                        rs.getString("data_summary")
                );

                return reports;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding reports: "
                    + e.getMessage()
            );
        }

        return null;
    }

    public boolean updateReport(Report reports) {

        String sql = "UPDATE reports SET "
                   + "generated_by = ?, "
                   + "report_type = ?, "
                   + "title = ?, "
                   + "start_date = ?, "
                   + "end_date = ?, "
                   + "file_format = ?, "
                   + "file_url = ?, "
                   + "data_summary = ? "
                   + "WHERE report_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (reports.getGeneratedBy() > 0) {

                statement.setInt(
                        1,
                        reports.getGeneratedBy()
                );

            } else {

                statement.setNull(1, Types.INTEGER);
            }

            statement.setString(
                    2,
                    reports.getReportType()
            );

            statement.setString(
                    3,
                    reports.getTitle()
            );

            if (reports.getStartDate() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                reports.getStartDate()
                        )
                );

            } else {

                statement.setNull(4, Types.DATE);
            }

            if (reports.getEndDate() != null) {

                statement.setDate(
                        5,
                        Date.valueOf(
                                reports.getEndDate()
                        )
                );

            } else {

                statement.setNull(5, Types.DATE);
            }

            statement.setString(
                    6,
                    reports.getFileFormat()
            );

            statement.setString(
                    7,
                    reports.getFileURL()
            );

            statement.setString(
                    8,
                    reports.getDataSummary()
            );

            statement.setInt(
                    9,
                    reports.getReportID()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating reports: "
                    + e.getMessage()
            );
        }

        return false;
    }
}
