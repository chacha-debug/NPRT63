/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Admin
 */
public class VolunteerOpportunity {

    private int opportunity_id;
    private int user_id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;
    private String location;
    private int max_slots;
    private int current_slots;
    private String category;
    private String status;
    private String required_skills;

    public VolunteerOpportunity() {
    }

    public VolunteerOpportunity(
            int opportunity_id,
            int user_id,
            String title,
            String description,
            LocalDate date,
            LocalTime start_time,
            LocalTime end_time,
            String location,
            int max_slots,
            int current_slots,
            String category,
            String status,
            String required_skills) {

        this.opportunity_id = opportunity_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.max_slots = max_slots;
        this.current_slots = current_slots;
        this.category = category;
        this.status = status;
        this.required_skills = required_skills;
    }

    public int getOppID() {
        return opportunity_id;
    }

    public void setOppID(int opportunity_id) {
        this.opportunity_id = opportunity_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxSlots() {
        return max_slots;
    }

    public void setMaxSlots(int max_slots) {
        this.max_slots = max_slots;
    }

    public int getCurrentSlots() {
        return current_slots;
    }

    public void setCurrentSlots(int current_slots) {
        this.current_slots = current_slots;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequiredSkills() {
        return required_skills;
    }

    public void setRequiredSkills(String required_skills) {
        this.required_skills = required_skills;
    }

    public int getAvailableSlots() {
        return max_slots - current_slots;
    }
}
