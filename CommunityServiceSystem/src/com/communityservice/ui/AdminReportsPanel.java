package com.communityservice.ui;

import com.communityservice.dao.ReportDAO;
import com.communityservice.models.Report;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminReportsPanel extends JPanel {

    private final ReportDAO reportDAO = new ReportDAO();
    private final int adminUserId;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Report> currentReports;

    public AdminReportsPanel(int adminUserId) {
        this.adminUserId = adminUserId;
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("Reports");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Type", "Format", "Generated", "Date Range"}, 0) {
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

        JButton addButton = new JButton("Generate Report");
        styleButton(addButton, UIColors.PRIMARY);
        addButton.addActionListener(e -> openAddDialog());
        actions.add(addButton);

        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton, UIColors.RED);
        deleteButton.addActionListener(e -> deleteSelected());
        actions.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, UIColors.STAT_ORANGE);
        refreshButton.addActionListener(e -> loadReports());
        actions.add(refreshButton);

        add(actions, BorderLayout.SOUTH);

        loadReports();
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

    private void openAddDialog() {
        AdminAddReportDialog dialog = new AdminAddReportDialog(
                SwingUtilities.getWindowAncestor(this), adminUserId);
        dialog.setVisible(true);
        if (dialog.wasSaved()) {
            loadReports();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Report report = currentReports.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete report \"" + report.getTitle() + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reportDAO.deleteReport(report.getReportID());
            loadReports();
        }
    }

    private void loadReports() {
        currentReports = reportDAO.getAllReports();
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");
        for (Report r : currentReports) {
            String generated = r.getGeneratedDate() != null ? r.getGeneratedDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")) : "";
            String range = (r.getStartDate() != null ? r.getStartDate().format(fmt) : "?")
                    + " – " + (r.getEndDate() != null ? r.getEndDate().format(fmt) : "?");
            tableModel.addRow(new Object[]{
                    r.getReportID(), r.getTitle(), r.getReportType(), r.getFileFormat(), generated, range
            });
        }
    }
}
