package com.communityservice.ui;

import com.communityservice.utils.EmailValidator;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends BasePanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JButton registerButton;
    private JButton helpButton;
    private JButton aboutButton;
    private JLabel statusLabel;

    public LoginPanel() {
        super("Welcome Back", "Log in to access your volunteer dashboard, track hours, and view opportunities.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel headerLabel = new JLabel("Sign In");
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel subHeader = new JLabel("Enter your account credentials to continue");
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        gbc.insets = new Insets(6, 0, 6, 0);

        // Email
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(emailLabel, setRow(gbc, row++));

        emailField = new JTextField();
        AppTheme.styleTextField(emailField);
        new TextPrompt("jane.doe@example.com", emailField);
        rightContentPanel.add(emailField, setRow(gbc, row++));

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(passLabel, setRow(gbc, row++));

        passwordField = new JPasswordField();
        AppTheme.styleTextField(passwordField);
        new TextPrompt("••••••••", passwordField);
        rightContentPanel.add(passwordField, setRow(gbc, row++));

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(AppTheme.FONT_SMALL);
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '•');
        });
        gbc.insets = new Insets(0, 0, 6, 0);
        rightContentPanel.add(showPasswordCheckBox, setRow(gbc, row++));

        gbc.insets = new Insets(6, 0, 6, 0);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(AppTheme.FONT_SMALL);
        statusLabel.setForeground(AppTheme.DANGER);
        rightContentPanel.add(statusLabel, setRow(gbc, row++));

        // Sign In Button
        loginButton = AppTheme.createPrimaryButton("Sign In");
        gbc.insets = new Insets(16, 0, 10, 0);
        rightContentPanel.add(loginButton, setRow(gbc, row++));
        loginButton.setEnabled(false);
        javax.swing.event.DocumentListener loginValidation = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLoginEnabled(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLoginEnabled(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLoginEnabled(); }
        };
        emailField.getDocument().addDocumentListener(loginValidation);
        passwordField.getDocument().addDocumentListener(loginValidation);
        updateLoginEnabled();
        bindEnterToButton(loginButton);

        // Links Row (Forgot Password & Register) - Increased to FONT_BODY for larger size
        JPanel linkBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        linkBox.setOpaque(false);

        forgotPasswordButton = new JButton("Forgot password?");
        forgotPasswordButton.setFont(AppTheme.FONT_BODY);
        forgotPasswordButton.setForeground(AppTheme.PRIMARY);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton = new JButton("Don't have an account? Register");
        registerButton.setFont(AppTheme.FONT_BODY);
        registerButton.setForeground(AppTheme.PRIMARY);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        linkBox.add(forgotPasswordButton);
        linkBox.add(new JLabel("•"));
        linkBox.add(registerButton);

        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(linkBox, setRow(gbc, row++));

        // Secondary Links Row (Help & About)
        JPanel secondaryLinkBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        secondaryLinkBox.setOpaque(false);

        helpButton = new JButton("Help");
        helpButton.setFont(AppTheme.FONT_SMALL);
        helpButton.setForeground(AppTheme.TEXT_MUTED);
        helpButton.setContentAreaFilled(false);
        helpButton.setBorderPainted(false);
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        aboutButton = new JButton("About");
        aboutButton.setFont(AppTheme.FONT_SMALL);
        aboutButton.setForeground(AppTheme.TEXT_MUTED);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBorderPainted(false);
        aboutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        secondaryLinkBox.add(helpButton);
        secondaryLinkBox.add(new JLabel("•"));
        secondaryLinkBox.add(aboutButton);

        gbc.insets = new Insets(4, 0, 0, 0);
        rightContentPanel.add(secondaryLinkBox, setRow(gbc, row++));
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public String getEmail() { return emailField.getText().trim(); }

    /** Email remembering was removed because it was not a reliable application feature. */
    public void saveRememberedEmail() { }
    public String getPassword() { return new String(passwordField.getPassword()); }

    /** Pre-fills the login email without touching the password. */
    public void setEmail(String email) { emailField.setText(email == null ? "" : email.trim()); }

    /** Clears only the sensitive password after an unsuccessful sign-in. */
    public void clearPassword() {
        passwordField.setText("");
        showPasswordCheckBox.setSelected(false);
        passwordField.setEchoChar('•');
        passwordField.requestFocusInWindow();
    }

    public void setLoginListener(ActionListener l) {
        loginButton.addActionListener(e -> {
            if (getEmail().isEmpty() || getPassword().isEmpty()) { showStatus("Enter both your email and password.", true); return; }
            showStatus(" ", false);
            l.actionPerformed(e);
        });
    }

    private boolean loginBusy = false;
    private void updateLoginEnabled() {
        boolean emailPresent = !getEmail().isEmpty();
        boolean emailValid = EmailValidator.isValidEmail(getEmail());
        boolean passwordPresent = !getPassword().isEmpty();
        boolean valid = emailValid && passwordPresent;
        if (loginButton != null) loginButton.setEnabled(!loginBusy && valid);
        if (!loginBusy && statusLabel != null) {
            if (!emailPresent && !passwordPresent) showStatus("Enter your email address and password to enable Sign In.", true);
            else if (!emailPresent) showStatus("Email address is required.", true);
            else if (!emailValid) showStatus("Enter a complete email address, e.g. name@example.com.", true);
            else if (!passwordPresent) showStatus("Password is required.", true);
            else showStatus("Ready to sign in.", false);
        }
    }

    public void setBusy(boolean busy) {
        loginBusy = busy;
        loginButton.setEnabled(!busy && EmailValidator.isValidEmail(getEmail()) && !getPassword().isEmpty());
        forgotPasswordButton.setEnabled(!busy);
        registerButton.setEnabled(!busy);
        emailField.setEnabled(!busy);
        passwordField.setEnabled(!busy);
        loginButton.setText(busy ? "Signing in..." : "Sign In");
    }
    public void showStatus(String message, boolean error) { statusLabel.setText(message == null ? " " : message); statusLabel.setForeground(error ? AppTheme.DANGER : AppTheme.SUCCESS); }

    public void setGoToRegisterListener(ActionListener l) { registerButton.addActionListener(l); }
    public void setForgotPasswordListener(ActionListener l) { forgotPasswordButton.addActionListener(l); }
    public void setHelpListener(ActionListener l) { helpButton.addActionListener(l); }
    public void setAboutListener(ActionListener l) { aboutButton.addActionListener(l); }

    /** Clears the email/password fields, e.g. after a successful login. */
    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        showPasswordCheckBox.setSelected(false);
        passwordField.setEchoChar('•');
    }
}