/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.communityservice.ui;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class LoadingIndicator extends JLabel {

    private Timer timer;
    private String baseText;
    private int dotCount;

    public LoadingIndicator() {
        setFont(AppTheme.FONT_SMALL);
        setForeground(AppTheme.TEXT_MUTED);
        setText(" ");
    }

    /**
     * Starts an animated "text..." loading state.
     */
    public void start(String text) {
        stopTimer();
        baseText = text;
        dotCount = 0;
        setForeground(AppTheme.TEXT_MUTED);
        setText(baseText);
        timer = new Timer(400, e -> {
            dotCount = (dotCount + 1) % 4;
            StringBuilder sb = new StringBuilder(baseText);
            for (int i = 0; i < dotCount; i++) {
                sb.append('.');
            }
            setText(sb.toString());
        });
        timer.start();
    }

    public void showError(String message) {
        stopTimer();
        setForeground(AppTheme.DANGER);
        setText(message);
    }

    public void showSuccess(String message) {
        stopTimer();
        // Use a success green color (Hex #16A34A)
        setForeground(new java.awt.Color(22, 163, 74));
        setText(message);
    }

    /**
     * Clears the message and stops any animation, keeping layout space
     * reserved.
     */
    public void clear() {
        stopTimer();
        setText(" ");
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

}
