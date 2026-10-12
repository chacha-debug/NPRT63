package com.communityservice.ui;

import com.communityservice.utils.InputValidator;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ProfileSetupPanel extends BasePanel {
    private JTextField phoneField, locationField;
    private JComboBox<String> availabilityCombo;
    private JTextArea bioArea;
    private JButton saveButton, backButton;
    private JLabel statusLabel;

    public ProfileSetupPanel() {
        super("Complete Profile", "Finish the required profile details before your account is verified.");
        addStepSidebar(4, "Account", "Role & Details", "Preferences", "Profile Setup");
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc=new GridBagConstraints(); gbc.insets=new Insets(5,0,5,0); gbc.fill=GridBagConstraints.HORIZONTAL; gbc.gridx=0; gbc.weightx=1;
        int row=0;
        JLabel title=new JLabel("Profile Setup"); title.setFont(AppTheme.FONT_TITLE); title.setForeground(AppTheme.TEXT_DARK); rightContentPanel.add(title,setRow(gbc,row++));
        JLabel sub=new JLabel("Step 4 of 4: Complete your volunteer profile"); sub.setFont(AppTheme.FONT_BODY); sub.setForeground(AppTheme.TEXT_MUTED); gbc.insets=new Insets(0,0,14,0); rightContentPanel.add(sub,setRow(gbc,row++)); gbc.insets=new Insets(5,0,5,0);

        rightContentPanel.add(requiredLabel("Phone Number"),setRow(gbc,row++));
        phoneField=new JTextField(); AppTheme.styleTextField(phoneField); new TextPrompt("+27 82 123 4567",phoneField); rightContentPanel.add(phoneField,setRow(gbc,row++));

        rightContentPanel.add(requiredLabel("Location / City"),setRow(gbc,row++));
        locationField=new JTextField(); AppTheme.styleTextField(locationField); new TextPrompt("e.g. Johannesburg, Gauteng",locationField); rightContentPanel.add(locationField,setRow(gbc,row++));

        rightContentPanel.add(requiredLabel("Weekly Availability"),setRow(gbc,row++));
        availabilityCombo=new JComboBox<>(new String[]{"Select availability...","Weekends Only","Weekdays Only","Flexible / Anytime","Evenings Only"}); AppTheme.styleComboBox(availabilityCombo); availabilityCombo.setSelectedIndex(0); rightContentPanel.add(availabilityCombo,setRow(gbc,row++));

        JLabel bioLabel=new JLabel("Short Bio / Interests (Optional)"); bioLabel.setFont(AppTheme.FONT_BODY); rightContentPanel.add(bioLabel,setRow(gbc,row++));
        bioArea=new JTextArea(3,20); AppTheme.styleTextArea(bioArea); rightContentPanel.add(new JScrollPane(bioArea),setRow(gbc,row++));
        statusLabel=new JLabel(" "); statusLabel.setFont(AppTheme.FONT_SMALL); statusLabel.setForeground(AppTheme.DANGER); rightContentPanel.add(statusLabel,setRow(gbc,row++));

        saveButton=AppTheme.createPrimaryButton("Save Profile & Continue"); saveButton.setEnabled(false); installValidation(); gbc.insets=new Insets(12,0,4,0); rightContentPanel.add(saveButton,setRow(gbc,row++)); bindEnterToButton(saveButton);
        backButton=AppTheme.createBackButton("← Back to Preferences"); rightContentPanel.add(backButton,setRow(gbc,row++));
    }


    private void installValidation(){javax.swing.event.DocumentListener d=new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}public void removeUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}public void changedUpdate(javax.swing.event.DocumentEvent e){updateSaveState();}};phoneField.getDocument().addDocumentListener(d);locationField.getDocument().addDocumentListener(d);availabilityCombo.addActionListener(e->updateSaveState());}
    private void updateSaveState(){
        boolean phoneOk=InputValidator.isValidSouthAfricanPhone(getPhoneNumber());
        boolean locationOk=!getLocationInput().isEmpty();
        boolean availabilityOk=availabilityCombo.getSelectedIndex()>0;
        if(saveButton!=null) saveButton.setEnabled(phoneOk&&locationOk&&availabilityOk);
        if(statusLabel!=null){
            if(getPhoneNumber().isEmpty()) hint("Phone number is required. Use 082 123 4567 or +27 82 123 4567.");
            else if(!phoneOk) hint("Enter a valid South African phone number, e.g. 082 123 4567 or +27 82 123 4567.");
            else if(!locationOk) hint("Location / City is required.");
            else if(!availabilityOk) hint("Select your weekly availability.");
            else ready();
        }
    }
    private void hint(String text){statusLabel.setText("<html>"+text+"</html>");statusLabel.setForeground(AppTheme.DANGER);}
    private void ready(){statusLabel.setText("All required fields are complete. You can continue.");statusLabel.setForeground(AppTheme.SUCCESS);}
    public void setBusy(boolean busy){
        phoneField.setEnabled(!busy); locationField.setEnabled(!busy); availabilityCombo.setEnabled(!busy); bioArea.setEnabled(!busy); backButton.setEnabled(!busy);
        saveButton.setEnabled(!busy && InputValidator.isValidSouthAfricanPhone(getPhoneNumber())&&!getLocationInput().isEmpty()&&availabilityCombo.getSelectedIndex()>0);
        saveButton.setText(busy?"Saving profile...":"Save Profile & Continue");
        if(busy){statusLabel.setText("Saving your profile and preparing email verification...");statusLabel.setForeground(AppTheme.TEXT_MUTED);}
    }
    private GridBagConstraints setRow(GridBagConstraints g,int row){g.gridy=row;return g;}
    public String getPhoneNumber(){return phoneField.getText().trim();} public String getLocationInput(){return locationField.getText().trim();} public String getAvailability(){return availabilityCombo.getSelectedIndex()>0?(String)availabilityCombo.getSelectedItem():"";} public String getBio(){return bioArea.getText().trim();}
    public void setSaveProfileListener(ActionListener l){ for(ActionListener a:saveButton.getActionListeners())saveButton.removeActionListener(a); saveButton.addActionListener(e->{ if(!InputValidator.isValidSouthAfricanPhone(getPhoneNumber())){statusLabel.setText("Enter a valid South African phone number, e.g. 082 123 4567 or +27 82 123 4567.");statusLabel.setForeground(AppTheme.DANGER);phoneField.requestFocusInWindow();return;} if(getLocationInput().isEmpty()){statusLabel.setText("Location is required.");statusLabel.setForeground(AppTheme.DANGER);locationField.requestFocusInWindow();return;} if(availabilityCombo.getSelectedIndex()==0){statusLabel.setText("Please select your weekly availability.");statusLabel.setForeground(AppTheme.DANGER);availabilityCombo.requestFocusInWindow();return;} statusLabel.setText(" ");l.actionPerformed(e);}); }
    public void setSkipListener(ActionListener l) { /* Profile completion is intentionally mandatory. */ }
    public void setBackListener(ActionListener l){for(ActionListener a:backButton.getActionListeners())backButton.removeActionListener(a);backButton.addActionListener(l);}
    public void clearFields(){phoneField.setText("");locationField.setText("");availabilityCombo.setSelectedIndex(0);bioArea.setText("");statusLabel.setText(" ");updateSaveState();}
}
