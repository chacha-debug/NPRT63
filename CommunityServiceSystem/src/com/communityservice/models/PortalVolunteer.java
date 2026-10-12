/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class PortalVolunteer {
    private int volunteer_id;
private int user_id;
private BigDecimal total_hours;
private String preferences;
private String academic_year;
private LocalDate date_of_birth;
private String phone_number;
private int completed_events_count;
public PortalVolunteer() {
}

public PortalVolunteer(int volunteer_id, int user_id,
                 BigDecimal total_hours, String preferences,
                 String academic_year, LocalDate date_of_birth,
                 String phone_number, int completed_events_count) {

    this.volunteer_id = volunteer_id;
    this.user_id = user_id;
    this.total_hours = total_hours;
    this.preferences = preferences;
    this.academic_year = academic_year;
    this.date_of_birth = date_of_birth;
    this.phone_number = phone_number;
    this.completed_events_count = completed_events_count;
}

    public int getVolunteerID() {
        return volunteer_id;
    }

    public void setVolunteerID(int volunteer_id) {
        this.volunteer_id = volunteer_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getTotalHours() {
        return total_hours;
    }

    public void setTotalHours(BigDecimal total_hours) {
        this.total_hours = total_hours;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getAcademicYear() {
        return academic_year;
    }

    public void setAcademicYear(String academic_year) {
        this.academic_year = academic_year;
    }

    public LocalDate getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getCompletedEventsCount() {
        return completed_events_count;
    }

    public void setCompletedEventsCount(int completed_events_count) {
        this.completed_events_count = completed_events_count;
    }
    
}
