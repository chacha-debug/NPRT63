package com.communityservice.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DashboardOverviewPanel extends BasePanel {

    private JLabel header;
    private JLabel roleLabel;
    private JLabel totalHoursLabel;
    private JLabel activeProjectsLabel;
    private JLabel upcomingEventsLabel;

    public DashboardOverviewPanel() {
        super("Dashboard", "Overview of your personal service statistics and active initiatives.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        header = new JLabel("Welcome Back!");
        header.setFont(AppTheme.FONT_TITLE);
        header.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(header, setRow(gbc, row++));

        roleLabel = new JLabel(" ");
        roleLabel.setFont(AppTheme.FONT_BODY);
        roleLabel.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0, 0, 12, 0);
        rightContentPanel.add(roleLabel, setRow(gbc, row++));
        gbc.insets = new Insets(6, 0, 6, 0);

        // Stat Cards Container
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        statsPanel.setOpaque(false);

        totalHoursLabel = new JLabel("0 hrs", JLabel.CENTER);
        activeProjectsLabel = new JLabel("0", JLabel.CENTER);
        upcomingEventsLabel = new JLabel("0", JLabel.CENTER);

        statsPanel.add(createStatCard("Logged Hours", totalHoursLabel));
        statsPanel.add(createStatCard("Active Projects", activeProjectsLabel));
        statsPanel.add(createStatCard("Upcoming Events", upcomingEventsLabel));

        gbc.insets = new Insets(16, 0, 16, 0);
        rightContentPanel.add(statsPanel, setRow(gbc, row++));
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(AppTheme.FONT_SMALL);
        titleLabel.setForeground(AppTheme.TEXT_MUTED);

        valueLabel.setFont(AppTheme.FONT_HEADER);
        valueLabel.setForeground(AppTheme.PRIMARY);

        card.add(titleLabel);
        card.add(valueLabel);
        return card;
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public void updateStats(int hours, int projects, int events) {
        totalHoursLabel.setText(hours + " hrs");
        activeProjectsLabel.setText(String.valueOf(projects));
        upcomingEventsLabel.setText(String.valueOf(events));
    }

    /** Personalizes the greeting with the signed-in user's name and role. */
    public void setUserInfo(String fullName, String role) {
        String firstName = (fullName == null || fullName.isBlank()) ? "there" : fullName.trim().split("\\s+")[0];
        header.setText("Welcome back, " + firstName + "!");
        roleLabel.setText(role == null ? " " : "Signed in as " + role);
    }
}