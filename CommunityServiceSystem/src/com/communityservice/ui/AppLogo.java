package com.communityservice.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Reusable "Community Service+" brand mark, used consistently across the
 * login/auth screens, the NGO dashboard, the volunteer dashboard, and the
 * About page — one consistent identity instead of ad-hoc icons per screen.
 *
 * Renders the app's actual logo image (icons/communityIco.png) scaled to
 * whatever size is requested. Falls back to a simple drawn "CS+" badge if
 * the image can't be found, so a missing resource never blanks out a
 * whole header.
 */
public class AppLogo extends JPanel {

    private static final String LOGO_RESOURCE = "/icons/communityIco.png";
    private static Image sourceImage; // loaded once, shared by every instance
    private static boolean loadAttempted = false;

    private final int badgeSize;
    private final Color badgeColor;
    private final Color textColor;

    public AppLogo(int badgeSize, Color badgeColor, Color textColor) {
        this.badgeSize = badgeSize;
        this.badgeColor = badgeColor;
        this.textColor = textColor;
        setOpaque(false);
        setPreferredSize(new Dimension(badgeSize, badgeSize));
    }

    private static synchronized Image loadSourceImage() {
        if (!loadAttempted) {
            loadAttempted = true;
            URL url = AppLogo.class.getResource(LOGO_RESOURCE);
            if (url != null) {
                sourceImage = new ImageIcon(url).getImage();
            }
        }
        return sourceImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        paintBadge(g2, badgeSize, badgeColor, textColor);
        g2.dispose();
    }

    private static void paintBadge(Graphics2D g2, int size, Color badgeColor, Color textColor) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Image logo = loadSourceImage();
        if (logo != null) {
            g2.drawImage(logo, 0, 0, size, size, null);
            return;
        }

        // Fallback: a plain drawn badge, so a missing image never leaves a blank hole.
        g2.setColor(badgeColor);
        int arc = Math.max(6, size / 3);
        g2.fillRoundRect(0, 0, size, size, arc, arc);
        g2.setColor(textColor);
        g2.setFont(new Font("Arial", Font.BOLD, (int) (size * 0.42)));
        String text = "CS+";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getAscent();
        g2.drawString(text, (size - textWidth) / 2f, (size + textHeight) / 2f - 2);
    }

    /** Badge + "Community Service+" wordmark side by side, for headers/sidebars. */
    public static JPanel withWordmark(int badgeSize, Color badgeColor, Color textColor,
                                       Color wordmarkColor, int wordmarkFontSize) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        AppLogo badge = new AppLogo(badgeSize, badgeColor, textColor);
        badge.setAlignmentY(Component.CENTER_ALIGNMENT);
        row.add(badge);
        row.add(Box.createHorizontalStrut(10));

        JLabel word = new JLabel("Community Service+");
        word.setFont(new Font("Segoe UI", Font.BOLD, wordmarkFontSize));
        word.setForeground(wordmarkColor);
        word.setAlignmentY(Component.CENTER_ALIGNMENT);
        row.add(word);

        return row;
    }

    /** Renders the badge to an Image, for use as a JFrame window/taskbar icon. */
    public static Image toImage(int size, Color badgeColor, Color textColor) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        paintBadge(g2, size, badgeColor, textColor);
        g2.dispose();
        return img;
    }
}
