package com.communityservice.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class WelcomePanel extends BasePanel {

    private JButton goToDashboardButton;

    public WelcomePanel() {
        super("Account Ready", "Welcome to Community Service+! Your account is active.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel iconLabel = new JLabel("✓", SwingConstants.CENTER);
        iconLabel.setFont(AppTheme.FONT_TITLE.deriveFont(48.0f));
        iconLabel.setForeground(AppTheme.PRIMARY);
        rightContentPanel.add(iconLabel, setRow(gbc, row++));

        JLabel headerLabel = new JLabel("Verification Successful!", SwingConstants.CENTER);
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel subHeader = new JLabel("<html><center>Your email has been verified. You can now explore opportunities, log hours, and connect with local projects.</center></html>", SwingConstants.CENTER);
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        goToDashboardButton = AppTheme.createPrimaryButton("Go to Dashboard");
        gbc.insets = new Insets(16, 0, 0, 0);
        rightContentPanel.add(goToDashboardButton, setRow(gbc, row++));
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public void setGoToDashboardListener(ActionListener l) {
        goToDashboardButton.addActionListener(l);
    }
}