/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.communityservice.models;

import java.time.LocalDateTime;

public class AuditLog {

    private int audit_log_id;
    private int user_id;
    private String action;
    private String entity_type;
    private int entity_id;
    private String ip_address;
    private LocalDateTime timestamp;
    private String details;
    private String status;

    public AuditLog() {
    }

    public AuditLog(int audit_log_id, int user_id, String action,
                    String entity_type, int entity_id,
                    String ip_address, LocalDateTime timestamp,
                    String details, String status) {

        this.audit_log_id = audit_log_id;
        this.user_id = user_id;
        this.action = action;
        this.entity_type = entity_type;
        this.entity_id = entity_id;
        this.ip_address = ip_address;
        this.timestamp = timestamp;
        this.details = details;
        this.status = status;
    }

    public int getLogID() {
        return audit_log_id;
    }

    public void setLogID(int audit_log_id) {
        this.audit_log_id = audit_log_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entity_type;
    }

    public void setEntityType(String entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntityID() {
        return entity_id;
    }

    public void setEntityID(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getIpAddress() {
        return ip_address;
    }

    public void setIpAddress(String ip_address) {
        this.ip_address = ip_address;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}