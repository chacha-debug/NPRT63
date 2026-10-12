/*

 * Click nbfs\://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

 * Click nbfs\://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

 */

package com.communityservice.ui;

import com.communityservice.DatabaseConnection;

import javax.swing.*;

import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import javax.swing.event.DocumentEvent;

import javax.swing.event.DocumentListener;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.time.Duration;

import java.time.LocalTime;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;

import java.util.List;

public class CreateEventPanel extends JPanel {

    // =========================================================

    // COLOURS

    // =========================================================

    private static final Color BACKGROUND =

            UIColors.BACKGROUND;

    private static final Color CARD_BORDER =

            UIColors.BORDER;

    private static final Color TEXT =

            UIColors.TEXT_GREY;

    private static final Color MUTED =

            UIColors.TEXT_GREY;

    private static final Color BLUE =

            UIColors.PRIMARY;

    private static final Color TEAL =

            UIColors.GREEN;

    // =========================================================

    // LOGGED-IN ngos

    // =========================================================

    private final int ngoUserID;

    // =========================================================

    // FORM COMPONENTS

    // =========================================================

    private JTextField eventNameField;

    private JTextArea descriptionArea;

    private JLabel descriptionCounter;

    private JComboBox<String> categoryCombo;

    private SimpleDateChooser eventDateChooser;

    private SimpleDateChooser deadlineChooser;

    private JSpinner startTimeSpinner;

    private JSpinner endTimeSpinner;

    private JTextField durationField;

    private JTextField locationField;

    private JTextArea locationNotesArea;

    private JSpinner volunteerSlotsSpinner;

    private JComboBox<String> minimumAgeCombo;

    private RoundedButton saveDraftButton;

    private RoundedButton publishButton;

    private JLabel publishValidationHint;

    private JPanel selectedCustomSkillsPanel;

    private final List<JToggleButton> skillButtons =

            new ArrayList<>();

    public CreateEventPanel() {

        this(1);

    }

    // =========================================================

    // REAL CONSTRUCTOR

    // =========================================================

    public CreateEventPanel(int ngoUserID) {

        this.ngoUserID = ngoUserID;

        setLayout(new BorderLayout());

        setBackground(BACKGROUND);

        add(

                createSectionHeader(),

                BorderLayout.NORTH

        );

        JPanel contentWrapper =

                new JPanel(

                        new GridBagLayout()

                );

        contentWrapper.setBackground(

                BACKGROUND

        );

        JPanel form =

                createMainForm();

        GridBagConstraints gbc =

                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.anchor =

                GridBagConstraints.NORTH;

        gbc.insets =

                new Insets(

                        18,

                        18,

                        18,

                        18

                );

        contentWrapper.add(

                form,

                gbc

        );

        JScrollPane scrollPane =

                new JScrollPane(

                        contentWrapper

                );

        scrollPane.setBorder(null);

        scrollPane

                .getViewport()

                .setBackground(

                        BACKGROUND

                );

        scrollPane

                .getVerticalScrollBar()

                .setUnitIncrement(16);

        scrollPane.setHorizontalScrollBarPolicy(

                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

        );

        add(

                scrollPane,

                BorderLayout.CENTER

        );

        setupDescriptionCounter();

        setupDurationCalculation();

    }

    // =========================================================

    // HEADER

    // =========================================================

    private JPanel createSectionHeader() {

        JPanel header =

                new JPanel(

                        new BorderLayout()

                );

        header.setBackground(

                Color.WHITE

        );

        header.setBorder(

                BorderFactory.createMatteBorder(

                        0,

                        0,

                        1,

                        0,

                        new Color(

                                225,

                                225,

                                225

                        )

                )

        );

        JLabel label =

                new JLabel(

                        "Fill in the details to list your event"

                );

        label.setFont(

                new Font(

                        "Arial",

                        Font.PLAIN,

                        16

                )

        );

        label.setForeground(TEXT);

        label.setBorder(

                BorderFactory.createEmptyBorder(

                        12,

                        18,

                        12,

                        18

                )

        );

        header.add(

                label,

                BorderLayout.WEST

        );

        return header;

    }

    // =========================================================

    // MAIN FORM

    // =========================================================

    private JPanel createMainForm() {

        JPanel form =

                new JPanel(

                        new GridBagLayout()

                );

        form.setBackground(

                BACKGROUND

        );

        form.setPreferredSize(

                new Dimension(

                        930,

                        690

                )

        );

        GridBagConstraints gbc =

                new GridBagConstraints();

        gbc.fill =

                GridBagConstraints.HORIZONTAL;

        gbc.anchor =

                GridBagConstraints.NORTH;

        gbc.weightx = 1.0;

        JPanel leftColumn =

                createLeftColumn();

        JPanel rightColumn =

                createRightColumn();

        leftColumn.setPreferredSize(

                new Dimension(

                        440,

                        660

                )

        );

        rightColumn.setPreferredSize(

                new Dimension(

                        440,

                        660

                )

        );

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.insets =

                new Insets(

                        0,

                        0,

                        0,

                        22

                );

        form.add(

                leftColumn,

                gbc

        );

        gbc.gridx = 1;

        gbc.insets =

                new Insets(

                        0,

                        0,

                        0,

                        0

                );

        form.add(

                rightColumn,

                gbc

        );

        return form;

    }

    // =========================================================

    // LEFT COLUMN

    // =========================================================

    private JPanel createLeftColumn() {

        JPanel column =

                verticalPanel(

                        BACKGROUND

                );

        // =====================================================

        // EVENT INFORMATION

        // =====================================================

        JPanel eventCard =

                createCard(

                        "Event Information"

                );

        eventCard.setMaximumSize(

                new Dimension(

                        440,

                        360

                )

        );

        eventCard.setPreferredSize(

                new Dimension(

                        440,

                        360

                )

        );

        // EVENT NAME

        eventNameField =

                createTextField();

        eventCard.add(

                createLabel(

                        "Event name *"

                )

        );

        eventCard.add(

                eventNameField

        );

        eventCard.add(

                createHint(

                        "Make it clear and descriptive"

                )

        );

        eventCard.add(

                Box.createVerticalStrut(

                        14

                )

        );

        // DESCRIPTION

        descriptionArea =

                new JTextArea(

                        6,

                        20

                );

        descriptionArea.setLineWrap(

                true

        );

        descriptionArea.setWrapStyleWord(

                true

        );

        JScrollPane descriptionScroll =

                new JScrollPane(

                        descriptionArea

                );

        descriptionScroll.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        descriptionScroll.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        120

                )

        );

        eventCard.add(

                createLabel(

                        "Description *"

                )

        );

        eventCard.add(

                descriptionScroll

        );

        descriptionCounter =

                createHint(

                        "0 / 500"

                );

        descriptionCounter.setAlignmentX(

                Component.RIGHT_ALIGNMENT

        );

        eventCard.add(

                descriptionCounter

        );

        eventCard.add(

                Box.createVerticalStrut(

                        14

                )

        );

        // CATEGORY

        eventCard.add(

                createLabel(

                        "Category *"

                )

        );

        categoryCombo =

                new JComboBox<>(

                        categoryValues()

                );

        categoryCombo.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        32

                )

        );

        categoryCombo.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        eventCard.add(

                categoryCombo

        );

        // =====================================================

        // REQUIREMENTS

        // =====================================================

        JPanel requirementsCard =

                createCard(

                        "Requirements"

                );

        requirementsCard.setMaximumSize(

                new Dimension(

                        440,

                        250

                )

        );

        requirementsCard.setPreferredSize(

                new Dimension(

                        440,

                        250

                )

        );

        JPanel requirementsRow =

                new JPanel(

                        new GridLayout(

                                1,

                                2,

                                14,

                                0

                        )

                );

        requirementsRow.setBackground(

                Color.WHITE

        );

        requirementsRow.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        // volunteers SLOTS

        JPanel slotsPanel =

                verticalPanel(

                        Color.WHITE

                );

        slotsPanel.add(

                createLabel(

                        "Volunteer slots *"

                )

        );

        volunteerSlotsSpinner =

                new JSpinner(

                        new SpinnerNumberModel(

                                1,

                                1,

                                1000,

                                1

                        )

                );

        volunteerSlotsSpinner.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        32

                )

        );

        slotsPanel.add(

                volunteerSlotsSpinner

        );

        // MINIMUM AGE

        JPanel agePanel =

                verticalPanel(

                        Color.WHITE

                );

        agePanel.add(

                createLabel(

                        "Minimum age"

                )

        );

        minimumAgeCombo =

                new JComboBox<>(

                        new String[]{

                                "No minimum",

                                "16+",

                                "18+",

                                "21+"

                        }

                );

        minimumAgeCombo.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        32

                )

        );

        agePanel.add(

                minimumAgeCombo

        );

        requirementsRow.add(

                slotsPanel

        );

        requirementsRow.add(

                agePanel

        );

        requirementsCard.add(

                requirementsRow

        );

        requirementsCard.add(

                Box.createVerticalStrut(

                        15

                )

        );

        requirementsCard.add(

                createLabel(

                        "Required skills"

                )

        );

        String[] availableSkills = {

                "Physical fitness", "Teamwork", "Logistics", "Teaching",

                "First aid", "Driving", "Communication", "Leadership",

                "Cooking", "Childcare", "Elderly care", "Medical / Nursing",

                "Fundraising", "Event planning", "Photography", "Translation",

                "Construction / Manual labor", "Gardening", "Computer skills",

                "Public speaking", "Administration", "Counseling",

                "Music / Performing arts", "Arts & crafts", "Animal care"

        };

        JPanel skillsWrap =

                new JPanel(

                        new FlowLayout(

                                FlowLayout.LEFT,

                                7,

                                4

                        )

                );

        skillsWrap.setBackground(

                Color.WHITE

        );

        skillsWrap.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        for (String skill : availableSkills) {

            skillsWrap.add(createSkillButton(skill));

        }

        // NGOs can add a project-specific skill when it is not in the predefined list.

        JPanel customSkillRow = new JPanel(new BorderLayout(6, 0));

        customSkillRow.setBackground(Color.WHITE);

        JTextField customRequiredSkillField = new JTextField();

        AppTheme.styleTextField(customRequiredSkillField);

        new TextPrompt("Other required skill...", customRequiredSkillField);

        JButton addCustomSkillButton = new JButton("+ Add skill");

        addCustomSkillButton.setFont(AppTheme.FONT_SMALL);

        addCustomSkillButton.addActionListener(e -> {

            String customSkill = customRequiredSkillField.getText().trim();

            if (!customSkill.isEmpty()) {

                JToggleButton customButton = createSkillButton(customSkill);

                customButton.setSelected(true);

                // Show newly added skills immediately, even when the large predefined list is clipped.

                if (selectedCustomSkillsPanel != null) {

                    selectedCustomSkillsPanel.add(customButton);

                    selectedCustomSkillsPanel.revalidate();

                    selectedCustomSkillsPanel.repaint();

                } else {

                    skillsWrap.add(customButton);

                }

                updateActionButtons();

                skillsWrap.revalidate();

                skillsWrap.repaint();

                customRequiredSkillField.setText("");

            }

        });

        customRequiredSkillField.addActionListener(e -> addCustomSkillButton.doClick());

        customSkillRow.add(customRequiredSkillField, BorderLayout.CENTER);

        customSkillRow.add(addCustomSkillButton, BorderLayout.EAST);

        requirementsCard.add(customSkillRow);

        selectedCustomSkillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 4));

        selectedCustomSkillsPanel.setBackground(Color.WHITE);

        selectedCustomSkillsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        requirementsCard.add(createLabel("Selected custom skills:"));
        requirementsCard.add(selectedCustomSkillsPanel);

        requirementsCard.add(

                skillsWrap

        );

        column.add(

                eventCard

        );

        column.add(

                Box.createVerticalStrut(

                        15

                )

        );

        column.add(

                requirementsCard

        );

        return column;

    }

    // =========================================================

    // RIGHT COLUMN

    // =========================================================

    private JPanel createRightColumn() {

        JPanel column =

                verticalPanel(

                        BACKGROUND

                );

        // =====================================================

        // DATE & TIME CARD

        // =====================================================

        JPanel dateTimeCard =

                createCard(

                        "Date & Time"

                );

        dateTimeCard.setMaximumSize(

                new Dimension(

                        440,

                        300

                )

        );

        dateTimeCard.setPreferredSize(

                new Dimension(

                        440,

                        300

                )

        );

        // DATES

        JPanel dateRow =

                new JPanel(

                        new GridLayout(

                                1,

                                2,

                                14,

                                0

                        )

                );

        dateRow.setBackground(

                Color.WHITE

        );

        dateRow.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        JPanel eventDatePanel =

                verticalPanel(

                        Color.WHITE

                );

        eventDatePanel.add(

                createLabel(

                        "Event date *"

                )

        );

        eventDateChooser =

                new SimpleDateChooser();

        eventDateChooser.setDateFormatString(

                "yyyy-MM-dd"

        );

        eventDatePanel.add(

                eventDateChooser

        );

        JPanel deadlinePanel =

                verticalPanel(

                        Color.WHITE

                );

        deadlinePanel.add(

                createLabel(

                        "Application deadline *"

                )

        );

        deadlineChooser =

                new SimpleDateChooser();

        deadlineChooser.setDateFormatString(

                "yyyy-MM-dd"

        );

        deadlinePanel.add(

                deadlineChooser

        );

        dateRow.add(

                eventDatePanel

        );

        dateRow.add(

                deadlinePanel

        );

        dateTimeCard.add(

                dateRow

        );

        dateTimeCard.add(

                Box.createVerticalStrut(

                        18

                )

        );

        // START / END TIMES

        JPanel timeRow =

                new JPanel(

                        new GridLayout(

                                1,

                                2,

                                14,

                                0

                        )

                );

        timeRow.setBackground(

                Color.WHITE

        );

        timeRow.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        JPanel startPanel =

                verticalPanel(

                        Color.WHITE

                );

        startPanel.add(

                createLabel(

                        "Start time *"

                )

        );

        startTimeSpinner =

                createTimeSpinner(

                        8,

                        0

                );

        startPanel.add(

                startTimeSpinner

        );

        JPanel endPanel =

                verticalPanel(

                        Color.WHITE

                );

        endPanel.add(

                createLabel(

                        "End time *"

                )

        );

        endTimeSpinner =

                createTimeSpinner(

                        12,

                        0

                );

        endPanel.add(

                endTimeSpinner

        );

        timeRow.add(

                startPanel

        );

        timeRow.add(

                endPanel

        );

        dateTimeCard.add(

                timeRow

        );

        dateTimeCard.add(

                Box.createVerticalStrut(

                        18

                )

        );

        // DURATION

        dateTimeCard.add(

                createLabel(

                        "Duration"

                )

        );

        durationField =

                createTextField();

        durationField.setEditable(

                false

        );

        durationField.setBackground(

                new Color(

                        240,

                        240,

                        240

                )

        );

        durationField.setHorizontalAlignment(

                SwingConstants.CENTER

        );

        dateTimeCard.add(

                durationField

        );

        // =====================================================

        // LOCATION CARD

        // =====================================================

        JPanel locationCard =

                createCard(

                        "Location"

                );

        locationCard.setMaximumSize(

                new Dimension(

                        440,

                        235

                )

        );

        locationCard.setPreferredSize(

                new Dimension(

                        440,

                        235

                )

        );

        locationField =

                createTextField();

        locationCard.add(

                createLabel(

                        "Location / venue *"

                )

        );

        locationCard.add(

                locationField

        );

        locationCard.add(

                createHint(

                        "Include the full street address"

                )

        );

        locationCard.add(

                Box.createVerticalStrut(

                        14

                )

        );

        locationNotesArea =

                new JTextArea(

                        4,

                        20

                );

        locationNotesArea.setLineWrap(

                true

        );

        locationNotesArea.setWrapStyleWord(

                true

        );

        JScrollPane notesScroll =

                new JScrollPane(

                        locationNotesArea

                );

        notesScroll.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        notesScroll.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        85

                )

        );

        locationCard.add(

                createLabel(

                        "Additional location notes"

                )

        );

        locationCard.add(

                notesScroll

        );

        // =====================================================

        // BUTTONS

        // =====================================================

        RoundedButton discardButton =

                createActionButton(

                        "Discard",

                        new Color(225, 230, 240)

                );

        discardButton.setForeground(BLUE);

        saveDraftButton =

                createActionButton(

                        "Save as Draft",

                        new Color(200, 225, 222)

                );

        saveDraftButton.setForeground(new Color(20, 100, 90));

        publishButton =

                createActionButton(

                        "Publish Event",

                        TEAL

                );

        discardButton.addActionListener(

                this::discardAction

        );

        saveDraftButton.addActionListener(

                e -> saveDraft()

        );

        publishButton.addActionListener(

                e -> publishEvent()

        );

        saveDraftButton.setEnabled(false);

        publishButton.setEnabled(false);

        javax.swing.event.DocumentListener eventValidation = new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent e){ updateActionButtons(); }

            public void removeUpdate(javax.swing.event.DocumentEvent e){ updateActionButtons(); }

            public void changedUpdate(javax.swing.event.DocumentEvent e){ updateActionButtons(); }

        };

        eventNameField.getDocument().addDocumentListener(eventValidation);

        descriptionArea.getDocument().addDocumentListener(eventValidation);

        locationField.getDocument().addDocumentListener(eventValidation);

        categoryCombo.addActionListener(e -> updateActionButtons());

        eventDateChooser.addChangeListener(e -> updateActionButtons());

        deadlineChooser.addChangeListener(e -> updateActionButtons());

        startTimeSpinner.addChangeListener(e -> updateActionButtons());

        endTimeSpinner.addChangeListener(e -> updateActionButtons());

        updateActionButtons();

        column.add(

                dateTimeCard

        );

        column.add(

                Box.createVerticalStrut(

                        15

                )

        );

        column.add(

                locationCard

        );

        column.add(

                Box.createVerticalStrut(

                        15

                )

        );

        // Keep all actions visible, full-width and vertically stacked.
        JPanel buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn, BoxLayout.Y_AXIS));
        buttonColumn.setBackground(Color.WHITE);
        buttonColumn.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (RoundedButton button : new RoundedButton[]{discardButton, saveDraftButton, publishButton}) {
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setPreferredSize(new Dimension(440, 44));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            buttonColumn.add(button);
            buttonColumn.add(Box.createVerticalStrut(10));
        }
        publishValidationHint = new JLabel(" ");
        publishValidationHint.setForeground(new Color(165, 65, 45));
        publishValidationHint.setFont(AppTheme.FONT_SMALL);
        publishValidationHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonColumn.add(publishValidationHint);
        column.add(buttonColumn);
        updateActionButtons();

        return column;

    }

    // =========================================================

    // TIME SPINNER

    // =========================================================

    private JSpinner createTimeSpinner(

            int hour,

            int minute

    ) {

        Calendar calendar =

                Calendar.getInstance();

        calendar.set(

                Calendar.HOUR_OF_DAY,

                hour

        );

        calendar.set(

                Calendar.MINUTE,

                minute

        );

        calendar.set(

                Calendar.SECOND,

                0

        );

        SpinnerDateModel model =

                new SpinnerDateModel();

        model.setValue(

                calendar.getTime()

        );

        JSpinner spinner =

                new JSpinner(

                        model

                );

        JSpinner.DateEditor editor =

                new JSpinner.DateEditor(

                        spinner,

                        "HH:mm"

                );

        spinner.setEditor(

                editor

        );

        spinner.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        32

                )

        );

        return spinner;

    }

    // =========================================================

    // DESCRIPTION COUNTER

    // =========================================================

    private void setupDescriptionCounter() {

        descriptionArea

                .getDocument()

                .addDocumentListener(

                        new DocumentListener() {

                            private void update() {

                                int length =

                                        descriptionArea

                                                .getText()

                                                .length();

                                if (

                                        length > 500

                                ) {

                                    descriptionArea.setText(

                                            descriptionArea

                                                    .getText()

                                                    .substring(

                                                            0,

                                                            500

                                                    )

                                    );

                                    length = 500;

                                }

                                descriptionCounter.setText(

                                        length

                                                + " / 500"

                                );

                            }

                            @Override

                            public void insertUpdate(

                                    DocumentEvent e

                            ) {

                                SwingUtilities.invokeLater(

                                        this::update

                                );

                            }

                            @Override

                            public void removeUpdate(

                                    DocumentEvent e

                            ) {

                                SwingUtilities.invokeLater(

                                        this::update

                                );

                            }

                            @Override

                            public void changedUpdate(

                                    DocumentEvent e

                            ) {

                                SwingUtilities.invokeLater(

                                        this::update

                                );

                            }

                        }

                );

    }

    // =========================================================

    // DURATION CALCULATION

    // =========================================================

    private void setupDurationCalculation() {

        ChangeListener listener =

                (ChangeEvent e) ->

                        calculateDuration();

        startTimeSpinner.addChangeListener(

                listener

        );

        endTimeSpinner.addChangeListener(

                listener

        );

        calculateDuration();

    }

    private void calculateDuration() {

        LocalTime start =

                getLocalTime(

                        startTimeSpinner

                );

        LocalTime end =

                getLocalTime(

                        endTimeSpinner

                );

        if (

                !end.isAfter(start)

        ) {

            durationField.setText(

                    "Invalid time"

            );

            return;

        }

        Duration duration =

                Duration.between(

                        start,

                        end

                );

        long hours =

                duration.toHours();

        long minutes =

                duration.toMinutes()

                        % 60;

        if (

                minutes == 0

        ) {

            durationField.setText(

                    hours

                            + (

                            hours == 1

                                    ? " hr"

                                    : " hrs"

                    )

            );

        } else {

            durationField.setText(

                    hours

                            + " hrs "

                            + minutes

                            + " min"

            );

        }

    }

    private LocalTime getLocalTime(

            JSpinner spinner

    ) {

        Date value =

                (Date)

                        spinner.getValue();

        Calendar calendar =

                Calendar.getInstance();

        calendar.setTime(

                value

        );

        return LocalTime.of(

                calendar.get(

                        Calendar.HOUR_OF_DAY

                ),

                calendar.get(

                        Calendar.MINUTE

                )

        );

    }

    private java.sql.Time getSQLTime(

            JSpinner spinner

    ) {

        LocalTime time =

                getLocalTime(

                        spinner

                );

        return java.sql.Time.valueOf(

                time

        );

    }

    // =========================================================

    // VALIDATION

    // =========================================================

    private void updateActionButtons() {

        if (saveDraftButton == null || publishButton == null) return;

        saveDraftButton.setEnabled(!eventNameField.getText().trim().isEmpty());

        String issue = publicationIssue();

        publishButton.setEnabled(issue == null);

        if (publishValidationHint != null) {

            publishValidationHint.setText(issue == null

                    ? "Ready to publish." : "<html><body style=\"width:400px\">Cannot publish: " + issue + "</body></html>");

            publishValidationHint.setForeground(issue == null

                    ? new Color(20, 110, 90) : new Color(165, 65, 45));

            publishValidationHint.setToolTipText(issue);

        }

    }

    private String publicationIssue() {

        if (eventNameField.getText().trim().isEmpty()) return "Enter an event name.";

        if (descriptionArea.getText().trim().isEmpty()) return "Enter a description.";

        if (categoryCombo.getSelectedIndex() <= 0) return "Select a category.";

        if (eventDateChooser.getDate() == null) return "Select the event date.";

        if (deadlineChooser.getDate() == null) return "Select an application deadline.";

        if (locationField.getText().trim().isEmpty()) return "Enter the event venue.";

        java.time.ZoneId zone = java.time.ZoneId.of("Africa/Johannesburg");

        java.time.LocalDate eventDay = eventDateChooser.getDate().toInstant().atZone(zone).toLocalDate();

        java.time.LocalDate deadlineDay = deadlineChooser.getDate().toInstant().atZone(zone).toLocalDate();

        java.time.LocalDate today = java.time.LocalDate.now(zone);

        if (eventDay.isBefore(today)) return "Event date cannot be in the past.";

        if (deadlineDay.isBefore(today)) return "Application deadline cannot be in the past.";

        if (deadlineDay.isAfter(eventDay)) return "Application deadline must be on or before the event date.";

        LocalTime start = getLocalTime(startTimeSpinner);

        LocalTime end = getLocalTime(endTimeSpinner);

        if (!end.isAfter(start)) return "End time must be later than start time.";

        if (eventDay.equals(today) && !start.isAfter(LocalTime.now(zone)))

            return "For an event today, start time must be in the future.";

        if (deadlineDay.equals(eventDay) && eventDay.equals(today)

                && !start.isAfter(LocalTime.now(zone)))

            return "Application deadline has already passed.";

        return null;

    }

    private boolean publishFieldsAreValid() {

        String issue = publicationIssue();

        if (issue != null) showWarning(issue);

        return issue == null;

    }

    private void showWarning(

            String message

    ) {

        JOptionPane.showMessageDialog(

                this,

                message,

                "Missing / Invalid Information",

                JOptionPane.WARNING_MESSAGE

        );

    }

    // =========================================================

    private boolean sameCalendarDay(Date a, Date b) {

        Calendar ca = Calendar.getInstance();

        Calendar cb = Calendar.getInstance();

        ca.setTime(a);

        cb.setTime(b);

        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR)

                && ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR);

    }

    // SAVE AS DRAFT

    // =========================================================

    private void saveDraft() {

        /*

         * For a draft, we allow the ngos to save an

         * incomplete event.

         *

         * The only thing we really need is a title.

         */

        if (

                eventNameField

                        .getText()

                        .trim()

                        .isEmpty()

        ) {

            showWarning(

                    "Please enter an event name before saving a draft."

            );

            return;

        }

        saveEvent(

                "Draft"

        );

    }

    // =========================================================

    // PUBLISH EVENT

    // =========================================================

    private void publishEvent() {

        if (

                !publishFieldsAreValid()

        ) {

            return;

        }

        saveEvent(

                "Open"

        );

    }

    // =========================================================

    // SAVE EVENT TO MYSQL

    // =========================================================

    private void saveEvent(

            String status

    ) {

        String sql =

                "INSERT INTO opportunities "

                        + "(ngo_user_id, title, description, date, "

                        + "application_deadline, start_time, end_time, "

                        + "location, location_notes, max_slots, "

                        + "current_slots, minimum_age, category, "

                        + "status, required_skills) "

                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (

                Connection conn =

                        DatabaseConnection.getConnection();

                PreparedStatement statement =

                        conn.prepareStatement(

                                sql

                        )

        ) {

            // ngos

            statement.setInt(

                    1,

                    ngoUserID

            );

            // TITLE

            statement.setString(

                    2,

                    eventNameField

                            .getText()

                            .trim()

            );

            // DESCRIPTION

            statement.setString(

                    3,

                    emptyToNull(

                            descriptionArea

                                    .getText()

                                    .trim()

                    )

            );

            // EVENT DATE

            if (

                    eventDateChooser

                            .getDate()

                            != null

            ) {

                statement.setDate(

                        4,

                        new java.sql.Date(

                                eventDateChooser

                                        .getDate()

                                        .getTime()

                        )

                );

            } else {

                statement.setNull(

                        4,

                        java.sql.Types.DATE

                );

            }

            // APPLICATION DEADLINE

            if (

                    deadlineChooser

                            .getDate()

                            != null

            ) {

                statement.setDate(

                        5,

                        new java.sql.Date(

                                deadlineChooser

                                        .getDate()

                                        .getTime()

                        )

                );

            } else {

                statement.setNull(

                        5,

                        java.sql.Types.DATE

                );

            }

            // START TIME

            statement.setTime(

                    6,

                    getSQLTime(

                            startTimeSpinner

                    )

            );

            // END TIME

            statement.setTime(

                    7,

                    getSQLTime(

                            endTimeSpinner

                    )

            );

            // LOCATION

            statement.setString(

                    8,

                    emptyToNull(

                            locationField

                                    .getText()

                                    .trim()

                    )

            );

            // LOCATION NOTES

            statement.setString(

                    9,

                    emptyToNull(

                            locationNotesArea

                                    .getText()

                                    .trim()

                    )

            );

            // MAX SLOTS

            statement.setInt(

                    10,

                    (Integer)

                            volunteerSlotsSpinner

                                    .getValue()

            );

            // CURRENT SLOTS

            statement.setInt(

                    11,

                    0

            );

            // MINIMUM AGE

            Integer minimum_age =

                    getSelectedMinimumAge();

            if (

                    minimum_age == null

            ) {

                statement.setNull(

                        12,

                        java.sql.Types.INTEGER

                );

            } else {

                statement.setInt(

                        12,

                        minimum_age

                );

            }

            // CATEGORY

            if (

                    categoryCombo

                            .getSelectedIndex()

                            == 0

            ) {

                statement.setNull(

                        13,

                        java.sql.Types.VARCHAR

                );

            } else {

                statement.setString(

                        13,

                        categoryCombo

                                .getSelectedItem()

                                .toString()

                );

            }

            // STATUS

            statement.setString(

                    14,

                    status

            );

            // SKILLS

            statement.setString(

                    15,

                    emptyToNull(

                            getSelectedSkills()

                    )

            );

            int rowsInserted =

                    statement.executeUpdate();

            if (

                    rowsInserted > 0

            ) {

                String message =

                        "Draft"

                                .equalsIgnoreCase(

                                        status

                                )

                                ? "Draft saved successfully!"

                                : "Event published successfully!";

                JOptionPane.showMessageDialog(

                        this,

                        message,

                        "Create Event",

                        JOptionPane.INFORMATION_MESSAGE

                );

                clearForm();

            }

        } catch (

                SQLException e

        ) {

            JOptionPane.showMessageDialog(

                    this,

                    "Could not save event.\n"

                            + e.getMessage(),

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE

            );

            e.printStackTrace();

        }

    }

    // =========================================================

    // MINIMUM AGE

    // =========================================================

    private Integer getSelectedMinimumAge() {

        String selected =

                minimumAgeCombo

                        .getSelectedItem()

                        .toString();

        switch (

                selected

        ) {

            case "16+":

                return 16;

            case "18+":

                return 18;

            case "21+":

                return 21;

            default:

                return null;

        }

    }

    // =========================================================

    // SELECTED SKILLS

    // =========================================================

    private String getSelectedSkills() {

        StringBuilder selected =

                new StringBuilder();

        for (

                JToggleButton button

                        : skillButtons

        ) {

            if (

                    button.isSelected()

            ) {

                if (

                        selected.length()

                                > 0

                ) {

                    selected.append(

                            ", "

                    );

                }

                selected.append(

                        button.getText()

                );

            }

        }

        return selected.toString();

    }

    // =========================================================

    // CLEAR FORM

    // =========================================================

    private void discardAction(

            ActionEvent e

    ) {

        int confirm =

                JOptionPane.showConfirmDialog(

                        this,

                        "Discard everything entered on this form?",

                        "Discard Event",

                        JOptionPane.YES_NO_OPTION

                );

        if (

                confirm

                        == JOptionPane.YES_OPTION

        ) {

            clearForm();

        }

    }

    private void clearForm() {

        eventNameField.setText("");

        descriptionArea.setText("");

        categoryCombo.setSelectedIndex(

                0

        );

        eventDateChooser.setDate(

                null

        );

        deadlineChooser.setDate(

                null

        );

        locationField.setText("");

        locationNotesArea.setText("");

        volunteerSlotsSpinner.setValue(

                1

        );

        minimumAgeCombo.setSelectedIndex(

                0

        );

        for (

                JToggleButton skillButton

                        : skillButtons

        ) {

            skillButton.setSelected(

                    false

            );

        }

        setSpinnerTime(

                startTimeSpinner,

                8,

                0

        );

        setSpinnerTime(

                endTimeSpinner,

                12,

                0

        );

        calculateDuration();

    }

    private void setSpinnerTime(

            JSpinner spinner,

            int hour,

            int minute

    ) {

        Calendar calendar =

                Calendar.getInstance();

        calendar.set(

                Calendar.HOUR_OF_DAY,

                hour

        );

        calendar.set(

                Calendar.MINUTE,

                minute

        );

        calendar.set(

                Calendar.SECOND,

                0

        );

        spinner.setValue(

                calendar.getTime()

        );

    }

    // =========================================================

    // CARD

    // =========================================================

    private JPanel createCard(

            String title

    ) {

        JPanel card =

                new JPanel();

        card.setLayout(

                new BoxLayout(

                        card,

                        BoxLayout.Y_AXIS

                )

        );

        card.setBackground(

                Color.WHITE

        );

        card.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        card.setBorder(

                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(

                                CARD_BORDER,

                                1,

                                true

                        ),

                        BorderFactory.createEmptyBorder(

                                12,

                                14,

                                14,

                                14

                        )

                )

        );

        JLabel titleLabel =

                new JLabel(

                        title

                );

        titleLabel.setForeground(

                BLUE

        );

        titleLabel.setFont(

                new Font(

                        "Arial",

                        Font.BOLD,

                        15

                )

        );

        titleLabel.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        card.add(

                titleLabel

        );

        card.add(

                Box.createVerticalStrut(

                        12

                )

        );

        return card;

    }

    // =========================================================

    // HELPERS

    // =========================================================

    private JPanel verticalPanel(

            Color background

    ) {

        JPanel panel =

                new JPanel();

        panel.setLayout(

                new BoxLayout(

                        panel,

                        BoxLayout.Y_AXIS

                )

        );

        panel.setBackground(

                background

        );

        return panel;

    }

    private JLabel createLabel(

            String text

    ) {

        JLabel label =

                new JLabel(

                        text

                );

        label.setFont(

                new Font(

                        "Arial",

                        Font.PLAIN,

                        13

                )

        );

        label.setForeground(

                TEXT

        );

        label.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        return label;

    }

    private JLabel createHint(

            String text

    ) {

        JLabel label =

                new JLabel(

                        text

                );

        label.setFont(

                new Font(

                        "Arial",

                        Font.PLAIN,

                        11

                )

        );

        label.setForeground(

                MUTED

        );

        label.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        return label;

    }

    private JTextField createTextField() {

        JTextField field =

                new JTextField();

        field.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        32

                )

        );

        field.setPreferredSize(

                new Dimension(

                        180,

                        32

                )

        );

        field.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        return field;

    }

    private JToggleButton createSkillButton(

            String text

    ) {

        JToggleButton button =

                new JToggleButton(

                        text

                );

        button.setFont(

                new Font(

                        "Arial",

                        Font.PLAIN,

                        12

                )

        );

        button.setForeground(

                BLUE

        );

        button.setBackground(

                new Color(

                        225,

                        230,

                        240

                )

        );

        button.setFocusPainted(

                false

        );

        button.setBorder(

                BorderFactory.createEmptyBorder(

                        6,

                        12,

                        6,

                        12

                )

        );

        skillButtons.add(

                button

        );

        return button;

    }

    private RoundedButton createActionButton(

            String text,

            Color color

    ) {

        RoundedButton button =

                new RoundedButton(

                        text,

                        18

                );

        button.setFont(

                new Font(

                        "Arial",

                        Font.BOLD,

                        17

                )

        );

        button.setForeground(

                Color.WHITE

        );

        button.setBackground(

                color

        );

        button.setFocusPainted(

                false

        );

        button.setBorderPainted(

                false

        );

        button.setContentAreaFilled(

                false

        );

        button.setAlignmentX(

                Component.LEFT_ALIGNMENT

        );

        button.setMaximumSize(

                new Dimension(

                        440,

                        46

                )

        );

        button.setPreferredSize(

                new Dimension(

                        440,

                        46

                )

        );

        return button;

    }

    private String[] categoryValues() {

        return new String[]{

                "Select category",

                "Education",

                "Health",

                "Environment",

                "Community Development"

        };

    }

    private String emptyToNull(

            String value

    ) {

        return value == null

                || value.trim().isEmpty()

                ? null

                : value.trim();

    }

    // =========================================================

    // ROUNDED BUTTON

    // =========================================================

    private static class RoundedButton

            extends JButton {

        private final int arc;

        RoundedButton(

                String text,

                int arc

        ) {

            super(text);

            this.arc =

                    arc;

            setOpaque(

                    false

            );

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {

                @Override

                public void mouseEntered(java.awt.event.MouseEvent e) {

                    repaint();

                }

                @Override

                public void mouseExited(java.awt.event.MouseEvent e) {

                    repaint();

                }

            });

        }

        @Override

        protected void paintComponent(

                Graphics g

        ) {

            Graphics2D g2 =

                    (Graphics2D)

                            g.create();

            g2.setRenderingHint(

                    RenderingHints.KEY_ANTIALIASING,

                    RenderingHints.VALUE_ANTIALIAS_ON

            );

            g2.setColor(

                    getModel().isRollover() ? getBackground().darker() : getBackground()

            );

            g2.fillRoundRect(

                    0,

                    0,

                    getWidth(),

                    getHeight(),

                    arc,

                    arc

            );

            g2.dispose();

            super.paintComponent(

                    g

            );

        }

    }

}