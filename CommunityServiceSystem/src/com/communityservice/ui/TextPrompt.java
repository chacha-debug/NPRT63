package com.communityservice.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

class TextPrompt extends JLabel implements FocusListener, DocumentListener {
    private final JTextComponent component;

    public TextPrompt(String text, JTextComponent component) {
        this.component = component;
        setText(text);
        setFont(component.getFont().deriveFont(Font.ITALIC));
        setForeground(new Color(160, 160, 160));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        setHorizontalAlignment(JLabel.LEADING);

        component.setLayout(new BorderLayout());
        component.add(this);
        component.addFocusListener(this);
        component.getDocument().addDocumentListener(this);

        checkForText();
    }

    private void checkForText() {
        if (component.getText().length() > 0) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    @Override
    public void focusGained(FocusEvent e) { checkForText(); }

    @Override
    public void focusLost(FocusEvent e) { checkForText(); }

    @Override
    public void insertUpdate(DocumentEvent e) { checkForText(); }

    @Override
    public void removeUpdate(DocumentEvent e) { checkForText(); }

    @Override
    public void changedUpdate(DocumentEvent e) { checkForText(); }
}

