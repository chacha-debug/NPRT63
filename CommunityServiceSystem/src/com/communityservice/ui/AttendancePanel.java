/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.AttendanceDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendancePanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private JComboBox<EventItem> eventComboBox;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    // Logged-in ngos user ID
    private final int ngoUserID;


    
    public AttendancePanel() {
        this(1);
    }


    // =========================================================
    // REAL CONSTRUCTOR
    // =========================================================

    public AttendancePanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);


        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading =
                new JLabel("Event Attendance");

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        heading.setForeground(BLUE);

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
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                15
                        )
                );

        mainPanel.setBackground(BACKGROUND);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        25,
                        25,
                        25
                )
        );


        // =====================================================
        // EVENT SELECTION
        // =====================================================

        JPanel eventPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                5
                        )
                );

        eventPanel.setBackground(BACKGROUND);


        JLabel eventLabel =
                new JLabel("Select Event:");

        eventLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        eventComboBox =
                new JComboBox<>();

        eventComboBox.setPreferredSize(
                new Dimension(
                        320,
                        30
                )
        );


        JButton loadButton =
                new JButton(
                        "Load Volunteers"
                );

        loadButton.setBackground(TEAL);
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);


        eventPanel.add(eventLabel);
        eventPanel.add(eventComboBox);
        eventPanel.add(loadButton);


        mainPanel.add(
                eventPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // TABLE MODEL
        // =====================================================

        String[] columns = {
                "Application ID",
                "Volunteer ID",
                "Volunteer",
                "Event",
                "Application Status",
                "Attendance Status",
                "Hours"
        };


        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };


        // =====================================================
        // TABLE
        // =====================================================

        attendanceTable =
                new JTable(tableModel);

        attendanceTable.setRowHeight(30);

        attendanceTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        attendanceTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        attendanceTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );


        JScrollPane scrollPane =
                new JScrollPane(
                        attendanceTable
                );


        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        buttonPanel.setBackground(BACKGROUND);


        JButton checkInButton =
                new JButton(
                        "Check In"
                );

        JButton completeButton =
                new JButton(
                        "Complete Attendance"
                );


        checkInButton.setBackground(TEAL);
        checkInButton.setForeground(Color.WHITE);
        checkInButton.setFocusPainted(false);


        completeButton.setBackground(BLUE);
        completeButton.setForeground(Color.WHITE);
        completeButton.setFocusPainted(false);


        buttonPanel.add(checkInButton);
        buttonPanel.add(completeButton);


        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // ACTIONS
        // =====================================================

        loadButton.addActionListener(
                e -> loadApprovedVolunteers()
        );

        checkInButton.addActionListener(
                e -> checkInSelectedVolunteer()
        );

        completeButton.addActionListener(
                e -> completeSelectedAttendance()
        );


        // Initial load
        loadEvents();
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        
        tableModel.setRowCount(0);

        
        loadEvents();
    }


    // =========================================================
    // LOAD EVENTS
    // =========================================================

    private void loadEvents() {

        eventComboBox.removeAllItems();


        List<Object[]> events =
                AttendanceDAO
                        .getEventsForNGO(
                                ngoUserID
                        );


        for (
                Object[] event
                : events
        ) {

            int opportunity_id =
                    Integer.parseInt(
                            event[0]
                                    .toString()
                    );


            String title =
                    event[1] == null
                            ? "Unnamed Event"
                            : event[1].toString();


            String date =
                    event[2] == null
                            ? "No date"
                            : event[2].toString();


            eventComboBox.addItem(
                    new EventItem(
                            opportunity_id,
                            title
                                    + " - "
                                    + date
                    )
            );
        }
    }


    // =========================================================
    // LOAD APPROVED VOLUNTEERS
    // =========================================================

    private void loadApprovedVolunteers() {

        tableModel.setRowCount(0);


        EventItem selectedEvent =
                (EventItem)
                        eventComboBox
                                .getSelectedItem();


        if (
                selectedEvent == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        List<Object[]> volunteers =
                AttendanceDAO
                        .getApprovedVolunteersForEvent(
                                ngoUserID,
                                selectedEvent.getOppID()
                        );


        if (
                volunteers.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no approved volunteers for this event.",
                    "No Approved Volunteers",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        for (
                Object[] volunteer
                : volunteers
        ) {

            int volunteer_id =
                    Integer.parseInt(
                            volunteer[1]
                                    .toString()
                    );


            Object[] attendance =
                    AttendanceDAO
                            .getAttendanceRecord(
                                    volunteer_id,
                                    selectedEvent.getOppID()
                            );


            String attendanceStatus =
                    attendance == null
                            ? "Not Checked In"
                            : attendance[2].toString();


            double hours =
                    attendance == null
                            ? 0
                            : Double.parseDouble(
                                    attendance[3]
                                            .toString()
                            );


            tableModel.addRow(
                    new Object[]{
                            volunteer[0],
                            volunteer[1],
                            volunteer[2],
                            volunteer[3],
                            volunteer[4],
                            attendanceStatus,
                            hours
                    }
            );
        }
    }


    // =========================================================
    // CHECK IN
    // =========================================================

    private void checkInSelectedVolunteer() {

        int selectedRow =
                attendanceTable
                        .getSelectedRow();


        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a volunteer first.",
                    "No Volunteer Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        EventItem selectedEvent =
                (EventItem)
                        eventComboBox
                                .getSelectedItem();


        if (
                selectedEvent == null
        ) {

            return;
        }


        int volunteer_id =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        1
                                )
                                .toString()
                );


        boolean success =
                AttendanceDAO
                        .checkInVolunteer(
                                volunteer_id,
                                selectedEvent.getOppID(),
                                ngoUserID
                        );


        if (
                success
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Volunteer checked in successfully!",
                    "Attendance",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // Reload table immediately
            loadApprovedVolunteers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "This volunteer has already been checked in.",
                    "Attendance",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // =========================================================
    // COMPLETE ATTENDANCE
    // =========================================================

    private void completeSelectedAttendance() {

        int selectedRow =
                attendanceTable
                        .getSelectedRow();


        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a volunteer first.",
                    "No Volunteer Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        EventItem selectedEvent =
                (EventItem)
                        eventComboBox
                                .getSelectedItem();


        if (
                selectedEvent == null
        ) {

            return;
        }


        int volunteer_id =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        1
                                )
                                .toString()
                );


        Object[] attendance =
                AttendanceDAO
                        .getAttendanceRecord(
                                volunteer_id,
                                selectedEvent.getOppID()
                        );


        // Volunteer must check in first
        if (
                attendance == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "The volunteer must be checked in first.",
                    "Attendance",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // Prevent completing twice
        if (
                "Completed"
                        .equalsIgnoreCase(
                                attendance[2]
                                        .toString()
                        )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Attendance has already been completed for this volunteer.",
                    "Attendance",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        String hoursText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter the number of hours completed:",
                        "Award Volunteer Hours",
                        JOptionPane.QUESTION_MESSAGE
                );


        if (
                hoursText == null
        ) {

            return;
        }


        double hours;


        try {

            hours =
                    Double.parseDouble(
                            hoursText.trim()
                    );

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid number of hours.",
                    "Invalid Hours",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (hours <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hours must be greater than 0.",
                    "Invalid Hours",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double eventDuration = AttendanceDAO.getEventDurationHours(ngoUserID, selectedEvent.getOppID());
        if (eventDuration > 0 && hours > eventDuration) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Hours awarded cannot exceed the event duration of %.2f hours.", eventDuration),
                    "Invalid Hours",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }


        boolean success =
                AttendanceDAO
                        .completeAttendance(
                                volunteer_id,
                                selectedEvent.getOppID(),
                                hours,
                                ngoUserID
                        );


        if (
                success
        ) {

            com.communityservice.models.Certificate certificate =
                    com.communityservice.utils.CertificateService.generateForCompletedAttendance(
                            volunteer_id, selectedEvent.getOppID());

            String message = "Attendance completed and " + hours + " hours awarded successfully!";
            if (certificate != null) {
                message += "\nCertificate " + certificate.getCertificationNumber()
                        + " was generated and saved to your application data.";
            } else {
                message += "\nAttendance was saved, but the certificate PDF could not be generated automatically.";
            }

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Attendance",
                    certificate != null ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
            );


            // Reload table so status/hours update immediately
            loadApprovedVolunteers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not complete attendance.",
                    "Attendance Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // EVENT ITEM
    // =========================================================

    private static class EventItem {

        private final int opportunity_id;
        private final String displayText;


        public EventItem(
                int opportunity_id,
                String displayText
        ) {

            this.opportunity_id =
                    opportunity_id;

            this.displayText =
                    displayText;
        }


        public int getOppID() {

            return opportunity_id;
        }


        @Override
        public String toString() {

            return displayText;
        }
    }
}