package com.communityservice.ui;

import com.communityservice.utils.EmailValidator;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ForgotPasswordPanel extends BasePanel {

    private JTextField emailField;
    private JButton sendResetLinkButton;
    private JButton backToLoginButton;
    private JLabel cooldownLabel;
    private JLabel statusLabel;
    private Timer cooldownTimer;

    public ForgotPasswordPanel() {
        super("Reset Password", "Enter your account email to receive a password reset link.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel headerLabel = new JLabel("Forgot Password?");
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel subHeader = new JLabel("Enter your registered email address below. If an account exists, we will send a 6-digit reset code to it.");
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        gbc.insets = new Insets(6, 0, 6, 0);

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(emailLabel, setRow(gbc, row++));

        emailField = new JTextField();
        AppTheme.styleTextField(emailField);
        new TextPrompt("name@example.com", emailField);
        rightContentPanel.add(emailField, setRow(gbc, row++));

        statusLabel = new JLabel(" "); statusLabel.setFont(AppTheme.FONT_SMALL); statusLabel.setForeground(AppTheme.DANGER);
        rightContentPanel.add(statusLabel, setRow(gbc, row++));

        sendResetLinkButton = AppTheme.createPrimaryButton("Send Reset Code");
        gbc.insets = new Insets(16, 0, 8, 0);
        rightContentPanel.add(sendResetLinkButton, setRow(gbc, row++));
        sendResetLinkButton.setEnabled(false);
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSendEnabled(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSendEnabled(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSendEnabled(); }
        });
        bindEnterToButton(sendResetLinkButton);
        cooldownLabel = new JLabel(" ");
        cooldownLabel.setFont(AppTheme.FONT_SMALL);
        cooldownLabel.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(cooldownLabel, setRow(gbc, row++));

        backToLoginButton = AppTheme.createBackButton("← Back to Sign In");
        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(backToLoginButton, setRow(gbc, row++));
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public String getEmail() { return emailField.getText().trim(); }

    public void setEmail(String email) { emailField.setText(email == null ? "" : email.trim()); }

    public void setSendResetLinkListener(ActionListener l) {
        sendResetLinkButton.addActionListener(e -> {
            String email = getEmail();
            if (email.isEmpty()) {
                statusLabel.setText("Enter your email address.");
                return;
            }
            if (!EmailValidator.isValidEmail(email)) {
                statusLabel.setText("Enter a valid email address.");
                return;
            }
            l.actionPerformed(e);
            startCooldown(60);
        });
    }

    private boolean coolingDown = false;
    private void updateSendEnabled() {
        boolean valid=EmailValidator.isValidEmail(getEmail()); sendResetLinkButton.setEnabled(!coolingDown && valid);
        if(!coolingDown){ if(getEmail().isEmpty()) showStatus("Enter your email address to enable Send Reset Code.",true); else if(!valid) showStatus("Enter a complete email address, e.g. name@example.com.",true); else showStatus("Ready to send a reset code.",false); }
    }
    public void setBusy(boolean busy) { sendResetLinkButton.setEnabled(!busy && !coolingDown && EmailValidator.isValidEmail(getEmail())); sendResetLinkButton.setText(busy ? "Sending..." : "Send Reset Code"); }
    public void showStatus(String message, boolean error) { statusLabel.setText(message == null ? " " : message); statusLabel.setForeground(error ? AppTheme.DANGER : AppTheme.SUCCESS); }

    private void startCooldown(int seconds) {
        if (cooldownTimer != null) cooldownTimer.stop();
        coolingDown = true;
        sendResetLinkButton.setEnabled(false);
        final int[] remaining = {seconds};
        cooldownLabel.setText("You can request another code in " + remaining[0] + " seconds.");
        cooldownTimer = new Timer(1000, e -> {
            remaining[0]--;
            if (remaining[0] <= 0) {
                cooldownTimer.stop();
                coolingDown = false;
                updateSendEnabled();
                cooldownLabel.setText(" ");
            } else {
                cooldownLabel.setText("You can request another code in " + remaining[0] + " seconds.");
            }
        });
        cooldownTimer.start();
    }

    public void setBackToLoginListener(ActionListener l) { backToLoginButton.addActionListener(l); }

    /** Clears the entered email, e.g. after the reset flow completes. */
    public void clearFields() {
        emailField.setText("");
    }
}