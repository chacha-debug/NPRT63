/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.Certificate;

import java.sql.*;

public class CertificateDAO {

    public Certificate addCertificate(
            Certificate certificate) {

        String sql =
                "INSERT INTO certificates "
                + "(volunteer_id, opportunity_id, issue_date, pdf_url, "
                + "certification_number) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1, certificate.getVolunteerID());

            statement.setInt(
                    2, certificate.getOppID());

            if (certificate.getIssueDate() != null) {
                statement.setDate(
                        3,
                        Date.valueOf(
                                certificate.getIssueDate()));
            } else {
                statement.setNull(
                        3, Types.DATE);
            }

            statement.setString(
                    4, certificate.getPdfURL());

            statement.setString(
                    5,
                    certificate.getCertificationNumber());

            if (statement.executeUpdate() > 0) {

                ResultSet keys =
                        statement.getGeneratedKeys();

                if (keys.next()) {
                    certificate.setCertID(
                            keys.getInt(1));

                    return certificate;
                }
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error(
                    "Error adding certificate: "
                    + e.getMessage());
        }

        return null;
    }

    public Certificate getCertificateByID(
            int certificate_id) {

        String sql =
                "SELECT * FROM certificates "
                + "WHERE certificate_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, certificate_id);

            ResultSet rs =
                    statement.executeQuery();

            if (rs.next()) {

                Certificate certificate =
                        new Certificate();

                certificate.setCertID(
                        rs.getInt("certificate_id"));

                certificate.setVolunteerID(
                        rs.getInt("volunteer_id"));

                certificate.setOppID(
                        rs.getInt("opportunity_id"));

                if (rs.getDate("issue_date") != null) {
                    certificate.setIssueDate(
                            rs.getDate("issue_date")
                                    .toLocalDate());
                }

                certificate.setPdfURL(
                        rs.getString("pdf_url"));

                certificate.setCertificationNumber(
                        rs.getString(
                                "certification_number"));

                return certificate;
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error(
                    "Error finding certificate: "
                    + e.getMessage());
        }

        return null;
    }

    public boolean updateCertificate(
            Certificate certificate) {

        String sql =
                "UPDATE certificates SET "
                + "volunteer_id = ?, "
                + "opportunity_id = ?, "
                + "issue_date = ?, "
                + "pdf_url = ?, "
                + "certification_number = ? "
                + "WHERE certificate_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, certificate.getVolunteerID());

            statement.setInt(
                    2, certificate.getOppID());

            if (certificate.getIssueDate() != null) {
                statement.setDate(
                        3,
                        Date.valueOf(
                                certificate.getIssueDate()));
            } else {
                statement.setNull(
                        3, Types.DATE);
            }

            statement.setString(
                    4, certificate.getPdfURL());

            statement.setString(
                    5,
                    certificate.getCertificationNumber());

            statement.setInt(
                    6, certificate.getCertID());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error(
                    "Error updating certificate: "
                    + e.getMessage());

            return false;
        }
    }

    public Certificate getCertificateForVolunteerEvent(int volunteerId, int opportunityId) {
        String sql = "SELECT * FROM certificates WHERE volunteer_id = ? AND opportunity_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, volunteerId);
            statement.setInt(2, opportunityId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Certificate c = new Certificate();
                    c.setCertID(rs.getInt("certificate_id"));
                    c.setVolunteerID(rs.getInt("volunteer_id"));
                    c.setOppID(rs.getInt("opportunity_id"));
                    if (rs.getDate("issue_date") != null) c.setIssueDate(rs.getDate("issue_date").toLocalDate());
                    c.setPdfURL(rs.getString("pdf_url"));
                    c.setCertificationNumber(rs.getString("certification_number"));
                    return c;
                }
            }
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error finding volunteer certificate", e);
        }
        return null;
    }

    public java.util.List<Certificate> getCertificatesForVolunteer(int volunteerId) {
        java.util.List<Certificate> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM certificates WHERE volunteer_id = ? ORDER BY issue_date DESC, certificate_id DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, volunteerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Certificate c = new Certificate();
                    c.setCertID(rs.getInt("certificate_id"));
                    c.setVolunteerID(rs.getInt("volunteer_id"));
                    c.setOppID(rs.getInt("opportunity_id"));
                    if (rs.getDate("issue_date") != null) c.setIssueDate(rs.getDate("issue_date").toLocalDate());
                    c.setPdfURL(rs.getString("pdf_url"));
                    c.setCertificationNumber(rs.getString("certification_number"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error listing volunteer certificates", e);
        }
        return list;
    }

}
