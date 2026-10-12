package com.communityservice.ui;

import com.communityservice.utils.EmailValidator;
import com.communityservice.utils.PasswordHasher;
import com.communityservice.utils.InputValidator;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RegistrationPanel extends BasePanel {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField organizationNameField;
    private JTextField registrationNumberField;
    private JLabel organizationNameLabel;
    private JLabel registrationNumberLabel;
    private JLabel registrationHintLabel;
    private String role = "Volunteer";
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JLabel strengthStatusLabel;
    private JLabel emailStatusLabel;
    private JLabel matchStatusLabel;
    private JButton nextButton;
    private JButton backToLoginButton;

    public RegistrationPanel() {
        super("Create Account", "Create secure credentials for your Community Service+ profile.");
        addStepSidebar(1, "Account", "Role & Details", "Preferences", "Profile Setup");
        buildForm();
        updateRoleFields();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;

        JLabel headerLabel = new JLabel("Register");
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 12, 0);
        JLabel subHeader = new JLabel("Step 1 of 4: Account credentials");
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        gbc.insets = new Insets(4, 0, 4, 0);

        // Full Name
        JLabel nameLabel = requiredLabel("Full Name");
        nameLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(nameLabel, setRow(gbc, row++));

        nameField = new JTextField();
        AppTheme.styleTextField(nameField);
        new TextPrompt("Jane Doe", nameField);
        rightContentPanel.add(nameField, setRow(gbc, row++));

        // NGO identity belongs to registration, not later organisation preferences.
        organizationNameLabel = requiredLabel("Organisation Name");
        organizationNameLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(organizationNameLabel, setRow(gbc, row++));
        organizationNameField = new JTextField(); AppTheme.styleTextField(organizationNameField);
        new TextPrompt("Registered or public organisation name", organizationNameField);
        rightContentPanel.add(organizationNameField, setRow(gbc, row++));
        registrationNumberLabel = requiredLabel("NPO / NPC / PBO Registration Number");
        registrationNumberLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(registrationNumberLabel, setRow(gbc, row++));
        registrationNumberField = new JTextField(); AppTheme.styleTextField(registrationNumberField);
        new TextPrompt("e.g. 123-456 NPO or 2024/123456/08", registrationNumberField);
        rightContentPanel.add(registrationNumberField, setRow(gbc, row++));
        registrationHintLabel = new JLabel(InputValidator.nonProfitRegistrationHint());
        registrationHintLabel.setFont(AppTheme.FONT_SMALL); registrationHintLabel.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(registrationHintLabel, setRow(gbc, row++));

        // Email
        JLabel emailLabel = requiredLabel("Email Address");
        emailLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(emailLabel, setRow(gbc, row++));

        emailField = new JTextField();
        AppTheme.styleTextField(emailField);
        new TextPrompt("jane.doe@example.com", emailField);
        rightContentPanel.add(emailField, setRow(gbc, row++));
        emailStatusLabel = new JLabel(" ");
        emailStatusLabel.setFont(AppTheme.FONT_SMALL);
        emailStatusLabel.setVisible(false);
        gbc.insets = new Insets(2, 0, 4, 0);
        rightContentPanel.add(emailStatusLabel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // Password
        JLabel passLabel = requiredLabel("Password");
        passLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(passLabel, setRow(gbc, row++));

        passwordField = new JPasswordField();
        AppTheme.styleTextField(passwordField);
        new TextPrompt("••••••••", passwordField);
        rightContentPanel.add(passwordField, setRow(gbc, row++));

        strengthStatusLabel = new JLabel(" ");
        strengthStatusLabel.setFont(AppTheme.FONT_SMALL);
        strengthStatusLabel.setVisible(false);
        gbc.insets = new Insets(2, 0, 4, 0);
        rightContentPanel.add(strengthStatusLabel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // Confirm Password
        JLabel confirmPassLabel = requiredLabel("Confirm Password");
        confirmPassLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(confirmPassLabel, setRow(gbc, row++));

        confirmPasswordField = new JPasswordField();
        AppTheme.styleTextField(confirmPasswordField);
        new TextPrompt("••••••••", confirmPasswordField);
        rightContentPanel.add(confirmPasswordField, setRow(gbc, row++));

        // Show Password Toggle Checkbox
        showPasswordCheckBox = new JCheckBox("Show Passwords");
        showPasswordCheckBox.setFont(AppTheme.FONT_SMALL);
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.addActionListener(e -> {
            char echoChar = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echoChar);
            confirmPasswordField.setEchoChar(echoChar);
        });
        gbc.insets = new Insets(2, 0, 2, 0);
        rightContentPanel.add(showPasswordCheckBox, setRow(gbc, row++));

        // Real-Time Match Label
        matchStatusLabel = new JLabel(" ");
        matchStatusLabel.setFont(AppTheme.FONT_SMALL);
        matchStatusLabel.setVisible(false);
        rightContentPanel.add(matchStatusLabel, setRow(gbc, row++));

        // Next Button
        nextButton = AppTheme.createPrimaryButton("Continue");
        gbc.insets = new Insets(10, 0, 6, 0);
        rightContentPanel.add(nextButton, setRow(gbc, row++));
        bindEnterToButton(nextButton);

        // Back to Login Link
        backToLoginButton = AppTheme.createBackButton("← Back to Login");
        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(backToLoginButton, setRow(gbc, row++));

        // Dynamic Document Listener
        DocumentListener passwordChangeListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validatePasswords(); updateNextEnabled(); }
            @Override public void removeUpdate(DocumentEvent e) { validatePasswords(); updateNextEnabled(); }
            @Override public void changedUpdate(DocumentEvent e) { validatePasswords(); updateNextEnabled(); }
        };

        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validateEmailInline(); }
            @Override public void removeUpdate(DocumentEvent e) { validateEmailInline(); }
            @Override public void changedUpdate(DocumentEvent e) { validateEmailInline(); }
        });

        passwordField.getDocument().addDocumentListener(passwordChangeListener);
        confirmPasswordField.getDocument().addDocumentListener(passwordChangeListener);
        DocumentListener formChange = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateNextEnabled(); }
            public void removeUpdate(DocumentEvent e) { updateNextEnabled(); }
            public void changedUpdate(DocumentEvent e) { updateNextEnabled(); }
        };
        nameField.getDocument().addDocumentListener(formChange);
        organizationNameField.getDocument().addDocumentListener(formChange);
        registrationNumberField.getDocument().addDocumentListener(formChange);
        updateNextEnabled();
    }

    private void updateNextEnabled() {
        if (nextButton == null) return;
        boolean valid = !getNameInput().isEmpty() && EmailValidator.isValidEmail(getEmailInput())
                && PasswordHasher.isStrongPassword(getPasswordInput())
                && getPasswordInput().equals(getConfirmPasswordInput());
        nextButton.setEnabled(valid);
    }

    private void validateEmailInline() {
        String email = getEmailInput();
        if (email.isEmpty()) {
            emailStatusLabel.setText(" ");
            emailStatusLabel.setVisible(false);
            emailStatusLabel.setForeground(AppTheme.TEXT_MUTED);
        } else if (!EmailValidator.isValidEmail(email)) {
            emailStatusLabel.setVisible(true);
            emailStatusLabel.setText("✕ Enter a valid email address.");
            emailStatusLabel.setForeground(AppTheme.DANGER);
        } else {
            emailStatusLabel.setVisible(true);
            emailStatusLabel.setText("✓ Email format looks valid.");
            emailStatusLabel.setForeground(AppTheme.SUCCESS);
        }
        updateNextEnabled();
    }

    private void validatePasswords() {
        String pass = getPasswordInput();
        String confirm = getConfirmPasswordInput();

        String strengthError = PasswordHasher.getPasswordStrengthError(pass);
        if (pass.isEmpty()) {
            strengthStatusLabel.setText(" "); strengthStatusLabel.setVisible(false);
        } else if (strengthError != null) {
            strengthStatusLabel.setVisible(true); strengthStatusLabel.setText(strengthError);
            strengthStatusLabel.setForeground(new Color(220, 53, 69));
        } else {
            strengthStatusLabel.setVisible(true); strengthStatusLabel.setText("✓ Strong password.");
            strengthStatusLabel.setForeground(new Color(40, 167, 69));
        }

        if (pass.isEmpty() && confirm.isEmpty()) {
            matchStatusLabel.setText(" "); matchStatusLabel.setVisible(false);
            return;
        }

        if (confirm.isEmpty()) {
            matchStatusLabel.setVisible(true); matchStatusLabel.setText("Please confirm your password.");
            matchStatusLabel.setForeground(AppTheme.TEXT_MUTED);
            return;
        }

        if (pass.equals(confirm)) {
            matchStatusLabel.setVisible(true); matchStatusLabel.setText("✓ Passwords match!");
            matchStatusLabel.setForeground(new Color(40, 167, 69));
        } else {
            matchStatusLabel.setVisible(true); matchStatusLabel.setText("✕ Passwords do not match.");
            matchStatusLabel.setForeground(new Color(220, 53, 69));
        }
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public String getNameInput() { return nameField.getText().trim(); }
    public String getEmailInput() { return emailField.getText().trim(); }
    public String getOrganizationNameInput() { return organizationNameField.getText().trim(); }
    public String getRegistrationNumberInput() { return registrationNumberField.getText().trim(); }
    public void setRole(String role) { this.role = role == null ? "Volunteer" : role; updateRoleFields(); }
    private void updateRoleFields() {
        boolean ngo = false; // NGO identity is collected on Step 2 (Role & Details).
        if (organizationNameLabel != null) organizationNameLabel.setVisible(ngo);
        if (organizationNameField != null) organizationNameField.setVisible(ngo);
        if (registrationNumberLabel != null) registrationNumberLabel.setVisible(ngo);
        if (registrationNumberField != null) registrationNumberField.setVisible(ngo);
        if (registrationHintLabel != null) registrationHintLabel.setVisible(ngo);
        updateNextEnabled(); revalidate(); repaint();
    }
    public String getPasswordInput() { return new String(passwordField.getPassword()); }
    public String getConfirmPasswordInput() { return new String(confirmPasswordField.getPassword()); }

    public void restoreInputs(String name, String email) { restoreInputs(name, email, "", ""); }
    public void restoreInputs(String name, String email, String orgName, String regNo) {
        nameField.setText(name == null ? "" : name);
        emailField.setText(email == null ? "" : email);
        organizationNameField.setText(orgName == null ? "" : orgName);
        registrationNumberField.setText(regNo == null ? "" : regNo);
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public void setNextListener(ActionListener l) {
        nextButton.addActionListener(e -> {
            if (getNameInput().isEmpty()) { emailStatusLabel.setText("✕ Full name is required."); emailStatusLabel.setForeground(AppTheme.DANGER); nameField.requestFocusInWindow(); return; }
            if (!EmailValidator.isValidEmail(getEmailInput())) { emailStatusLabel.setVisible(true);
            emailStatusLabel.setText("✕ Enter a valid email address."); emailStatusLabel.setForeground(AppTheme.DANGER); emailField.requestFocusInWindow(); return; }
            String strengthError = PasswordHasher.getPasswordStrengthError(getPasswordInput());
            if (strengthError != null) { strengthStatusLabel.setText("✕ " + strengthError); strengthStatusLabel.setForeground(AppTheme.DANGER); passwordField.requestFocusInWindow(); return; }
            if (!getPasswordInput().equals(getConfirmPasswordInput())) { matchStatusLabel.setVisible(true); matchStatusLabel.setText("✕ Passwords do not match."); matchStatusLabel.setForeground(AppTheme.DANGER); confirmPasswordField.requestFocusInWindow(); return; }
            l.actionPerformed(e);
        });
    }

    public void setBackToLoginListener(ActionListener l) { backToLoginButton.addActionListener(l); }

    /** Resets all inputs, e.g. after registration completes so old data isn't reused. */
    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        organizationNameField.setText("");
        registrationNumberField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        showPasswordCheckBox.setSelected(false);
        passwordField.setEchoChar('•');
        confirmPasswordField.setEchoChar('•');
        matchStatusLabel.setText(" ");
        strengthStatusLabel.setText(" "); strengthStatusLabel.setVisible(false);
    }
}