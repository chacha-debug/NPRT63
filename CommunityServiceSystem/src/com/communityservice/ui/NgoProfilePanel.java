package com.communityservice.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NgoProfilePanel extends BasePanel {

    private JTextField orgNameField;
    private JTextField regNumberField;
    private JComboBox<String> orgTypeCombo;
    private JTextField websiteField;
    private JTextField contactPersonField;
    private JTextArea missionArea;

    private JButton finishButton;
    private JButton backButton;

    public NgoProfilePanel() {
        super("NGO Profile", "Set up your organization credentials to post and manage community opportunities.");
        addStepSidebar(3, "Account Credentials", "Role & Details", "Preferences");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel titleLabel = new JLabel("Organization Details");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(titleLabel, setRow(gbc, row++));

        JLabel subtitleLabel = new JLabel("Step 3 of 3: NGO Setup");
        subtitleLabel.setFont(AppTheme.FONT_BODY);
        subtitleLabel.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0, 0, 16, 0);
        rightContentPanel.add(subtitleLabel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // Org Name
        rightContentPanel.add(new JLabel("Organization Name:"), setRow(gbc, row++));
        orgNameField = new JTextField();
        AppTheme.styleTextField(orgNameField);
        new TextPrompt("e.g. Hope Community Foundation", orgNameField);
        rightContentPanel.add(orgNameField, setRow(gbc, row++));

        // NPO / Reg Number
        rightContentPanel.add(new JLabel("NPO / Registration Number:"), setRow(gbc, row++));
        regNumberField = new JTextField();
        AppTheme.styleTextField(regNumberField);
        new TextPrompt("e.g. 123-456 NPO", regNumberField);
        rightContentPanel.add(regNumberField, setRow(gbc, row++));

        // Org Type
        rightContentPanel.add(new JLabel("Organization Type:"), setRow(gbc, row++));
        String[] types = {"Non-Profit Organization (NPO)", "Community-Based Organization (CBO)", "Educational Institution", "Social Enterprise"};
        orgTypeCombo = new JComboBox<>(types);
        orgTypeCombo.setFont(AppTheme.FONT_BODY);
        orgTypeCombo.setBackground(Color.WHITE);
        rightContentPanel.add(orgTypeCombo, setRow(gbc, row++));

        // Contact Person Title
        rightContentPanel.add(new JLabel("Primary Contact Person / Role:"), setRow(gbc, row++));
        contactPersonField = new JTextField();
        AppTheme.styleTextField(contactPersonField);
        new TextPrompt("e.g. Jane Doe (Volunteer Coordinator)", contactPersonField);
        rightContentPanel.add(contactPersonField, setRow(gbc, row++));

        // Website Link
        rightContentPanel.add(new JLabel("Website / Public Link (Optional):"), setRow(gbc, row++));
        websiteField = new JTextField();
        AppTheme.styleTextField(websiteField);
        new TextPrompt("https://example.org", websiteField);
        rightContentPanel.add(websiteField, setRow(gbc, row++));

        // Mission Statement
        rightContentPanel.add(new JLabel("Organization Mission / Focus:"), setRow(gbc, row++));
        missionArea = new JTextArea(7, 20);
        missionArea.setLineWrap(true);
        missionArea.setWrapStyleWord(true);
        missionArea.setFont(AppTheme.FONT_BODY);
        JScrollPane scroll = new JScrollPane(missionArea);
        scroll.setPreferredSize(new Dimension(100, 140));
        rightContentPanel.add(scroll, setRow(gbc, row++));

        // Action Buttons
        finishButton = AppTheme.createPrimaryButton("Complete NGO Setup");
        gbc.insets = new Insets(14, 0, 6, 0);
        rightContentPanel.add(finishButton, setRow(gbc, row++));

        backButton = AppTheme.createBackButton("← Back");
        backButton.setFont(AppTheme.FONT_BODY);
        backButton.setForeground(AppTheme.TEXT_MUTED);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(backButton, setRow(gbc, row++));
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public void setFinishListener(ActionListener l) {
        finishButton.addActionListener(e -> {
            if (orgNameField.getText().trim().isEmpty() || regNumberField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your Organization Name and NPO Registration Number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            l.actionPerformed(e);
        });
    }

    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }
}