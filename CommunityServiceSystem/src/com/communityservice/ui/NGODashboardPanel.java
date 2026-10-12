/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class NGODashboardPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color ACTIVE_BLUE =
            UIColors.PRIMARY_LIGHT;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private final Map<String, RoundedNavButton>
            navigationButtons =
            new HashMap<>();

    private JLabel pageTitleLabel;

    
    private final int ngoUserID;


    
    private NGOMainPanel dashboardPanel;

    private MyEventPanel myEventPanel;

    private VolunteerManagementPanel volunteerManagementPanel;

    private ApplicationPanel applicationPanel;

    private AttendancePanel attendancePanel;

    private ReportPanel reportPanel;

    private OrganizationProfilePanel organizationProfilePanel;


    
    public NGODashboardPanel() {

        this(1);
    }


    
    public NGODashboardPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(
                new BorderLayout()
        );

        setBackground(
                UIColors.BACKGROUND
        );


        
        add(
                createTopBar(),
                BorderLayout.NORTH
        );


        

        add(
                createSidebar(),
                BorderLayout.WEST
        );


        
        cardLayout =
                new CardLayout();

        contentPanel =
                new JPanel(
                        cardLayout
                );

        contentPanel.setBackground(
                UIColors.BACKGROUND
        );


        
        dashboardPanel =
                new NGOMainPanel(
                        ngoUserID,
                        () -> {

                            showPage(
                                    "createEvent"
                            );

                            pageTitleLabel.setText(
                                    "Create Event"
                            );
                        }
                );


        contentPanel.add(
                dashboardPanel,
                "dashboard"
        );


        

        CreateEventPanel createEventPanel =
                new CreateEventPanel(
                        ngoUserID
                );


        contentPanel.add(
                createEventPanel,
                "createEvent"
        );


        
        myEventPanel =
                new MyEventPanel(
                        ngoUserID
                );


        contentPanel.add(
                myEventPanel,
                "myEvent"
        );


        
        volunteerManagementPanel =
                new VolunteerManagementPanel(
                        ngoUserID
                );


        contentPanel.add(
                volunteerManagementPanel,
                "volunteers"
        );


        

        applicationPanel =
                new ApplicationPanel(
                        ngoUserID
                );


        contentPanel.add(
                applicationPanel,
                "application"
        );


        
        attendancePanel =
                new AttendancePanel(
                        ngoUserID
                );


        contentPanel.add(
                attendancePanel,
                "attendance"
        );


        

        reportPanel =
                new ReportPanel(
                        ngoUserID
                );


        contentPanel.add(
                reportPanel,
                "reports"
        );


        
        organizationProfilePanel =
                new OrganizationProfilePanel(
                        ngoUserID
                );


        contentPanel.add(
                organizationProfilePanel,
                "organizationProfile"
        );


        
        SharedSettingsPanel settingsPanel =
                new SharedSettingsPanel(
                        ngoUserID
                );


        contentPanel.add(
                settingsPanel,
                "settings"
        );


        
        add(
                contentPanel,
                BorderLayout.CENTER
        );


        
        showPage(
                "dashboard"
        );

        pageTitleLabel.setText(
                "Dashboard"
        );
    }


    
    public int getNgoUserID() {

        return ngoUserID;
    }


    

    private String getOrganisationName() {
        try {
            com.communityservice.models.NGO profile =
                    new com.communityservice.dao.NGODAO().getNGOByUserId(ngoUserID);
            if (profile != null && profile.getOrgName() != null
                    && !profile.getOrgName().trim().isEmpty()) {
                return profile.getOrgName().trim();
            }
        } catch (Exception ignored) { }
        return "Organisation";
    }

    private JPanel createTopBar() {

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );


        topBar.setBackground(
                BLUE
        );


        topBar.setPreferredSize(
                new Dimension(
                        0,
                        62
                )
        );


        
        JLabel brand =
                new JLabel(
                        "Community Service+"
                );


        brand.setForeground(
                Color.WHITE
        );


        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        brand.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        10,
                        0,
                        0
                )
        );


        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brandRow.setOpaque(false);
        brandRow.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        brandRow.add(brand);

        topBar.add(
                brandRow,
                BorderLayout.WEST
        );


        
        pageTitleLabel =
                new JLabel(
                        "Dashboard",
                        SwingConstants.CENTER
                );


        pageTitleLabel.setForeground(
                new Color(
                        190,
                        200,
                        225
                )
        );


        pageTitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );


        topBar.add(
                pageTitleLabel,
                BorderLayout.CENTER
        );


        
        JPanel ngoPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                10
                        )
                );


        ngoPanel.setBackground(
                BLUE
        );


        ngoPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        5,
                        0,
                        18
                )
        );


        ngoPanel.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        JLabel avatar =
                createProfileIcon();


        JLabel ngo =
                new JLabel(
                        getOrganisationName()
                );


        ngo.setForeground(
                Color.WHITE
        );


        ngo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        ngo.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        avatar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        ngoPanel.add(
                avatar
        );


        ngoPanel.add(
                ngo
        );


       
        JPopupMenu profileMenu =
                new JPopupMenu();


        JMenuItem profileItem =
                new JMenuItem(
                        "Organization Profile"
                );


        JMenuItem settingsItem =
                new JMenuItem(
                        "Settings"
                );


        JMenuItem logoutItem =
                new JMenuItem(
                        "Logout"
                );


        profileMenu.add(
                profileItem
        );


        profileMenu.add(
                settingsItem
        );


        profileMenu.addSeparator();


        profileMenu.add(
                logoutItem
        );


        
        profileItem.addActionListener(
                e -> {

                    showPage(
                            "organizationProfile"
                    );


                    pageTitleLabel.setText(
                            "Organization Profile"
                    );
                }
        );


        
        settingsItem.addActionListener(
                e -> {

                    showPage(
                            "settings"
                    );


                    pageTitleLabel.setText(
                            "Settings"
                    );
                }
        );


        
        logoutItem.addActionListener(
                e -> logout()
        );


        
        MouseAdapter profileClickListener =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        profileMenu.show(
                                ngoPanel,
                                ngoPanel.getWidth()
                                        - profileMenu
                                        .getPreferredSize()
                                        .width,
                                ngoPanel.getHeight()
                        );
                    }
                };


        ngoPanel.addMouseListener(
                profileClickListener
        );


        avatar.addMouseListener(
                profileClickListener
        );


        ngo.addMouseListener(
                profileClickListener
        );


        topBar.add(
                ngoPanel,
                BorderLayout.EAST
        );


        return topBar;
    }


    
    /** Set by the host app so "Logout" returns to the login screen instead of closing the whole window. */
    private Runnable logoutAction;

    public void setLogoutListener(Runnable logoutAction) {
        this.logoutAction = logoutAction;
    }

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (
                choice != JOptionPane.YES_OPTION
        ) {

            return;
        }

        if (logoutAction != null) {
            logoutAction.run();
            return;
        }

        Window window =
                SwingUtilities.getWindowAncestor(
                        this
                );


        if (
                window != null
        ) {

            window.dispose();
        }
    }


    
    private RoundPersonIcon createProfileIcon() {
        return new RoundPersonIcon(34, Color.WHITE, BLUE);
    }


    
    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();


        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );


        sidebar.setBackground(
                BLUE
        );


        sidebar.setPreferredSize(
                new Dimension(
                        195,
                        0
                )
        );


        sidebar.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        8,
                        12,
                        8
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Dashboard",
                        "dashboard",
                        "Dashboard"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


       

        sidebar.add(
                createSidebarButton(
                        "Create Events",
                        "createEvent",
                        "Create Event"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "My Event",
                        "myEvent",
                        "My Event"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        
        sidebar.add(
                createSidebarButton(
                        "Volunteers",
                        "volunteers",
                        "Volunteer Management"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Applications",
                        "application",
                        "Applications"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Attendance",
                        "attendance",
                        "Attendance"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Reports",
                        "reports",
                        "Reports"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Organization profile",
                        "organizationProfile",
                        "Organization Profile"
                )
        );


        sidebar.add(
                Box.createVerticalStrut(
                        4
                )
        );


        

        sidebar.add(
                createSidebarButton(
                        "Settings",
                        "settings",
                        "Settings"
                )
        );


        sidebar.add(Box.createVerticalGlue());

        RoundedNavButton logoutButton = new RoundedNavButton("Logout");
        logoutButton.addActionListener(e -> logout());
        sidebar.add(logoutButton);

        return sidebar;
    }


    

    private RoundedNavButton createSidebarButton(
            String text,
            String page,
            String title
    ) {

        RoundedNavButton button =
                new RoundedNavButton(
                        text
                );


        button.addActionListener(
                e -> {

                    showPage(
                            page
                    );


                    pageTitleLabel.setText(
                            title
                    );
                }
        );


        navigationButtons.put(
                page,
                button
        );


        return button;
    }


    
    public void showPage(
            String name
    ) {

        
        if (
                "dashboard".equals(name)
                        && dashboardPanel != null
        ) {

            dashboardPanel.refreshData();
        }


        
        if (
                "myEvent".equals(name)
                        && myEventPanel != null
        ) {

            myEventPanel.refreshData();
        }


        
        if (
                "volunteers".equals(name)
                        && volunteerManagementPanel != null
        ) {

            volunteerManagementPanel.refreshData();
        }


        
        if (
                "application".equals(name)
                        && applicationPanel != null
        ) {

            applicationPanel.refreshData();
        }


       

        if (
                "attendance".equals(name)
                        && attendancePanel != null
        ) {

            attendancePanel.refreshData();
        }


        
        if (
                "reports".equals(name)
                        && reportPanel != null
        ) {

            reportPanel.refreshData();
        }


        
        if (
                "organizationProfile".equals(name)
                        && organizationProfilePanel != null
        ) {

            organizationProfilePanel.refreshData();
        }


        

        cardLayout.show(
                contentPanel,
                name
        );


        
        for (
                Map.Entry<
                        String,
                        RoundedNavButton
                        > entry
                : navigationButtons.entrySet()
        ) {

            entry.getValue()
                    .setActive(
                            entry.getKey().equals(name)
                    );
        }
    }


    

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    JFrame frame =
                            new JFrame(
                                    "ngos Dashboard"
                            );


                    frame.setDefaultCloseOperation(
                            JFrame.EXIT_ON_CLOSE
                    );


                    frame.setSize(
                            1180,
                            780
                    );


                    frame.setMinimumSize(
                            new Dimension(
                                    1080,
                                    700
                            )
                    );


                    frame.setLocationRelativeTo(
                            null
                    );


                    

                    frame.setContentPane(
                            new NGODashboardPanel(
                                    1
                            )
                    );


                    frame.setVisible(
                            true
                    );
                }
        );
    }


    

}
