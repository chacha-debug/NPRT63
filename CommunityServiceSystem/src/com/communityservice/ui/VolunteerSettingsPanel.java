/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// File: ui/VolunteerSettingsPanel.java
package com.communityservice.ui;

import javax.swing.*;
import java.awt.*;

public class VolunteerSettingsPanel extends JPanel {
    private final int user_id;

    public VolunteerSettingsPanel(int user_id) {
        this.user_id = user_id;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIColors.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        settings.setBackground(UIColors.CARD);
        settings.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIColors.BORDER),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JCheckBox notifications = new JCheckBox("Enable notifications");
        notifications.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        notifications.setBackground(UIColors.CARD);
        notifications.setSelected(true);

        JCheckBox emailUpdates = new JCheckBox("Receive email updates");
        emailUpdates.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        emailUpdates.setBackground(UIColors.CARD);
        emailUpdates.setSelected(true);

        JButton save = new JButton("Save Settings");
        save.setBackground(UIColors.PRIMARY);
        save.setForeground(Color.WHITE);
        save.setFont(new Font("Segoe UI", Font.BOLD, 14));
        save.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        save.setFocusPainted(false);
        save.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings saved successfully."));

        settings.add(notifications);
        settings.add(Box.createVerticalStrut(15));
        settings.add(emailUpdates);
        settings.add(Box.createVerticalStrut(25));
        settings.add(save);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIColors.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        wrapper.add(settings, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }
}