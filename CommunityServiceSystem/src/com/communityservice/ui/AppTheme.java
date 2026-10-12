package com.communityservice.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppTheme {
    public static final Color NAVY_PRIMARY = new Color(36, 68, 148);
    public static final Color PRIMARY = new Color(64, 190, 181);
    public static final Color PRIMARY_HOVER = new Color(52, 165, 156);
    
    public static final Color BACKGROUND = new Color(248, 249, 251);
    public static final Color TEXT_DARK = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color BORDER_COLOR = new Color(205, 211, 220);
    public static final Color DANGER = new Color(210, 80, 80);
    public static final Color SUCCESS = new Color(22, 163, 74);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 25);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 13);

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(PRIMARY);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setForeground(TEXT_DARK);
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 42));
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_BODY);
        area.setForeground(TEXT_DARK);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    // Styled Password Toggle Button (#14B8A6 accent color)
    public static JButton createIconButton(String iconText) {
        JButton btn = new JButton(iconText);
        btn.setFont(FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(42, 38));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return new RoundedButton(text, 8);
    }

    public static JButton createBackButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(190, 42));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    public static class RoundedButton extends JButton {
        private int cornerRadius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.cornerRadius = radius;
            setFont(FONT_BODY);
            setForeground(Color.WHITE);
            setBackground(PRIMARY);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(190, 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (!isEnabled()) {
                g2.setColor(new Color(203, 213, 225));
            } else if (getModel().isPressed()) {
                g2.setColor(PRIMARY_HOVER.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(PRIMARY_HOVER);
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}