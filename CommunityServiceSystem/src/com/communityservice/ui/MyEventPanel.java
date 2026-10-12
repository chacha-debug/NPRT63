/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyEventPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private static final Color RED =
            UIColors.RED;

    private JTable eventTable;
    private DefaultTableModel model;

    // Logged-in ngos
    private final int ngoUserID;


    
    public MyEventPanel() {
        this(1);
    }


    // =========================================================
    // REAL CONSTRUCTOR
    // =========================================================

    public MyEventPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);


        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading =
                new JLabel("My Events");

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
        // TABLE MODEL
        // =====================================================

        String[] columns = {
                "Event ID",
                "Title",
                "Date",
                "Location",
                "Slots",
                "Status"
        };


        model =
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

        eventTable =
                new JTable(model);

        eventTable.setRowHeight(32);

        eventTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        eventTable.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        eventTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );


        JScrollPane tableScroll =
                new JScrollPane(
                        eventTable
                );

        tableScroll.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        25,
                        10,
                        25
                )
        );


        add(
                tableScroll,
                BorderLayout.CENTER
        );


        // =====================================================
        // BUTTON AREA
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                10
                        )
                );

        buttonPanel.setBackground(
                BACKGROUND
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        25,
                        15,
                        25
                )
        );


        JButton viewDetailsButton =
                createButton(
                        "View Details",
                        TEAL
                );


        JButton publishButton =
                createButton(
                        "Publish Draft",
                        BLUE
                );


        JButton closeButton =
                createButton(
                        "Close Event",
                        new Color(
                                105,
                                105,
                                105
                        )
                );


        JButton cancelButton =
                createButton(
                        "Cancel Event",
                        RED
                );


        buttonPanel.add(
                viewDetailsButton
        );

        buttonPanel.add(
                publishButton
        );

        buttonPanel.add(
                closeButton
        );

        buttonPanel.add(
                cancelButton
        );


        add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // ACTIONS
        // =====================================================

        viewDetailsButton.addActionListener(
                e -> viewEventDetails()
        );


        publishButton.addActionListener(
                e -> publishSelectedDraft()
        );


        closeButton.addActionListener(
                e -> closeSelectedEvent()
        );


        cancelButton.addActionListener(
                e -> cancelSelectedEvent()
        );


        // Initial load
        loadEvents();
    }


    // =========================================================
    // CREATE BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(
                background
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        return button;
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        loadEvents();
    }


    // =========================================================
    // GET SELECTED EVENT ID
    // =========================================================

    private Integer getSelectedEventID() {

        int selectedRow =
                eventTable.getSelectedRow();


        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }


        return Integer.parseInt(
                model.getValueAt(
                        selectedRow,
                        0
                ).toString()
        );
    }


    // =========================================================
    // GET SELECTED STATUS
    // =========================================================

    private String getSelectedStatus() {

        int selectedRow =
                eventTable.getSelectedRow();


        if (
                selectedRow == -1
        ) {

            return null;
        }


        Object value =
                model.getValueAt(
                        selectedRow,
                        5
                );


        return value == null
                ? ""
                : value.toString();
    }


    // =========================================================
    // PUBLISH DRAFT
    // =========================================================

    private void publishSelectedDraft() {

        Integer eventID =
                getSelectedEventID();


        if (
                eventID == null
        ) {

            return;
        }


        String currentStatus =
                getSelectedStatus();


        if (
                !"Draft".equalsIgnoreCase(
                        currentStatus
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only draft events can be published.",
                    "Cannot Publish",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Publish this event?\n\n"
                                + "It will become visible as an open event.",
                        "Publish Event",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {

            return;
        }


        updateEventStatus(
                eventID,
                "Open",
                "Event published successfully!"
        );
    }


    // =========================================================
    // CLOSE EVENT
    // =========================================================

    private void closeSelectedEvent() {

        Integer eventID =
                getSelectedEventID();


        if (
                eventID == null
        ) {

            return;
        }


        String currentStatus =
                getSelectedStatus();


        if (
                !"Open".equalsIgnoreCase(
                        currentStatus
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only open events can be closed.",
                    "Cannot Close Event",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Close this event?\n\n"
                                + "Volunteers should no longer be able to apply.",
                        "Close Event",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {

            return;
        }


        updateEventStatus(
                eventID,
                "Closed",
                "Event closed successfully!"
        );
    }


    // =========================================================
    // CANCEL EVENT
    // =========================================================

    private void cancelSelectedEvent() {

        Integer eventID =
                getSelectedEventID();


        if (
                eventID == null
        ) {

            return;
        }


        String currentStatus =
                getSelectedStatus();


        if (
                "Cancelled".equalsIgnoreCase(
                        currentStatus
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "This event is already cancelled.",
                    "Event Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        if (
                "Closed".equalsIgnoreCase(
                        currentStatus
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "A closed event cannot be cancelled.",
                    "Cannot Cancel Event",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this event?",
                        "Cancel Event",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {

            return;
        }


        updateEventStatus(
                eventID,
                "Cancelled",
                "Event cancelled successfully."
        );
    }


    // =========================================================
    // UPDATE EVENT STATUS
    // =========================================================

    private void updateEventStatus(
            int eventID,
            String newStatus,
            String successMessage
    ) {

        String sql =
                "UPDATE opportunities "
                        + "SET status = ? "
                        + "WHERE opportunity_id = ? "
                        + "AND ngo_user_id = ?";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    newStatus
            );

            statement.setInt(
                    2,
                    eventID
            );

            statement.setInt(
                    3,
                    ngoUserID
            );


            int updated =
                    statement.executeUpdate();


            if (
                    updated > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        successMessage,
                        "Event Status",
                        JOptionPane.INFORMATION_MESSAGE
                );


                loadEvents();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "The event status could not be updated.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update event status.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // VIEW EVENT DETAILS
    // =========================================================

    private void viewEventDetails() {

        Integer eventID =
                getSelectedEventID();


        if (
                eventID == null
        ) {

            return;
        }


        String sql =
                "SELECT "
                        + "title, "
                        + "description, "
                        + "date, "
                        + "application_deadline, "
                        + "start_time, "
                        + "end_time, "
                        + "location, "
                        + "location_notes, "
                        + "max_slots, "
                        + "current_slots, "
                        + "minimum_age, "
                        + "category, "
                        + "status, "
                        + "required_skills "
                        + "FROM opportunities "
                        + "WHERE opportunity_id = ? "
                        + "AND ngo_user_id = ?";


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
                    eventID
            );

            statement.setInt(
                    2,
                    ngoUserID
            );


            ResultSet rs =
                    statement.executeQuery();


            if (
                    rs.next()
            ) {

                String start_time =
                        rs.getTime("start_time") == null
                                ? "Not set"
                                : rs.getTime(
                                        "start_time"
                                ).toString();


                String end_time =
                        rs.getTime("end_time") == null
                                ? "Not set"
                                : rs.getTime(
                                        "end_time"
                                ).toString();


                String deadline =
                        rs.getDate(
                                "application_deadline"
                        ) == null
                                ? "Not set"
                                : rs.getDate(
                                        "application_deadline"
                                ).toString();


                String location_notes =
                        rs.getString(
                                "location_notes"
                        );


                if (
                        location_notes == null
                                || location_notes
                                .trim()
                                .isEmpty()
                ) {

                    location_notes =
                            "None";
                }


                String required_skills =
                        rs.getString(
                                "required_skills"
                        );


                if (
                        required_skills == null
                                || required_skills
                                .trim()
                                .isEmpty()
                ) {

                    required_skills =
                            "None";
                }


                String minimum_age =
                        rs.getObject(
                                "minimum_age"
                        ) == null
                                ? "No minimum"
                                : rs.getInt(
                                        "minimum_age"
                                ) + "+";


                String details =
                        "Event ID: "
                                + eventID

                                + "\n\nTitle: "
                                + rs.getString(
                                        "title"
                                )

                                + "\nDescription: "
                                + rs.getString(
                                        "description"
                                )

                                + "\nEvent Date: "
                                + rs.getDate(
                                        "date"
                                )

                                + "\nApplication Deadline: "
                                + deadline

                                + "\nStart Time: "
                                + start_time

                                + "\nEnd Time: "
                                + end_time

                                + "\nLocation: "
                                + rs.getString(
                                        "location"
                                )

                                + "\nLocation Notes: "
                                + location_notes

                                + "\nCategory: "
                                + rs.getString(
                                        "category"
                                )

                                + "\nStatus: "
                                + rs.getString(
                                        "status"
                                )

                                + "\nMaximum Slots: "
                                + rs.getInt(
                                        "max_slots"
                                )

                                + "\nCurrent Slots: "
                                + rs.getInt(
                                        "current_slots"
                                )

                                + "\nMinimum Age: "
                                + minimum_age

                                + "\nRequired Skills: "
                                + required_skills;


                JTextArea textArea =
                        new JTextArea(
                                details
                        );

                textArea.setEditable(
                        false
                );

                textArea.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                14
                        )
                );

                textArea.setBackground(
                        BACKGROUND
                );

                textArea.setBorder(
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                );


                JOptionPane.showMessageDialog(
                        this,
                        textArea,
                        "Event Details",
                        JOptionPane.INFORMATION_MESSAGE
                );


            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Event could not be found.",
                        "Event Not Found",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load event details.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // LOAD EVENTS
    // =========================================================

    private void loadEvents() {

        model.setRowCount(
                0
        );


        String sql =
                "SELECT "
                        + "opportunity_id, "
                        + "title, "
                        + "date, "
                        + "location, "
                        + "max_slots, "
                        + "status "
                        + "FROM opportunities "
                        + "WHERE ngo_user_id = ? "
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

                model.addRow(
                        new Object[]{
                                rs.getInt(
                                        "opportunity_id"
                                ),
                                rs.getString(
                                        "title"
                                ),
                                rs.getDate(
                                        "date"
                                ),
                                rs.getString(
                                        "location"
                                ),
                                rs.getInt(
                                        "max_slots"
                                ),
                                rs.getString(
                                        "status"
                                )
                        }
                );
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
}