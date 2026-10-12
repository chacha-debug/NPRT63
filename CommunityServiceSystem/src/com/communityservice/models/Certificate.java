/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;
import java.time.LocalDate;

/**
 *
 * @author Admin
 */
public class Certificate {
     private int certificate_id;
    private int volunteer_id;
    private int opportunity_id;
    private LocalDate issue_date;
    private String pdf_url;
    private String certification_number;

    public Certificate() {
    }

    public Certificate(int certificate_id, int volunteer_id, int opportunity_id,
                       LocalDate issue_date, String pdf_url,
                       String certification_number) {

        this.certificate_id = certificate_id;
        this.volunteer_id = volunteer_id;
        this.opportunity_id = opportunity_id;
        this.issue_date = issue_date;
        this.pdf_url = pdf_url;
        this.certification_number = certification_number;
    }

    public int getCertID() {
        return certificate_id;
    }

    public void setCertID(int certificate_id) {
        this.certificate_id = certificate_id;
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

    public LocalDate getIssueDate() {
        return issue_date;
    }

    public void setIssueDate(LocalDate issue_date) {
        this.issue_date = issue_date;
    }

    public String getPdfURL() {
        return pdf_url;
    }

    public void setPdfURL(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getCertificationNumber() {
        return certification_number;
    }

    @Override
    public String toString() {
        String number = certification_number == null || certification_number.isBlank()
                ? "Certificate #" + certificate_id : certification_number;
        String date = issue_date == null ? "Date unavailable" : issue_date.toString();
        return number + "  •  Issued " + date;
    }

    public void setCertificationNumber(String certification_number) {
        this.certification_number = certification_number;
    }
    
}
