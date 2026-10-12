package com.communityservice.ui;

import com.communityservice.utils.SkillCatalog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PreferencesPanel extends BasePanel {

    private final List<JCheckBox> causeCheckBoxes = new ArrayList<>();
    private JComboBox<String> presetSkillsCombo;
    private JTextField customSkillField;
    private JButton addSkillButton;
    private JPanel tagsPanel;
    private JComboBox<String> availabilityCombo;
    private JTextArea notesArea;

    private JButton finishButton;
    private JButton backButton;

    private final List<String> selectedSkills = new ArrayList<>();

    public PreferencesPanel() {
        super("Preferences & Skills", "Select your causes, expertise, and availability to match with projects.");
        addStepSidebar(3, "Account", "Role & Details", "Preferences", "Profile Setup");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int row = 0;
        
        JLabel titleLabel = new JLabel("Customize Preferences");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(titleLabel, setRow(gbc, row++));

        JLabel subtitleLabel = new JLabel("Step 3 of 4: Preferences & skills");
        subtitleLabel.setFont(AppTheme.FONT_BODY);
        subtitleLabel.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets = new Insets(0, 0, 16, 0);
        rightContentPanel.add(subtitleLabel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // --- 1. Causes & Focus Areas ---
        JLabel causesLabel = requiredLabel("Select Causes & Focus Areas");
        causesLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(causesLabel, setRow(gbc, row++));

        JPanel causesGrid = new JPanel(new GridLayout(0, 4, 10, 4));
        causesGrid.setOpaque(false);

        String[] causes = SkillCatalog.FOCUS_AREAS;

        for (String cause : causes) {
            JCheckBox cb = new JCheckBox(cause);
            cb.setFont(AppTheme.FONT_BODY);
            cb.setOpaque(false);
            causeCheckBoxes.add(cb);
            causesGrid.add(cb);
        }

        gbc.insets = new Insets(2, 0, 10, 0);
        rightContentPanel.add(causesGrid, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // --- 2. Skills & Certifications ---
        JLabel skillsLabel = requiredLabel("Select or Type Skills / Certifications");
        skillsLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(skillsLabel, setRow(gbc, row++));

        String[] presetSkills = new String[SkillCatalog.SKILLS.length + 1];
        presetSkills[0] = "Select a predefined skill...";
        System.arraycopy(SkillCatalog.SKILLS, 0, presetSkills, 1, SkillCatalog.SKILLS.length);
        presetSkillsCombo = new JComboBox<>(presetSkills);
        styleComboBox(presetSkillsCombo);
        presetSkillsCombo.addActionListener(e -> {
            if (presetSkillsCombo.getSelectedIndex() > 0) {
                addSkillTag((String) presetSkillsCombo.getSelectedItem());
                presetSkillsCombo.setSelectedIndex(0);
            }
        });
        rightContentPanel.add(presetSkillsCombo, setRow(gbc, row++));

        // Custom skill input row (+ Add button)
        JPanel customSkillRow = new JPanel(new BorderLayout(8, 0));
        customSkillRow.setOpaque(false);

        customSkillField = new JTextField();
        AppTheme.styleTextField(customSkillField);
        new TextPrompt("Or type a custom skill (e.g. Python, Agile)", customSkillField);

        addSkillButton = new JButton("+ Add");
        addSkillButton.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
        addSkillButton.setForeground(Color.WHITE);
        addSkillButton.setBackground(AppTheme.PRIMARY);
        addSkillButton.setFocusPainted(false);
        addSkillButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addSkillButton.addActionListener(e -> {
            String custom = customSkillField.getText().trim();
            if (!custom.isEmpty()) {
                addSkillTag(custom);
                customSkillField.setText("");
            }
        });

        customSkillRow.add(customSkillField, BorderLayout.CENTER);
        customSkillRow.add(addSkillButton, BorderLayout.EAST);
        rightContentPanel.add(customSkillRow, setRow(gbc, row++));

        // Skill Chips Container
        tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        tagsPanel.setOpaque(false);
        gbc.insets = new Insets(2, 0, 8, 0);
        rightContentPanel.add(tagsPanel, setRow(gbc, row++));
        gbc.insets = new Insets(4, 0, 4, 0);

        // No default skill: the volunteer must choose their own skills.

        // --- 3. Weekly Availability ---
        JLabel availLabel = requiredLabel("Weekly Commitment Availability");
        availLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(availLabel, setRow(gbc, row++));

        String[] availOptions = {"Select availability...", "< 1 hour / week", "1-3 hours / week", "3-5 hours / week", "5-10 hours / week", "10+ hours / week"};
        availabilityCombo = new JComboBox<>(availOptions);
        availabilityCombo.setSelectedIndex(0);
        styleComboBox(availabilityCombo);
        rightContentPanel.add(availabilityCombo, setRow(gbc, row++));

        // --- 4. Additional Notes ---
        JLabel notesLabel = new JLabel("Additional Notes or Preferences (Optional):");
        notesLabel.setFont(AppTheme.FONT_BODY);
        rightContentPanel.add(notesLabel, setRow(gbc, row++));

        notesArea = new JTextArea(3, 20);
        notesArea.setFont(AppTheme.FONT_BODY);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 205, 210), 1, true));
        rightContentPanel.add(notesScroll, setRow(gbc, row++));

        // --- Action Buttons ---
        finishButton = AppTheme.createPrimaryButton("Save Preferences & Complete Setup");
        gbc.insets = new Insets(12, 0, 4, 0);
        rightContentPanel.add(finishButton, setRow(gbc, row++));
        bindEnterToButton(finishButton);

        backButton = AppTheme.createBackButton("← Back");
        backButton.setFont(AppTheme.FONT_BODY);
        backButton.setForeground(AppTheme.TEXT_MUTED);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0, 0, 0, 0);
        rightContentPanel.add(backButton, setRow(gbc, row++));
    }

    private void addSkillTag(String skill) {
        if (selectedSkills.contains(skill)) return;
        selectedSkills.add(skill);

        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        chip.setBackground(new Color(235, 248, 245));
        chip.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY, 1, true));

        JLabel skillText = new JLabel(skill);
        skillText.setFont(AppTheme.FONT_SMALL);
        skillText.setForeground(AppTheme.PRIMARY);

        JLabel removeBtn = new JLabel("×");
        removeBtn.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
        removeBtn.setForeground(new Color(220, 53, 69));
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedSkills.remove(skill);
                tagsPanel.remove(chip);
                tagsPanel.revalidate();
                tagsPanel.repaint();
            }
        });

        chip.add(skillText);
        chip.add(removeBtn);
        tagsPanel.add(chip);
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(AppTheme.FONT_BODY);
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(box.getPreferredSize().width, 36));
    }

    private GridBagConstraints setRow(GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        return gbc;
    }

    public List<String> getSelectedCauses() {
        List<String> causes = new ArrayList<>();
        for (JCheckBox cb : causeCheckBoxes) {
            if (cb.isSelected()) causes.add(cb.getText());
        }
        return causes;
    }

    public List<String> getSkills() { return new ArrayList<>(selectedSkills); }
    public String getAvailability() { return (String) availabilityCombo.getSelectedItem(); }
    public String getNotes() { return notesArea.getText().trim(); }

    public void setFinishListener(ActionListener l) {
        finishButton.addActionListener(e -> {
            if (getSelectedCauses().isEmpty()) { javax.swing.JOptionPane.showMessageDialog(this, "Please select at least one cause or focus area.", "Required field", javax.swing.JOptionPane.WARNING_MESSAGE); return; }
            if (getSkills().isEmpty()) { javax.swing.JOptionPane.showMessageDialog(this, "Please select or add at least one skill.", "Required field", javax.swing.JOptionPane.WARNING_MESSAGE); return; }
            if (availabilityCombo.getSelectedIndex() == 0) { javax.swing.JOptionPane.showMessageDialog(this, "Please select your weekly availability.", "Required field", javax.swing.JOptionPane.WARNING_MESSAGE); return; }
            l.actionPerformed(e);
        });
    }

    public void setBackListener(ActionListener l) { backButton.addActionListener(l); }

    /** Resets causes, skills, availability and notes to a clean state. */
    public void clearFields() {
        for (JCheckBox cb : causeCheckBoxes) {
            cb.setSelected(false);
        }
        selectedSkills.clear();
        tagsPanel.removeAll();
        tagsPanel.revalidate();
        tagsPanel.repaint();
        availabilityCombo.setSelectedIndex(0);
        notesArea.setText("");
    }
}