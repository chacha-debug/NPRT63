/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Report {

    private int reportID;
    private int generated_by;
    private String report_type;
    private String title;
    private LocalDateTime generatedDate;
    private LocalDate start_date;
    private LocalDate end_date;
    private String file_format;
    private String file_url;
    private String data_summary;

    public Report() {
    }

    public Report(int reportID, int generated_by,
                  String report_type, String title,
                  LocalDateTime generatedDate,
                  LocalDate start_date, LocalDate end_date,
                  String file_format, String file_url,
                  String data_summary) {

        this.reportID = reportID;
        this.generated_by = generated_by;
        this.report_type = report_type;
        this.title = title;
        this.generatedDate = generatedDate;
        this.start_date = start_date;
        this.end_date = end_date;
        this.file_format = file_format;
        this.file_url = file_url;
        this.data_summary = data_summary;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getGeneratedBy() {
        return generated_by;
    }

    public void setGeneratedBy(int generated_by) {
        this.generated_by = generated_by;
    }

    public String getReportType() {
        return report_type;
    }

    public void setReportType(String report_type) {
        this.report_type = report_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public void setEndDate(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getFileFormat() {
        return file_format;
    }

    public void setFileFormat(String file_format) {
        this.file_format = file_format;
    }

    public String getFileURL() {
        return file_url;
    }

    public void setFileURL(String file_url) {
        this.file_url = file_url;
    }

    public String getDataSummary() {
        return data_summary;
    }

    public void setDataSummary(String data_summary) {
        this.data_summary = data_summary;
    }
}
