/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.OpportunityApplicationDAO;
import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.models.OpportunityApplication;
import com.communityservice.models.PortalVolunteer;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Displays all applications belonging to the logged-in volunteers.
 */
public class MyApplicationsPanel extends JPanel {

    private final int user_id;

    private final VolunteerPortalVolunteerDAO volunteerDAO;
    private final OpportunityApplicationDAO applicationDAO;

    private JPanel contentPanel;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MyApplicationsPanel(int user_id) {

        this.user_id = user_id;

        volunteerDAO =
                new VolunteerPortalVolunteerDAO();

        applicationDAO =
                new OpportunityApplicationDAO();

        initialize();

        loadApplications();
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
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                UIColors.PRIMARY
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        JLabel title =
                new JLabel(
                        "MY APPLICATIONS"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(
                Color.WHITE
        );

        header.add(
                title,
                BorderLayout.WEST
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        refreshButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        refreshButton.setFocusPainted(
                false
        );

        refreshButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        refreshButton.addActionListener(
                e -> loadApplications()
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT PANEL
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
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        contentPanel
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        15
                );

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
    // LOAD APPLICATIONS
    // =========================================================

    private void loadApplications() {

        contentPanel.removeAll();

        PortalVolunteer volunteers =
                volunteerDAO.getVolunteerByUserID(
                        user_id
                );

        if (volunteers == null) {

            showEmptyMessage(
                    "Volunteer information could not be found."
            );

            refresh();

            return;
        }

        List<OpportunityApplication> applications =
                applicationDAO.getApplicationsByVolunteer(
                        volunteers.getVolunteerID()
                );

        if (
                applications == null
                ||
                applications.isEmpty()
        ) {

            showEmptyMessage(
                    "You have not applied for any opportunities yet."
            );

            refresh();

            return;
        }

        for (
                OpportunityApplication application :
                applications
        ) {

            contentPanel.add(
                    createApplicationCard(
                            application
                    )
            );

            contentPanel.add(
                    Box.createVerticalStrut(
                            15
                    )
            );
        }

        refresh();
    }

    // =========================================================
    // CREATE APPLICATION CARD
    // =========================================================

    private JPanel createApplicationCard(
            OpportunityApplication application
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                UIColors.CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // =====================================================
        // INFORMATION
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
                application.getOpportunityTitle();

        if (
                titleText == null
                ||
                titleText.trim().isEmpty()
        ) {

            titleText =
                    "Opportunity #"
                    +
                    application.getOppID();
        }

        JLabel title =
                new JLabel(
                        titleText
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
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
                application.getCategory() != null
                &&
                !application.getCategory()
                        .trim()
                        .isEmpty()
        ) {

            JLabel category =
                    new JLabel(
                            application.getCategory()
                    );

            category.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            category.setForeground(
                    UIColors.TEXT_GREY
            );

            information.add(
                    Box.createVerticalStrut(
                            5
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
                application.getOpportunityDate()
                != null
        ) {

            JLabel date =
                    new JLabel(
                            "Date: "
                            +
                            application
                                    .getOpportunityDate()
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
                            7
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
                application.getStartTime()
                != null
        ) {

            String timeText =
                    "Time: "
                    +
                    application.getStartTime();

            if (
                    application.getEndTime()
                    != null
            ) {

                timeText +=
                        " - "
                        +
                        application.getEndTime();
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
                            4
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
                application.getOpportunityLocation()
                != null
                &&
                !application.getOpportunityLocation()
                        .trim()
                        .isEmpty()
        ) {

            JLabel location =
                    new JLabel(
                            "Location: "
                            +
                            application
                                    .getOpportunityLocation()
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
                            4
                    )
            );

            information.add(
                    location
            );
        }

        // =====================================================
        // ADD INFORMATION
        // =====================================================

        card.add(
                information,
                BorderLayout.CENTER
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel rightPanel =
                new JPanel();

        rightPanel.setLayout(
                new BoxLayout(
                        rightPanel,
                        BoxLayout.Y_AXIS
                )
        );

        rightPanel.setBackground(
                UIColors.CARD
        );

        rightPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 18, 0, 0)
        );
        // Keep the action column stable so status/actions do not drift or clip.
        rightPanel.setPreferredSize(new Dimension(170, 150));
        rightPanel.setMinimumSize(new Dimension(170, 150));

        // =====================================================
        // STATUS
        // =====================================================

        JLabel statusLabel =
                createStatusLabel(
                        application.getStatus()
                );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        rightPanel.add(
                statusLabel
        );

        rightPanel.add(
                Box.createVerticalStrut(
                        12
                )
        );

        // =====================================================
        // VIEW DETAILS
        // =====================================================

        JButton detailsButton =
                new JButton(
                        "View Details"
                );

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new Font("Arial", Font.PLAIN, 12));
        withdrawButton.setFocusPainted(false);
        withdrawButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        withdrawButton.setVisible("Pending".equalsIgnoreCase(application.getStatus()));
        withdrawButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Withdraw this pending application? This cannot be undone.",
                    "Confirm Withdrawal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            int volunteerId = volunteerDAO.getVolunteerByUserID(user_id).getVolunteerID();
            if (applicationDAO.withdrawApplication(application.getAppID(), volunteerId)) {
                loadApplications();
            } else {
                JOptionPane.showMessageDialog(this,
                        "The application could not be withdrawn. It may already have been reviewed.",
                        "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        detailsButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        detailsButton.setFocusPainted(
                false
        );

        detailsButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsButton.setPreferredSize(new Dimension(130, 34));
        detailsButton.setMaximumSize(new Dimension(130, 34));
        withdrawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawButton.setPreferredSize(new Dimension(130, 34));
        withdrawButton.setMaximumSize(new Dimension(130, 34));

        detailsButton.addActionListener(
                e ->
                        showApplicationDetails(
                                application
                        )
        );

        rightPanel.add(detailsButton);
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(withdrawButton);

        // =====================================================
        // ADD RIGHT PANEL
        // =====================================================

        card.add(
                rightPanel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // STATUS LABEL
    // =========================================================

    private JLabel createStatusLabel(
            String status
    ) {

        if (
                status == null
                ||
                status.trim().isEmpty()
        ) {

            status =
                    "Unknown";
        }

        JLabel label =
                new JLabel(
                        status.toUpperCase()
                );

        label.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setOpaque(
                true
        );

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        7,
                        14,
                        7,
                        14
                )
        );

        if (
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            label.setForeground(
                    Color.WHITE
            );

            label.setBackground(
                    UIColors.ORANGE
            );

        } else if (
                status.equalsIgnoreCase(
                        "Accepted"
                )
                ||
                status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            label.setForeground(
                    Color.WHITE
            );

            label.setBackground(
                    UIColors.GREEN
            );

        } else if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            label.setForeground(
                    Color.WHITE
            );

            label.setBackground(
                    UIColors.RED
            );

        } else {

            label.setForeground(
                    Color.WHITE
            );

            label.setBackground(
                    UIColors.BLUE
            );
        }

        return label;
    }

    // =========================================================
    // VIEW APPLICATION DETAILS
    // =========================================================

    private void showApplicationDetails(
            OpportunityApplication application
    ) {

        StringBuilder message =
                new StringBuilder();

        String title =
                application.getOpportunityTitle();

        if (
                title == null
                ||
                title.trim().isEmpty()
        ) {

            title =
                    "Opportunity #"
                    +
                    application.getOppID();
        }

        message.append(
                title
        );

        message.append(
                "\n\n"
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        message.append(
                "Application Status: "
        );

        message.append(
                application.getStatus()
        );

        message.append(
                "\n"
        );

        // -----------------------------------------------------
        // CATEGORY
        // -----------------------------------------------------

        if (
                application.getCategory()
                != null
        ) {

            message.append(
                    "Category: "
            );

            message.append(
                    application.getCategory()
            );

            message.append(
                    "\n"
            );
        }

        // -----------------------------------------------------
        // DATE
        // -----------------------------------------------------

        if (
                application.getOpportunityDate()
                != null
        ) {

            message.append(
                    "Date: "
            );

            message.append(
                    application
                            .getOpportunityDate()
                            .format(
                                    dateFormatter
                            )
            );

            message.append(
                    "\n"
            );
        }

        // -----------------------------------------------------
        // TIME
        // -----------------------------------------------------

        if (
                application.getStartTime()
                != null
        ) {

            message.append(
                    "Time: "
            );

            message.append(
                    application.getStartTime()
            );

            if (
                    application.getEndTime()
                    != null
            ) {

                message.append(
                        " - "
                );

                message.append(
                        application.getEndTime()
                );
            }

            message.append(
                    "\n"
            );
        }

        // -----------------------------------------------------
        // LOCATION
        // -----------------------------------------------------

        if (
                application.getOpportunityLocation()
                != null
        ) {

            message.append(
                    "Location: "
            );

            message.append(
                    application
                            .getOpportunityLocation()
            );

            message.append(
                    "\n"
            );
        }

        // -----------------------------------------------------
        // REQUIRED SKILLS
        // -----------------------------------------------------

        if (
                application.getRequiredSkills()
                != null
                &&
                !application.getRequiredSkills()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "\nRequired Skills:\n"
            );

            message.append(
                    application.getRequiredSkills()
            );
        }

        // -----------------------------------------------------
        // DESCRIPTION
        // -----------------------------------------------------

        if (
                application.getOpportunityDescription()
                != null
                &&
                !application.getOpportunityDescription()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "\n\nDescription:\n"
            );

            message.append(
                    application
                            .getOpportunityDescription()
            );
        }

        // -----------------------------------------------------
        // REJECTION REASON
        // -----------------------------------------------------

        if (
                application.getRejectionReason()
                != null
                &&
                !application.getRejectionReason()
                        .trim()
                        .isEmpty()
        ) {

            message.append(
                    "\n\nRejection Reason:\n"
            );

            message.append(
                    application.getRejectionReason()
            );
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Application Details",
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

        wrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        180
                )
        );

        JLabel label =
                new JLabel(
                        message
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        label.setForeground(
                UIColors.TEXT_GREY
        );

        wrapper.add(
                label
        );

        contentPanel.add(
                wrapper
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
