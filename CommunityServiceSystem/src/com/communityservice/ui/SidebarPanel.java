/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.communityservice.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Sidebar navigation for the PortalVolunteer System Dashboard.
 *
 * Handles:
 * - Dashboard
 * - Available Opportunities
 * - My Applications
 * - My Events
 * - Hours Log
 * - Ranking
 * - Reports
 * - Profile
 * - Settings
 * - Logout
 *
 * The active page is visually highlighted.
 */
public class SidebarPanel extends JPanel {

    // =========================================================
    // MAIN DASHBOARD FRAME
    // =========================================================

    private final VolunteerDashboardPanel dashboardFrame;

    // =========================================================
    // NAVIGATION BUTTONS
    // =========================================================

    private JButton dashboardButton;
    private JButton opportunitiesButton;
    private JButton applicationsButton;
    private JButton eventsButton;
    private JButton hoursButton;
    private JButton rankingButton;
    private JButton reportsButton;
    private JButton certificatesButton;
    private JButton profileButton;
    private JButton settingsButton;
    private JButton logoutButton;

    // =========================================================
    // BUTTON COLORS
    // =========================================================

    private static final Color NORMAL_COLOR =
            UIColors.SIDEBAR;

    private static final Color HOVER_COLOR =
            UIColors.PRIMARY_LIGHT;

    private static final Color ACTIVE_COLOR =
            UIColors.PRIMARY_LIGHT;

    private static final Color TEXT_COLOR =
            Color.WHITE;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SidebarPanel(VolunteerDashboardPanel dashboardFrame) {

        this.dashboardFrame = dashboardFrame;

        initialize();
    }

    // =========================================================
    // INITIALIZE SIDEBAR
    // =========================================================

    private void initialize() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                UIColors.SIDEBAR
        );

        setPreferredSize(
                new Dimension(
                        195,
                        0
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        // =====================================================
        // TOP SECTION
        // =====================================================

        JPanel topPanel =
                new JPanel();

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS
                )
        );

        topPanel.setBackground(
                UIColors.SIDEBAR
        );

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        14,
                        10,
                        14
                )
        );

        // =====================================================
        // (brand header removed — now handled by the shared top bar)
        // =====================================================

        topPanel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        // =====================================================
        // CREATE NAVIGATION BUTTONS
        // =====================================================

        dashboardButton =
                createNavigationButton(
                        "Dashboard"
                );

        opportunitiesButton =
                createNavigationButton(
                        "Available Opportunities"
                );

        applicationsButton =
                createNavigationButton(
                        "My Applications"
                );

        eventsButton =
                createNavigationButton(
                        "My Events"
                );

        hoursButton =
                createNavigationButton(
                        "Hours Log"
                );

        rankingButton =
                createNavigationButton(
                        "Ranking"
                );

        reportsButton =
                createNavigationButton(
                        "Reports"
                );

        certificatesButton = createNavigationButton("Certificates");

        profileButton =
                createNavigationButton(
                        "Profile"
                );

        settingsButton =
                createNavigationButton(
                        "Settings"
                );

        // =====================================================
        // ADD DASHBOARD
        // =====================================================

        topPanel.add(
                dashboardButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD AVAILABLE OPPORTUNITIES
        // =====================================================

        topPanel.add(
                opportunitiesButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD MY APPLICATIONS
        // =====================================================

        topPanel.add(
                applicationsButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD MY EVENTS
        // =====================================================

        topPanel.add(
                eventsButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD HOURS LOG
        // =====================================================

        topPanel.add(
                hoursButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD RANKING
        // =====================================================

        topPanel.add(
                rankingButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD REPORTS
        // =====================================================

        topPanel.add(reportsButton);
        topPanel.add(Box.createVerticalStrut(7));
        topPanel.add(certificatesButton);
        topPanel.add(Box.createVerticalStrut(7));

        // =====================================================
        // ADD PROFILE
        // =====================================================

        topPanel.add(
                profileButton
        );

        topPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );

        // =====================================================
        // ADD SETTINGS
        // =====================================================

        topPanel.add(
                settingsButton
        );

        // =====================================================
        // ADD TOP PANEL
        // =====================================================

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // LOGOUT SECTION
        // =====================================================

        JPanel bottomPanel =
                new JPanel();

        bottomPanel.setLayout(
                new BorderLayout()
        );

        bottomPanel.setBackground(
                UIColors.SIDEBAR
        );

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        14,
                        20,
                        14
                )
        );

        logoutButton =
                createNavigationButton(
                        "Logout"
                );

        bottomPanel.add(
                logoutButton,
                BorderLayout.CENTER
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        // -----------------------------------------------------
        // DASHBOARD
        // -----------------------------------------------------

        dashboardButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "dashboard"
                    );

                    setActiveButton(
                            dashboardButton
                    );
                }
        );

        // -----------------------------------------------------
        // AVAILABLE OPPORTUNITIES
        // -----------------------------------------------------

        opportunitiesButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "events"
                    );

                    setActiveButton(
                            opportunitiesButton
                    );
                }
        );

        // -----------------------------------------------------
        // MY APPLICATIONS
        // -----------------------------------------------------

        applicationsButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "applications"
                    );

                    setActiveButton(
                            applicationsButton
                    );
                }
        );

        // -----------------------------------------------------
        // MY EVENTS
        // -----------------------------------------------------

        eventsButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "my-events"
                    );

                    setActiveButton(
                            eventsButton
                    );
                }
        );

        // -----------------------------------------------------
        // HOURS LOG
        // -----------------------------------------------------

        hoursButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "hours"
                    );

                    setActiveButton(
                            hoursButton
                    );
                }
        );

        // -----------------------------------------------------
        // RANKING
        // -----------------------------------------------------

        rankingButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "leaderboard"
                    );

                    setActiveButton(
                            rankingButton
                    );
                }
        );

        // -----------------------------------------------------
        // REPORTS
        // -----------------------------------------------------

        reportsButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "reports"
                    );

                    setActiveButton(
                            reportsButton
                    );
                }
        );

        // -----------------------------------------------------
        // CERTIFICATES
        // -----------------------------------------------------
        certificatesButton.addActionListener(e -> {
            dashboardFrame.showPage("certificates");
            setActiveButton(certificatesButton);
        });

        // -----------------------------------------------------
        // PROFILE
        // -----------------------------------------------------

        profileButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "profile"
                    );

                    setActiveButton(
                            profileButton
                    );
                }
        );

        // -----------------------------------------------------
        // SETTINGS
        // -----------------------------------------------------

        settingsButton.addActionListener(
                e -> {

                    dashboardFrame.showPage(
                            "settings"
                    );

                    setActiveButton(
                            settingsButton
                    );
                }
        );

        // -----------------------------------------------------
        // LOGOUT
        // -----------------------------------------------------

        logoutButton.addActionListener(
                e -> handleLogout()
        );

        // =====================================================
        // DEFAULT ACTIVE PAGE
        // =====================================================

        setActiveButton(
                dashboardButton
        );
    }

    // =========================================================
    // CREATE NAVIGATION BUTTON
    // =========================================================

    private JButton createNavigationButton(
            String text
    ) {
        return new RoundedNavButton(text);
    }

    // =========================================================
    // SET ACTIVE BUTTON
    // =========================================================

    private void setActiveButton(
            JButton activeButton
    ) {

        JButton[] buttons = {

                dashboardButton,
                opportunitiesButton,
                applicationsButton,
                eventsButton,
                hoursButton,
                rankingButton,
                reportsButton,
                certificatesButton,
                profileButton,
                settingsButton

        };

        for (
                JButton button :
                buttons
        ) {

            if (
                    button == null
            ) {

                continue;
            }

            if (button instanceof RoundedNavButton) {
                ((RoundedNavButton) button).setActive(button == activeButton);
            }
        }
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout() {

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                result != JOptionPane.YES_OPTION
        ) {

            return;
        }

        dashboardFrame.triggerLogout();
    }
}