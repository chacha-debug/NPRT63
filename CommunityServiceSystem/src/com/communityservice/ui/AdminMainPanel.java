package com.communityservice.ui;

import com.communityservice.dao.AuditLogDAO;
import com.communityservice.dao.NGODAO;
import com.communityservice.dao.UserDAO;
import com.communityservice.dao.VolunteerDAO;
import com.communityservice.models.AuditLog;
import com.communityservice.models.NGO;
import com.communityservice.models.User;
import com.communityservice.models.Volunteer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class AdminMainPanel extends JPanel {

    private final UserDAO userDAO = new UserDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private final NGODAO ngoDAO = new NGODAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    private JPanel statsRow;
    private JPanel activityList;

    public AdminMainPanel(AdminDashboardPanel host, int adminUserId) {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        User currentAdmin = userDAO.getUserById(adminUserId);
        String adminName = currentAdmin != null && currentAdmin.getFullName() != null
                && !currentAdmin.getFullName().trim().isEmpty()
                ? currentAdmin.getFullName().trim() : "Administrator";
        JLabel heading = new JLabel("Welcome, " + adminName);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        add(heading, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        center.add(statsRow);

        center.add(Box.createVerticalStrut(24));

        JLabel activityHeading = new JLabel("Recent Activity");
        activityHeading.setFont(new Font("Arial", Font.BOLD, 16));
        activityHeading.setForeground(UIColors.TEXT_DARK);
        activityHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        activityHeading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        center.add(activityHeading);

        activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityList.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(activityList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        center.add(scroll);

        add(center, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        List<User> users = userDAO.getAllUsers();
        List<Volunteer> volunteers = volunteerDAO.getAllVolunteers();
        List<NGO> ngos = ngoDAO.getAllNGOs();
        List<NGO> pending = ngoDAO.getPendingVerificationNGOs();

        statsRow.removeAll();
        statsRow.add(createStatCard("Total Users", String.valueOf(users.size()), UIColors.PRIMARY));
        statsRow.add(createStatCard("Volunteers", String.valueOf(volunteers.size()), UIColors.GREEN));
        statsRow.add(createStatCard("NGOs", String.valueOf(ngos.size()), UIColors.STAT_ORANGE));
        statsRow.add(createStatCard("Pending NGO Verification", String.valueOf(pending.size()), UIColors.RED));

        activityList.removeAll();
        List<AuditLog> logs = auditLogDAO.getAllAuditLogs();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
        int shown = 0;
        for (AuditLog log : logs) {
            if (shown >= 8) {
                break;
            }
            activityList.add(createActivityRow(log, fmt));
            activityList.add(Box.createVerticalStrut(6));
            shown++;
        }
        if (shown == 0) {
            JLabel empty = new JLabel("No activity recorded yet.");
            empty.setForeground(UIColors.TEXT_GREY);
            activityList.add(empty);
        }

        revalidate();
        repaint();
    }

    private JPanel createStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColors.BORDER, 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Arial", Font.PLAIN, 13));
        labelText.setForeground(UIColors.TEXT_GREY);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Arial", Font.BOLD, 28));
        valueText.setForeground(accent);
        valueText.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueText.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        card.add(labelText);
        card.add(valueText);
        return card;
    }

    private JPanel createActivityRow(AuditLog log, DateTimeFormatter fmt) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(UIColors.CARD);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColors.BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        String action = log.getAction() != null ? log.getAction() : "Action";
        String entity = log.getEntityType() != null ? (" · " + log.getEntityType()) : "";
        JLabel text = new JLabel(action + entity + " — user #" + log.getUserID());
        text.setFont(new Font("Arial", Font.PLAIN, 13));
        text.setForeground(UIColors.TEXT_DARK);
        row.add(text, BorderLayout.WEST);

        String when = log.getTimestamp() != null ? log.getTimestamp().format(fmt) : "";
        JLabel timeLabel = new JLabel(when);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(UIColors.TEXT_GREY);
        row.add(timeLabel, BorderLayout.EAST);

        return row;
    }
}
