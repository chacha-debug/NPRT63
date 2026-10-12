package com.communityservice.ui;

import com.communityservice.dao.AuditLogDAO;
import com.communityservice.dao.UserDAO;
import com.communityservice.models.AuditLog;
import com.communityservice.models.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AdminUsersPanel extends JPanel {

    private final UserDAO userDAO = new UserDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();
    private final int adminUserId;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<User> currentUsers;

    public AdminUsersPanel(int adminUserId) {
        this.adminUserId = adminUserId;
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("All Users");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Full Name", "Email", "Role", "Verified", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(UIColors.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(UIColors.HIGHLIGHT);
        centerColumn(0);
        centerColumn(4);
        centerColumn(5);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIColors.BORDER));
        add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, UIColors.PRIMARY);
        refreshButton.addActionListener(e -> loadUsers());
        actions.add(refreshButton);

        JButton toggleButton = new JButton("Activate / Deactivate Selected");
        styleButton(toggleButton, UIColors.STAT_ORANGE);
        toggleButton.addActionListener(e -> toggleSelectedUser());
        actions.add(toggleButton);

        JButton removeButton = new JButton("Remove Account");
        styleButton(removeButton, UIColors.RED);
        removeButton.addActionListener(e -> removeSelectedUser());
        actions.add(removeButton);

        JButton approveAdminButton = new JButton("Approve Admin");
        styleButton(approveAdminButton, UIColors.GREEN);
        approveAdminButton.addActionListener(e -> approveSelectedAdmin());
        actions.add(approveAdminButton);

        JButton rejectAdminButton = new JButton("Reject Admin");
        styleButton(rejectAdminButton, UIColors.RED);
        rejectAdminButton.addActionListener(e -> rejectSelectedAdmin());
        actions.add(rejectAdminButton);

        JButton restoreButton = new JButton("Restore Account");
        styleButton(restoreButton, UIColors.GREEN);
        restoreButton.addActionListener(e -> restoreSelectedUser());
        actions.add(restoreButton);

        JButton[] selectionActions = {toggleButton, removeButton, approveAdminButton, rejectAdminButton, restoreButton};
        for (JButton b : selectionActions) b.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selected = table.getSelectedRow() >= 0;
                for (JButton b : selectionActions) b.setEnabled(selected);
            }
        });

        add(actions, BorderLayout.SOUTH);

        loadUsers();
    }

    private void centerColumn(int index) {
        DefaultTableCellRenderer centered = new DefaultTableCellRenderer();
        centered.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(index).setCellRenderer(centered);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 20, 36));
    }

    private void loadUsers() {
        currentUsers = userDAO.getAllUsers();
        tableModel.setRowCount(0);
        for (User user : currentUsers) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isVerified() ? "Yes" : "No",
                    statusLabel(user)
            });
        }
    }

    private String statusLabel(User user) {
        if (user.isDeleted()) {
            return "Removed";
        }
        if ("Administrator".equalsIgnoreCase(user.getRole())) {
            String approval = userDAO.getAdminApprovalStatus(user.getUserId());
            if ("PENDING".equalsIgnoreCase(approval)) return "Admin Pending Approval";
            if ("REJECTED".equalsIgnoreCase(approval)) return "Admin Rejected";
        }
        return user.isActive() ? "Active" : "Inactive";
    }

    /** Returns the selected user, or null (after showing a warning) if nothing is selected. */
    private User getSelectedUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return currentUsers.get(row);
    }

    private void toggleSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        if (user.isDeleted()) {
            JOptionPane.showMessageDialog(this,
                    "This account has been removed. Restore it first before changing its active status.",
                    "Account Removed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean newStatus = !user.isActive();

        int confirm = JOptionPane.showConfirmDialog(this,
                (newStatus ? "Activate" : "Deactivate") + " " + user.getFullName() + "'s account?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (userDAO.setUserActiveStatus(user.getUserId(), newStatus)) {
            logAction(newStatus ? "Activate Account" : "Deactivate Account", user, null);
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Could not update this user's status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        if (user.isDeleted()) {
            JOptionPane.showMessageDialog(this, "That account has already been removed.", "Already Removed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (user.getUserId() == adminUserId) {
            JOptionPane.showMessageDialog(this, "You can't remove the account you're currently signed in with.",
                    "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String reason = JOptionPane.showInputDialog(this,
                "Remove " + user.getFullName() + "'s account.\n"
                        + "They will immediately be blocked from signing in again.\n\n"
                        + "Reason (shown to the user, optional):",
                "Remove Account", JOptionPane.WARNING_MESSAGE);
        if (reason == null) {
            // Cancelled
            return;
        }

        if (userDAO.softDeleteUser(user.getUserId(), adminUserId, reason.isBlank() ? null : reason.trim())) {
            logAction("Remove Account", user, reason);
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Could not remove this account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restoreSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        if (!user.isDeleted()) {
            JOptionPane.showMessageDialog(this, "That account hasn't been removed.", "Not Removed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Restore " + user.getFullName() + "'s account? They will be able to sign in again.",
                "Confirm Restore", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (userDAO.restoreUser(user.getUserId())) {
            logAction("Restore Account", user, null);
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Could not restore this account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void approveSelectedAdmin() {
        User user = getSelectedUser();
        if (user == null || !"Administrator".equalsIgnoreCase(user.getRole())) {
            JOptionPane.showMessageDialog(this, "Select a pending administrator account.", "Not an Administrator", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!"PENDING".equalsIgnoreCase(userDAO.getAdminApprovalStatus(user.getUserId()))) {
            JOptionPane.showMessageDialog(this, "This administrator is not awaiting approval.", "No Approval Needed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Approve " + user.getFullName() + " as an administrator? This activates administrator access.",
                "Confirm Administrator Approval", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (userDAO.approveAdmin(user.getUserId())) {
            logAction("Approve Administrator", user, "Administrator application approved.");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Could not approve this administrator account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectSelectedAdmin() {
        User user = getSelectedUser();
        if (user == null || !"Administrator".equalsIgnoreCase(user.getRole())) {
            JOptionPane.showMessageDialog(this, "Select a pending administrator account.", "Not an Administrator", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!"PENDING".equalsIgnoreCase(userDAO.getAdminApprovalStatus(user.getUserId()))) {
            JOptionPane.showMessageDialog(this, "This administrator is not awaiting approval.", "No Approval Needed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reject " + user.getFullName() + "'s administrator application? They will not receive administrator access.",
                "Confirm Administrator Rejection", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (userDAO.rejectAdmin(user.getUserId())) {
            logAction("Reject Administrator", user, "Administrator application rejected.");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Could not reject this administrator account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logAction(String action, User targetUser, String details) {
        AuditLog log = new AuditLog();
        log.setUserID(adminUserId);
        log.setAction(action);
        log.setEntityType("User");
        log.setEntityID(targetUser.getUserId());
        log.setIpAddress("local");
        log.setDetails(details != null && !details.isBlank()
                ? details.trim()
                : (action + " — " + targetUser.getEmail()));
        log.setStatus("Success");
        auditLogDAO.addAuditLog(log);
    }
}
