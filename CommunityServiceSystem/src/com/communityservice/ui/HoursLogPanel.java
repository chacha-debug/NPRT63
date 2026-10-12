/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// File: ui/HoursLogPanel.java
package com.communityservice.ui;

import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.models.PortalVolunteer;
import com.communityservice.models.AttendanceRecord;
import com.communityservice.dao.AttendanceRecordDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class HoursLogPanel extends JPanel {
    private final int user_id;
    private final VolunteerPortalVolunteerDAO volunteerDAO;
    private final AttendanceRecordDAO attendanceDAO;
    private JLabel totalLabel, approvedLabel, pendingLabel;
    private JPanel recordsPanel;

    public HoursLogPanel(int user_id) {
        this.user_id = user_id;
        volunteerDAO = new VolunteerPortalVolunteerDAO();
        attendanceDAO = new AttendanceRecordDAO();
        initialize();
        loadHours();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Hours Log");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(UIColors.BACKGROUND);
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        main.add(createSummary(), BorderLayout.NORTH);

        recordsPanel = new JPanel();
        recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));
        recordsPanel.setBackground(UIColors.BACKGROUND);

        JScrollPane scroll = new JScrollPane(recordsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIColors.BACKGROUND);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private JPanel createSummary() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(UIColors.BACKGROUND);

        totalLabel = new JLabel("0");
        approvedLabel = new JLabel("0");
        pendingLabel = new JLabel("0");

        panel.add(createStatCard("Total Hours", totalLabel, UIColors.STAT_BLUE));
        panel.add(createStatCard("Approved Records", approvedLabel, UIColors.GREEN));
        panel.add(createStatCard("Pending Records", pendingLabel, UIColors.ORANGE));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(title);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 13));

        value.setForeground(Color.WHITE);
        value.setFont(new Font("Segoe UI", Font.BOLD, 24));
        value.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }

    private void loadHours() {
        PortalVolunteer volunteers = volunteerDAO.getVolunteerByUserID(user_id);
        if (volunteers == null) return;

        List<AttendanceRecord> records = attendanceDAO.getAttendanceByVolunteer(volunteers.getVolunteerID());
        recordsPanel.removeAll();

        BigDecimal total = BigDecimal.ZERO;
        int approved = 0, pending = 0;

        for (AttendanceRecord record : records) {
            if (record.getHoursAwarded() != null) {
                total = total.add(record.getHoursAwarded());
            }
            if (record.getStatus() != null && record.getStatus().equalsIgnoreCase("Approved")) {
                approved++;
            } else if (record.getStatus() != null && record.getStatus().equalsIgnoreCase("Pending")) {
                pending++;
            }
            recordsPanel.add(createRecordCard(record));
            recordsPanel.add(Box.createVerticalStrut(10));
        }

        totalLabel.setText(total.toString());
        approvedLabel.setText(String.valueOf(approved));
        pendingLabel.setText(String.valueOf(pending));

        if (records.isEmpty()) {
            JLabel empty = new JLabel("No attendance records found.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(UIColors.TEXT_GREY);
            recordsPanel.add(empty);
        }

        recordsPanel.revalidate();
        recordsPanel.repaint();
    }

    private JPanel createRecordCard(AttendanceRecord record) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel left = new JLabel("Opportunity #" + record.getOppID() + "    |    " + record.getStatus());
        left.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel right = new JLabel(record.getHoursAwarded() + " hours");
        right.setFont(new Font("Segoe UI", Font.BOLD, 16));
        right.setForeground(UIColors.GREEN);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }
}