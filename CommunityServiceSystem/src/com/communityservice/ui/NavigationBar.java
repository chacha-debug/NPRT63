package com.communityservice.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NavigationBar extends JPanel {

    private JButton dashboardBtn;
    private JButton profileBtn;
    private JButton verifyNgosBtn;
    private JButton logoutBtn;

    public NavigationBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 50));

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.add(new RoundPersonIcon(28, AppTheme.NAVY_PRIMARY, Color.WHITE));
        JLabel brandLabel = new JLabel("Community Service+");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        brandLabel.setForeground(AppTheme.NAVY_PRIMARY);
        brandRow.add(brandLabel);
        add(brandRow);

        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(24, 1));
        add(spacer);

        dashboardBtn = createNavButton("Dashboard");
        profileBtn = createNavButton("Profile");
        verifyNgosBtn = createNavButton("Verify NGOs");
        verifyNgosBtn.setVisible(false); // shown only for Administrator accounts
        logoutBtn = createNavButton("Sign Out");
        logoutBtn.setForeground(AppTheme.DANGER);

        add(dashboardBtn);
        add(profileBtn);
        add(verifyNgosBtn);
        add(logoutBtn);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppTheme.FONT_BODY);
        btn.setForeground(AppTheme.TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setDashboardListener(ActionListener l) { dashboardBtn.addActionListener(l); }
    public void setProfileListener(ActionListener l) { profileBtn.addActionListener(l); }
    public void setVerifyNgosListener(ActionListener l) { verifyNgosBtn.addActionListener(l); }
    public void setLogoutListener(ActionListener l) { logoutBtn.addActionListener(l); }

    /** Shows or hides the admin-only "Verify NGOs" link, e.g. based on the signed-in user's role. */
    public void setAdminLinksVisible(boolean visible) { verifyNgosBtn.setVisible(visible); }
}
