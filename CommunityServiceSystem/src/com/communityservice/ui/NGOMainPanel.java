/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.NGODashboardDAO;
import com.communityservice.dao.NGODAO;
import com.communityservice.models.NGO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NGOMainPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private static final Color TEXT_GRAY =
            UIColors.TEXT_GREY;

    // Logged-in ngos user
    private final int ngoUserID;

    // Dashboard statistic labels
    private JLabel totalEventsValue;
    private JLabel openEventsValue;
    private JLabel totalApplicationsValue;
    private JLabel pendingApplicationsValue;

    
    private JPanel lowerSection;

    
    private final Runnable openCreateEventAction;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NGOMainPanel(
            int ngoUserID,
            Runnable openCreateEventAction
    ) {

        this.ngoUserID = ngoUserID;
        this.openCreateEventAction = openCreateEventAction;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainContent = new JPanel();

        mainContent.setLayout(
                new BoxLayout(
                        mainContent,
                        BoxLayout.Y_AXIS
                )
        );

        mainContent.setBackground(BACKGROUND);

        mainContent.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        28,
                        25,
                        28
                )
        );


        // =====================================================
        // WELCOME SECTION
        // =====================================================

        JPanel welcomePanel = new JPanel();

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        welcomePanel.setBackground(BACKGROUND);

        welcomePanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel heading =
                new JLabel(
                        "Welcome, " + getOrganisationName()
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        heading.setForeground(BLUE);

        heading.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel subtitle =
                new JLabel(
                        "Here is what's happening with your volunteer opportunities."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(TEXT_GRAY);

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        welcomePanel.add(heading);

        welcomePanel.add(
                Box.createVerticalStrut(7)
        );

        welcomePanel.add(subtitle);


        mainContent.add(welcomePanel);

        mainContent.add(
                Box.createVerticalStrut(25)
        );


        // =====================================================
        // STATISTIC CARDS
        // =====================================================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        cardsPanel.setBackground(BACKGROUND);

        cardsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        cardsPanel.setPreferredSize(
                new Dimension(
                        900,
                        145
                )
        );

        cardsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        totalEventsValue =
                createValueLabel();

        openEventsValue =
                createValueLabel();

        totalApplicationsValue =
                createValueLabel();

        pendingApplicationsValue =
                createValueLabel();


        cardsPanel.add(
                createCard(
                        "Total Events",
                        totalEventsValue,
                        "Events created"
                )
        );


        cardsPanel.add(
                createCard(
                        "Open Events",
                        openEventsValue,
                        "Currently active"
                )
        );


        cardsPanel.add(
                createCard(
                        "Applications",
                        totalApplicationsValue,
                        "Total received"
                )
        );


        cardsPanel.add(
                createCard(
                        "Pending",
                        pendingApplicationsValue,
                        "Needs review"
                )
        );


        mainContent.add(cardsPanel);

        mainContent.add(
                Box.createVerticalStrut(25)
        );


        // =====================================================
        // LOWER SECTION
        // =====================================================

        lowerSection =
                createLowerSection();

        mainContent.add(
                lowerSection
        );

        mainContent.add(
                Box.createVerticalStrut(20)
        );


        // =====================================================
        // CREATE EVENT BUTTON
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        buttonPanel.setBackground(BACKGROUND);

        buttonPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JButton createEventButton =
                new JButton(
                        "+ Create New Event"
                );

        createEventButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        createEventButton.setForeground(
                Color.WHITE
        );

        createEventButton.setBackground(
                TEAL
        );

        createEventButton.setFocusPainted(
                false
        );

        createEventButton.setPreferredSize(
                new Dimension(
                        180,
                        42
                )
        );


        createEventButton.addActionListener(
                e -> {

                    if (
                            openCreateEventAction != null
                    ) {

                        openCreateEventAction.run();
                    }
                }
        );


        buttonPanel.add(
                createEventButton
        );


        mainContent.add(
                buttonPanel
        );


        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        mainContent
                );

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =====================================================
        // INITIAL DATABASE LOAD
        // =====================================================

        loadDashboardData();
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        // Refresh statistic cards
        loadDashboardData();

        /*
         * Recent Events and Recent Applications contain
         * components that were created using database data.
         *
         * Therefore we rebuild this section when Dashboard
         * is opened again.
         */

        Container parent =
                lowerSection.getParent();


        if (
                parent != null
        ) {

            int position = -1;

            Component[] components =
                    parent.getComponents();


            for (
                    int i = 0;
                    i < components.length;
                    i++
            ) {

                if (
                        components[i] == lowerSection
                ) {

                    position = i;
                    break;
                }
            }


            if (
                    position != -1
            ) {

                parent.remove(
                        lowerSection
                );


                lowerSection =
                        createLowerSection();


                parent.add(
                        lowerSection,
                        position
                );


                parent.revalidate();
                parent.repaint();
            }
        }
    }


    // =========================================================
    // CREATE LOWER SECTION
    // =========================================================

    private JPanel createLowerSection() {

        JPanel section =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                18,
                                0
                        )
                );

        section.setBackground(
                BACKGROUND
        );

        section.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        section.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        270
                )
        );

        section.setPreferredSize(
                new Dimension(
                        900,
                        270
                )
        );


        JPanel eventsPanel =
                createRecentEventsPanel();


        JPanel applicationsPanel =
                createRecentApplicationsPanel();


        section.add(
                eventsPanel
        );

        section.add(
                applicationsPanel
        );


        return section;
    }


    // =========================================================
    // RECENT EVENTS
    // =========================================================

    private JPanel createRecentEventsPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        225,
                                        225
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "Recent Events"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                BLUE
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        panel.add(title);

        panel.add(
                Box.createVerticalStrut(15)
        );


        List<Object[]> events =
                NGODashboardDAO
                        .getRecentEvents(
                                ngoUserID
                        );


        if (
                events.isEmpty()
        ) {

            JLabel empty =
                    new JLabel(
                            "No events created yet."
                    );

            empty.setForeground(
                    TEXT_GRAY
            );

            empty.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            panel.add(empty);

            return panel;
        }


        for (
                Object[] event : events
        ) {

            String eventName =
                    event[1] == null
                            ? "Unnamed Event"
                            : event[1].toString();


            String eventDate =
                    event[2] == null
                            ? "No date"
                            : event[2].toString();


            String status =
                    event[3] == null
                            ? ""
                            : event[3].toString();


            JLabel eventNameLabel =
                    new JLabel(
                            eventName
                    );

            eventNameLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            eventNameLabel.setForeground(
                    BLUE
            );

            eventNameLabel.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );


            JLabel detailsLabel =
                    new JLabel(
                            eventDate
                                    + "   •   "
                                    + status
                    );

            detailsLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            12
                    )
            );

            detailsLabel.setForeground(
                    TEXT_GRAY
            );

            detailsLabel.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );


            panel.add(
                    eventNameLabel
            );

            panel.add(
                    Box.createVerticalStrut(3)
            );

            panel.add(
                    detailsLabel
            );

            panel.add(
                    Box.createVerticalStrut(14)
            );
        }


        return panel;
    }


    // =========================================================
    // RECENT APPLICATIONS
    // =========================================================

    private JPanel createRecentApplicationsPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        225,
                                        225
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "Recent Applications"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                BLUE
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        panel.add(title);

        panel.add(
                Box.createVerticalStrut(15)
        );


        List<Object[]> applications =
                NGODashboardDAO
                        .getRecentApplications(
                                ngoUserID
                        );


        if (
                applications.isEmpty()
        ) {

            JLabel empty =
                    new JLabel(
                            "No applications received yet."
                    );

            empty.setForeground(
                    TEXT_GRAY
            );

            empty.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            panel.add(empty);

            return panel;
        }


        for (
                Object[] application : applications
        ) {

            String volunteerName =
                    application[1] == null
                            ? "Unknown Volunteer"
                            : application[1].toString();


            String eventName =
                    application[2] == null
                            ? "Unknown Event"
                            : application[2].toString();


            String application_date =
                    application[3] == null
                            ? "No date"
                            : application[3].toString();


            String status =
                    application[4] == null
                            ? ""
                            : application[4].toString();


            JLabel volunteerLabel =
                    new JLabel(
                            volunteerName
                    );

            volunteerLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            volunteerLabel.setForeground(
                    BLUE
            );

            volunteerLabel.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );


            JLabel eventLabel =
                    new JLabel(
                            "Applied for: "
                                    + eventName
                    );

            eventLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            12
                    )
            );

            eventLabel.setForeground(
                    TEXT_GRAY
            );

            eventLabel.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );


            JLabel detailsLabel =
                    new JLabel(
                            status
                                    + "   •   "
                                    + application_date
                    );

            detailsLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            12
                    )
            );

            detailsLabel.setForeground(
                    TEXT_GRAY
            );

            detailsLabel.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );


            panel.add(
                    volunteerLabel
            );

            panel.add(
                    Box.createVerticalStrut(3)
            );

            panel.add(
                    eventLabel
            );

            panel.add(
                    Box.createVerticalStrut(3)
            );

            panel.add(
                    detailsLabel
            );

            panel.add(
                    Box.createVerticalStrut(14)
            );
        }


        return panel;
    }


    // =========================================================
    // VALUE LABEL
    // =========================================================

    private JLabel createValueLabel() {

        JLabel label =
                new JLabel(
                        "0"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        label.setForeground(
                TEAL
        );


        return label;
    }


    // =========================================================
    // STATISTIC CARD
    // =========================================================

    private JPanel createCard(
            String title,
            JLabel value,
            String description
    ) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        225,
                                        225
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );


        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        titleLabel.setForeground(
                BLUE
        );


        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        descriptionLabel.setForeground(
                TEXT_GRAY
        );


        titleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        value.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        descriptionLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        card.add(
                titleLabel
        );

        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(
                value
        );

        card.add(
                Box.createVerticalStrut(5)
        );

        card.add(
                descriptionLabel
        );


        return card;
    }


    // =========================================================
    // LOAD DASHBOARD DATABASE DATA
    // =========================================================

    private void loadDashboardData() {

        totalEventsValue.setText(
                String.valueOf(
                        NGODashboardDAO
                                .getTotalEvents(
                                        ngoUserID
                                )
                )
        );


        openEventsValue.setText(
                String.valueOf(
                        NGODashboardDAO
                                .getOpenEvents(
                                        ngoUserID
                                )
                )
        );


        totalApplicationsValue.setText(
                String.valueOf(
                        NGODashboardDAO
                                .getTotalApplications(
                                        ngoUserID
                                )
                )
        );


        pendingApplicationsValue.setText(
                String.valueOf(
                        NGODashboardDAO
                                .getPendingApplications(
                                        ngoUserID
                                )
                )
        );
    }

    private String getOrganisationName() {
        try {
            NGO ngo = new NGODAO().getNGOByUserId(ngoUserID);
            if (ngo != null && ngo.getOrgName() != null && !ngo.getOrgName().trim().isEmpty()) {
                return ngo.getOrgName().trim();
            }
        } catch (Exception ignored) { }
        return "Organisation";
    }
}