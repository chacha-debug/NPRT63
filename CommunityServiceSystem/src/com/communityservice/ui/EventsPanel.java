/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.OpportunityApplicationDAO;
import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.dao.VolunteerOpportunityDAO;

import com.communityservice.models.OpportunityApplication;
import com.communityservice.models.PortalVolunteer;
import com.communityservice.models.VolunteerOpportunity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Events Panel
 *
 * Displays upcoming volunteers opportunities.
 *
 * Features:
 * - Loads opportunities from the database
 * - Displays only upcoming open opportunities
 * - Displays available slots
 * - Allows volunteers to view opportunity details
 * - Allows volunteers to apply
 * - Detects existing applications
 * - Displays application status
 * - Refreshes data from the database
 */
public class EventsPanel extends JPanel {

    // =========================================================
    // USER
    // =========================================================

    private final int user_id;


    // =========================================================
    // DATABASE / DAO
    // =========================================================

    private final VolunteerPortalVolunteerDAO volunteerDAO;

    private final VolunteerOpportunityDAO opportunityDAO;

    private final OpportunityApplicationDAO applicationDAO;


    // =========================================================
    // UI
    // =========================================================

    private JPanel contentPanel;

    private JLabel opportunityCountLabel;


    // =========================================================
    // DATE FORMAT
    // =========================================================

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern(
                    "MMM dd, yyyy"
            );


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public EventsPanel(int user_id) {

        this.user_id = user_id;

        volunteerDAO =
                new VolunteerPortalVolunteerDAO();

        opportunityDAO =
                new VolunteerOpportunityDAO();

        applicationDAO =
                new OpportunityApplicationDAO();

        initialize();

        loadOpportunities();
    }


    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initialize() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                UIColors.BACKGROUND
        );


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                createHeader();

        add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // CONTENT
        // =====================================================

        contentPanel =
                new JPanel();

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setBackground(
                UIColors.BACKGROUND
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        30,
                        25
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        contentPanel
                );

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollPane.getViewport()
                .setBackground(
                        UIColors.BACKGROUND
                );


        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                UIColors.PRIMARY
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );


        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                UIColors.PRIMARY
        );


        JLabel title =
                new JLabel(
                        "Volunteer Opportunities"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(
                Color.WHITE
        );


        JLabel subtitle =
                new JLabel(
                        "Discover upcoming opportunities and apply to volunteers."
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                new Color(
                        225,
                        232,
                        248
                )
        );


        opportunityCountLabel =
                new JLabel(
                        "Loading opportunities..."
                );

        opportunityCountLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        opportunityCountLabel.setForeground(
                new Color(
                        210,
                        220,
                        242
                )
                );


        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        titlePanel.add(
                subtitle
        );

        titlePanel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        titlePanel.add(
                opportunityCountLabel
        );


        header.add(
                titlePanel,
                BorderLayout.WEST
        );


        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        JButton refreshButton =
                createHeaderButton(
                        "Refresh"
                );


        refreshButton.addActionListener(
                e -> loadOpportunities()
        );


        header.add(
                refreshButton,
                BorderLayout.EAST
        );


        return header;
    }


    // =========================================================
    // HEADER BUTTON
    // =========================================================

    private JButton createHeaderButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                UIColors.PRIMARY
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        return button;
    }


    // =========================================================
    // LOAD OPPORTUNITIES
    // =========================================================

    private void loadOpportunities() {

        if (contentPanel == null) {
            return;
        }


        contentPanel.removeAll();


        opportunityCountLabel.setText(
                "Loading opportunities..."
        );


        List<VolunteerOpportunity> opportunities;


        try {

            opportunities =
                    opportunityDAO
                            .getOpenOpportunities();

        } catch (Exception e) {

            showErrorMessage(
                    "Unable to load opportunities.\n"
                    + "Please check your database connection."
            );

            opportunityCountLabel.setText(
                    "Unable to load opportunities"
            );

            refresh();

            return;
        }


        if (
                opportunities == null
                ||
                opportunities.isEmpty()
        ) {

            opportunityCountLabel.setText(
                    "No opportunities available"
            );

            showEmptyMessage(
                    "There are currently no opportunities available."
            );

            refresh();

            return;
        }


        LocalDate today =
                LocalDate.now();


        int displayed =
                0;


        for (
                VolunteerOpportunity opportunity :
                opportunities
        ) {


            // =================================================
            // DATE CHECK
            // =================================================

            if (
                    opportunity.getDate()
                    == null
            ) {

                continue;
            }


            if (
                    opportunity.getDate()
                            .isBefore(
                                    today
                            )
            ) {

                continue;
            }


            // =================================================
            // STATUS CHECK
            // =================================================

            if (
                    opportunity.getStatus()
                    == null
                    ||
                    !opportunity.getStatus()
                            .equalsIgnoreCase(
                                    "Open"
                            )
            ) {

                continue;
            }


            // =================================================
            // SLOT CHECK
            // =================================================

            if (
                    opportunity.getCurrentSlots()
                    >=
                    opportunity.getMaxSlots()
            ) {

                continue;
            }


            // =================================================
            // CARD
            // =================================================

            JPanel card =
                    createOpportunityCard(
                            opportunity
                    );


            contentPanel.add(
                    card
            );


            contentPanel.add(
                    Box.createVerticalStrut(
                            15
                    )
            );


            displayed++;
        }


        // =====================================================
        // RESULT
        // =====================================================

        if (
                displayed == 0
        ) {

            opportunityCountLabel.setText(
                    "No upcoming opportunities"
            );

            showEmptyMessage(
                    "There are no upcoming opportunities available."
            );

        } else {

            opportunityCountLabel.setText(
                    displayed
                    +
                    (
                            displayed == 1
                                    ? " opportunity available"
                                    : " opportunities available"
                    )
            );
        }


        refresh();
    }


    // =========================================================
    // CREATE OPPORTUNITY CARD
    // =========================================================

    private JPanel createOpportunityCard(
            VolunteerOpportunity opportunity
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        card.setBackground(
                UIColors.CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.BORDER
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        205
                )
        );


        // =====================================================
        // LEFT INFORMATION
        // =====================================================

        JPanel information =
                new JPanel();

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );

        information.setBackground(
                UIColors.CARD
        );


        // =====================================================
        // TITLE
        // =====================================================

        String titleText =
                opportunity.getTitle();


        if (
                titleText == null
                ||
                titleText.trim().isEmpty()
        ) {

            titleText =
                    "Opportunity #"
                    +
                    opportunity.getOppID();
        }


        JLabel title =
                new JLabel(
                        titleText
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(
                UIColors.TEXT_DARK
        );


        information.add(
                title
        );


        // =====================================================
        // CATEGORY
        // =====================================================

        if (
                opportunity.getCategory()
                != null
                &&
                !opportunity.getCategory()
                        .trim()
                        .isEmpty()
        ) {

            JLabel category =
                    new JLabel(
                            opportunity.getCategory()
                    );

            category.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            category.setForeground(
                    UIColors.PRIMARY
            );

            category.setOpaque(
                    true
            );

            category.setBackground(
                    UIColors.HIGHLIGHT
            );

            category.setBorder(
                    new EmptyBorder(
                            5,
                            9,
                            5,
                            9
                    )
            );


            information.add(
                    Box.createVerticalStrut(
                            7
                    )
            );


            information.add(
                    category
            );
        }


        // =====================================================
        // DATE
        // =====================================================

        if (
                opportunity.getDate()
                != null
        ) {

            JLabel date =
                    new JLabel(
                            "Date: "
                            +
                            opportunity.getDate()
                                    .format(
                                            dateFormatter
                                    )
                    );

            date.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            13
                    )
            );

            date.setForeground(
                    UIColors.TEXT_DARK
            );


            information.add(
                    Box.createVerticalStrut(
                            12
                    )
            );

            information.add(
                    date
            );
        }


        // =====================================================
        // TIME
        // =====================================================

        if (
                opportunity.getStartTime()
                != null
        ) {

            String timeText =
                    "Time: "
                    +
                    opportunity.getStartTime();


            if (
                    opportunity.getEndTime()
                    != null
            ) {

                timeText +=
                        " - "
                        +
                        opportunity.getEndTime();
            }


            JLabel time =
                    new JLabel(
                            timeText
                    );

            time.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            time.setForeground(
                    UIColors.TEXT_GREY
            );


            information.add(
                    Box.createVerticalStrut(
                            5
                    )
            );

            information.add(
                    time
            );
        }


        // =====================================================
        // LOCATION
        // =====================================================

        if (
                opportunity.getLocation()
                != null
                &&
                !opportunity.getLocation()
                        .trim()
                        .isEmpty()
        ) {

            JLabel location =
                    new JLabel(
                            "Location: "
                            +
                            opportunity.getLocation()
                    );

            location.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            location.setForeground(
                    UIColors.TEXT_GREY
            );


            information.add(
                    Box.createVerticalStrut(
                            5
                    )
            );

            information.add(
                    location
            );
        }


        // =====================================================
        // DESCRIPTION PREVIEW
        // =====================================================

        if (
                opportunity.getDescription()
                != null
                &&
                !opportunity.getDescription()
                        .trim()
                        .isEmpty()
        ) {

            String description =
                    opportunity.getDescription()
                            .trim();


            if (
                    description.length()
                    > 100
            ) {

                description =
                        description.substring(
                                0,
                                100
                        )
                        +
                        "...";
            }


            JLabel descriptionLabel =
                    new JLabel(
                            "<html>"
                            +
                            description
                            +
                            "</html>"
                    );

            descriptionLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            12
                    )
            );

            descriptionLabel.setForeground(
                    UIColors.TEXT_GREY
            );


            information.add(
                    Box.createVerticalStrut(
                            9
                    )
            );

            information.add(
                    descriptionLabel
            );
        }


        // =====================================================
        // SLOT INFORMATION
        // =====================================================

        int availableSlots =
                opportunity.getMaxSlots()
                -
                opportunity.getCurrentSlots();


        JLabel slots =
                new JLabel(
                        availableSlots
                        +
                        (
                                availableSlots == 1
                                        ? " slot available"
                                        : " slots available"
                        )
                );

        slots.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        slots.setForeground(
                UIColors.GREEN
        );


        information.add(
                Box.createVerticalStrut(
                        9
                )
        );

        information.add(
                slots
        );


        card.add(
                information,
                BorderLayout.CENTER
        );


        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel actions =
                new JPanel();

        actions.setLayout(
                new BoxLayout(
                        actions,
                        BoxLayout.Y_AXIS
                )
        );

        actions.setBackground(
                UIColors.CARD
        );

        actions.setBorder(
                new EmptyBorder(
                        5,
                        10,
                        5,
                        0
                )
        );


        // =====================================================
        // APPLICATION STATUS
        // =====================================================

        PortalVolunteer volunteers =
                volunteerDAO
                        .getVolunteerByUserID(
                                user_id
                        );


        OpportunityApplication existing =
                null;


        if (
                volunteers != null
        ) {

            existing =
                    applicationDAO
                            .getApplicationByVolunteerAndOpportunity(
                                    volunteers.getVolunteerID(),
                                    opportunity.getOppID()
                            );
        }


        // =====================================================
        // VIEW DETAILS BUTTON
        // =====================================================

        JButton detailsButton =
                createSecondaryButton(
                        "View Details"
                );


        detailsButton.addActionListener(
                e ->
                        showOpportunityDetails(
                                opportunity
                        )
        );


        actions.add(
                detailsButton
        );


        actions.add(
                Box.createVerticalStrut(
                        10
                )
        );


        // =====================================================
        // APPLY / STATUS BUTTON
        // =====================================================

        JButton applyButton;


        if (
                existing != null
        ) {

            String status =
                    existing.getStatus();


            if (
                    status == null
                    ||
                    status.trim().isEmpty()
            ) {

                status =
                        "Applied";
            }


            applyButton =
                    createStatusButton(
                            status
                    );

            applyButton.setEnabled(
                    false
            );

        } else {

            applyButton =
                    createPrimaryButton(
                            "Apply"
                    );


            applyButton.addActionListener(
                    e ->
                            applyForOpportunity(
                                    opportunity
                            )
            );
        }


        actions.add(
                applyButton
        );


        // =====================================================
        // CARD ACTIONS
        // =====================================================

        card.add(
                actions,
                BorderLayout.EAST
        );


        // =====================================================
        // HOVER EFFECT
        // =====================================================

        addHoverEffect(
                card
        );


        return card;
    }


    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                UIColors.PRIMARY
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        return button;
    }


    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                UIColors.PRIMARY
        );

        button.setBackground(
                UIColors.CARD
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        UIColors.BORDER_DARK
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        return button;
    }


    // =========================================================
    // STATUS BUTTON
    // =========================================================

    private JButton createStatusButton(
            String status
    ) {

        JButton button =
                new JButton(
                        status
                );


        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );


        button.setFocusPainted(
                false
        );


        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );


        button.setEnabled(
                false
        );


        // =====================================================
        // STATUS COLOUR
        // =====================================================

        if (
                status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            button.setForeground(
                    UIColors.GREEN
            );

            button.setBackground(
                    UIColors.GREEN_LIGHT
            );

        } else if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            button.setForeground(
                    UIColors.RED
            );

            button.setBackground(
                    UIColors.RED_LIGHT
            );

        } else if (
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            button.setForeground(
                    UIColors.ORANGE
            );

            button.setBackground(
                    UIColors.ORANGE_LIGHT
            );

        } else {

            button.setForeground(
                    UIColors.BLUE
            );

            button.setBackground(
                    UIColors.BLUE_LIGHT
            );
        }


        return button;
    }


    // =========================================================
    // APPLY
    // =========================================================

    private void applyForOpportunity(
            VolunteerOpportunity opportunity
    ) {

        PortalVolunteer volunteers =
                volunteerDAO
                        .getVolunteerByUserID(
                                user_id
                        );


        // =====================================================
        // VOLUNTEER CHECK
        // =====================================================

        if (
                volunteers == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Volunteer information could not be found.\n\n"
                    +
                    "Please make sure your volunteers profile exists.",
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =====================================================
        // DOUBLE CHECK APPLICATION
        // =====================================================

        OpportunityApplication existing =
                applicationDAO
                        .getApplicationByVolunteerAndOpportunity(
                                volunteers.getVolunteerID(),
                                opportunity.getOppID()
                        );


        if (
                existing != null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "You have already applied for this opportunity.\n\n"
                    +
                    "Current status: "
                    +
                    existing.getStatus(),
                    "Already Applied",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        // =====================================================
        // CONFIRMATION
        // =====================================================

        String title =
                opportunity.getTitle();


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Would you like to apply for:\n\n"
                        +
                        title
                        +
                        "\n\n"
                        +
                        "Your application will be sent to "
                        +
                        "the administrator for review.",
                        "Confirm Application",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (
                confirm
                !=
                JOptionPane.YES_OPTION
        ) {

            return;
        }


        // =====================================================
        // SUBMIT APPLICATION
        // =====================================================

        boolean success =
                applicationDAO.applyForOpportunity(
                        volunteers.getVolunteerID(),
                        opportunity.getOppID()
                );


        // =====================================================
        // SUCCESS
        // =====================================================

        if (
                success
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application submitted successfully!\n\n"
                    +
                    "Status: Pending\n\n"
                    +
                    "An administrator will review your application.",
                    "Application Submitted",
                    JOptionPane.INFORMATION_MESSAGE
            );


            loadOpportunities();


        } else {


            // =================================================
            // FAILURE
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to submit your application.\n\n"
                    +
                    "The opportunity may no longer be available, "
                    +
                    "or you may already have an application.",
                    "Application Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // VIEW DETAILS
    // =========================================================

    private void showOpportunityDetails(
            VolunteerOpportunity opportunity
    ) {

        StringBuilder message =
                new StringBuilder();


        // =====================================================
        // TITLE
        // =====================================================

        message.append(
                opportunity.getTitle()
        );


        message.append(
                "\n\n"
        );


        // =====================================================
        // CATEGORY
        // =====================================================

        if (
                opportunity.getCategory()
                != null
                &&
                !opportunity.getCategory()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "Category: "
            );

            message.append(
                    opportunity.getCategory()
            );

            message.append(
                    "\n"
            );
        }


        // =====================================================
        // DATE
        // =====================================================

        if (
                opportunity.getDate()
                != null
        ) {

            message.append(
                    "Date: "
            );

            message.append(
                    opportunity.getDate()
                            .format(
                                    dateFormatter
                            )
            );

            message.append(
                    "\n"
            );
        }


        // =====================================================
        // TIME
        // =====================================================

        if (
                opportunity.getStartTime()
                != null
        ) {

            message.append(
                    "Time: "
            );

            message.append(
                    opportunity.getStartTime()
            );


            if (
                    opportunity.getEndTime()
                    != null
            ) {

                message.append(
                        " - "
                );

                message.append(
                        opportunity.getEndTime()
                );
            }


            message.append(
                    "\n"
            );
        }


        // =====================================================
        // LOCATION
        // =====================================================

        if (
                opportunity.getLocation()
                != null
                &&
                !opportunity.getLocation()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "Location: "
            );

            message.append(
                    opportunity.getLocation()
            );

            message.append(
                    "\n"
            );
        }


        // =====================================================
        // SLOTS
        // =====================================================

        int availableSlots =
                opportunity.getMaxSlots()
                -
                opportunity.getCurrentSlots();


        message.append(
                "Available Slots: "
        );

        message.append(
                availableSlots
        );

        message.append(
                " / "
        );

        message.append(
                opportunity.getMaxSlots()
        );


        // =====================================================
        // REQUIRED SKILLS
        // =====================================================

        if (
                opportunity.getRequiredSkills()
                != null
                &&
                !opportunity.getRequiredSkills()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "\n\nRequired Skills:\n"
            );

            message.append(
                    opportunity.getRequiredSkills()
            );
        }


        // =====================================================
        // DESCRIPTION
        // =====================================================

        if (
                opportunity.getDescription()
                != null
                &&
                !opportunity.getDescription()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "\n\nDescription:\n"
            );

            message.append(
                    opportunity.getDescription()
            );
        }


        // =====================================================
        // SHOW DIALOG
        // =====================================================

        JTextArea textArea =
                new JTextArea(
                        message.toString()
                );

        textArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        textArea.setForeground(
                UIColors.TEXT_DARK
        );

        textArea.setBackground(
                UIColors.CARD
        );

        textArea.setEditable(
                false
        );

        textArea.setLineWrap(
                true
        );

        textArea.setWrapStyleWord(
                true
        );

        textArea.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        textArea
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        UIColors.BORDER
                )
        );

        scrollPane.setPreferredSize(
                new Dimension(
                        520,
                        350
                )
        );


        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Opportunity Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================================
    // EMPTY MESSAGE
    // =========================================================

    private void showEmptyMessage(
            String message
    ) {

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setBackground(
                UIColors.BACKGROUND
        );

        wrapper.setBorder(
                new EmptyBorder(
                        60,
                        20,
                        60,
                        20
                )
        );

        wrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        250
                )
        );


        JPanel emptyCard =
                new JPanel();

        emptyCard.setLayout(
                new BoxLayout(
                        emptyCard,
                        BoxLayout.Y_AXIS
                )
        );

        emptyCard.setBackground(
                UIColors.CARD
        );

        emptyCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.BORDER
                        ),
                        new EmptyBorder(
                                30,
                                50,
                                30,
                                50
                        )
                )
        );


        JLabel icon =
                new JLabel(
                        "○"
                );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        icon.setForeground(
                UIColors.PRIMARY
        );


        JLabel title =
                new JLabel(
                        "No Opportunities"
                );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        title.setForeground(
                UIColors.TEXT_DARK
        );


        JLabel description =
                new JLabel(
                        message
                );

        description.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        description.setForeground(
                UIColors.TEXT_GREY
        );


        emptyCard.add(
                icon
        );

        emptyCard.add(
                Box.createVerticalStrut(
                        8
                )
        );

        emptyCard.add(
                title
        );

        emptyCard.add(
                Box.createVerticalStrut(
                        6
                )
        );

        emptyCard.add(
                description
        );


        wrapper.add(
                emptyCard
        );


        contentPanel.add(
                wrapper
        );
    }


    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showErrorMessage(
            String message
    ) {

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setBackground(
                UIColors.BACKGROUND
        );

        wrapper.setBorder(
                new EmptyBorder(
                        50,
                        20,
                        50,
                        20
                )
        );


        JLabel label =
                new JLabel(
                        "<html><center>"
                        +
                        message.replace(
                                "\n",
                                "<br>"
                        )
                        +
                        "</center></html>"
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        label.setForeground(
                UIColors.RED
        );


        wrapper.add(
                label
        );


        contentPanel.add(
                wrapper
        );
    }


    // =========================================================
    // HOVER EFFECT
    // =========================================================

    private void addHoverEffect(
            JPanel card
    ) {

        card.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        card.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                UIColors.PRIMARY_LIGHT
                                        ),
                                        new EmptyBorder(
                                                18,
                                                20,
                                                18,
                                                20
                                        )
                                )
                        );

                        card.setBackground(
                                UIColors.CARD_ALT
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        card.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                UIColors.BORDER
                                        ),
                                        new EmptyBorder(
                                                18,
                                                20,
                                                18,
                                                20
                                        )
                                )
                        );

                        card.setBackground(
                                UIColors.CARD
                        );
                    }
                }
        );
    }


    // =========================================================
    // REFRESH UI
    // =========================================================

    private void refresh() {

        contentPanel.revalidate();

        contentPanel.repaint();
    }
}
