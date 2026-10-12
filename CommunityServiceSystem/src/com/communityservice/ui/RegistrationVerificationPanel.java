package com.communityservice.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegistrationVerificationPanel extends BasePanel {

    private final JTextField[] codeFields = new JTextField[6];
    private JButton verifyButton;
    private JButton resendButton;
    private JButton backButton;
    private JLabel cooldownLabel;
    private JLabel statusLabel;
    private Timer cooldownTimer;
    private int cooldownSeconds;

    public RegistrationVerificationPanel() {
        super("Verify Email", "Confirm your identity to activate your account and keep it secure.");
        addStepSidebar(3, "Choose Role", "Account & Profile", "Verify Email");
        buildForm();
        startResendCooldown(60);
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int row = 0;

        JLabel headerLabel = new JLabel("Enter Registration Code");
        headerLabel.setFont(AppTheme.FONT_TITLE);
        headerLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(headerLabel, setRow(gbc, row++));

        gbc.insets = new Insets(0, 0, 16, 0);
        JLabel subHeader = new JLabel("Enter the 6-digit code sent to your email. The code expires in 15 minutes.");
        subHeader.setFont(AppTheme.FONT_BODY);
        subHeader.setForeground(AppTheme.TEXT_MUTED);
        rightContentPanel.add(subHeader, setRow(gbc, row++));

        gbc.insets = new Insets(6, 0, 6, 0);
        rightContentPanel.add(new JLabel("6-Digit Code"), setRow(gbc, row++));

        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        otpPanel.setOpaque(false);
        for (int i = 0; i < 6; i++) {
            final int index = i;
            JTextField field = new JTextField();
            codeFields[i] = field;
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setFont(AppTheme.FONT_TITLE);
            field.setPreferredSize(new java.awt.Dimension(44, 44));
            field.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true));
            ((AbstractDocument) field.getDocument()).setDocumentFilter(new OneDigitFilter());
            field.addKeyListener(new KeyAdapter() {
                @Override public void keyTyped(KeyEvent e) {
                    if (!Character.isDigit(e.getKeyChar())) e.consume();
                }
                @Override public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && field.getText().isEmpty() && index > 0) {
                        codeFields[index - 1].requestFocusInWindow();
                    } else if (Character.isDigit(e.getKeyChar()) && index < 5) {
                        codeFields[index + 1].requestFocusInWindow();
                    }
                }
            });
            otpPanel.add(field);
        }
        rightContentPanel.add(otpPanel, setRow(gbc, row++));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(AppTheme.FONT_SMALL);
        statusLabel.setForeground(AppTheme.DANGER);
        rightContentPanel.add(statusLabel, setRow(gbc, row++));

        verifyButton = AppTheme.createPrimaryButton("Verify & Activate Account");
        gbc.insets = new Insets(16, 0, 8, 0);
        rightContentPanel.add(verifyButton, setRow(gbc, row++));
        bindEnterToButton(verifyButton);
        verifyButton.setEnabled(false);
        javax.swing.event.DocumentListener otpValidity = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e){ updateVerifyEnabled(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e){ updateVerifyEnabled(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e){ updateVerifyEnabled(); }
        };
        for (JTextField f : codeFields) f.getDocument().addDocumentListener(otpValidity);

        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        linksPanel.setOpaque(false);
        resendButton = createLinkButton("Resend Code");
        cooldownLabel = new JLabel();
        cooldownLabel.setFont(AppTheme.FONT_SMALL);
        cooldownLabel.setForeground(AppTheme.TEXT_MUTED);
        JLabel separator = new JLabel("•");
        separator.setForeground(AppTheme.TEXT_MUTED);
        backButton = createLinkButton("Back");
        linksPanel.add(resendButton);
        linksPanel.add(cooldownLabel);
        linksPanel.add(separator);
        linksPanel.add(backButton);
        gbc.insets = new Insets(4, 0, 0, 0);
        rightContentPanel.add(linksPanel, setRow(gbc, row++));
    }

    private JButton createLinkButton(String text) {
        return AppTheme.createBackButton(text);
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) { gbc.gridy = row; return gbc; }

    public String getCodeInput() {
        StringBuilder sb = new StringBuilder(6);
        for (JTextField f : codeFields) sb.append(f.getText().trim());
        return sb.toString();
    }

    public void setVerifyListener(ActionListener l) {
        verifyButton.addActionListener(e -> {
            if (!getCodeInput().matches("\\d{6}")) {
                showStatus("Enter all 6 digits of the verification code.", true);
                return;
            }
            l.actionPerformed(e);
        });
    }

    private void updateVerifyEnabled() {
        boolean complete=getCodeInput().matches("\\d{6}");
        if (verifyButton != null && verifyButton.getText() != null && !verifyButton.getText().contains("...")) verifyButton.setEnabled(complete);
        if(statusLabel!=null && !complete) showStatus("Enter all 6 digits to enable verification.", true);
    }

    public void showStatus(String message, boolean error) { statusLabel.setText(message == null ? " " : message); statusLabel.setForeground(error ? AppTheme.DANGER : AppTheme.SUCCESS); }
    public void setBusy(boolean busy) {
        verifyButton.setEnabled(!busy && getCodeInput().matches("\\d{6}"));
        backButton.setEnabled(!busy);
        if (busy) resendButton.setEnabled(false); else resendButton.setEnabled(cooldownSeconds == 0);
        for (JTextField field : codeFields) field.setEnabled(!busy);
        verifyButton.setText(busy ? "Verifying..." : "Verify & Activate Account");
    }

    public void setResendListener(ActionListener l) {
        resendButton.addActionListener(e -> {
            if (!resendButton.isEnabled()) return;
            l.actionPerformed(e);
            startResendCooldown(60);
        });
    }

    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }

    public void startResendCooldown(int seconds) {
        if (cooldownTimer != null) cooldownTimer.stop();
        cooldownSeconds = Math.max(0, seconds);
        resendButton.setEnabled(cooldownSeconds == 0);
        updateCooldownLabel();
        if (cooldownSeconds > 0) {
            cooldownTimer = new Timer(1000, e -> {
                cooldownSeconds--;
                updateCooldownLabel();
                if (cooldownSeconds <= 0) {
                    cooldownTimer.stop();
                    resendButton.setEnabled(true);
                }
            });
            cooldownTimer.start();
        }
    }

    private void updateCooldownLabel() {
        cooldownLabel.setText(cooldownSeconds > 0 ? "(" + cooldownSeconds + "s)" : "");
    }

    private static final class OneDigitFilter extends DocumentFilter {
        @Override public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
            replace(fb, offset, 0, text, attrs);
        }
        @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            String digits = text.replaceAll("\\D", "");
            if (digits.isEmpty()) {
                if (text.isEmpty()) super.replace(fb, offset, length, text, attrs);
                return;
            }
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String candidate = current.substring(0, offset) + digits + current.substring(Math.min(current.length(), offset + length));
            if (candidate.length() <= 1) super.replace(fb, offset, length, digits.substring(0, 1), attrs);
        }
    }

    public void clearFields() {
        for (JTextField field : codeFields) field.setText("");
        codeFields[0].requestFocusInWindow();
    }
}
