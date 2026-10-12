package com.communityservice.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * The single shared sidebar-navigation button style used by every dashboard
 * (NGO, Volunteer, Administrator) so their sidebars look and feel identical:
 * a rounded pill, centered white text, a subtle hover highlight, and a
 * distinct "active" background for whichever page is currently showing.
 */
public class RoundedNavButton extends JButton {

    private final int arc;
    private Color idleColor;
    private Color hoverColor;
    private boolean active = false;

    public RoundedNavButton(String text) {
        this(text, 12);
    }

    public RoundedNavButton(String text, int arc) {
        super(text);
        this.arc = arc;
        this.idleColor = UIColors.PRIMARY;
        this.hoverColor = UIColors.PRIMARY_LIGHT;

        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 15));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setAlignmentX(CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        setPreferredSize(new Dimension(179, 54));
        setBackground(idleColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }

    /** Marks this as the button for the currently-showing page (darker background, stays highlighted). */
    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill;
        if (active) {
            fill = UIColors.PRIMARY_DARK;
        } else if (getModel().isRollover()) {
            fill = hoverColor;
        } else {
            fill = idleColor;
        }

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.dispose();

        super.paintComponent(g);
    }
}
