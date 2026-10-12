package com.communityservice.utils;

import com.communityservice.DatabaseConnection;
import com.communityservice.dao.CertificateDAO;
import com.communityservice.models.Certificate;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

/**
 * Creates a certificate and its actual PDF file after completed attendance.
 */
public final class CertificateService {
    private CertificateService() {}

    public static Certificate generateForCompletedAttendance(int volunteerId, int opportunityId) {
        CertificateDAO dao = new CertificateDAO();
        Certificate existing = dao.getCertificateForVolunteerEvent(volunteerId, opportunityId);
        if (existing != null) return existing;

        String name = null;
        String event = null;
        double hours = 0;
        java.sql.Date eventDate = null;
        String sql = "SELECT u.full_name, o.title, o.date, ar.hours_awarded "
                + "FROM attendance_records ar "
                + "JOIN volunteers v ON ar.volunteer_id = v.volunteer_id "
                + "JOIN users u ON v.user_id = u.user_id "
                + "JOIN opportunities o ON ar.opportunity_id = o.opportunity_id "
                + "WHERE ar.volunteer_id = ? AND ar.opportunity_id = ? AND ar.status = 'Completed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ps.setInt(2, opportunityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                name = rs.getString("full_name");
                event = rs.getString("title");
                eventDate = rs.getDate("date");
                hours = rs.getDouble("hours_awarded");
            }
        } catch (Exception e) {
            AppLogger.error("Could not prepare certificate data", e);
            return null;
        }

        String number = "CS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Path directory = Paths.get(System.getProperty("user.home"), "CommunityServicePlus", "certificates");
        Path pdf = directory.resolve(number + ".pdf");
        try {
            ExportUtil.writeTextPdf(pdf, "COMMUNITY SERVICE+ CERTIFICATE",
                    Arrays.asList(
                            "Certificate No: " + number,
                            "This certificate is proudly presented to",
                            name,
                            "",
                            "For successfully completing the community service event:",
                            event,
                            "Event date: " + (eventDate == null ? "N/A" : eventDate.toLocalDate()),
                            String.format("Volunteer hours awarded: %.2f", hours),
                            "",
                            "Community Service+"
                    ));
        } catch (Exception e) {
            AppLogger.error("Could not generate certificate PDF", e);
            return null;
        }

        Certificate certificate = new Certificate();
        certificate.setVolunteerID(volunteerId);
        certificate.setOppID(opportunityId);
        certificate.setIssueDate(LocalDate.now());
        certificate.setPdfURL(pdf.toAbsolutePath().toString());
        certificate.setCertificationNumber(number);
        return dao.addCertificate(certificate);
    }
    /** Recreates a certificate PDF locally when its stored path came from another computer or was removed. */
    public static Path rebuildPdf(Certificate certificate) {
        if (certificate == null) return null;
        String name = null;
        String event = null;
        double hours = 0;
        java.sql.Date eventDate = null;
        String sql = "SELECT u.full_name, o.title, o.date, ar.hours_awarded "
                + "FROM attendance_records ar "
                + "JOIN volunteers v ON ar.volunteer_id = v.volunteer_id "
                + "JOIN users u ON v.user_id = u.user_id "
                + "JOIN opportunities o ON ar.opportunity_id = o.opportunity_id "
                + "WHERE ar.volunteer_id = ? AND ar.opportunity_id = ? AND ar.status = 'Completed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, certificate.getVolunteerID());
            ps.setInt(2, certificate.getOppID());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                name = rs.getString("full_name");
                event = rs.getString("title");
                eventDate = rs.getDate("date");
                hours = rs.getDouble("hours_awarded");
            }
        } catch (Exception e) { AppLogger.error("Could not rebuild certificate data", e); return null; }

        String number = certificate.getCertificationNumber();
        if (number == null || number.isBlank()) number = "CS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Path directory = Paths.get(System.getProperty("user.home"), "CommunityServicePlus", "certificates");
        Path pdf = directory.resolve(number + ".pdf");
        try {
            ExportUtil.writeTextPdf(pdf, "COMMUNITY SERVICE+ CERTIFICATE", Arrays.asList(
                    "Certificate No: " + number, "This certificate is proudly presented to", name, "",
                    "For successfully completing the community service event:", event,
                    "Event date: " + (eventDate == null ? "N/A" : eventDate.toLocalDate()),
                    String.format("Volunteer hours awarded: %.2f", hours), "", "Community Service+"));
            certificate.setPdfURL(pdf.toAbsolutePath().toString());
            certificate.setCertificationNumber(number);
            new CertificateDAO().updateCertificate(certificate);
            return pdf;
        } catch (Exception e) { AppLogger.error("Could not rebuild certificate PDF", e); return null; }
    }

}
