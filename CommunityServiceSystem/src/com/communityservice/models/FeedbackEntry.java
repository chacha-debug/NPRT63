/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDateTime;

public class FeedbackEntry {

    private int feedback_id;
    private int volunteer_id;
    private int opportunity_id;
    private int rating;
    private String comment;
    private LocalDateTime submission_date;

    public FeedbackEntry() {
    }

    public FeedbackEntry(int feedback_id, int volunteer_id, int opportunity_id,
                         int rating, String comment,
                         LocalDateTime submission_date) {

        this.feedback_id = feedback_id;
        this.volunteer_id = volunteer_id;
        this.opportunity_id = opportunity_id;
        this.rating = rating;
        this.comment = comment;
        this.submission_date = submission_date;
    }

    public int getFeedbackID() {
        return feedback_id;
    }

    public void setFeedbackID(int feedback_id) {
        this.feedback_id = feedback_id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getSubmissionDate() {
        return submission_date;
    }

    public void setSubmissionDate(LocalDateTime submission_date) {
        this.submission_date = submission_date;
    }
}