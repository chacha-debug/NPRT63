package com.communityservice.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AboutPanel extends BasePanel {
    private static final String APP_VERSION = "1.0.0";
    private JButton backButton;

    public AboutPanel() {
        super("About Community Service+", "A community-service platform built to connect people, organisations, and meaningful opportunities.");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints(); gbc.gridx = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(6,0,6,0);
        int row = 0;

        JPanel hero = new JPanel(new BorderLayout(18, 0)); hero.setBackground(new Color(240, 253, 250));
        hero.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY, 1, true), BorderFactory.createEmptyBorder(18,18,18,18)));
        JLabel mark = new JLabel("CS+"); mark.setHorizontalAlignment(SwingConstants.CENTER); mark.setOpaque(true); mark.setBackground(AppTheme.PRIMARY); mark.setForeground(Color.WHITE);
        mark.setFont(new Font("SansSerif", Font.BOLD, 24)); mark.setPreferredSize(new Dimension(70,70));
        JPanel heroText = new JPanel(); heroText.setOpaque(false); heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Community Service+"); name.setFont(AppTheme.FONT_TITLE); name.setForeground(AppTheme.TEXT_DARK); name.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tagline = html("Connecting volunteers with trusted organisations and opportunities that strengthen communities.", 440, AppTheme.FONT_BODY, AppTheme.TEXT_MUTED); tagline.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel version = new JLabel("Version " + APP_VERSION); version.setFont(AppTheme.FONT_SMALL); version.setForeground(AppTheme.PRIMARY); version.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroText.add(name); heroText.add(Box.createVerticalStrut(4)); heroText.add(tagline); heroText.add(Box.createVerticalStrut(7)); heroText.add(version);
        hero.add(mark, BorderLayout.WEST); hero.add(heroText, BorderLayout.CENTER);
        gbc.insets = new Insets(0,0,16,0); rightContentPanel.add(hero, setRow(gbc,row++));

        JLabel purposeTitle = new JLabel("Why Community Service+ exists"); purposeTitle.setFont(AppTheme.FONT_HEADER); purposeTitle.setForeground(AppTheme.TEXT_DARK);
        gbc.insets = new Insets(2,0,5,0); rightContentPanel.add(purposeTitle, setRow(gbc,row++));
        JLabel purpose = html("Community Service+ brings the volunteer journey into one place: account creation, organisation verification, opportunity management, applications, and service activity. The platform is designed to make participation easier while giving administrators the controls needed to maintain a trustworthy community.", 570, AppTheme.FONT_BODY, AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0,0,15,0); rightContentPanel.add(purpose, setRow(gbc,row++));

        JLabel who = new JLabel("Built for the whole community"); who.setFont(AppTheme.FONT_HEADER); who.setForeground(AppTheme.TEXT_DARK);
        gbc.insets = new Insets(2,0,8,0); rightContentPanel.add(who, setRow(gbc,row++));
        JPanel grid = new JPanel(new GridLayout(1,3,10,0)); grid.setOpaque(false);
        grid.add(feature("V", "Volunteers", "Create a profile, discover opportunities, apply, and keep service activity organised."));
        grid.add(feature("N", "NGOs", "Complete organisation verification, publish opportunities, and connect with volunteers."));
        grid.add(feature("A", "Administrators", "Review NGO registrations, oversee accounts, and maintain an auditable platform."));
        gbc.insets = new Insets(0,0,16,0); rightContentPanel.add(grid, setRow(gbc,row++));

        JPanel trust = new JPanel(new BorderLayout(12,0)); trust.setBackground(Color.WHITE);
        trust.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR,1,true), BorderFactory.createEmptyBorder(13,14,13,14)));
        JLabel shield = new JLabel("✓"); shield.setFont(new Font("SansSerif", Font.BOLD, 22)); shield.setForeground(AppTheme.PRIMARY);
        trust.add(shield, BorderLayout.WEST);
        trust.add(html("<b>Designed with trust in mind</b><br>Email verification, protected passwords, login safeguards, administrator approval for NGOs, and audit logging support safer account and administration workflows.", 490, AppTheme.FONT_BODY, AppTheme.TEXT_DARK), BorderLayout.CENTER);
        gbc.insets = new Insets(0,0,16,0); rightContentPanel.add(trust, setRow(gbc,row++));

        JLabel tech = new JLabel("Technology"); tech.setFont(AppTheme.FONT_HEADER); tech.setForeground(AppTheme.TEXT_DARK);
        gbc.insets = new Insets(0,0,7,0); rightContentPanel.add(tech, setRow(gbc,row++));
        JPanel chips = new JPanel(); chips.setLayout(new BoxLayout(chips, BoxLayout.X_AXIS)); chips.setOpaque(false);
        for (String s : new String[]{"Java", "Swing", "MySQL", "JDBC", "JavaMail"}) { chips.add(chip(s)); chips.add(Box.createHorizontalStrut(7)); }
        gbc.insets = new Insets(0,0,16,0); rightContentPanel.add(chips, setRow(gbc,row++));

        JLabel note = html("Community Service+ is an academic software project focused on practical community-service coordination, secure account workflows, and role-based administration.", 570, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0,0,10,0); rightContentPanel.add(note, setRow(gbc,row++));
        JLabel copyright = new JLabel("© " + java.time.Year.now() + " Community Service+. All rights reserved."); copyright.setFont(AppTheme.FONT_SMALL); copyright.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0,0,8,0); rightContentPanel.add(copyright, setRow(gbc,row++));

        backButton = AppTheme.createBackButton("← Back");
        gbc.insets = new Insets(0,0,0,0); rightContentPanel.add(backButton, setRow(gbc,row));
    }

    private JPanel feature(String initial, String title, String description) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR,1,true), BorderFactory.createEmptyBorder(13,13,13,13)));
        JLabel icon = new JLabel(initial); icon.setHorizontalAlignment(SwingConstants.CENTER); icon.setOpaque(true); icon.setBackground(new Color(240,253,250)); icon.setForeground(AppTheme.PRIMARY); icon.setFont(new Font("SansSerif",Font.BOLD,16)); icon.setPreferredSize(new Dimension(32,32)); icon.setMaximumSize(new Dimension(32,32)); icon.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel(title); t.setFont(AppTheme.FONT_HEADER); t.setForeground(AppTheme.TEXT_DARK); t.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel d = html(description, 155, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED); d.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(icon); p.add(Box.createVerticalStrut(8)); p.add(t); p.add(Box.createVerticalStrut(4)); p.add(d); return p;
    }

    private JLabel chip(String text) { JLabel c = new JLabel(text); c.setFont(AppTheme.FONT_SMALL); c.setForeground(AppTheme.PRIMARY); c.setOpaque(true); c.setBackground(new Color(240,253,250)); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY,1,true),BorderFactory.createEmptyBorder(5,10,5,10))); return c; }
    private JLabel html(String text, int width, Font font, Color color) { JLabel l = new JLabel("<html><body style='width: " + width + "px'>" + text + "</body></html>"); l.setFont(font); l.setForeground(color); return l; }
    private GridBagConstraints setRow(GridBagConstraints gbc, int row) { gbc.gridy=row; return gbc; }
    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }
}
