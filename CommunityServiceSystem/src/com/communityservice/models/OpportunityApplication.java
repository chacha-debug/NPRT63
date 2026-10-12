/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class OpportunityApplication {

    private int application_id;
    private int volunteer_id;
    private int opportunity_id;
    private LocalDateTime application_date;
    private String status;
    private boolean overrideFlag;
    private String rejection_reason;
    private Integer reviewed_by;
    private LocalDateTime reviewed_date;

    private String opportunityTitle;
    private String opportunityDescription;
    private LocalDate opportunityDate;
    private LocalTime start_time;
    private LocalTime end_time;
    private String opportunityLocation;
    private Integer max_slots;
    private Integer current_slots;
    private String category;
    private String opportunityStatus;
    private String required_skills;

    public OpportunityApplication() {
    }

    public OpportunityApplication(
            int application_id,
            int volunteer_id,
            int opportunity_id,
            LocalDateTime application_date,
            String status,
            boolean overrideFlag,
            String rejection_reason,
            Integer reviewed_by,
            LocalDateTime reviewed_date) {

        this.application_id = application_id;
        this.volunteer_id = volunteer_id;
        this.opportunity_id = opportunity_id;
        this.application_date = application_date;
        this.status = status;
        this.overrideFlag = overrideFlag;
        this.rejection_reason = rejection_reason;
        this.reviewed_by = reviewed_by;
        this.reviewed_date = reviewed_date;
    }

    public int getAppID() {
        return application_id;
    }

    public void setAppID(int application_id) {
        this.application_id = application_id;
    }

    public int getVolunteerID() {
        return volunteer_id;
    }

    public void setVolunteerID(int volunteer_id) {
        this.volunteer_id = volunteer_id;
    }

    public int getOppID() {
        return opportunity_id;
    }

    public void setOppID(int opportunity_id) {
        this.opportunity_id = opportunity_id;
    }

    public LocalDateTime getApplicationDate() {
        return application_date;
    }

    public void setApplicationDate(LocalDateTime application_date) {
        this.application_date = application_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOverrideFlag() {
        return overrideFlag;
    }

    public void setOverrideFlag(boolean overrideFlag) {
        this.overrideFlag = overrideFlag;
    }

    public String getRejectionReason() {
        return rejection_reason;
    }

    public void setRejectionReason(String rejection_reason) {
        this.rejection_reason = rejection_reason;
    }

    public Integer getReviewedBy() {
        return reviewed_by;
    }

    public void setReviewedBy(Integer reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

    public LocalDateTime getReviewedDate() {
        return reviewed_date;
    }

    public void setReviewedDate(LocalDateTime reviewed_date) {
        this.reviewed_date = reviewed_date;
    }

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }

    public String getOpportunityDescription() {
        return opportunityDescription;
    }

    public void setOpportunityDescription(String opportunityDescription) {
        this.opportunityDescription = opportunityDescription;
    }

    public LocalDate getOpportunityDate() {
        return opportunityDate;
    }

    public void setOpportunityDate(LocalDate opportunityDate) {
        this.opportunityDate = opportunityDate;
    }

    public LocalTime getStartTime() {
        return start_time;
    }

    public void setStartTime(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEndTime() {
        return end_time;
    }

    public void setEndTime(LocalTime end_time) {
        this.end_time = end_time;
    }

    public String getOpportunityLocation() {
        return opportunityLocation;
    }

    public void setOpportunityLocation(String opportunityLocation) {
        this.opportunityLocation = opportunityLocation;
    }

    public Integer getMaxSlots() {
        return max_slots;
    }

    public void setMaxSlots(Integer max_slots) {
        this.max_slots = max_slots;
    }

    public Integer getCurrentSlots() {
        return current_slots;
    }

    public void setCurrentSlots(Integer current_slots) {
        this.current_slots = current_slots;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpportunityStatus() {
        return opportunityStatus;
    }

    public void setOpportunityStatus(String opportunityStatus) {
        this.opportunityStatus = opportunityStatus;
    }

    public String getRequiredSkills() {
        return required_skills;
    }

    public void setRequiredSkills(String required_skills) {
        this.required_skills = required_skills;
    }
}