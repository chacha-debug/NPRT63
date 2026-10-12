/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
  
package com.communityservice.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AttendanceRecord {

    private int attendanceID;
    private int volunteer_id;
    private int opportunity_id;
    private LocalDateTime check_in_time;
    private String status;
    private BigDecimal hours_awarded;
    private Integer reviewed_by;

    public AttendanceRecord() {
    }

    public AttendanceRecord(
            int attendanceID,
            int volunteer_id,
            int opportunity_id,
            LocalDateTime check_in_time,
            String status,
            BigDecimal hours_awarded,
            Integer reviewed_by) {

        this.attendanceID = attendanceID;
        this.volunteer_id = volunteer_id;
        this.opportunity_id = opportunity_id;
        this.check_in_time = check_in_time;
        this.status = status;
        this.hours_awarded = hours_awarded;
        this.reviewed_by = reviewed_by;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
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

    public LocalDateTime getCheckInTime() {
        return check_in_time;
    }

    public void setCheckInTime(LocalDateTime check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getHoursAwarded() {
        return hours_awarded;
    }

    public void setHoursAwarded(BigDecimal hours_awarded) {
        this.hours_awarded = hours_awarded;
    }

    public Integer getReviewedBy() {
        return reviewed_by;
    }

    public void setReviewedBy(Integer reviewed_by) {
        this.reviewed_by = reviewed_by;
    }
    
}