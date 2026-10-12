/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import java.awt.Color;

/**
 * Central colour palette for the PortalVolunteer System.
 *
 * Keeping all colours in one class makes it much easier
 * to maintain a consistent visual design throughout
 * the application.
 */
public final class UIColors {

    private UIColors() {
        // Prevent creating objects from this utility class.
    }

    // =========================================================
    // BRAND / PRIMARY COLORS
    // =========================================================

    /**
     * Main application blue.
     */
    public static final Color PRIMARY =
            new Color(36, 68, 148);

    /**
     * Lighter version of the primary blue.
     */
    public static final Color PRIMARY_LIGHT =
            new Color(103, 126, 190);

    /**
     * Darker version of the primary blue.
     */
    public static final Color PRIMARY_DARK =
            new Color(26, 50, 110);


    // =========================================================
    // SIDEBAR
    // =========================================================

    /**
     * Main sidebar colour.
     */
    public static final Color SIDEBAR =
            PRIMARY;

    /**
     * Dark sidebar colour.
     */
    public static final Color SIDEBAR_DARK =
            PRIMARY_DARK;

    /**
     * Sidebar hover / selected item colour.
     */
    public static final Color SIDEBAR_HOVER =
            new Color(64, 91, 153);

    /**
     * Sidebar selected item background.
     */
    public static final Color SIDEBAR_SELECTED =
            new Color(70, 98, 160);


    // =========================================================
    // APPLICATION BACKGROUND
    // =========================================================

    /**
     * Main background used behind cards and panels.
     *
     * This prevents screens from looking completely white.
     */
    public static final Color BACKGROUND =
            new Color(248, 249, 251);

    /**
     * Slightly darker background for sections.
     */
    public static final Color BACKGROUND_DARK =
            new Color(235, 238, 244);


    // =========================================================
    // CARDS
    // =========================================================

    /**
     * Standard card background.
     */
    public static final Color CARD =
            Color.WHITE;

    /**
     * Slightly darker card background.
     */
    public static final Color CARD_ALT =
            new Color(249, 250, 252);

    /**
     * Card and component borders.
     */
    public static final Color BORDER =
            new Color(205, 211, 220);

    /**
     * Stronger border used for selected/active components.
     */
    public static final Color BORDER_DARK =
            new Color(205, 210, 220);


    // =========================================================
    // DASHBOARD STATISTICS
    // =========================================================

    /**
     * Blue/teal statistic colour.
     */
    public static final Color STAT_BLUE =
            new Color(64, 190, 181);

    /**
     * Green statistic colour.
     */
    public static final Color STAT_GREEN =
            new Color(64, 190, 181);

    /**
     * Orange statistic colour.
     */
    public static final Color STAT_ORANGE =
            new Color(241, 148, 40);

    /**
     * Purple statistic colour.
     */
    public static final Color STAT_PURPLE =
            new Color(142, 68, 173);


    // =========================================================
    // STATUS COLORS
    // =========================================================

    /**
     * Success / approved / available.
     */
    public static final Color GREEN =
            new Color(64, 190, 181);

    /**
     * Information / pending / general information.
     */
    public static final Color BLUE =
            new Color(52, 152, 219);

    /**
     * Warning / pending action.
     */
    public static final Color ORANGE =
            new Color(241, 148, 40);

    /**
     * Error / rejected / unavailable.
     */
    public static final Color RED =
            new Color(210, 80, 80);

    /**
     * Special / leaderboard / achievement.
     */
    public static final Color PURPLE =
            new Color(142, 68, 173);


    // =========================================================
    // SOFT STATUS BACKGROUNDS
    // =========================================================

    /**
     * Soft green background for success messages.
     */
    public static final Color GREEN_LIGHT =
            new Color(229, 247, 242);

    /**
     * Soft blue background for information.
     */
    public static final Color BLUE_LIGHT =
            new Color(232, 243, 252);

    /**
     * Soft orange background for warnings.
     */
    public static final Color ORANGE_LIGHT =
            new Color(253, 241, 225);

    /**
     * Soft red background for errors.
     */
    public static final Color RED_LIGHT =
            new Color(250, 232, 230);

    /**
     * Soft purple background for special sections.
     */
    public static final Color PURPLE_LIGHT =
            new Color(241, 232, 247);


    // =========================================================
    // TEXT
    // =========================================================

    /**
     * Main dark text.
     */
    public static final Color TEXT_DARK =
            new Color(35, 35, 35);

    /**
     * Secondary text.
     */
    public static final Color TEXT_GREY =
            new Color(110, 115, 125);

    /**
     * Light / muted text.
     */
    public static final Color TEXT_LIGHT =
            new Color(180, 185, 195);

    /**
     * White text.
     */
    public static final Color TEXT_WHITE =
            Color.WHITE;


    // =========================================================
    // SPECIAL UI COLORS
    // =========================================================

    /**
     * Highlighted area.
     */
    public static final Color HIGHLIGHT =
            new Color(232, 241, 255);

    /**
     * Gold used for achievements / leaderboard.
     */
    public static final Color GOLD =
            new Color(220, 170, 35);

    /**
     * Standard white.
     */
    public static final Color WHITE =
            Color.WHITE;

    /**
     * Disabled component background.
     */
    public static final Color DISABLED_BACKGROUND =
            new Color(235, 237, 241);

    /**
     * Disabled component text.
     */
    public static final Color DISABLED_TEXT =
            new Color(150, 154, 162);
}