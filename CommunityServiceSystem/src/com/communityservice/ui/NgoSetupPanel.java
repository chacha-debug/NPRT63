package com.communityservice.ui;

import com.communityservice.utils.InputValidator;
import com.communityservice.utils.SkillCatalog;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class NgoSetupPanel extends BasePanel {
    private final List<JCheckBox> focusChecks=new ArrayList<>();
    private final List<String> selectedSkills=new ArrayList<>();
    private JTextField contactPhoneField,operatingRegionField,customSkillField;
    private JComboBox<String> skillCombo,orgScaleCombo; private JTextArea missionArea; private JPanel skillTags;
    private JButton saveButton,backButton; private JLabel statusLabel;

    public NgoSetupPanel(){super("NGO Profile","Tell us about your organisation, the communities you serve, and the support your projects need.");addStepSidebar(3,"Account","Role & Details","NGO Profile");buildForm();}
    private void buildForm(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(4,0,4,0);g.fill=GridBagConstraints.HORIZONTAL;g.gridx=0;g.weightx=1;int r=0;
        JLabel t=new JLabel("Organisation Setup");t.setFont(AppTheme.FONT_TITLE);t.setForeground(AppTheme.TEXT_DARK);rightContentPanel.add(t,row(g,r++));
        JLabel sub=new JLabel("Step 3 of 3: Organisation profile & project needs");sub.setFont(AppTheme.FONT_BODY);sub.setForeground(AppTheme.TEXT_MUTED);rightContentPanel.add(sub,row(g,r++));
        rightContentPanel.add(requiredLabel("Primary Focus Areas / Sectors"),row(g,r++));
        JPanel grid=new JPanel(new GridLayout(0,4,8,3));grid.setOpaque(false);for(String x:SkillCatalog.FOCUS_AREAS){JCheckBox c=new JCheckBox(x);c.setFont(AppTheme.FONT_SMALL);c.setOpaque(false);focusChecks.add(c);grid.add(c);}rightContentPanel.add(grid,row(g,r++));
        rightContentPanel.add(requiredLabel("Skills / Certifications your projects commonly require"),row(g,r++));
        skillCombo=new JComboBox<>(); skillCombo.addItem("Select a common skill..."); for(String x:SkillCatalog.SKILLS)skillCombo.addItem(x);AppTheme.styleComboBox(skillCombo);rightContentPanel.add(skillCombo,row(g,r++));
        JPanel sr=new JPanel(new BorderLayout(8,0));sr.setOpaque(false);customSkillField=field("Or type another skill / certification");JButton add=AppTheme.createPrimaryButton("+ Add Skill");add.setPreferredSize(new Dimension(120,42));add.addActionListener(e->addSkill());sr.add(customSkillField);sr.add(add,BorderLayout.EAST);rightContentPanel.add(sr,row(g,r++));
        skillCombo.addActionListener(e->{if(skillCombo.getSelectedIndex()>0){addSkill((String)skillCombo.getSelectedItem());skillCombo.setSelectedIndex(0);}});
        skillTags=new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));skillTags.setOpaque(false);rightContentPanel.add(skillTags,row(g,r++));
        rightContentPanel.add(requiredLabel("Official Contact Phone"),row(g,r++));contactPhoneField=field("e.g. 082 123 4567 or +27 82 123 4567");rightContentPanel.add(contactPhoneField,row(g,r++));
        rightContentPanel.add(requiredLabel("Primary Operating City / Province"),row(g,r++));operatingRegionField=field("e.g. Johannesburg, Gauteng");rightContentPanel.add(operatingRegionField,row(g,r++));
        rightContentPanel.add(requiredLabel("Organisation Size / Volunteer Capacity"),row(g,r++));orgScaleCombo=new JComboBox<>(new String[]{"Select organisation size...","Community-based (1-50 volunteers)","Regional NPO (50-250 volunteers)","National NGO (250+ volunteers)"});AppTheme.styleComboBox(orgScaleCombo);rightContentPanel.add(orgScaleCombo,row(g,r++));
        rightContentPanel.add(requiredLabel("Mission Statement / Overview"),row(g,r++));missionArea=new JTextArea(8,20); missionArea.setLineWrap(true); missionArea.setWrapStyleWord(true); AppTheme.styleTextArea(missionArea); JScrollPane missionScroll=new JScrollPane(missionArea); missionScroll.setPreferredSize(new Dimension(100,150)); missionScroll.setMinimumSize(new Dimension(100,130)); rightContentPanel.add(missionScroll,row(g,r++));
        statusLabel=new JLabel(" ");statusLabel.setFont(AppTheme.FONT_SMALL);rightContentPanel.add(statusLabel,row(g,r++));
        saveButton=AppTheme.createPrimaryButton("Save Organisation Profile & Continue"); saveButton.setEnabled(false); rightContentPanel.add(saveButton,row(g,r++));bindEnterToButton(saveButton); installValidation(); backButton=AppTheme.createBackButton("← Back to Account");rightContentPanel.add(backButton,row(g,r++));}

    private void installValidation(){
        javax.swing.event.DocumentListener d=new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}public void removeUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}public void changedUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}};
        contactPhoneField.getDocument().addDocumentListener(d); operatingRegionField.getDocument().addDocumentListener(d); missionArea.getDocument().addDocumentListener(d);
        orgScaleCombo.addActionListener(e->updateSaveState()); for(JCheckBox c:focusChecks)c.addActionListener(e->updateSaveState());
    }
    private void updateSaveState(){
        boolean valid=InputValidator.isValidSouthAfricanPhone(getContactPhone())&&!getOperatingRegion().isEmpty()&&orgScaleCombo.getSelectedIndex()>0&&!getMissionStatement().isEmpty()&&!getFocusAreas().isEmpty()&&!selectedSkills.isEmpty();
        saveButton.setEnabled(valid);
        if(statusLabel!=null){
            if(getFocusAreas().isEmpty()) show("Select at least one primary focus area / sector.",true);
            else if(selectedSkills.isEmpty()) show("Select or add at least one required skill / certification.",true);
            else if(getContactPhone().isEmpty()) show("Official contact phone is required.",true);
            else if(!InputValidator.isValidSouthAfricanPhone(getContactPhone())) show("Enter a valid South African number, e.g. 082 123 4567 or +27 82 123 4567.",true);
            else if(getOperatingRegion().isEmpty()) show("Primary operating city / province is required.",true);
            else if(orgScaleCombo.getSelectedIndex()==0) show("Select the organisation size / volunteer capacity.",true);
            else if(getMissionStatement().isEmpty()) show("Mission statement / overview is required.",true);
            else show("All required fields are complete. You can continue.",false);
        }
    }
    public void setBusy(boolean busy){
        for(JCheckBox c:focusChecks)c.setEnabled(!busy); skillCombo.setEnabled(!busy); customSkillField.setEnabled(!busy); contactPhoneField.setEnabled(!busy); operatingRegionField.setEnabled(!busy); orgScaleCombo.setEnabled(!busy); missionArea.setEnabled(!busy); backButton.setEnabled(!busy);
        saveButton.setEnabled(!busy && InputValidator.isValidSouthAfricanPhone(getContactPhone())&&!getOperatingRegion().isEmpty()&&orgScaleCombo.getSelectedIndex()>0&&!getMissionStatement().isEmpty()&&!getFocusAreas().isEmpty()&&!selectedSkills.isEmpty());
        saveButton.setText(busy?"Saving organisation...":"Save Organisation Profile & Continue");
        if(busy) show("Saving your organisation profile and preparing email verification...",false);
    }
    private JTextField field(String prompt){JTextField f=new JTextField();AppTheme.styleTextField(f);new TextPrompt(prompt,f);return f;}
    private void addSkill(){addSkill(customSkillField.getText().trim());customSkillField.setText("");}
    private void addSkill(String s){if(s==null)return;final String value=s.trim();if(value.isEmpty()||selectedSkills.stream().anyMatch(x->x.equalsIgnoreCase(value)))return;selectedSkills.add(value);JLabel tag=new JLabel(value+"  ×");tag.setFont(AppTheme.FONT_SMALL);tag.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY),BorderFactory.createEmptyBorder(3,6,3,6)));tag.setCursor(new Cursor(Cursor.HAND_CURSOR));tag.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){selectedSkills.remove(value);skillTags.remove(tag);skillTags.revalidate();skillTags.repaint();updateSaveState();}});skillTags.add(tag);skillTags.revalidate();skillTags.repaint();updateSaveState();}
    private void show(String s,boolean err){statusLabel.setText("<html>"+s+"</html>");statusLabel.setForeground(err?AppTheme.DANGER:AppTheme.TEXT_MUTED);}
    private GridBagConstraints row(GridBagConstraints g,int r){g.gridy=r;return g;}
    public void setSaveListener(ActionListener l){for(ActionListener a:saveButton.getActionListeners())saveButton.removeActionListener(a);saveButton.addActionListener(e->{if(getFocusAreas().isEmpty()){show("Select at least one primary focus area.",true);return;}if(selectedSkills.isEmpty()){show("Select or add at least one skill / certification your projects require.",true);return;}if(!InputValidator.isValidSouthAfricanPhone(getContactPhone())){show("Enter a valid South African contact number.",true);return;}if(getOperatingRegion().isEmpty()){show("Primary operating city/province is required.",true);return;}if(orgScaleCombo.getSelectedIndex()==0){show("Select the organisation size.",true);return;}if(getMissionStatement().isEmpty()){show("Mission statement is required.",true);return;}show(" ",false);l.actionPerformed(e);});}
    public void setBackListener(ActionListener l){for(ActionListener a:backButton.getActionListeners())backButton.removeActionListener(a);backButton.addActionListener(l);}
    public String getContactPhone(){return contactPhoneField.getText().trim();}public String getOperatingRegion(){return operatingRegionField.getText().trim();}public String getOrgScale(){return orgScaleCombo.getSelectedIndex()>0?(String)orgScaleCombo.getSelectedItem():"";}public String getMissionStatement(){return missionArea.getText().trim();}
    public List<String> getFocusAreas(){List<String>x=new ArrayList<>();for(JCheckBox c:focusChecks)if(c.isSelected())x.add(c.getText());return x;}public List<String> getRequiredSkills(){return new ArrayList<>(selectedSkills);}
    public void clearFields(){contactPhoneField.setText("");operatingRegionField.setText("");orgScaleCombo.setSelectedIndex(0);missionArea.setText("");for(JCheckBox c:focusChecks)c.setSelected(false);selectedSkills.clear();skillTags.removeAll();statusLabel.setText(" ");updateSaveState();}
}
