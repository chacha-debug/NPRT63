package com.communityservice.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JLabel;

/**
 * A simple circular "person" avatar — a filled circle with a head-and-
 * shoulders silhouette drawn inside it, entirely via Java2D. Used
 * consistently as the account/profile indicator across every screen
 * (login, NGO, volunteer, and admin dashboards) instead of an image file,
 * so it always renders correctly regardless of how the project is packaged.
 */
public class RoundPersonIcon extends JLabel {

    private final int size;
    private final Color circleColor;
    private final Color personColor;

    public RoundPersonIcon(int size, Color circleColor, Color personColor) {
        this.size = size;
        this.circleColor = circleColor;
        this.personColor = personColor;
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        paint(g2, size, circleColor, personColor);
        g2.dispose();
    }

    private static void paint(Graphics2D g2, int size, Color circleColor, Color personColor) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Outer circle (the "round" part)
        g2.setColor(circleColor);
        g2.fillOval(0, 0, size, size);

        // Clip everything else to the circle so the silhouette never pokes outside it
        Shape oldClip = g2.getClip();
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));

        g2.setColor(personColor);
        // Head
        float headSize = size * 0.36f;
        g2.fill(new Ellipse2D.Float((size - headSize) / 2f, size * 0.16f, headSize, headSize));
        // Shoulders/body
        float bodyWidth = size * 0.72f;
        float bodyHeight = size * 0.5f;
        g2.fill(new Ellipse2D.Float((size - bodyWidth) / 2f, size * 0.56f, bodyWidth, bodyHeight));

        g2.setClip(oldClip);
    }

    /** Renders this icon to an Image, e.g. for use as a JFrame window/taskbar icon. */
    public static java.awt.Image toImage(int size, Color circleColor, Color personColor) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        paint(g2, size, circleColor, personColor);
        g2.dispose();
        return img;
    }
}
