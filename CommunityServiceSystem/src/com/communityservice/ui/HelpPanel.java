package com.communityservice.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.*;

public class HelpPanel extends BasePanel {
    private JButton backButton;
    private static final String SUPPORT_EMAIL = "communityservicesupport@gmail.com";

    private static final String[][] FAQS = {
        {"Account & sign-in", "I can't sign in — what should I check?",
         "Confirm that you are using the email address you registered with. Passwords are case-sensitive. After repeated failed attempts, the account may be temporarily locked. Use Forgot password? if you cannot remember your password."},
        {"Email verification", "I didn't receive my verification code. What should I do?",
         "Check Spam or Junk, wait a moment, then use Resend Code. Only the newest verification code should be used. Verification codes expire, so request a new one if the previous code is no longer accepted."},
        {"Password recovery", "How do I reset a forgotten password?",
         "Select Forgot password? on the sign-in page, enter your registered email address, then follow the verification steps. Your new password must contain at least 8 characters, uppercase and lowercase letters, a number, and a special character."},
        {"NGO approval", "Why can't my NGO sign in after verifying its email?",
         "Email verification and administrator approval are separate steps. After email verification, an NGO is submitted for administrator review. The NGO can sign in only after it has been approved. The registered email address is notified when the application is approved or rejected."},
        {"NGO approval", "What happens if an NGO registration is rejected?",
         "The NGO receives a status email containing the administrator's rejection reason. If the information was entered incorrectly, contact support before creating another account so the issue can be reviewed."},
        {"Volunteers", "How do volunteer opportunities work?",
         "Complete your volunteer profile and preferences, then browse available opportunities. Open an opportunity to review its details before applying. Your dashboard keeps your account activity and relevant service information together."},
        {"NGOs", "How do NGOs create opportunities?",
         "Once the organisation has been approved, sign in to the NGO dashboard and use the event or opportunity creation area. Complete the required event details, dates, location, capacity, and required skills before submitting."},
        {"Security", "Why is my account temporarily locked?",
         "The application limits repeated failed sign-in attempts to reduce password guessing. Wait until the lockout period ends or use the password-recovery process. Never share passwords, verification codes, or reset codes with another person."}
    };

    public HelpPanel() {
        super("Help Center", "Guidance for volunteers, NGOs, and administrators using Community Service+.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        int row = 0;

        JLabel title = new JLabel("How can we help?");
        title.setFont(AppTheme.FONT_TITLE); title.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(title, setRow(gbc, row++));

        JLabel subtitle = html("Choose a topic below for quick guidance. For account-specific problems, contact support and include a short description of what happened.", 560, AppTheme.FONT_BODY, AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0, 0, 14, 0); rightContentPanel.add(subtitle, setRow(gbc, row++));

        JPanel quick = new JPanel(new GridLayout(1, 3, 10, 0)); quick.setOpaque(false);
        quick.add(infoCard("01", "Register", "Create a Volunteer or NGO account and verify your email."));
        quick.add(infoCard("02", "Complete setup", "Finish your profile. NGOs are then reviewed by an administrator."));
        quick.add(infoCard("03", "Get involved", "Browse, apply, create opportunities, and manage community-service activity."));
        gbc.insets = new Insets(0, 0, 16, 0); rightContentPanel.add(quick, setRow(gbc, row++));

        JLabel faqTitle = new JLabel("Frequently asked questions");
        faqTitle.setFont(AppTheme.FONT_HEADER); faqTitle.setForeground(AppTheme.TEXT_DARK);
        gbc.insets = new Insets(2, 0, 8, 0); rightContentPanel.add(faqTitle, setRow(gbc, row++));

        JPanel faqList = new JPanel(); faqList.setLayout(new BoxLayout(faqList, BoxLayout.Y_AXIS)); faqList.setOpaque(false);
        for (String[] faq : FAQS) { faqList.add(createFaqCard(faq[0], faq[1], faq[2])); faqList.add(Box.createVerticalStrut(9)); }
        JScrollPane scroll = new JScrollPane(faqList); scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16); scroll.getViewport().setOpaque(false); scroll.setOpaque(false);
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; gbc.insets = new Insets(0, 0, 10, 0);
        rightContentPanel.add(scroll, setRow(gbc, row++));
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;

        JPanel contact = new JPanel(new BorderLayout(14, 0)); contact.setBackground(new Color(240, 253, 250));
        contact.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY, 1, true), BorderFactory.createEmptyBorder(13, 15, 13, 15)));
        contact.add(html("<b>Still need help?</b><br>Contact Community Service+ Support at " + SUPPORT_EMAIL + ". Do not include your password or verification code.", 390, AppTheme.FONT_BODY, AppTheme.TEXT_DARK), BorderLayout.CENTER);
        JButton email = AppTheme.createPrimaryButton("Email Support"); email.setPreferredSize(new Dimension(145, 38)); email.addActionListener(e -> openSupportEmail()); contact.add(email, BorderLayout.EAST);
        gbc.insets = new Insets(4, 0, 8, 0); rightContentPanel.add(contact, setRow(gbc, row++));

        backButton = AppTheme.createBackButton("← Back");
        backButton.setContentAreaFilled(false); backButton.setBorderPainted(false); backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0,0,0,0); rightContentPanel.add(backButton, setRow(gbc, row));
    }

    private JPanel infoCard(String number, String title, String text) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JLabel n = new JLabel(number); n.setFont(AppTheme.FONT_SMALL); n.setForeground(AppTheme.PRIMARY); n.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel(title); t.setFont(AppTheme.FONT_HEADER); t.setForeground(AppTheme.TEXT_DARK); t.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel d = html(text, 155, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED); d.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(n); p.add(Box.createVerticalStrut(4)); p.add(t); p.add(Box.createVerticalStrut(4)); p.add(d); return p;
    }

    private JPanel createFaqCard(String category, String question, String answer) {
        JPanel card = new JPanel(); card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true), BorderFactory.createEmptyBorder(11, 14, 12, 14)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT); card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        JLabel cat = new JLabel(category.toUpperCase()); cat.setFont(AppTheme.FONT_SMALL); cat.setForeground(AppTheme.PRIMARY); cat.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel q = html("<b>" + question + "</b>", 520, AppTheme.FONT_BODY, AppTheme.TEXT_DARK); q.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextArea a = new JTextArea(answer); a.setEditable(false); a.setFocusable(false); a.setLineWrap(true); a.setWrapStyleWord(true); a.setOpaque(false);
        a.setFont(AppTheme.FONT_BODY); a.setForeground(AppTheme.TEXT_MUTED); a.setBorder(BorderFactory.createEmptyBorder(5,0,0,0)); a.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cat); card.add(Box.createVerticalStrut(3)); card.add(q); card.add(a); return card;
    }

    private JLabel html(String text, int width, Font font, Color color) {
        JLabel l = new JLabel("<html><body style='width: " + width + "px'>" + text + "</body></html>"); l.setFont(font); l.setForeground(color); return l;
    }

    private void openSupportEmail() {
        try { if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            Desktop.getDesktop().mail(new URI("mailto:" + SUPPORT_EMAIL + "?subject=Community%20Service%2B%20Support%20Request")); return; }
        } catch (Exception ignored) { }
        JOptionPane.showMessageDialog(this, "Please email " + SUPPORT_EMAIL, "Contact Support", JOptionPane.INFORMATION_MESSAGE);
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) { gbc.gridy = row; return gbc; }
    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }
}
