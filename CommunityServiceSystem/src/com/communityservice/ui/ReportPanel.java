/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportPanel extends JPanel {

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color READ_ONLY =
            UIColors.BACKGROUND_DARK;


    // =========================================================
    // LOGGED-IN ngos
    // =========================================================

    private final int ngoUserID;


    // =========================================================
    // COMPONENTS
    // =========================================================

    private JComboBox<EventItem> eventComboBox;

    private JTextField dateField;
    private JTextField startTimeField;
    private JTextField endTimeField;

    private JTextArea eventSummaryArea;
    private JTextArea feedbackArea;
    private JButton submitButton;


    
    public ReportPanel() {
        this(1);
    }


    // =========================================================
    // REAL CONSTRUCTOR
    // =========================================================

    public ReportPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(
                new BorderLayout()
        );

        setBackground(
                BACKGROUND
        );


        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading =
                new JLabel(
                        "Event Report"
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        heading.setForeground(
                BLUE
        );

        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        15,
                        25
                )
        );

        add(
                heading,
                BorderLayout.NORTH
        );


        // =====================================================
        // WRAPPER
        // =====================================================

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setBackground(
                BACKGROUND
        );


        // =====================================================
        // FORM CARD
        // =====================================================

        JPanel formCard =
                new JPanel();

        formCard.setLayout(
                new BoxLayout(
                        formCard,
                        BoxLayout.Y_AXIS
                )
        );

        formCard.setBackground(
                Color.WHITE
        );

        formCard.setPreferredSize(
                new Dimension(
                        560,
                        590
                )
        );

        formCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        // =====================================================
        // SELECT EVENT
        // =====================================================

        JLabel eventLabel =
                new JLabel(
                        "Select Event *"
                );

        eventLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        eventLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        eventComboBox =
                new JComboBox<>();

        eventComboBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        32
                )
        );

        eventComboBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        formCard.add(
                eventLabel
        );

        formCard.add(
                Box.createVerticalStrut(
                        5
                )
        );

        formCard.add(
                eventComboBox
        );

        formCard.add(
                Box.createVerticalStrut(
                        18
                )
        );


        // =====================================================
        // EVENT DATE
        // =====================================================

        dateField =
                createReadOnlyField();


        addField(
                formCard,
                "Event Date",
                dateField
        );


        // =====================================================
        // START / END TIME
        // =====================================================

        JPanel timePanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        timePanel.setBackground(
                Color.WHITE
        );

        timePanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        timePanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        65
                )
        );


        JPanel startPanel =
                createLabeledPanel(
                        "Start Time"
                );

        startTimeField =
                createReadOnlyField();

        startPanel.add(
                startTimeField
        );


        JPanel endPanel =
                createLabeledPanel(
                        "End Time"
                );

        endTimeField =
                createReadOnlyField();

        endPanel.add(
                endTimeField
        );


        timePanel.add(
                startPanel
        );

        timePanel.add(
                endPanel
        );


        formCard.add(
                timePanel
        );

        formCard.add(
                Box.createVerticalStrut(
                        20
                )
        );


        // =====================================================
        // EVENT SUMMARY
        // =====================================================

        JLabel summaryLabel =
                new JLabel(
                        "How did the event go? *"
                );

        summaryLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        summaryLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        formCard.add(
                summaryLabel
        );

        formCard.add(
                Box.createVerticalStrut(
                        5
                )
        );


        eventSummaryArea =
                new JTextArea(
                        5,
                        20
                );

        eventSummaryArea.setLineWrap(
                true
        );

        eventSummaryArea.setWrapStyleWord(
                true
        );


        JScrollPane summaryScroll =
                new JScrollPane(
                        eventSummaryArea
                );

        summaryScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        summaryScroll.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );


        formCard.add(
                summaryScroll
        );

        formCard.add(
                Box.createVerticalStrut(
                        18
                )
        );


        // =====================================================
        // COMMUNITY FEEDBACK
        // =====================================================

        JLabel feedbackLabel =
                new JLabel(
                        "Community Feedback *"
                );

        feedbackLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        feedbackLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        formCard.add(
                feedbackLabel
        );

        formCard.add(
                Box.createVerticalStrut(
                        5
                )
        );


        feedbackArea =
                new JTextArea(
                        5,
                        20
                );

        feedbackArea.setLineWrap(
                true
        );

        feedbackArea.setWrapStyleWord(
                true
        );


        JScrollPane feedbackScroll =
                new JScrollPane(
                        feedbackArea
                );

        feedbackScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        feedbackScroll.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );


        formCard.add(
                feedbackScroll
        );

        formCard.add(
                Box.createVerticalStrut(
                        20
                )
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(
                Color.WHITE
        );

        buttonPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JButton cancelButton =
                new JButton(
                        "Cancel"
                );


        submitButton =
                new JButton(
                        "Submit Report"
                );


        submitButton.setBackground(
                TEAL
        );

        submitButton.setForeground(
                Color.WHITE
        );

        submitButton.setFocusPainted(
                false
        );


        cancelButton.setFocusPainted(
                false
        );


        cancelButton.addActionListener(
                e -> clearForm()
        );


        submitButton.setEnabled(false);
        submitButton.addActionListener(
                e -> submitReport()
        );
        javax.swing.event.DocumentListener reportValidation = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e){ updateSubmitEnabled(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e){ updateSubmitEnabled(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e){ updateSubmitEnabled(); }
        };
        eventSummaryArea.getDocument().addDocumentListener(reportValidation);
        feedbackArea.getDocument().addDocumentListener(reportValidation);
        eventComboBox.addActionListener(e -> updateSubmitEnabled());
        updateSubmitEnabled();


        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                submitButton
        );


        formCard.add(
                buttonPanel
        );


        wrapper.add(
                formCard
        );


        add(
                wrapper,
                BorderLayout.CENTER
        );


        // =====================================================
        // EVENT SELECTION ACTION
        // =====================================================

        eventComboBox.addActionListener(
                e -> loadSelectedEventDetails()
        );


        // Initial database load
        loadEvents();
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        /*
         * Reload events whenever the Reports page is reopened.
         * This allows newly created events to appear immediately.
         */
        loadEvents();
    }


    // =========================================================
    // LOAD ngos EVENTS
    // =========================================================

    private void loadEvents() {

        eventComboBox.removeAllItems();


        String sql =
                "SELECT "
                        + "opportunity_id, "
                        + "title, "
                        + "date, "
                        + "start_time, "
                        + "end_time, "
                        + "status "
                        + "FROM opportunities "
                        + "WHERE ngo_user_id = ? "
                        + "AND status <> 'Draft' "
                        + "AND status <> 'Cancelled' "
                        + "ORDER BY date DESC";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    ngoUserID
            );


            ResultSet rs =
                    statement.executeQuery();


            while (
                    rs.next()
            ) {

                EventItem item =
                        new EventItem(
                                rs.getInt(
                                        "opportunity_id"
                                ),
                                rs.getString(
                                        "title"
                                ),
                                rs.getDate(
                                        "date"
                                ),
                                rs.getTime(
                                        "start_time"
                                ),
                                rs.getTime(
                                        "end_time"
                                ),
                                rs.getString(
                                        "status"
                                )
                        );


                eventComboBox.addItem(
                        item
                );
            }


            if (
                    eventComboBox.getItemCount()
                            == 0
            ) {

                clearEventDetails();
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load events.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // LOAD SELECTED EVENT DETAILS
    // =========================================================

    private void loadSelectedEventDetails() {

        EventItem selected =
                (EventItem)
                        eventComboBox
                                .getSelectedItem();


        if (
                selected == null
        ) {

            clearEventDetails();

            return;
        }


        if (
                selected.getDate()
                        != null
        ) {

            dateField.setText(
                    selected
                            .getDate()
                            .toString()
            );

        } else {

            dateField.setText(
                    "Not set"
            );
        }


        if (
                selected.getStartTime()
                        != null
        ) {

            startTimeField.setText(
                    selected
                            .getStartTime()
                            .toString()
            );

        } else {

            startTimeField.setText(
                    "Not set"
            );
        }


        if (
                selected.getEndTime()
                        != null
        ) {

            endTimeField.setText(
                    selected
                            .getEndTime()
                            .toString()
            );

        } else {

            endTimeField.setText(
                    "Not set"
            );
        }
    }


    // =========================================================
    // SUBMIT reports
    // =========================================================

    private void updateSubmitEnabled() {
        if (submitButton == null) return;
        submitButton.setEnabled(eventComboBox.getSelectedItem() != null
                && !eventSummaryArea.getText().trim().isEmpty()
                && !feedbackArea.getText().trim().isEmpty());
    }

    private void submitReport() {

        EventItem selected =
                (EventItem)
                        eventComboBox
                                .getSelectedItem();


        if (
                selected == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                eventSummaryArea
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please describe how the event went.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                feedbackArea
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the community feedback.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // BUILD reports SUMMARY
        // =====================================================

        String summary =
                "Event ID: "
                        + selected.getOppID()

                        + "\nEvent Status: "
                        + selected.getStatus()

                        + "\nStart Time: "
                        + safeTime(
                                selected.getStartTime()
                        )

                        + "\nEnd Time: "
                        + safeTime(
                                selected.getEndTime()
                        )

                        + "\nEvent Summary: "
                        + eventSummaryArea
                        .getText()
                        .trim()

                        + "\nCommunity Feedback: "
                        + feedbackArea
                        .getText()
                        .trim();


        // =====================================================
        // reports DATE
        // =====================================================

        java.sql.Date reportDate =
                selected.getDate();


        // =====================================================
        // INSERT reports
        // =====================================================

        String sql =
                "INSERT INTO reports "
                        + "(generated_by, "
                        + "report_type, "
                        + "title, "
                        + "generated_date, "
                        + "start_date, "
                        + "end_date, "
                        + "file_format, "
                        + "data_summary) "
                        + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            // ngos USER ID
            statement.setInt(
                    1,
                    ngoUserID
            );


            // reports TYPE
            statement.setString(
                    2,
                    "Event Report"
            );


            // EVENT TITLE
            statement.setString(
                    3,
                    selected.getTitle()
            );


            // START DATE
            if (
                    reportDate != null
            ) {

                statement.setDate(
                        4,
                        reportDate
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.DATE
                );
            }


            // END DATE
            if (
                    reportDate != null
            ) {

                statement.setDate(
                        5,
                        reportDate
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }


            // FORMAT
            statement.setString(
                    6,
                    "Database"
            );


            // reports CONTENT
            statement.setString(
                    7,
                    summary
            );


            int rowsInserted =
                    statement.executeUpdate();


            if (
                    rowsInserted > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Report submitted successfully!",
                        "Report",
                        JOptionPane.INFORMATION_MESSAGE
                );


                clearForm();
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save report.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        eventSummaryArea.setText(
                ""
        );

        feedbackArea.setText(
                ""
        );


        if (
                eventComboBox.getItemCount()
                        > 0
        ) {

            eventComboBox.setSelectedIndex(
                    0
            );

            loadSelectedEventDetails();

        } else {

            clearEventDetails();
        }
    }


    private void clearEventDetails() {

        dateField.setText(
                ""
        );

        startTimeField.setText(
                ""
        );

        endTimeField.setText(
                ""
        );
    }


    // =========================================================
    // READ ONLY FIELD
    // =========================================================

    private JTextField createReadOnlyField() {

        JTextField field =
                new JTextField();

        field.setEditable(
                false
        );

        field.setBackground(
                READ_ONLY
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        32
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        return field;
    }


    // =========================================================
    // CREATE LABELED PANEL
    // =========================================================

    private JPanel createLabeledPanel(
            String labelText
    ) {

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


        JLabel label =
                new JLabel(
                        labelText
                );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        panel.add(
                label
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );


        return panel;
    }


    // =========================================================
    // ADD FIELD
    // =========================================================

    private void addField(
            JPanel panel,
            String labelText,
            JComponent component
    ) {

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        component.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        component.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        32
                )
        );


        panel.add(
                label
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                component
        );

        panel.add(
                Box.createVerticalStrut(
                        18
                )
        );
    }


    // =========================================================
    // SAFE TIME
    // =========================================================

    private String safeTime(
            java.sql.Time time
    ) {

        return time == null
                ? "Not set"
                : time.toString();
    }


    // =========================================================
    // EVENT ITEM
    // =========================================================

    private static class EventItem {

        private final int opportunity_id;
        private final String title;

        private final java.sql.Date date;
        private final java.sql.Time start_time;
        private final java.sql.Time end_time;

        private final String status;


        public EventItem(
                int opportunity_id,
                String title,
                java.sql.Date date,
                java.sql.Time start_time,
                java.sql.Time end_time,
                String status
        ) {

            this.opportunity_id =
                    opportunity_id;

            this.title =
                    title;

            this.date =
                    date;

            this.start_time =
                    start_time;

            this.end_time =
                    end_time;

            this.status =
                    status;
        }


        public int getOppID() {

            return opportunity_id;
        }


        public String getTitle() {

            return title;
        }


        public java.sql.Date getDate() {

            return date;
        }


        public java.sql.Time getStartTime() {

            return start_time;
        }


        public java.sql.Time getEndTime() {

            return end_time;
        }


        public String getStatus() {

            return status;
        }


        @Override
        public String toString() {

            String dateText =
                    date == null
                            ? "No date"
                            : date.toString();


            return title
                    + " - "
                    + dateText
                    + " ("
                    + status
                    + ")";
        }
    }
}