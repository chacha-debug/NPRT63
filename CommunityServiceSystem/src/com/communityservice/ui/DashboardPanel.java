/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.ui;

import com.communityservice.dao.AttendanceRecordDAO;
import com.communityservice.dao.OpportunityApplicationDAO;
import com.communityservice.dao.VolunteerPortalUserDAO;
import com.communityservice.dao.VolunteerPortalVolunteerDAO;

import com.communityservice.models.AttendanceRecord;
import com.communityservice.models.OpportunityApplication;
import com.communityservice.models.PortalUser;
import com.communityservice.models.PortalVolunteer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final int user_id;

    private final VolunteerPortalUserDAO userDAO;
    private final VolunteerPortalVolunteerDAO volunteerDAO;
    private final AttendanceRecordDAO attendanceDAO;
    private final OpportunityApplicationDAO applicationDAO;

    private JLabel welcomeLabel;
    private JLabel welcomeMessage;

    private JLabel totalHoursValue;
    private JLabel approvedHoursValue;
    private JLabel rankValue;
    private JLabel completedEventsValue;

    private JPanel applicationsContainer;
    private JPanel rankingContainer;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("hh:mm a");

    public DashboardPanel(int user_id) {

        this.user_id = user_id;

        userDAO = new VolunteerPortalUserDAO();
        volunteerDAO = new VolunteerPortalVolunteerDAO();
        attendanceDAO = new AttendanceRecordDAO();
        applicationDAO = new OpportunityApplicationDAO();

        initialize();
        loadDashboardData();
    }

    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initialize() {

        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        setBorder(
                new EmptyBorder(
                        18,
                        22,
                        18,
                        22
                )
        );

        JPanel main = new JPanel(
                new BorderLayout(
                        0,
                        16
                )
        );

        main.setBackground(UIColors.BACKGROUND);

        // Welcome section
        main.add(
                createWelcomePanel(),
                BorderLayout.NORTH
        );

        JPanel center = new JPanel(
                new BorderLayout(
                        0,
                        16
                )
        );

        center.setBackground(UIColors.BACKGROUND);

        // Statistics
        center.add(
                createStatisticsPanel(),
                BorderLayout.NORTH
        );

        // Main lower section
        JPanel bottom = new JPanel(
                new GridLayout(
                        1,
                        2,
                        16,
                        0
                )
        );

        bottom.setBackground(UIColors.BACKGROUND);

        bottom.add(
                createApplicationsPanel()
        );

        bottom.add(
                createRankingPanel()
        );

        center.add(
                bottom,
                BorderLayout.CENTER
        );

        main.add(
                center,
                BorderLayout.CENTER
        );

        add(
                main,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // WELCOME PANEL
    // =========================================================

    private JPanel createWelcomePanel() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(UIColors.CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                18,
                                14,
                                18
                        )
                )
        );

        welcomeLabel = new JLabel(
                "Welcome back"
        );

        welcomeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        welcomeLabel.setForeground(
                UIColors.TEXT_DARK
        );

        welcomeMessage = new JLabel(
                "Loading your volunteers activity..."
        );

        welcomeMessage.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        welcomeMessage.setForeground(
                UIColors.TEXT_GREY
        );

        JPanel text = new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setBackground(UIColors.CARD);

        text.add(welcomeLabel);

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(welcomeMessage);

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private JPanel createStatisticsPanel() {

        JPanel panel = new JPanel(
                new GridLayout(
                        1,
                        4,
                        14,
                        0
                )
        );

        panel.setBackground(
                UIColors.BACKGROUND
        );

        totalHoursValue = new JLabel("0.00");

        approvedHoursValue = new JLabel("0.00");

        rankValue = new JLabel("-");

        completedEventsValue = new JLabel("0");

        panel.add(
                createStatCard(
                        totalHoursValue,
                        "Total hrs",
                        "Hours recorded"
                )
        );

        panel.add(
                createStatCard(
                        approvedHoursValue,
                        "Approved Hours",
                        "Verified attendance"
                )
        );

        panel.add(
                createStatCard(
                        rankValue,
                        "Current Rank",
                        "Volunteer ranking"
                )
        );

        panel.add(
                createStatCard(
                        completedEventsValue,
                        "Completed Events",
                        "Events completed"
                )
        );

        return panel;
    }

    private JPanel createStatCard(
            JLabel value,
            String title,
            String subtitle
    ) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(
                UIColors.STAT_GREEN
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        10,
                        12,
                        10
                )
        );

        value.setForeground(Color.WHITE);

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        value.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitleLabel = new JLabel(subtitle);

        subtitleLabel.setForeground(
                Color.WHITE
        );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(
                Box.createVerticalGlue()
        );

        card.add(value);

        card.add(
                Box.createVerticalStrut(3)
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(3)
        );

        card.add(subtitleLabel);

        card.add(
                Box.createVerticalGlue()
        );

        return card;
    }

    // =========================================================
    // APPLICATIONS PANEL
    // =========================================================

    private JPanel createApplicationsPanel() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        /*
         * Blue outer panel to match the Figma design.
         */
        panel.setBackground(
                UIColors.PRIMARY
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        JLabel title = new JLabel(
                "  MY VOLUNTEER ACTIVITIES"
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                Color.WHITE
        );

        title.setOpaque(true);

        title.setBackground(
                UIColors.PRIMARY
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        8,
                        14,
                        8
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        applicationsContainer = new JPanel();

        applicationsContainer.setLayout(
                new BoxLayout(
                        applicationsContainer,
                        BoxLayout.Y_AXIS
                )
        );

        applicationsContainer.setBackground(
                UIColors.PRIMARY
        );

        applicationsContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        JScrollPane scroll = new JScrollPane(
                applicationsContainer
        );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getViewport().setBackground(
                UIColors.PRIMARY
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // RANKING PANEL
    // =========================================================

    private JPanel createRankingPanel() {

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(
                UIColors.CARD
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        UIColors.BORDER
                )
        );

        JLabel title = new JLabel(
                "  VOLUNTEER RANKINGS"
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        title.setForeground(
                UIColors.TEXT_DARK
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        8,
                        14,
                        8
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        rankingContainer = new JPanel();

        rankingContainer.setLayout(
                new BoxLayout(
                        rankingContainer,
                        BoxLayout.Y_AXIS
                )
        );

        rankingContainer.setBackground(
                UIColors.CARD
        );

        rankingContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        JScrollPane scroll = new JScrollPane(
                rankingContainer
        );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getViewport().setBackground(
                UIColors.CARD
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JButton viewFull = new JButton(
                "View Full Rankings"
        );

        styleButton(viewFull);

        viewFull.addActionListener(
                e -> {

                    VolunteerDashboardPanel dashboard =
                            (VolunteerDashboardPanel) SwingUtilities
                                    .getAncestorOfClass(VolunteerDashboardPanel.class, this);

                    if (dashboard != null) {

                        dashboard.showPage("leaderboard");
                    }
                }
        );

        JPanel bottom = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        8
                )
        );

        bottom.setBackground(
                UIColors.CARD
        );

        bottom.add(viewFull);

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================================================
    // LOAD DASHBOARD DATA
    // =========================================================

    private void loadDashboardData() {

        PortalUser users =
                userDAO.getUserByID(user_id);

        PortalVolunteer volunteers =
                volunteerDAO.getVolunteerByUserID(user_id);

        // -----------------------------------------------------
        // USER INFORMATION
        // -----------------------------------------------------

        if (users != null) {

            String full_name =
                    users.getFullName();

            if (full_name == null ||
                    full_name.trim().isEmpty()) {

                full_name = "Volunteer";
            }

            welcomeLabel.setText(
                    "Welcome, " + full_name
            );

        } else {

            welcomeLabel.setText(
                    "Welcome, Volunteer"
            );
        }

        // -----------------------------------------------------
        // VOLUNTEER INFORMATION
        // -----------------------------------------------------

        if (volunteers == null) {

            welcomeMessage.setText(
                    "Volunteer profile information could not be found."
            );

            totalHoursValue.setText("0.00");
            approvedHoursValue.setText("0.00");
            rankValue.setText("-");
            completedEventsValue.setText("0");

            return;
        }

        BigDecimal total_hours =
                volunteers.getTotalHours();

        if (total_hours == null) {
            total_hours = BigDecimal.ZERO;
        }

        totalHoursValue.setText(
                formatNumber(total_hours)
        );

        completedEventsValue.setText(
                String.valueOf(
                        volunteers.getCompletedEventsCount()
                )
        );

        // -----------------------------------------------------
        // ATTENDANCE
        // -----------------------------------------------------

        List<AttendanceRecord> records =
                attendanceDAO.getAttendanceByVolunteer(
                        volunteers.getVolunteerID()
                );

        BigDecimal approvedHours =
                BigDecimal.ZERO;

        int pendingHours = 0;

        if (records != null) {

            for (AttendanceRecord record : records) {

                if (record == null) {
                    continue;
                }

                String status =
                        record.getStatus();

                BigDecimal hours =
                        record.getHoursAwarded();

                if (hours != null &&
                        status != null &&
                        status.equalsIgnoreCase(
                                "Approved"
                        )) {

                    approvedHours =
                            approvedHours.add(hours);
                }

                if (status != null &&
                        status.equalsIgnoreCase(
                                "Pending"
                        )) {

                    pendingHours++;
                }
            }
        }

        approvedHoursValue.setText(
                formatNumber(approvedHours)
        );

        // -----------------------------------------------------
        // APPLICATIONS
        // -----------------------------------------------------

        List<OpportunityApplication> applications =
                applicationDAO.getApplicationsByVolunteer(
                        volunteers.getVolunteerID()
                );

        int applicationCount =
                applications == null
                        ? 0
                        : applications.size();

        loadApplications(applications);

        // -----------------------------------------------------
        // RANKING
        // -----------------------------------------------------

        calculateRank(volunteers);

        loadRanking();

        // -----------------------------------------------------
        // WELCOME MESSAGE
        // -----------------------------------------------------

        welcomeMessage.setText(
                "You have "
                + applicationCount
                + " volunteers application"
                + (applicationCount == 1 ? "" : "s")
                + " and "
                + pendingHours
                + " pending hour record"
                + (pendingHours == 1 ? "" : "s")
                + "."
        );
    }

    // =========================================================
    // LOAD APPLICATIONS
    // =========================================================

    private void loadApplications(
            List<OpportunityApplication> applications
    ) {

        applicationsContainer.removeAll();

        if (applications == null ||
                applications.isEmpty()) {

            JLabel empty = new JLabel(
                    "No volunteers activities found."
            );

            empty.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );

            empty.setForeground(
                    Color.WHITE
            );

            empty.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            applicationsContainer.add(
                    Box.createVerticalStrut(20)
            );

            applicationsContainer.add(empty);

        } else {

            applications.sort(
                    Comparator.comparing(
                            OpportunityApplication::getApplicationDate,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            int count =
                    Math.min(
                            applications.size(),
                            5
                    );

            for (int i = 0;
                    i < count;
                    i++) {

                OpportunityApplication application =
                        applications.get(i);

                JPanel card =
                        createApplicationCard(
                                application
                        );

                card.setAlignmentX(
                        Component.LEFT_ALIGNMENT
                );

                applicationsContainer.add(card);

                if (i < count - 1) {

                    applicationsContainer.add(
                            Box.createVerticalStrut(10)
                    );
                }
            }
        }

        applicationsContainer.revalidate();
        applicationsContainer.repaint();
    }

    // =========================================================
    // APPLICATION CARD
    // =========================================================

    private JPanel createApplicationCard(
            OpportunityApplication application
    ) {

        JPanel card = new JPanel(
                new BorderLayout(
                        12,
                        5
                )
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        /*
         * Make cards stretch across the blue area.
         */
        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        JPanel information = new JPanel();

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );

        information.setBackground(
                Color.WHITE
        );

        String title =
                application.getOpportunityTitle();

        if (title == null ||
                title.trim().isEmpty()) {

            title =
                    "Opportunity #"
                    + application.getOppID();
        }

        JLabel opportunity =
                new JLabel(title);

        opportunity.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        opportunity.setForeground(
                UIColors.TEXT_DARK
        );

        information.add(opportunity);

        // -----------------------------------------------------
        // Opportunity date/time
        // -----------------------------------------------------

        String dateTime =
                buildOpportunityDateTime(
                        application
                );

        if (!dateTime.isEmpty()) {

            JLabel date =
                    new JLabel(dateTime);

            date.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            date.setForeground(
                    UIColors.TEXT_DARK
            );

            information.add(
                    Box.createVerticalStrut(4)
            );

            information.add(date);
        }

        // -----------------------------------------------------
        // Location
        // -----------------------------------------------------

        String location =
                application.getOpportunityLocation();

        if (location != null &&
                !location.trim().isEmpty()) {

            JLabel locationLabel =
                    new JLabel(
                            "Location: " + location
                    );

            locationLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            locationLabel.setForeground(
                    UIColors.TEXT_GREY
            );

            information.add(
                    Box.createVerticalStrut(3)
            );

            information.add(
                    locationLabel
            );
        }

        // -----------------------------------------------------
        // Application date
        // -----------------------------------------------------

        if (application.getApplicationDate() != null) {

            JLabel applied =
                    new JLabel(
                            "Applied: "
                            + formatDateTime(
                                    application
                                            .getApplicationDate()
                            )
                    );

            applied.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            applied.setForeground(
                    UIColors.TEXT_GREY
            );

            information.add(
                    Box.createVerticalStrut(3)
            );

            information.add(applied);
        }

        // -----------------------------------------------------
        // Status
        // -----------------------------------------------------

        String applicationStatus =
                application.getStatus();

        if (applicationStatus == null ||
                applicationStatus.trim().isEmpty()) {

            applicationStatus = "Pending";
        }

        JLabel status =
                new JLabel(
                        applicationStatus
                );

        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        status.setForeground(
                getStatusColor(
                        applicationStatus
                )
        );

        JPanel statusPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        statusPanel.setBackground(
                Color.WHITE
        );

        statusPanel.add(status);

        card.add(
                information,
                BorderLayout.CENTER
        );

        card.add(
                statusPanel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // BUILD OPPORTUNITY DATE/TIME
    // =========================================================

    private String buildOpportunityDateTime(
            OpportunityApplication application
    ) {

        StringBuilder result =
                new StringBuilder();

        LocalDate date =
                application.getOpportunityDate();

        LocalTime start =
                application.getStartTime();

        LocalTime end =
                application.getEndTime();

        if (date != null) {

            result.append(
                    formatDate(date)
            );
        }

        if (start != null) {

            if (result.length() > 0) {
                result.append(" • ");
            }

            result.append(
                    formatTime(start)
            );

            if (end != null) {

                result.append(" - ");
                result.append(
                        formatTime(end)
                );
            }
        }

        return result.toString();
    }

    // =========================================================
    // LOAD RANKING
    // =========================================================

    private void loadRanking() {

        rankingContainer.removeAll();

        List<PortalVolunteer> volunteers =
                volunteerDAO.getAllVolunteers();

        if (volunteers == null ||
                volunteers.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "No volunteers rankings available."
                    );

            empty.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            empty.setForeground(
                    UIColors.TEXT_GREY
            );

            rankingContainer.add(empty);

            rankingContainer.revalidate();
            rankingContainer.repaint();

            return;
        }

        volunteers.sort(
                (v1, v2) -> {

                    BigDecimal h1 =
                            v1.getTotalHours();

                    BigDecimal h2 =
                            v2.getTotalHours();

                    if (h1 == null) {
                        h1 = BigDecimal.ZERO;
                    }

                    if (h2 == null) {
                        h2 = BigDecimal.ZERO;
                    }

                    return h2.compareTo(h1);
                }
        );

        int count =
                Math.min(
                        volunteers.size(),
                        5
                );

        for (int i = 0;
                i < count;
                i++) {

            PortalVolunteer vol =
                    volunteers.get(i);

            int rank = i + 1;

            JPanel rankingRow =
                    createRankingRow(
                            vol,
                            rank
                    );

            rankingRow.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            rankingContainer.add(
                    rankingRow
            );

            if (i < count - 1) {

                rankingContainer.add(
                        Box.createVerticalStrut(6)
                );
            }
        }

        rankingContainer.revalidate();
        rankingContainer.repaint();
    }

    // =========================================================
    // RANKING ROW
    // =========================================================

    private JPanel createRankingRow(
            PortalVolunteer volunteers,
            int rank
    ) {

        JPanel row = new JPanel(
                new BorderLayout()
        );

        row.setBackground(
                UIColors.CARD
        );

        row.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        4,
                        12,
                        4
                )
        );

        String volunteerName =
                getVolunteerName(
                        volunteers
                );

        JLabel name =
                new JLabel(
                        rank + ".  " + volunteerName
                );

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        name.setForeground(
                UIColors.TEXT_DARK
        );

        BigDecimal hours =
                volunteers.getTotalHours();

        if (hours == null) {
            hours = BigDecimal.ZERO;
        }

        JLabel hoursLabel =
                new JLabel(
                        formatNumber(hours)
                        + " hrs"
                );

        hoursLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        hoursLabel.setForeground(
                UIColors.TEXT_GREY
        );

        row.add(
                name,
                BorderLayout.CENTER
        );

        row.add(
                hoursLabel,
                BorderLayout.EAST
        );

        return row;
    }

    // =========================================================
    // GET VOLUNTEER NAME FROM USER TABLE
    // =========================================================

    private String getVolunteerName(
            PortalVolunteer volunteers
    ) {

        if (volunteers == null) {
            return "Volunteer";
        }

        PortalUser users =
                userDAO.getUserByID(
                        volunteers.getUserID()
                );

        if (users != null) {

            String name =
                    users.getFullName();

            if (name != null &&
                    !name.trim().isEmpty()) {

                return name;
            }
        }

        return "Volunteer #" +
                volunteers.getVolunteerID();
    }

    // =========================================================
    // CALCULATE CURRENT RANK
    // =========================================================

    private void calculateRank(
            PortalVolunteer currentVolunteer
    ) {

        List<PortalVolunteer> volunteers =
                volunteerDAO.getAllVolunteers();

        if (volunteers == null ||
                volunteers.isEmpty()) {

            rankValue.setText("-");
            return;
        }

        volunteers.sort(
                (v1, v2) -> {

                    BigDecimal h1 =
                            v1.getTotalHours();

                    BigDecimal h2 =
                            v2.getTotalHours();

                    if (h1 == null) {
                        h1 = BigDecimal.ZERO;
                    }

                    if (h2 == null) {
                        h2 = BigDecimal.ZERO;
                    }

                    return h2.compareTo(h1);
                }
        );

        int rank = 1;

        for (PortalVolunteer vol :
                volunteers) {

            if (vol.getVolunteerID()
                    == currentVolunteer
                            .getVolunteerID()) {

                rankValue.setText(
                        "#" + rank
                );

                return;
            }

            rank++;
        }

        rankValue.setText("-");
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private String formatNumber(
            BigDecimal value
    ) {

        if (value == null) {
            return "0.00";
        }

        return value.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        ).toString();
    }

    private String formatDate(
            LocalDate date
    ) {

        if (date == null) {
            return "";
        }

        return date.format(
                DATE_FORMAT
        );
    }

    private String formatTime(
            LocalTime time
    ) {

        if (time == null) {
            return "";
        }

        return time.format(
                TIME_FORMAT
        );
    }

    private String formatDateTime(
            LocalDateTime dateTime
    ) {

        if (dateTime == null) {
            return "";
        }

        return dateTime.format(
                DateTimeFormatter.ofPattern(
                        "MMM dd, yyyy hh:mm a"
                )
        );
    }

    private Color getStatusColor(
            String status
    ) {

        if (status == null) {
            return UIColors.TEXT_GREY;
        }

        if (status.equalsIgnoreCase(
                "Approved"
        )) {

            return UIColors.GREEN;
        }

        if (status.equalsIgnoreCase(
                "Rejected"
        )) {

            return UIColors.RED;
        }

        if (status.equalsIgnoreCase(
                "Pending"
        )) {

            return UIColors.ORANGE;
        }

        return UIColors.TEXT_GREY;
    }

    private void styleButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                UIColors.PRIMARY
        );

        button.setBackground(
                Color.WHITE
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UIColors.PRIMARY
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                14,
                                7,
                                14
                        )
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    public void refreshDashboard() {

        loadDashboardData();
    }
}