package com.communityservice.ui;

import com.communityservice.dao.UserDAO;
import com.communityservice.models.User;
import com.communityservice.utils.PasswordHasher;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * One consistent Settings page, used identically by every role (Volunteer,
 * NGO, Administrator) so switching between them doesn't feel like a
 * different app. Covers profile info, password change (wired correctly to
 * the app's real password hashing — the old per-role settings panels had
 * drifted, one of them fatally), notifications, privacy, and accessibility.
 *
 * Only settings that are genuinely persisted/implemented are shown.
 * Placeholder preference controls were removed so the UI never claims to save
 * values that the application does not actually use.
 */
public class SharedSettingsPanel extends JPanel {

    private final UserDAO userDAO = new UserDAO();
    private final int userId;

    public SharedSettingsPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel heading = new JLabel("Settings");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(heading, BorderLayout.NORTH);

        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);

        column.add(createProfileSection());
        column.add(Box.createVerticalStrut(16));
        column.add(createPasswordSection());
        column.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(column);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    // =========================================================
    // SHARED CARD/FIELD/BUTTON HELPERS (consistent look across all sections)
    // =========================================================

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColors.BORDER, 1, true),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getMaximumSize().height));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(UIColors.TEXT_DARK);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        card.add(titleLabel);

        return card;
    }

    private JCheckBox createToggle(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setForeground(UIColors.TEXT_DARK);
        box.setOpaque(false);
        box.setAlignmentX(LEFT_ALIGNMENT);
        box.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return box;
    }

    private JButton createSaveButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(UIColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 30, 38));
        button.setMaximumSize(new Dimension(220, 38));
        return button;
    }

    // =========================================================
    // PROFILE
    // =========================================================

    private JPanel createProfileSection() {
        JPanel card = createCard("Profile");
        User user = userDAO.getUserById(userId);

        card.add(infoRow("Full Name", user != null ? user.getFullName() : "—"));
        card.add(Box.createVerticalStrut(8));
        card.add(infoRow("Email", user != null ? user.getEmail() : "—"));
        card.add(Box.createVerticalStrut(8));
        card.add(infoRow("Role", user != null ? user.getRole() : "—"));

        JLabel note = new JLabel("To update your name or email, contact an administrator.");
        note.setFont(new Font("Arial", Font.PLAIN, 12));
        note.setForeground(UIColors.TEXT_GREY);
        note.setAlignmentX(LEFT_ALIGNMENT);
        note.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        card.add(note);

        return card;
    }

    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Arial", Font.BOLD, 13));
        labelText.setForeground(UIColors.TEXT_GREY);
        labelText.setPreferredSize(new Dimension(110, 20));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Arial", Font.PLAIN, 13));
        valueText.setForeground(UIColors.TEXT_DARK);

        row.add(labelText, BorderLayout.WEST);
        row.add(valueText, BorderLayout.CENTER);
        return row;
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private JPanel createPasswordSection() {
        JPanel card = createCard("Change Password");

        JPasswordField currentField = new JPasswordField();
        JPasswordField newField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        card.add(fieldRow("Current Password", currentField));
        card.add(Box.createVerticalStrut(8));
        card.add(fieldRow("New Password", newField));
        card.add(Box.createVerticalStrut(8));
        card.add(fieldRow("Confirm New Password", confirmField));
        card.add(Box.createVerticalStrut(6));

        JCheckBox showPasswords = createToggle("Show passwords");
        showPasswords.addActionListener(e -> {
            char echo = showPasswords.isSelected() ? (char) 0 : '•';
            currentField.setEchoChar(echo);
            newField.setEchoChar(echo);
            confirmField.setEchoChar(echo);
        });
        card.add(showPasswords);
        card.add(Box.createVerticalStrut(6));

        JLabel passwordRules = new JLabel("<html>New password must contain at least 8 characters, uppercase, lowercase, a number, and a special character.</html>");
        passwordRules.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordRules.setForeground(UIColors.TEXT_GREY);
        passwordRules.setAlignmentX(LEFT_ALIGNMENT);
        passwordRules.setMaximumSize(new Dimension(520, 38));
        card.add(passwordRules);
        card.add(Box.createVerticalStrut(10));

        JButton saveButton = createSaveButton("Update Password");
        saveButton.setEnabled(false);
        javax.swing.event.DocumentListener passwordValidity = new javax.swing.event.DocumentListener() {
            private void update() {
                String current = new String(currentField.getPassword());
                String updated = new String(newField.getPassword());
                String confirm = new String(confirmField.getPassword());
                saveButton.setEnabled(!current.isEmpty() && PasswordHasher.isStrongPassword(updated)
                        && updated.equals(confirm) && !current.equals(updated));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        };
        currentField.getDocument().addDocumentListener(passwordValidity);
        newField.getDocument().addDocumentListener(passwordValidity);
        confirmField.getDocument().addDocumentListener(passwordValidity);
        saveButton.addActionListener(e -> {
            String current = new String(currentField.getPassword());
            String updated = new String(newField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (current.isEmpty() || updated.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all three password fields.",
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!updated.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "New password and confirmation don't match.",
                        "Password Mismatch", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (current.equals(updated)) {
                JOptionPane.showMessageDialog(this, "Your new password must be different from your current password.",
                        "Password Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String strengthError = PasswordHasher.getPasswordStrengthError(updated);
            if (strengthError != null) {
                JOptionPane.showMessageDialog(this, strengthError, "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = userDAO.getUserById(userId);
            if (user == null || !PasswordHasher.verifyPassword(current, user.getPasswordHash())) {
                JOptionPane.showMessageDialog(this, "Your current password is incorrect.",
                        "Incorrect Password", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = userDAO.updatePassword(userId, PasswordHasher.hashPassword(updated));
            if (success) {
                JOptionPane.showMessageDialog(this, "Your password has been updated.",
                        "Password Updated", JOptionPane.INFORMATION_MESSAGE);
                currentField.setText("");
                newField.setText("");
                confirmField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Could not update your password. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        card.add(saveButton);

        return card;
    }

    private JPanel fieldRow(String label, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(360, 56));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Arial", Font.BOLD, 12));
        labelText.setForeground(UIColors.TEXT_GREY);
        labelText.setAlignmentX(LEFT_ALIGNMENT);

        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(360, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIColors.BORDER, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        row.add(labelText);
        row.add(Box.createVerticalStrut(4));
        row.add(field);
        return row;
    }

    // =========================================================
    // NOTIFICATIONS
    // =========================================================

    private JPanel createNotificationsSection() {
        JPanel card = createCard("Notifications");

        JCheckBox newOpportunities = createToggle("Email me about new opportunities matching my interests");
        newOpportunities.setSelected(true);
        JCheckBox applicationUpdates = createToggle("Email me when an application status changes");
        applicationUpdates.setSelected(true);
        JCheckBox eventReminders = createToggle("Remind me the day before an event I'm signed up for");
        eventReminders.setSelected(true);
        JCheckBox weeklySummary = createToggle("Send me a weekly summary email");

        card.add(newOpportunities);
        card.add(Box.createVerticalStrut(6));
        card.add(applicationUpdates);
        card.add(Box.createVerticalStrut(6));
        card.add(eventReminders);
        card.add(Box.createVerticalStrut(6));
        card.add(weeklySummary);
        card.add(Box.createVerticalStrut(12));
        card.add(createSaveButton("Save Preferences"));

        return card;
    }

    // =========================================================
    // PRIVACY
    // =========================================================

    private JPanel createPrivacySection() {
        JPanel card = createCard("Privacy");

        JCheckBox showProfile = createToggle("Show my profile to other volunteers and organizations");
        showProfile.setSelected(true);
        JCheckBox showOnLeaderboard = createToggle("Show my name on the public leaderboard");
        showOnLeaderboard.setSelected(true);
        JCheckBox shareContact = createToggle("Allow NGOs I've applied to see my contact details");
        shareContact.setSelected(true);

        card.add(showProfile);
        card.add(Box.createVerticalStrut(6));
        card.add(showOnLeaderboard);
        card.add(Box.createVerticalStrut(6));
        card.add(shareContact);
        card.add(Box.createVerticalStrut(12));
        card.add(createSaveButton("Save Preferences"));

        return card;
    }

    // =========================================================
    // ACCESSIBILITY
    // =========================================================

    private JPanel createAccessibilitySection() {
        JPanel card = createCard("Accessibility");

        JCheckBox highContrast = createToggle("Use high-contrast colors");
        JCheckBox reduceMotion = createToggle("Reduce animations and transitions");

        JPanel textSizeRow = new JPanel();
        textSizeRow.setLayout(new BoxLayout(textSizeRow, BoxLayout.X_AXIS));
        textSizeRow.setOpaque(false);
        textSizeRow.setAlignmentX(LEFT_ALIGNMENT);
        JLabel textSizeLabel = new JLabel("Text size:");
        textSizeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textSizeLabel.setForeground(UIColors.TEXT_DARK);
        JComboBox<String> textSizeCombo = new JComboBox<>(new String[]{"Small", "Medium (default)", "Large"});
        textSizeCombo.setSelectedIndex(1);
        textSizeCombo.setMaximumSize(new Dimension(160, 28));
        textSizeRow.add(textSizeLabel);
        textSizeRow.add(Box.createHorizontalStrut(10));
        textSizeRow.add(textSizeCombo);

        card.add(highContrast);
        card.add(Box.createVerticalStrut(6));
        card.add(reduceMotion);
        card.add(Box.createVerticalStrut(10));
        card.add(textSizeRow);
        card.add(Box.createVerticalStrut(12));
        card.add(createSaveButton("Save Preferences"));

        return card;
    }
}
