package com.communityservice.ui;

import com.communityservice.utils.InputValidator;
import java.awt.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Step 2: role selection plus the identity details required by that role. */
public class RoleSelectionPanel extends BasePanel {
    private JRadioButton volunteerRadio, ngoRadio;
    private JPanel dynamicDetailsPanel;
    private JComboBox<String> raceCombo, dayCombo, monthCombo, yearCombo, genderCombo;
    private JTextField orgNameField, regNumberField;
    private JLabel validationLabel;
    private JButton continueButton, backButton;

    public RoleSelectionPanel() {
        super("Choose Your Role", "Tell us how you want to participate in the community.");
        addStepSidebar(2, "Account", "Role & Details", "Preferences", "Profile Setup");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,0,4,0); gbc.fill=GridBagConstraints.HORIZONTAL; gbc.gridx=0; gbc.weightx=1;
        int row=0;
        JLabel title=new JLabel("Select Your Role"); title.setFont(AppTheme.FONT_TITLE); title.setForeground(AppTheme.TEXT_DARK);
        rightContentPanel.add(title,setRow(gbc,row++));
        JLabel sub=new JLabel("Step 2 of 4: Role and required details"); sub.setFont(AppTheme.FONT_BODY); sub.setForeground(AppTheme.TEXT_MUTED);
        gbc.insets=new Insets(0,0,12,0); rightContentPanel.add(sub,setRow(gbc,row++)); gbc.insets=new Insets(4,0,4,0);
        rightContentPanel.add(requiredLabel("Choose Portal Role"),setRow(gbc,row++));
        JPanel roles=new JPanel(new GridLayout(1,2,12,0)); roles.setOpaque(false); ButtonGroup group=new ButtonGroup();
        volunteerRadio=new JRadioButton("Volunteer"); ngoRadio=new JRadioButton("NGO / Org"); group.add(volunteerRadio); group.add(ngoRadio); volunteerRadio.setSelected(true);
        ActionListener change=e->updateDynamicFields(); volunteerRadio.addActionListener(change); ngoRadio.addActionListener(change);
        roles.add(createRoleCard(volunteerRadio,"👤","Participate & Log Hours")); roles.add(createRoleCard(ngoRadio,"🏛️","Host & Manage Projects"));
        rightContentPanel.add(roles,setRow(gbc,row++));
        dynamicDetailsPanel=new JPanel(new GridLayout(0,1,0,5)); dynamicDetailsPanel.setOpaque(false);
        rightContentPanel.add(dynamicDetailsPanel,setRow(gbc,row++));
        validationLabel=new JLabel(" "); validationLabel.setFont(AppTheme.FONT_SMALL); validationLabel.setForeground(AppTheme.DANGER);
        rightContentPanel.add(validationLabel,setRow(gbc,row++));
        continueButton=AppTheme.createPrimaryButton("Continue to Step 3"); gbc.insets=new Insets(10,0,4,0); rightContentPanel.add(continueButton,setRow(gbc,row++)); bindEnterToButton(continueButton);
        backButton=AppTheme.createBackButton("← Back to Account"); gbc.insets=new Insets(0,0,0,0); rightContentPanel.add(backButton,setRow(gbc,row++));
        updateDynamicFields();
    }

    private void updateDynamicFields() {
        if(dynamicDetailsPanel==null)return;
        dynamicDetailsPanel.removeAll(); validationLabel.setText(" ");
        if(volunteerRadio.isSelected()) buildVolunteerFields(); else buildNgoFields();
        updateContinueEnabled(); dynamicDetailsPanel.revalidate(); dynamicDetailsPanel.repaint();
    }

    private void buildVolunteerFields() {
        dynamicDetailsPanel.add(requiredLabel("Race"));
        raceCombo=new JComboBox<>(new String[]{"Select race","African","Coloured","Indian / Asian","White","Other","Prefer to self-describe"}); styleComboBox(raceCombo); raceCombo.addActionListener(e->updateContinueEnabled()); dynamicDetailsPanel.add(raceCombo);
        dynamicDetailsPanel.add(requiredLabel("Date of Birth"));
        JPanel dob=new JPanel(new GridLayout(1,3,6,0)); dob.setOpaque(false);
        dayCombo=new JComboBox<>(withPrompt("Day",generateDays())); monthCombo=new JComboBox<>(new String[]{"Month","01","02","03","04","05","06","07","08","09","10","11","12"}); yearCombo=new JComboBox<>(withPrompt("Year",generateYears()));
        for(JComboBox<String> b:new JComboBox[]{dayCombo,monthCombo,yearCombo}){styleComboBox(b); b.addActionListener(e->updateContinueEnabled()); dob.add(b);} dynamicDetailsPanel.add(dob);
        dynamicDetailsPanel.add(requiredLabel("Gender"));
        genderCombo=new JComboBox<>(new String[]{"Select gender","Female","Male","Non-binary","Other","Prefer to self-describe"}); styleComboBox(genderCombo); genderCombo.addActionListener(e->updateContinueEnabled()); dynamicDetailsPanel.add(genderCombo);
    }

    private void buildNgoFields() {
        dynamicDetailsPanel.add(requiredLabel("Organisation Name")); orgNameField=new JTextField(); AppTheme.styleTextField(orgNameField); new TextPrompt("Registered or public organisation name",orgNameField); dynamicDetailsPanel.add(orgNameField);
        dynamicDetailsPanel.add(requiredLabel("NPO / NPC / PBO Registration Number")); regNumberField=new JTextField(); AppTheme.styleTextField(regNumberField); new TextPrompt("e.g. 123-456 NPO or 2024/123456/08",regNumberField); dynamicDetailsPanel.add(regNumberField);
        JLabel hint=new JLabel(InputValidator.nonProfitRegistrationHint()); hint.setFont(AppTheme.FONT_SMALL); hint.setForeground(AppTheme.TEXT_MUTED); dynamicDetailsPanel.add(hint);
        DocumentListener dl=new DocumentListener(){public void insertUpdate(DocumentEvent e){updateContinueEnabled();} public void removeUpdate(DocumentEvent e){updateContinueEnabled();} public void changedUpdate(DocumentEvent e){updateContinueEnabled();}};
        orgNameField.getDocument().addDocumentListener(dl); regNumberField.getDocument().addDocumentListener(dl);
    }

    private void updateContinueEnabled(){ if(continueButton==null)return; boolean ok;
        if(volunteerRadio.isSelected()) ok=raceCombo!=null&&raceCombo.getSelectedIndex()>0&&genderCombo!=null&&genderCombo.getSelectedIndex()>0&&getDateOfBirth()!=null;
        else ok=orgNameField!=null&&!orgNameField.getText().trim().isEmpty()&&regNumberField!=null&&InputValidator.isValidSouthAfricanNonProfitRegistration(regNumberField.getText().trim());
        continueButton.setEnabled(ok);
    }
    public LocalDate getDateOfBirth(){try{if(dayCombo==null||dayCombo.getSelectedIndex()==0||monthCombo.getSelectedIndex()==0||yearCombo.getSelectedIndex()==0)return null; LocalDate d=LocalDate.of(Integer.parseInt((String)yearCombo.getSelectedItem()),Integer.parseInt((String)monthCombo.getSelectedItem()),Integer.parseInt((String)dayCombo.getSelectedItem())); return d.isBefore(LocalDate.now())?d:null;}catch(DateTimeException|NumberFormatException e){return null;}}
    private String[] withPrompt(String prompt,String[] values){String[] r=new String[values.length+1];r[0]=prompt;System.arraycopy(values,0,r,1,values.length);return r;}
    private JPanel createRoleCard(JRadioButton radio,String icon,String subtitle){JPanel card=new JPanel(new BorderLayout(5,5));card.setBackground(Color.WHITE);Border b=BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,224,230),1,true),BorderFactory.createEmptyBorder(10,8,10,8));card.setBorder(b);JLabel il=new JLabel(icon,SwingConstants.CENTER);il.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));JPanel c=new JPanel(new FlowLayout(FlowLayout.CENTER,4,0));c.setOpaque(false);radio.setFont(AppTheme.FONT_BODY.deriveFont(Font.BOLD));radio.setOpaque(false);c.add(radio);JLabel sl=new JLabel(subtitle,SwingConstants.CENTER);sl.setFont(AppTheme.FONT_SMALL);sl.setForeground(AppTheme.TEXT_MUTED);card.add(il,BorderLayout.NORTH);card.add(c,BorderLayout.CENTER);card.add(sl,BorderLayout.SOUTH);MouseAdapter m=new MouseAdapter(){public void mouseClicked(MouseEvent e){radio.setSelected(true);updateDynamicFields();}};card.addMouseListener(m);il.addMouseListener(m);sl.addMouseListener(m);return card;}
    private void styleComboBox(JComboBox<String>b){b.setFont(AppTheme.FONT_BODY);b.setBackground(Color.WHITE);b.setPreferredSize(new Dimension(b.getPreferredSize().width,36));}
    private String[] generateDays(){String[]d=new String[31];for(int i=1;i<=31;i++)d[i-1]=String.format("%02d",i);return d;}
    private String[] generateYears(){int y=Calendar.getInstance().get(Calendar.YEAR);String[]a=new String[100];for(int i=0;i<100;i++)a[i]=String.valueOf(y-i);return a;}
    private GridBagConstraints setRow(GridBagConstraints g,int r){g.gridy=r;return g;}
    public String getSelectedRole(){return ngoRadio.isSelected()?"NGO":"Volunteer";}
    public String getSelectedRace(){return raceCombo!=null&&raceCombo.getSelectedIndex()>0?(String)raceCombo.getSelectedItem():"";}
    public String getGender(){return genderCombo!=null&&genderCombo.getSelectedIndex()>0?(String)genderCombo.getSelectedItem():"";}
    public String getOrgName(){return orgNameField==null?"":orgNameField.getText().trim();}
    public String getRegNumber(){return regNumberField==null?"":regNumberField.getText().trim();}
    public String getStaffId(){return "";} public String getDepartment(){return "";}
    public void setContinueListener(ActionListener l){for(ActionListener a:continueButton.getActionListeners())continueButton.removeActionListener(a);continueButton.addActionListener(e->{if(l!=null)l.actionPerformed(e);});}
    public void setBackListener(ActionListener l){for(ActionListener a:backButton.getActionListeners())backButton.removeActionListener(a);backButton.addActionListener(l);}
    public void setSelectedRole(String role){if("NGO".equalsIgnoreCase(role))ngoRadio.setSelected(true);else volunteerRadio.setSelected(true);updateDynamicFields();}
    public void clearFields(){volunteerRadio.setSelected(true);updateDynamicFields();}
}
