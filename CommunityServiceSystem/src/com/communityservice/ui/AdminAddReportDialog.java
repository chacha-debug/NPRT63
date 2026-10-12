package com.communityservice.ui;

import com.communityservice.dao.ReportDAO;
import com.communityservice.models.Report;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AdminAddReportDialog extends JDialog {

    private final ReportDAO reportDAO = new ReportDAO();
    private final int adminUserId;
    private boolean saved = false;

    private JTextField titleField;
    private JComboBox<String> typeCombo;
    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> formatCombo;
    private JTextArea summaryArea;
    private JButton saveButton;

    public AdminAddReportDialog(java.awt.Window parent, int adminUserId) {
        super(parent, "Generate New Report", ModalityType.APPLICATION_MODAL);
        this.adminUserId = adminUserId;
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int row = 0;

        form.add(label("Report Title"), pos(gbc, row++));
        titleField = new JTextField(24);
        form.add(titleField, pos(gbc, row++));

        form.add(label("Report Type"), pos(gbc, row++));
        typeCombo = new JComboBox<>(new String[]{"User Activity", "NGO Verification", "Volunteer Hours", "Attendance", "Other"});
        form.add(typeCombo, pos(gbc, row++));

        form.add(label("Start Date (YYYY-MM-DD)"), pos(gbc, row++));
        startDateField = new JTextField(LocalDate.now().minusDays(30).toString());
        form.add(startDateField, pos(gbc, row++));

        form.add(label("End Date (YYYY-MM-DD)"), pos(gbc, row++));
        endDateField = new JTextField(LocalDate.now().toString());
        form.add(endDateField, pos(gbc, row++));

        form.add(label("File Format"), pos(gbc, row++));
        formatCombo = new JComboBox<>(new String[]{"PDF", "CSV", "XLSX"});
        form.add(formatCombo, pos(gbc, row++));

        form.add(label("Summary"), pos(gbc, row++));
        summaryArea = new JTextArea(4, 24);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBorder(BorderFactory.createLineBorder(UIColors.BORDER));
        form.add(summaryArea, pos(gbc, row++));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        saveButton = new JButton("Generate Report");
        saveButton.setEnabled(false);
        saveButton.setBackground(UIColors.PRIMARY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 13));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> save());
        javax.swing.event.DocumentListener validation = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e){ updateSaveEnabled(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e){ updateSaveEnabled(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e){ updateSaveEnabled(); }
        };
        titleField.getDocument().addDocumentListener(validation);
        startDateField.getDocument().addDocumentListener(validation);
        endDateField.getDocument().addDocumentListener(validation);
        updateSaveEnabled();
        buttons.add(cancelButton);
        buttons.add(saveButton);

        gbc.insets = new Insets(16, 0, 0, 0);
        form.add(buttons, pos(gbc, row++));

        setContentPane(form);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(UIColors.TEXT_GREY);
        return l;
    }

    private GridBagConstraints pos(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    private void updateSaveEnabled() {
        boolean valid = !titleField.getText().trim().isEmpty();
        try {
            LocalDate start = LocalDate.parse(startDateField.getText().trim());
            LocalDate end = LocalDate.parse(endDateField.getText().trim());
            valid = valid && !end.isBefore(start);
        } catch (Exception ex) { valid = false; }
        saveButton.setEnabled(valid);
    }

    private void save() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a report title.", "Missing Title", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateField.getText().trim());
            endDate = LocalDate.parse(endDateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dates must be in YYYY-MM-DD format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Report report = new Report();
        report.setGeneratedBy(adminUserId);
        report.setReportType((String) typeCombo.getSelectedItem());
        report.setTitle(title);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setFileFormat((String) formatCombo.getSelectedItem());
        report.setDataSummary(summaryArea.getText().trim());
        report.setGeneratedDate(LocalDateTime.now());

        if (reportDAO.addReport(report)) {
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Could not save the report. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean wasSaved() {
        return saved;
    }
}
