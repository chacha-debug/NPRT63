/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// File: ui/ProfilePanel.java
package com.communityservice.ui;

import com.communityservice.dao.VolunteerPortalUserDAO;
import com.communityservice.dao.ProfileFieldsDAO;
import com.communityservice.models.PortalUser;
import com.communityservice.models.UserProfile;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private final int user_id;
    private final VolunteerPortalUserDAO userDAO;
    private final ProfileFieldsDAO profileDAO;
    private JLabel name, email, location;

    public ProfilePanel(int user_id) {
        this.user_id = user_id;
        userDAO = new VolunteerPortalUserDAO();
        profileDAO = new ProfileFieldsDAO();
        initialize();
        loadProfile();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        name = new JLabel("Loading...");
        email = new JLabel("Loading...");
        location = new JLabel("Loading...");

        name.setFont(new Font("Segoe UI", Font.BOLD, 24));
        name.setForeground(UIColors.TEXT_DARK);
        
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(UIColors.TEXT_GREY);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(UIColors.TEXT_GREY);

        JLabel locationLabel = new JLabel("Location");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        locationLabel.setForeground(UIColors.TEXT_GREY);

        email.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        location.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        card.add(nameLabel);
        card.add(name);
        card.add(Box.createVerticalStrut(20));
        card.add(emailLabel);
        card.add(email);
        card.add(Box.createVerticalStrut(20));
        card.add(locationLabel);
        card.add(location);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIColors.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        wrapper.add(card, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    private void loadProfile() {
        PortalUser users = userDAO.getUserByID(user_id);
        if (users == null) {
            name.setText("Unknown User");
            return;
        }
        name.setText(users.getFullName());
        email.setText(users.getEmail());

        UserProfile profile = profileDAO.getUserProfileByUserID(user_id);
        location.setText(profile != null && profile.getLocation() != null ? profile.getLocation() : "Not provided");
    }
}