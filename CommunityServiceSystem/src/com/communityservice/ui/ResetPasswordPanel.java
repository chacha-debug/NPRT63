package com.communityservice.ui;



import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import com.communityservice.utils.PasswordHasher;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPasswordField;

public class ResetPasswordPanel extends BasePanel {

    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel matchStatusLabel;
    private JButton submitButton;
    private JCheckBox showPasswordsCheckBox;

    public ResetPasswordPanel() {
        super("New Password", "Create a strong password for your account security.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel headerLabel = new JLabel("Set New Password");
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel subHeader = new JLabel("Your new password must be different from previous passwords.");
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        gbc.insets = new Insets(6, 0, 6, 0);

        // New Password
        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(newPassLabel, setRow(gbc, row++));

        newPasswordField = new JPasswordField();
        AppTheme.styleTextField(newPasswordField);
        new TextPrompt("••••••••", newPasswordField);
        rightContentPanel.add(newPasswordField, setRow(gbc, row++));

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirm New Password");
        confirmPassLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(confirmPassLabel, setRow(gbc, row++));

        confirmPasswordField = new JPasswordField();
        AppTheme.styleTextField(confirmPasswordField);
        new TextPrompt("••••••••", confirmPasswordField);
        rightContentPanel.add(confirmPasswordField, setRow(gbc, row++));

        showPasswordsCheckBox = new JCheckBox("Show Passwords");
        showPasswordsCheckBox.setFont(AppTheme.FONT_SMALL);
        showPasswordsCheckBox.setOpaque(false);
        showPasswordsCheckBox.addActionListener(e -> {
            char echo = showPasswordsCheckBox.isSelected() ? (char) 0 : '•';
            newPasswordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        rightContentPanel.add(showPasswordsCheckBox, setRow(gbc, row++));

        // Real-Time Validation Status Label
        matchStatusLabel = new JLabel(" ");
        matchStatusLabel.setFont(AppTheme.FONT_SMALL);
        gbc.insets = new Insets(2, 0, 4, 0);
        rightContentPanel.add(matchStatusLabel, setRow(gbc, row++));

        submitButton = AppTheme.createPrimaryButton("Reset Password");
        submitButton.setEnabled(false); // Initially disabled until passwords match
        gbc.insets = new Insets(12, 0, 0, 0);
        rightContentPanel.add(submitButton, setRow(gbc, row++));

        // Attach listeners for real-time validation
        DocumentListener passwordChangeListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validatePasswords(); }
            @Override public void removeUpdate(DocumentEvent e) { validatePasswords(); }
            @Override public void changedUpdate(DocumentEvent e) { validatePasswords(); }
        };

        newPasswordField.getDocument().addDocumentListener(passwordChangeListener);
        confirmPasswordField.getDocument().addDocumentListener(passwordChangeListener);
        bindEnterToButton(submitButton);
    }

    private void validatePasswords() {
        String pass = getNewPassword();
        String confirm = getConfirmPassword();

        String strengthError = PasswordHasher.getPasswordStrengthError(pass);
        if (pass.isEmpty()) {
            matchStatusLabel.setText("Enter a new password.");
            submitButton.setEnabled(false);
        } else if (strengthError != null) {
            matchStatusLabel.setText("✕ " + strengthError);
            submitButton.setEnabled(false);
        } else if (confirm.isEmpty()) {
            matchStatusLabel.setText("Strong password. Please confirm it.");
            submitButton.setEnabled(false);
        } else if (!pass.equals(confirm)) {
            matchStatusLabel.setText("✕ Passwords do not match.");
            submitButton.setEnabled(false);
        } else {
            matchStatusLabel.setText("✓ Strong passwords match.");
            submitButton.setEnabled(true);
        }
    }

    public String getNewPassword() { return new String(newPasswordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }

    public void setSubmitListener(ActionListener l) {
        if (submitButton != null) {
            submitButton.addActionListener(e -> {
                if (submitButton.isEnabled()) {
                    l.actionPerformed(e);
                }
            });
        }
    }

    /** Clears the password fields, e.g. after the reset completes. */
    public void clearFields() {
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        matchStatusLabel.setText(" ");
        submitButton.setEnabled(false);
        showPasswordsCheckBox.setSelected(false);
        newPasswordField.setEchoChar('•');
        confirmPasswordField.setEchoChar('•');
    }
    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

}
