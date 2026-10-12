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

public class OrganizationProfilePanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    // =========================================================
    // LOGGED-IN ngos USER ID
    // =========================================================

    private final int ngoUserID;


    // =========================================================
    // FORM FIELDS
    // =========================================================

    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField orgNameField;
    private JTextField registrationNumberField;
    private JTextField contactPhoneField;
    private JTextField verifiedStatusField;
    private JTextField websiteField;


    
    public OrganizationProfilePanel() {
        this(1);
    }


    // =========================================================
    //  CONSTRUCTOR
    // =========================================================

    public OrganizationProfilePanel(int ngoUserID) {

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
                        "Organization Profile"
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
                        520,
                        520
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
        // CREATE FIELDS
        // =====================================================

        fullNameField =
                new JTextField();

        emailField =
                new JTextField();

        orgNameField =
                new JTextField();

        registrationNumberField =
                new JTextField();

        contactPhoneField =
                new JTextField();

        verifiedStatusField =
                new JTextField();

        websiteField =
                new JTextField();


        // =====================================================
        // ADD FIELDS
        // =====================================================

        addField(
                formCard,
                "Contact Person",
                fullNameField
        );

        addField(
                formCard,
                "Email",
                emailField
        );

        addField(
                formCard,
                "Organization Name",
                orgNameField
        );

        addField(
                formCard,
                "Registration Number",
                registrationNumberField
        );

        addField(
                formCard,
                "Contact Phone",
                contactPhoneField
        );

        addField(
                formCard,
                "Verified Status",
                verifiedStatusField
        );

        addField(
                formCard,
                "Website",
                websiteField
        );


        

        verifiedStatusField.setEditable(
                false
        );

        verifiedStatusField.setBackground(
                new Color(
                        240,
                        240,
                        240
                )
        );


        // =====================================================
        // BUTTON AREA
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


        JButton saveButton =
                new JButton(
                        "Save Changes"
                );

        saveButton.setBackground(
                TEAL
        );

        saveButton.setForeground(
                Color.WHITE
        );

        saveButton.setFocusPainted(
                false
        );

        saveButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        saveButton.addActionListener(
                e -> saveProfile()
        );


        buttonPanel.add(
                saveButton
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
        // INITIAL DATABASE LOAD
        // =====================================================

        loadProfile();
    }


    // =========================================================
    // ADD FIELD
    // =========================================================

    private void addField(
            JPanel panel,
            String labelText,
            JTextField field
    ) {

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.setMaximumSize(
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
                field
        );

        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );
    }


    // =========================================================
    // LOAD ORGANIZATION PROFILE
    // =========================================================

    private void loadProfile() {

        String sql =
                "SELECT "
                        + "u.full_name, "
                        + "u.email, "
                        + "n.org_name, "
                        + "n.registration_number, "
                        + "n.contact_phone, "
                        + "n.verified_status, "
                        + "n.website "
                        + "FROM users u "
                        + "JOIN ngos n "
                        + "ON u.user_id = n.user_id "
                        + "WHERE u.user_id = ?";


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


            if (
                    rs.next()
            ) {

                fullNameField.setText(
                        safeText(
                                rs.getString(
                                        "full_name"
                                )
                        )
                );

                emailField.setText(
                        safeText(
                                rs.getString(
                                        "email"
                                )
                        )
                );

                orgNameField.setText(
                        safeText(
                                rs.getString(
                                        "org_name"
                                )
                        )
                );

                registrationNumberField.setText(
                        safeText(
                                rs.getString(
                                        "registration_number"
                                )
                        )
                );

                contactPhoneField.setText(
                        safeText(
                                rs.getString(
                                        "contact_phone"
                                )
                        )
                );

                verifiedStatusField.setText(
                        safeText(
                                rs.getString(
                                        "verified_status"
                                )
                        )
                );

                websiteField.setText(
                        safeText(
                                rs.getString(
                                        "website"
                                )
                        )
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No organization profile was found for this account.",
                        "Profile Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load organization profile.\n"
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

        
        loadProfile();
    }


    // =========================================================
    // SAVE ORGANIZATION PROFILE
    // =========================================================

    private void saveProfile() {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (
                fullNameField
                        .getText()
                        .trim()
                        .isEmpty()

                || emailField
                        .getText()
                        .trim()
                        .isEmpty()

                || orgNameField
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // UPDATE USER ACCOUNT
        // =====================================================

        String userSql =
                "UPDATE users "
                        + "SET full_name = ?, "
                        + "email = ? "
                        + "WHERE user_id = ?";


        

        String ngoSql =
                "UPDATE ngos "
                        + "SET org_name = ?, "
                        + "registration_number = ?, "
                        + "contact_phone = ?, "
                        + "website = ? "
                        + "WHERE user_id = ?";


        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            
            conn.setAutoCommit(
                    false
            );


            try (
                    PreparedStatement userStatement =
                            conn.prepareStatement(
                                    userSql
                            );

                    PreparedStatement ngoStatement =
                            conn.prepareStatement(
                                    ngoSql
                            )
            ) {

                // =============================================
                // USER TABLE
                // =============================================

                userStatement.setString(
                        1,
                        fullNameField
                                .getText()
                                .trim()
                );

                userStatement.setString(
                        2,
                        emailField
                                .getText()
                                .trim()
                );

                userStatement.setInt(
                        3,
                        ngoUserID
                );


                // =============================================
                // ngos TABLE
                // =============================================

                ngoStatement.setString(
                        1,
                        orgNameField
                                .getText()
                                .trim()
                );

                ngoStatement.setString(
                        2,
                        registrationNumberField
                                .getText()
                                .trim()
                );

                ngoStatement.setString(
                        3,
                        contactPhoneField
                                .getText()
                                .trim()
                );

                ngoStatement.setString(
                        4,
                        websiteField
                                .getText()
                                .trim()
                );

                ngoStatement.setInt(
                        5,
                        ngoUserID
                );


                int userRows =
                        userStatement.executeUpdate();

                int ngoRows =
                        ngoStatement.executeUpdate();


                // =============================================
                // CHECK THAT ACCOUNT EXISTS
                // =============================================

                if (
                        userRows == 0
                        || ngoRows == 0
                ) {

                    conn.rollback();

                    JOptionPane.showMessageDialog(
                            this,
                            "The organization profile could not be updated because the account was not found.",
                            "Profile Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }


                // =============================================
                // SAVE BOTH UPDATES
                // =============================================

                conn.commit();


                JOptionPane.showMessageDialog(
                        this,
                        "Organization profile updated successfully!",
                        "Profile",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // Reload database values after saving
                loadProfile();


            } catch (
                    SQLException e
            ) {

                conn.rollback();

                throw e;
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update organization profile.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // NULL-SAFE DATABASE TEXT
    // =========================================================

    private String safeText(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }
}