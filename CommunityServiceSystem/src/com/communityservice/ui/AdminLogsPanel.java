package com.communityservice.ui;

import com.communityservice.dao.AuditLogDAO;
import com.communityservice.models.AuditLog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminLogsPanel extends JPanel {

    private final AuditLogDAO auditLogDAO = new AuditLogDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    public AdminLogsPanel() {
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("Audit Logs");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Action", "Entity", "IP Address", "Status", "When"}, 0) {
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

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIColors.BORDER));
        add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(UIColors.PRIMARY);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setOpaque(true);
        refreshButton.setPreferredSize(new Dimension(120, 36));
        refreshButton.addActionListener(e -> refreshLogs());
        actions.add(refreshButton);
        add(actions, BorderLayout.SOUTH);

        refreshLogs();
    }

    public void refreshLogs() {
        List<AuditLog> logs = auditLogDAO.getAllAuditLogs();
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
        for (AuditLog log : logs) {
            tableModel.addRow(new Object[]{
                    log.getLogID(),
                    log.getUserID(),
                    log.getAction(),
                    (log.getEntityType() != null ? log.getEntityType() : "") + (log.getEntityID() > 0 ? " #" + log.getEntityID() : ""),
                    log.getIpAddress(),
                    log.getStatus(),
                    log.getTimestamp() != null ? log.getTimestamp().format(fmt) : ""
            });
        }
    }
}
