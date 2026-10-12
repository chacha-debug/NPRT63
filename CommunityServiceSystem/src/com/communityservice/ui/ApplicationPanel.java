/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.ApplicationDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationPanel extends JPanel {

    private static final Color BLUE =
            UIColors.PRIMARY;

    private static final Color TEAL =
            UIColors.GREEN;

    private static final Color REJECT_RED =
            UIColors.RED;

    private static final Color BACKGROUND =
            UIColors.BACKGROUND;

    private JTable applicationTable;
    private DefaultTableModel model;

    // Logged-in ngos user ID
    private final int ngoUserID;


   

    public ApplicationPanel() {

        this(1);
    }


    // =========================================================
    // REAL CONSTRUCTOR
    // =========================================================

    public ApplicationPanel(int ngoUserID) {

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
                        "Volunteer Applications"
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
        // MAIN AREA
        // =====================================================

        JPanel applicationArea =
                new JPanel(
                        new BorderLayout()
                );

        applicationArea.setBackground(
                Color.WHITE
        );

        applicationArea.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        25,
                        25,
                        25
                )
        );


        add(
                applicationArea,
                BorderLayout.CENTER
        );


        // =====================================================
        // TABLE MODEL
        // =====================================================

        String[] columns = {
                "Application ID",
                "Applicant Name",
                "Event",
                "Application Date",
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

        applicationTable =
                new JTable(
                        model
                );

        applicationTable.setRowHeight(
                32
        );

        applicationTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        applicationTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        applicationTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );


        JScrollPane tableScroll =
                new JScrollPane(
                        applicationTable
                );


        applicationArea.add(
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
                                15
                        )
                );

        buttonPanel.setBackground(
                Color.WHITE
        );


        JButton rejectButton =
                new JButton(
                        "Reject Selected"
                );

        rejectButton.setBackground(
                REJECT_RED
        );

        rejectButton.setForeground(
                Color.WHITE
        );

        rejectButton.setFocusPainted(
                false
        );


        JButton approveButton =
                new JButton(
                        "Approve Selected"
                );

        approveButton.setBackground(
                TEAL
        );

        approveButton.setForeground(
                Color.WHITE
        );

        approveButton.setFocusPainted(
                false
        );


        buttonPanel.add(
                rejectButton
        );

        buttonPanel.add(
                approveButton
        );


        applicationArea.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        approveButton.addActionListener(
                e -> approveSelectedApplication()
        );

        rejectButton.addActionListener(
                e -> rejectSelectedApplication()
        );


        // Initial database load
        loadApplications();
    }


    // =========================================================
    // LOAD APPLICATIONS
    // =========================================================

    private void loadApplications() {

        model.setRowCount(
                0
        );


        List<Object[]> applications =
                ApplicationDAO
                        .getApplicationsForNGO(
                                ngoUserID
                        );


        for (
                Object[] row
                : applications
        ) {

            model.addRow(
                    row
            );
        }
    }


    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshData() {

        loadApplications();
    }


    // =========================================================
    // APPROVE APPLICATION
    // =========================================================

    private void approveSelectedApplication() {

        int selectedRow =
                applicationTable
                        .getSelectedRow();


        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first.",
                    "No Application Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int application_id =
                Integer.parseInt(
                        model
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Approve this application?",
                        "Confirm Approval",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {

            return;
        }


        boolean success =
                ApplicationDAO
                        .updateApplicationStatus(
                                application_id,
                                "Approved",
                                null,
                                ngoUserID
                        );


        if (
                success
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application approved successfully!",
                    "Application",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // Refresh immediately after approval
            loadApplications();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not approve the application.",
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    private void rejectSelectedApplication() {

        int selectedRow =
                applicationTable
                        .getSelectedRow();


        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first.",
                    "No Application Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        String reason =
                JOptionPane.showInputDialog(
                        this,
                        "Enter rejection reason:",
                        "Reject Application",
                        JOptionPane.QUESTION_MESSAGE
                );


        if (
                reason == null
                ||
                reason.trim().isEmpty()
        ) {

            return;
        }


        int application_id =
                Integer.parseInt(
                        model
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Reject this application?",
                        "Confirm Rejection",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {

            return;
        }


        boolean success =
                ApplicationDAO
                        .updateApplicationStatus(
                                application_id,
                                "Rejected",
                                reason.trim(),
                                ngoUserID
                        );


        if (
                success
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application rejected successfully.",
                    "Application",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // Refresh immediately after rejection
            loadApplications();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not reject the application.",
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}