package com.communityservice.ui;

import com.communityservice.dao.SystemPolicyDAO;
import com.communityservice.models.SystemPolicy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminPolicyPanel extends JPanel {

    private final SystemPolicyDAO policyDAO = new SystemPolicyDAO();
    private final int adminUserId;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<SystemPolicy> currentPolicies;

    public AdminPolicyPanel(int adminUserId) {
        this.adminUserId = adminUserId;
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("System Policies");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(UIColors.TEXT_DARK);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Version", "Effective Date", "Active"}, 0) {
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

        JButton addButton = new JButton("Add Policy");
        styleButton(addButton, UIColors.PRIMARY);
        addButton.addActionListener(e -> openAddDialog());
        actions.add(addButton);

        JButton toggleButton = new JButton("Toggle Active");
        styleButton(toggleButton, UIColors.STAT_ORANGE);
        toggleButton.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) toggleButton.setEnabled(table.getSelectedRow() >= 0); });
        toggleButton.addActionListener(e -> toggleSelected());
        actions.add(toggleButton);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, UIColors.GREEN);
        refreshButton.addActionListener(e -> loadPolicies());
        actions.add(refreshButton);

        add(actions, BorderLayout.SOUTH);

        loadPolicies();
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

    private void loadPolicies() {
        currentPolicies = policyDAO.getAllSystemPolicies();
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");
        for (SystemPolicy p : currentPolicies) {
            tableModel.addRow(new Object[]{
                    p.getPolicyID(), p.getPolicyTitle(), p.getVersion(),
                    p.getEffectiveDate() != null ? p.getEffectiveDate().format(fmt) : "",
                    p.is_active() ? "Active" : "Inactive"
            });
        }
    }

    private void toggleSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a policy first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SystemPolicy policy = currentPolicies.get(row);
        policy.setActive(!policy.is_active());
        policy.setLastUpdatedBy(adminUserId);
        policy.setLastUpdateDate(LocalDateTime.now());
        if (policyDAO.updateSystemPolicy(policy)) {
            loadPolicies();
        } else {
            JOptionPane.showMessageDialog(this, "Could not update this policy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Add System Policy", JDialog.ModalityType.APPLICATION_MODAL);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        int row = 0;

        JLabel titleLbl = new JLabel("Policy Title");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = row++;
        form.add(titleLbl, gbc);
        JTextField titleField = new JTextField(24);
        gbc.gridy = row++;
        form.add(titleField, gbc);

        JLabel versionLbl = new JLabel("Version");
        gbc.gridy = row++;
        form.add(versionLbl, gbc);
        JTextField versionField = new JTextField("1.0");
        gbc.gridy = row++;
        form.add(versionField, gbc);

        JLabel effectiveLbl = new JLabel("Effective Date (YYYY-MM-DD)");
        gbc.gridy = row++;
        form.add(effectiveLbl, gbc);
        JTextField effectiveField = new JTextField(LocalDate.now().toString());
        gbc.gridy = row++;
        form.add(effectiveField, gbc);

        JLabel contentLbl = new JLabel("Policy Content");
        gbc.gridy = row++;
        form.add(contentLbl, gbc);
        JTextArea contentArea = new JTextArea(5, 24);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createLineBorder(UIColors.BORDER));
        gbc.gridy = row++;
        form.add(contentArea, gbc);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dialog.dispose());
        JButton save = new JButton("Save Policy");
        save.setEnabled(false);
        save.setBackground(UIColors.PRIMARY);
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        javax.swing.event.DocumentListener policyValidation = new javax.swing.event.DocumentListener() {
            private void update(){
                boolean ok = !titleField.getText().trim().isEmpty() && !contentArea.getText().trim().isEmpty();
                try { LocalDate.parse(effectiveField.getText().trim()); } catch (Exception ex) { ok = false; }
                save.setEnabled(ok);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e){update();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){update();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){update();}
        };
        titleField.getDocument().addDocumentListener(policyValidation);
        contentArea.getDocument().addDocumentListener(policyValidation);
        effectiveField.getDocument().addDocumentListener(policyValidation);
        save.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a policy title.", "Missing Title", JOptionPane.WARNING_MESSAGE);
                return;
            }
            LocalDate effectiveDate;
            try {
                effectiveDate = LocalDate.parse(effectiveField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Date must be in YYYY-MM-DD format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SystemPolicy policy = new SystemPolicy();
            policy.setPolicyTitle(title);
            policy.setPolicyContent(contentArea.getText().trim());
            policy.setVersion(versionField.getText().trim());
            policy.setEffectiveDate(effectiveDate);
            policy.setLastUpdatedBy(adminUserId);
            policy.setLastUpdateDate(LocalDateTime.now());
            policy.setActive(true);

            if (policyDAO.addSystemPolicy(policy)) {
                dialog.dispose();
                loadPolicies();
            } else {
                JOptionPane.showMessageDialog(dialog, "Could not save the policy.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttons.add(cancel);
        buttons.add(save);
        gbc.insets = new Insets(16, 0, 0, 0);
        gbc.gridy = row++;
        form.add(buttons, gbc);

        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
