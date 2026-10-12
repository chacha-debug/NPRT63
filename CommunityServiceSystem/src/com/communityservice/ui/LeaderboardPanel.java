/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// File: ui/LeaderboardPanel.java
// File: ui/LeaderboardPanel.java
package com.communityservice.ui;

import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.models.PortalVolunteer;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private final VolunteerPortalVolunteerDAO volunteerDAO;
    private JPanel rankingContainer;

    public LeaderboardPanel() {
        volunteerDAO = new VolunteerPortalVolunteerDAO();
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Leaderboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        rankingContainer = new JPanel();
        rankingContainer.setLayout(new BoxLayout(rankingContainer, BoxLayout.Y_AXIS));
        rankingContainer.setBackground(UIColors.BACKGROUND);
        rankingContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(rankingContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(UIColors.BACKGROUND);
        scrollPane.getViewport().setBackground(UIColors.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(UIColors.PRIMARY);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadLeaderboard());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UIColors.BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadLeaderboard();
    }

    private void loadLeaderboard() {
        rankingContainer.removeAll();
        List<PortalVolunteer> volunteers = volunteerDAO.getAllVolunteers();

        if (volunteers == null || volunteers.isEmpty()) {
            showMessage("No volunteers ranking data available.");
            refreshContainer();
            return;
        }

        volunteers.sort((v1, v2) -> {
            BigDecimal h1 = v1.getTotalHours() != null ? v1.getTotalHours() : BigDecimal.ZERO;
            BigDecimal h2 = v2.getTotalHours() != null ? v2.getTotalHours() : BigDecimal.ZERO;
            return h2.compareTo(h1);
        });

        int position = 1;
        for (PortalVolunteer vol : volunteers) {
            rankingContainer.add(createRankingCard(position, vol));
            rankingContainer.add(Box.createVerticalStrut(12));
            position++;
        }
        refreshContainer();
    }

    private JPanel createRankingCard(int position, PortalVolunteer volunteers) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel rank = new JLabel("#" + position);
        rank.setFont(new Font("Segoe UI", Font.BOLD, 22));
        rank.setForeground(position <= 3 ? UIColors.GOLD : UIColors.TEXT_DARK);
        card.add(rank, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(UIColors.CARD);

        JLabel volunteer_id = new JLabel("Volunteer #" + volunteers.getVolunteerID());
        volunteer_id.setFont(new Font("Segoe UI", Font.BOLD, 17));
        volunteer_id.setForeground(UIColors.TEXT_DARK);
        center.add(volunteer_id);

        JLabel academic_year = new JLabel("Academic Year: " + volunteers.getAcademicYear());
        academic_year.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        academic_year.setForeground(UIColors.TEXT_GREY);
        center.add(academic_year);

        card.add(center, BorderLayout.CENTER);

        BigDecimal hours = volunteers.getTotalHours() != null ? volunteers.getTotalHours() : BigDecimal.ZERO;
        JLabel hoursLabel = new JLabel(hours.toString() + " hrs");
        hoursLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hoursLabel.setForeground(UIColors.GREEN);
        card.add(hoursLabel, BorderLayout.EAST);

        return card;
    }

    private void showMessage(String message) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        label.setForeground(UIColors.TEXT_GREY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        rankingContainer.add(Box.createVerticalStrut(40));
        rankingContainer.add(label);
    }

    private void refreshContainer() {
        rankingContainer.revalidate();
        rankingContainer.repaint();
    }
}