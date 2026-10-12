/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// File: ui/ReportsPanel.java
package com.communityservice.ui;

import com.communityservice.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.nio.file.Path;
import java.util.Arrays;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.communityservice.utils.ExportUtil;

public class ReportsPanel extends JPanel {
    private final int user_id;
    private JLabel totalApplications, approved, pending, rejected, total_hours;

    public ReportsPanel(int user_id) {
        this.user_id = user_id;
        initialize();
        loadReports();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 15, 15));
        cards.setBackground(UIColors.BACKGROUND);
        cards.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        totalApplications = new JLabel("0");
        approved = new JLabel("0");
        pending = new JLabel("0");
        rejected = new JLabel("0");
        total_hours = new JLabel("0");

        cards.add(createCard("Total Applications", totalApplications, UIColors.STAT_BLUE));
        cards.add(createCard("Approved", approved, UIColors.GREEN));
        cards.add(createCard("Pending", pending, UIColors.ORANGE));
        cards.add(createCard("Rejected", rejected, UIColors.RED));
        cards.add(createCard("Total Hours", total_hours, UIColors.PURPLE));

        add(cards, BorderLayout.CENTER);

        JPanel exportBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        exportBar.setOpaque(false);
        JButton csvButton = new JButton("Export CSV");
        JButton pdfButton = new JButton("Export PDF");
        csvButton.addActionListener(e -> exportSummary(false));
        pdfButton.addActionListener(e -> exportSummary(true));
        exportBar.add(csvButton);
        exportBar.add(pdfButton);
        add(exportBar, BorderLayout.SOUTH);
    }

    private void exportSummary(boolean pdf) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(pdf ? "Save Report PDF" : "Save Report CSV");
        chooser.setSelectedFile(new java.io.File("community-service-report." + (pdf ? "pdf" : "csv")));
        chooser.setFileFilter(new FileNameExtensionFilter(pdf ? "PDF files" : "CSV files", pdf ? "pdf" : "csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            Path path = chooser.getSelectedFile().toPath();
            if (pdf) {
                ExportUtil.writeTextPdf(path, "Community Service+ Volunteer Report",
                        Arrays.asList(
                                "Total Applications: " + totalApplications.getText(),
                                "Approved Applications: " + approved.getText(),
                                "Pending Applications: " + pending.getText(),
                                "Rejected Applications: " + rejected.getText(),
                                "Total Hours: " + total_hours.getText()
                        ));
            } else {
                ExportUtil.writeCsv(path, Arrays.asList(
                        new String[]{"Metric", "Value"},
                        new String[]{"Total Applications", totalApplications.getText()},
                        new String[]{"Approved", approved.getText()},
                        new String[]{"Pending", pending.getText()},
                        new String[]{"Rejected", rejected.getText()},
                        new String[]{"Total Hours", total_hours.getText()}
                ));
            }
            JOptionPane.showMessageDialog(this, "Report exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            com.communityservice.utils.AppLogger.error("Report export failed", ex);
            JOptionPane.showMessageDialog(this, "The report could not be exported. Please check the selected location and try again.",
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCard(String title, JLabel value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel(title);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));

        value.setForeground(Color.WHITE);
        value.setFont(new Font("Segoe UI", Font.BOLD, 30));
        value.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private void loadReports() {
        String sql = "SELECT COUNT(*) AS total, " +
                     "SUM(CASE WHEN status='Approved' THEN 1 ELSE 0 END) AS approved, " +
                     "SUM(CASE WHEN status='Pending' THEN 1 ELSE 0 END) AS pending, " +
                     "SUM(CASE WHEN status='Rejected' THEN 1 ELSE 0 END) AS rejected " +
                     "FROM opportunity_applications a JOIN volunteers v ON a.volunteer_id = v.volunteer_id WHERE v.user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                totalApplications.setText(String.valueOf(result.getInt("total")));
                approved.setText(String.valueOf(result.getInt("approved")));
                pending.setText(String.valueOf(result.getInt("pending")));
                rejected.setText(String.valueOf(result.getInt("rejected")));
            }
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Report query failed", e);
            JOptionPane.showMessageDialog(this, "Reports could not be loaded. Please check the database connection.",
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }

        String hoursSQL = "SELECT COALESCE(SUM(a.hours_awarded),0) FROM attendance_records a JOIN volunteers v ON a.volunteer_id = v.volunteer_id WHERE v.user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(hoursSQL)) {
            statement.setInt(1, user_id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                total_hours.setText(result.getBigDecimal(1).toString());
            }
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Hours report query failed", e);
        }
    }
}