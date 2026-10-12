package com.communityservice.ui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminSetupPanel extends BasePanel {

    private JTextField workEmailField;
    private JComboBox<String> accessRoleCombo;
    private JCheckBox modAlertsCheck, auditLogsCheck, userApprovalCheck;
    private JTextField adminNotesField;

    private JButton saveButton;
    private JButton backButton;

    public AdminSetupPanel() {
        super("Admin Setup", "Configure administrative controls, access permissions, and system notifications.");
        addStepSidebar(3, "Account Credentials", "Role & Details", "Admin Configuration");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel titleLabel = new JLabel("System Admin Configuration");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(titleLabel, setRow(gbc, row++));

        JLabel subtitleLabel = new JLabel("Step 3 of 3: Security & Operational Permissions");
        subtitleLabel.setFont(AppTheme.FONT_BODY);
        subtitleLabel.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0, 0, 12, 0);
        rightContentPanel.add(subtitleLabel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // Security / Emergency Email
        rightContentPanel.add(createLabel("Official Staff Work Email:"), setRow(gbc, row++));
        workEmailField = new JTextField();
        AppTheme.styleTextField(workEmailField);
        new TextPrompt("admin.name@platform.org", workEmailField);
        rightContentPanel.add(workEmailField, setRow(gbc, row++));

        // Permission Tier
        rightContentPanel.add(createLabel("Initial Access Tier:"), setRow(gbc, row++));
        JLabel tierNote = createLabel("New administrator applications start at Level 1. Higher privileges must be granted separately by platform governance.");
        tierNote.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(tierNote, setRow(gbc, row++));
        String[] tiers = {"Level 1: Content Moderator & Verification"};
        accessRoleCombo = new JComboBox<>(tiers);
        accessRoleCombo.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(accessRoleCombo, setRow(gbc, row++));

        // Subscribed Alerts & Responsibilities
        rightContentPanel.add(createLabel("System Notification Subscriptions:"), setRow(gbc, row++));
        JPanel checkPanel = new JPanel(new GridLayout(3, 1, 5, 2));
        checkPanel.setOpaque(false);

        userApprovalCheck = new JCheckBox("Receive New NGO Verification Requests", true);
        modAlertsCheck = new JCheckBox("Receive Flagged Content & User Reports", true);
        auditLogsCheck = new JCheckBox("Receive Weekly System Audit Summaries", false);

        checkPanel.add(userApprovalCheck);
        checkPanel.add(modAlertsCheck);
        checkPanel.add(auditLogsCheck);
        rightContentPanel.add(checkPanel, setRow(gbc, row++));

        // Admin Security Note / Key
        rightContentPanel.add(createLabel("Security Clearance / Note (Optional):"), setRow(gbc, row++));
        adminNotesField = new JTextField();
        AppTheme.styleTextField(adminNotesField);
        new TextPrompt("e.g. Authorized by Operations Board", adminNotesField);
        rightContentPanel.add(adminNotesField, setRow(gbc, row++));

        // Submit Button
        saveButton = AppTheme.createPrimaryButton("Save Admin Configuration & Finish");
        gbc.insets = new Insets(14, 0, 4, 0);
        rightContentPanel.add(saveButton, setRow(gbc, row++));
        bindEnterToButton(saveButton);

        backButton = AppTheme.createBackButton("← Back");
        backButton.setFont(AppTheme.FONT_BODY);
        backButton.setForeground(AppTheme.TEXT_MUTED);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(backButton, setRow(gbc, row++));
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(AppTheme.TEXT_DARK);
        return l;
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public void setSaveListener(ActionListener l) {
        saveButton.addActionListener(e -> {
            if (workEmailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your staff work email.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            l.actionPerformed(e);
        });
    }

    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }

    public String getAccessTier() { return accessRoleCombo != null ? (String) accessRoleCombo.getSelectedItem() : ""; }

    /** Resets all admin setup fields to a clean state. */
    public void clearFields() {
        workEmailField.setText("");
        if (accessRoleCombo != null) accessRoleCombo.setSelectedIndex(0);
        userApprovalCheck.setSelected(true);
        modAlertsCheck.setSelected(true);
        auditLogsCheck.setSelected(false);
        adminNotesField.setText("");
    }
}