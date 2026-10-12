package com.communityservice.ui;

import java.awt.BorderLayout;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Minimal, dependency-free stand-in for com.toedter.calendar.JDateChooser
 * (the JCalendar library isn't available as a dependency in this project).
 * Supports exactly the API this project uses: setDateFormatString(),
 * getDate() (returns null until a date has actually been chosen), and
 * setDate(). Backed by a plain JSpinner, so no external jar is required.
 */
public class SimpleDateChooser extends JPanel {

    private final JSpinner spinner;
    private boolean hasDate = false;

    public SimpleDateChooser() {
        super(new BorderLayout());
        setOpaque(false);
        spinner = new JSpinner(new SpinnerDateModel());
        setDateFormatString("yyyy-MM-dd");
        spinner.addChangeListener(e -> hasDate = true);
        add(spinner, BorderLayout.CENTER);
    }

    public void setDateFormatString(String pattern) {
        spinner.setEditor(new JSpinner.DateEditor(spinner, pattern));
    }

    /** Returns the selected date, or null if the user hasn't picked one yet. */
    public Date getDate() {
        return hasDate ? (Date) spinner.getValue() : null;
    }

    public void setDate(Date date) {
        if (date != null) {
            spinner.setValue(date);
            hasDate = true;
        } else {
            hasDate = false;
        }
    }

    public void addChangeListener(javax.swing.event.ChangeListener listener) {
        spinner.addChangeListener(listener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        spinner.setEnabled(enabled);
    }
}
