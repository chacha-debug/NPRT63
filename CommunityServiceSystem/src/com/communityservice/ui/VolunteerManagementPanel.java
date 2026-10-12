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

public class VolunteerManagementPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private JTable volunteerTable;
    private DefaultTableModel model;
    private JTextField searchField;

    // Logged-in ngos user
    private final int ngoUserID;


    

    public VolunteerManagementPanel() {
        this(1);
    }


    // =========================================================
    // REAL CONSTRUCTOR
    // =========================================================

    public VolunteerManagementPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);


        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading =
                new JLabel("Volunteer Management");

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
                        10,
                        25
                )
        );

        add(
                heading,
                BorderLayout.NORTH
        );


        // =====================================================
        // MAIN AREA
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
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
        // SEARCH AREA
        // =====================================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                5
                        )
                );

        searchPanel.setBackground(BACKGROUND);


        JLabel searchLabel =
                new JLabel(
                        "Search Volunteer:"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        searchField =
                new JTextField(25);


        JButton searchButton =
                new JButton(
                        "Search"
                );

        searchButton.setBackground(TEAL);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);


        JButton showAllButton =
                new JButton(
                        "Show All"
                );

        showAllButton.setFocusPainted(false);


        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);


        mainPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // TABLE MODEL
        // =====================================================

        String[] columns = {
                "Volunteer ID",
                "Full Name",
                "Email",
                "Phone",
                "Academic Year",
                "Preferences",
                "Total Hours"
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

        volunteerTable =
                new JTable(model);

        volunteerTable.setRowHeight(30);

        volunteerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        volunteerTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        volunteerTable
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
                        volunteerTable
                );


        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =====================================================
        // BOTTOM BUTTON
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.setBackground(BACKGROUND);


        JButton viewDetailsButton =
                new JButton(
                        "View Details"
                );

        viewDetailsButton.setBackground(TEAL);
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setFocusPainted(false);


        bottomPanel.add(
                viewDetailsButton
        );


        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // ACTIONS
        // =====================================================

        searchButton.addActionListener(
                e -> searchVolunteers()
        );


        showAllButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadAllVolunteers();
                }
        );


        viewDetailsButton.addActionListener(
                e -> viewVolunteerDetails()
        );


        // Initial database load
        loadAllVolunteers();
    }


    // =========================================================
    // LOAD ngos'S VOLUNTEERS
    // =========================================================

    private void loadAllVolunteers() {

        model.setRowCount(0);


        String sql =
                "SELECT DISTINCT "
                        + "v.volunteer_id, "
                        + "u.full_name, "
                        + "u.email, "
                        + "v.phone_number, "
                        + "v.academic_year, "
                        + "v.preferences, "
                        + "v.total_hours "
                        + "FROM volunteers v "
                        + "JOIN users u "
                        + "ON v.user_id = u.user_id "
                        + "JOIN opportunity_applications oa "
                        + "ON oa.volunteer_id = v.volunteer_id "
                        + "JOIN opportunities vo "
                        + "ON oa.opportunity_id = vo.opportunity_id "
                        + "WHERE vo.ngo_user_id = ? "
                        + "AND oa.status = 'Approved' "
                        + "AND u.is_active = TRUE "
                        + "ORDER BY u.full_name";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    ngoUserID
            );


            ResultSet rs =
                    ps.executeQuery();


            while (
                    rs.next()
            ) {

                model.addRow(
                        new Object[]{
                                rs.getInt(
                                        "volunteer_id"
                                ),
                                rs.getString(
                                        "full_name"
                                ),
                                rs.getString(
                                        "email"
                                ),
                                rs.getString(
                                        "phone_number"
                                ),
                                rs.getString(
                                        "academic_year"
                                ),
                                rs.getString(
                                        "preferences"
                                ),
                                rs.getDouble(
                                        "total_hours"
                                )
                        }
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load volunteers.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        searchField.setText("");

        loadAllVolunteers();
    }


    // =========================================================
    // SEARCH ngos'S VOLUNTEERS
    // =========================================================

    private void searchVolunteers() {

        String search =
                searchField
                        .getText()
                        .trim();


        if (
                search.isEmpty()
        ) {

            loadAllVolunteers();

            return;
        }


        model.setRowCount(0);


        String sql =
                "SELECT DISTINCT "
                        + "v.volunteer_id, "
                        + "u.full_name, "
                        + "u.email, "
                        + "v.phone_number, "
                        + "v.academic_year, "
                        + "v.preferences, "
                        + "v.total_hours "
                        + "FROM volunteers v "
                        + "JOIN users u "
                        + "ON v.user_id = u.user_id "
                        + "JOIN opportunity_applications oa "
                        + "ON oa.volunteer_id = v.volunteer_id "
                        + "JOIN opportunities vo "
                        + "ON oa.opportunity_id = vo.opportunity_id "
                        + "WHERE vo.ngo_user_id = ? "
                        + "AND oa.status = 'Approved' "
                        + "AND u.is_active = TRUE "
                        + "AND ("
                        + "u.full_name LIKE ? "
                        + "OR u.email LIKE ? "
                        + "OR v.preferences LIKE ? "
                        + "OR v.academic_year LIKE ?"
                        + ") "
                        + "ORDER BY u.full_name";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            String value =
                    "%" + search + "%";


            ps.setInt(
                    1,
                    ngoUserID
            );

            ps.setString(
                    2,
                    value
            );

            ps.setString(
                    3,
                    value
            );

            ps.setString(
                    4,
                    value
            );

            ps.setString(
                    5,
                    value
            );


            ResultSet rs =
                    ps.executeQuery();


            while (
                    rs.next()
            ) {

                model.addRow(
                        new Object[]{
                                rs.getInt(
                                        "volunteer_id"
                                ),
                                rs.getString(
                                        "full_name"
                                ),
                                rs.getString(
                                        "email"
                                ),
                                rs.getString(
                                        "phone_number"
                                ),
                                rs.getString(
                                        "academic_year"
                                ),
                                rs.getString(
                                        "preferences"
                                ),
                                rs.getDouble(
                                        "total_hours"
                                )
                        }
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not search volunteers.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // VIEW volunteers DETAILS
    // =========================================================

    private void viewVolunteerDetails() {

        int selectedRow =
                volunteerTable
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


        int volunteer_id =
                Integer.parseInt(
                        model
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );


        String sql =
                "SELECT DISTINCT "
                        + "u.full_name, "
                        + "u.email, "
                        + "v.phone_number, "
                        + "v.academic_year, "
                        + "v.date_of_birth, "
                        + "v.preferences, "
                        + "v.total_hours, "
                        + "v.completed_events_count, "
                        + "v.bio, "
                        + "v.interests, "
                        + "v.availability, "
                        + "v.location "
                        + "FROM volunteers v "
                        + "JOIN users u "
                        + "ON v.user_id = u.user_id "
                        + "JOIN opportunity_applications oa "
                        + "ON oa.volunteer_id = v.volunteer_id "
                        + "JOIN opportunities vo "
                        + "ON oa.opportunity_id = vo.opportunity_id "
                        + "WHERE v.volunteer_id = ? "
                        + "AND vo.ngo_user_id = ? "
                        + "AND oa.status = 'Approved' "
                        + "AND u.is_active = TRUE";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    volunteer_id
            );

            ps.setInt(
                    2,
                    ngoUserID
            );


            ResultSet rs =
                    ps.executeQuery();


            if (
                    rs.next()
            ) {

                String details =
                        "Name: "
                                + safeText(
                                        rs.getString(
                                                "full_name"
                                        )
                                )

                                + "\nEmail: "
                                + safeText(
                                        rs.getString(
                                                "email"
                                        )
                                )

                                + "\nPhone: "
                                + safeText(
                                        rs.getString(
                                                "phone_number"
                                        )
                                )

                                + "\nAcademic Year: "
                                + safeText(
                                        rs.getString(
                                                "academic_year"
                                        )
                                )

                                + "\nDate of Birth: "
                                + (
                                rs.getDate(
                                        "date_of_birth"
                                ) == null
                                        ? "Not provided"
                                        : rs.getDate(
                                                "date_of_birth"
                                        ).toString()
                                )

                                + "\nPreferences: "
                                + safeText(
                                        rs.getString(
                                                "preferences"
                                        )
                                )

                                + "\nTotal Hours: "
                                + rs.getDouble(
                                        "total_hours"
                                )

                                + "\nCompleted Events: "
                                + rs.getInt(
                                        "completed_events_count"
                                )

                                + "\nBio: "
                                + safeText(
                                        rs.getString(
                                                "bio"
                                        )
                                )

                                + "\nInterests: "
                                + safeText(
                                        rs.getString(
                                                "interests"
                                        )
                                )

                                + "\nAvailability: "
                                + safeText(
                                        rs.getString(
                                                "availability"
                                        )
                                )

                                + "\nLocation: "
                                + safeText(
                                        rs.getString(
                                                "location"
                                        )
                                );


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
                        "Volunteer Details",
                        JOptionPane.INFORMATION_MESSAGE
                );


            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "This volunteer is not connected to one of your approved applications.",
                        "Volunteer Not Available",
                        JOptionPane.WARNING_MESSAGE
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load volunteer details.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // NULL-SAFE TEXT
    // =========================================================

    private String safeText(
            String value
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return "Not provided";
        }

        return value;
    }
}