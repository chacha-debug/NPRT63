package com.communityservice.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import com.communityservice.dao.VolunteerPortalUserDAO;
import com.communityservice.models.PortalUser;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Main content area for the volunteer dashboard: a top bar (brand + page
 * title + account icon), a sidebar (west), and a CardLayout content area
 * (center) switching between the volunteer's pages (Dashboard, Events,
 * Hours, Leaderboard, Reports, Profile, Settings, My Applications).
 * Embedded as a single card in the host app's own CardLayout. Styled
 * identically to the NGO and admin dashboards (same top bar, same
 * RoundedNavButton sidebar, same RoundPersonIcon) for a consistent look.
 */
public class VolunteerDashboardPanel extends JPanel {

    private final int user_id;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel pageTitleLabel;

    /** Set by the host app so "Logout" returns to the login screen instead of doing nothing. */
    private Runnable logoutAction;

    public VolunteerDashboardPanel(int user_id) {
        this.user_id = user_id;
        initializeLayout();
    }

    public void setLogoutListener(Runnable logoutAction) {
        this.logoutAction = logoutAction;
    }

    /** Called by SidebarPanel after the user confirms they want to log out. */
    public void triggerLogout() {
        if (logoutAction != null) {
            logoutAction.run();
        }
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        add(createTopBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIColors.BACKGROUND);

        DashboardPanel dashboard = new DashboardPanel(user_id);
        EventsPanel events = new EventsPanel(user_id);
        VolunteerMyEventsPanel myEvents = new VolunteerMyEventsPanel(user_id);
        HoursLogPanel hours = new HoursLogPanel(user_id);
        LeaderboardPanel leaderboard = new LeaderboardPanel();
        ReportsPanel reports = new ReportsPanel(user_id);
        CertificatesPanel certificates = new CertificatesPanel(user_id);
        ProfilePanel profile = new ProfilePanel(user_id);
        SharedSettingsPanel settings = new SharedSettingsPanel(user_id);
        MyApplicationsPanel applications = new MyApplicationsPanel(user_id);

        contentPanel.add(dashboard, "dashboard");
        contentPanel.add(events, "events");
        contentPanel.add(myEvents, "my-events");
        contentPanel.add(hours, "hours");
        contentPanel.add(leaderboard, "leaderboard");
        contentPanel.add(reports, "reports");
        contentPanel.add(certificates, "certificates");
        contentPanel.add(profile, "profile");
        contentPanel.add(settings, "settings");
        contentPanel.add(applications, "applications");

        SidebarPanel sidebar = new SidebarPanel(this);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        showPage("dashboard");
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIColors.PRIMARY);
        topBar.setPreferredSize(new Dimension(0, 62));

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        JLabel brand = new JLabel("Community Service+");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Arial", Font.BOLD, 20));
        brandRow.add(brand);
        topBar.add(brandRow, BorderLayout.WEST);

        pageTitleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        pageTitleLabel.setForeground(Color.WHITE);
        pageTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(pageTitleLabel, BorderLayout.CENTER);

        JPanel accountRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accountRow.setOpaque(false);
        accountRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        JLabel roleLabel = new JLabel(getDisplayName());
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        accountRow.add(roleLabel);
        accountRow.add(new RoundPersonIcon(34, Color.WHITE, UIColors.PRIMARY));
        topBar.add(accountRow, BorderLayout.EAST);

        return topBar;
    }

    public void showPage(String page) {
        cardLayout.show(contentPanel, page);
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(pageTitleFor(page));
        }
    }

    private String pageTitleFor(String page) {
        switch (page) {
            case "events": return "Available Opportunities";
            case "my-events": return "My Events";
            case "hours": return "Hours Log";
            case "leaderboard": return "Leaderboard";
            case "reports": return "Reports";
            case "certificates": return "Certificates";
            case "profile": return "Profile";
            case "settings": return "Settings";
            case "applications": return "My Applications";
            default: return "Dashboard";
        }
    }

    private String getDisplayName() {
        try {
            PortalUser user = new VolunteerPortalUserDAO().getUserByID(user_id);
            if (user != null && user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
                return user.getFullName().trim();
            }
        } catch (Exception ignored) { }
        return "Volunteer";
    }

    public int getUserID() {
        return user_id;
    }
}
