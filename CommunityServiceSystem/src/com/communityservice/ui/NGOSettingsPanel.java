/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NGOSettingsPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    // Logged-in ngos user ID
    private final int ngoUserID;

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;


    

    public NGOSettingsPanel() {
        this(1);
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NGOSettingsPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);


        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading =
                new JLabel("Settings");

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
        // CENTER WRAPPER
        // =====================================================

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setBackground(BACKGROUND);


        // =====================================================
        // CARD
        // =====================================================

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(Color.WHITE);

        card.setPreferredSize(
                new Dimension(
                        500,
                        360
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
                )
        );


        // =====================================================
        // CARD TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "Change Password"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(BLUE);

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(
                        20
                )
        );


        // =====================================================
        // PASSWORD FIELDS
        // =====================================================

        currentPasswordField =
                new JPasswordField();

        newPasswordField =
                new JPasswordField();

        confirmPasswordField =
                new JPasswordField();


        addPasswordField(
                card,
                "Current Password",
                currentPasswordField
        );

        addPasswordField(
                card,
                "New Password",
                newPasswordField
        );

        addPasswordField(
                card,
                "Confirm New Password",
                confirmPasswordField
        );


        // =====================================================
        // BUTTON
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttonPanel.setBackground(
                Color.WHITE
        );

        buttonPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JButton changeButton =
                new JButton(
                        "Change Password"
                );

        changeButton.setBackground(
                TEAL
        );

        changeButton.setForeground(
                Color.WHITE
        );

        changeButton.setFocusPainted(
                false
        );

        changeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );


        changeButton.addActionListener(
                e -> changePassword()
        );


        buttonPanel.add(
                changeButton
        );

        card.add(
                buttonPanel
        );


        wrapper.add(
                card
        );


        add(
                wrapper,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // ADD PASSWORD FIELD
    // =========================================================

    private void addPasswordField(
            JPanel panel,
            String labelText,
            JPasswordField field
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


        panel.add(label);

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(field);

        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private void changePassword() {

        String currentPassword =
                new String(
                        currentPasswordField
                                .getPassword()
                );

        String newPassword =
                new String(
                        newPasswordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );


        // =====================================================
        // VALIDATE EMPTY FIELDS
        // =====================================================

        if (
                currentPassword.isEmpty()
                        || newPassword.isEmpty()
                        || confirmPassword.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all password fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // CHECK PASSWORD MATCH
        // =====================================================

        if (
                !newPassword.equals(
                        confirmPassword
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "New passwords do not match.",
                    "Password Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // PASSWORD LENGTH
        // =====================================================

        if (
                newPassword.length() < 6
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password must contain at least 6 characters.",
                    "Password Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // PREVENT SAME PASSWORD
        // =====================================================

        if (
                currentPassword.equals(
                        newPassword
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your new password must be different from your current password.",
                    "Password Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // SQL
        // =====================================================

        String selectSql =
                "SELECT password_hash "
                        + "FROM users "
                        + "WHERE user_id = ?";


        String updateSql =
                "UPDATE users "
                        + "SET password_hash = ? "
                        + "WHERE user_id = ?";


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement selectStatement =
                        conn.prepareStatement(
                                selectSql
                        )
        ) {

            // REAL LOGGED-IN ngos
            selectStatement.setInt(
                    1,
                    ngoUserID
            );


            ResultSet rs =
                    selectStatement.executeQuery();


            // =================================================
            // ACCOUNT NOT FOUND
            // =================================================

            if (
                    !rs.next()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "User account could not be found.",
                        "Account Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            String storedPassword =
                    rs.getString(
                            "password_hash"
                    );


            if (
                    storedPassword == null
                            || storedPassword.trim().isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "No password is configured for this account.",
                        "Account Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            

            String hashedCurrentPassword =
                    hashPassword(
                            currentPassword
                    );


            boolean currentMatches =
                    storedPassword.equals(
                            currentPassword
                    )
                            ||
                            storedPassword.equals(
                                    hashedCurrentPassword
                            );


            if (
                    !currentMatches
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Current password is incorrect.",
                        "Password Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // UPDATE PASSWORD
            // =================================================

            try (
                    PreparedStatement updateStatement =
                            conn.prepareStatement(
                                    updateSql
                            )
            ) {

                String hashedNewPassword =
                        hashPassword(
                                newPassword
                        );


                updateStatement.setString(
                        1,
                        hashedNewPassword
                );


                updateStatement.setInt(
                        2,
                        ngoUserID
                );


                int updated =
                        updateStatement.executeUpdate();


                if (
                        updated > 0
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Password changed successfully!",
                            "Settings",
                            JOptionPane.INFORMATION_MESSAGE
                    );


                    clearFields();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Password could not be changed.",
                            "Settings Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }


        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not change password.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // HASH PASSWORD
    // =========================================================

    private String hashPassword(
            String password
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );


            byte[] encodedHash =
                    digest.digest(
                            password.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );


            StringBuilder hexString =
                    new StringBuilder();


            for (
                    byte b : encodedHash
            ) {

                String hex =
                        Integer.toHexString(
                                0xff & b
                        );


                if (
                        hex.length() == 1
                ) {

                    hexString.append('0');
                }


                hexString.append(hex);
            }


            return hexString.toString();


        } catch (
                NoSuchAlgorithmException e
        ) {

            throw new RuntimeException(
                    "SHA-256 hashing is not available.",
                    e
            );
        }
    }


    // =========================================================
    // CLEAR PASSWORD FIELDS
    // =========================================================

    private void clearFields() {

        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }
}