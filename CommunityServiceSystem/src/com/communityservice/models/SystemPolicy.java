/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SystemPolicy {

    private int system_policy_id;
    private String policy_title;
    private String policy_content;
    private String version;
    private LocalDate effective_date;
    private int last_updated_by;
    private LocalDateTime last_update_date;
    private boolean is_active;

    public SystemPolicy() {
    }

    public SystemPolicy(int system_policy_id, String policy_title,
                        String policy_content, String version,
                        LocalDate effective_date,
                        int last_updated_by,
                        LocalDateTime last_update_date,
                        boolean is_active) {

        this.system_policy_id = system_policy_id;
        this.policy_title = policy_title;
        this.policy_content = policy_content;
        this.version = version;
        this.effective_date = effective_date;
        this.last_updated_by = last_updated_by;
        this.last_update_date = last_update_date;
        this.is_active = is_active;
    }

    public int getPolicyID() {
        return system_policy_id;
    }

    public void setPolicyID(int system_policy_id) {
        this.system_policy_id = system_policy_id;
    }

    public String getPolicyTitle() {
        return policy_title;
    }

    public void setPolicyTitle(String policy_title) {
        this.policy_title = policy_title;
    }

    public String getPolicyContent() {
        return policy_content;
    }

    public void setPolicyContent(String policy_content) {
        this.policy_content = policy_content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getEffectiveDate() {
        return effective_date;
    }

    public void setEffectiveDate(LocalDate effective_date) {
        this.effective_date = effective_date;
    }

    public int getLastUpdatedBy() {
        return last_updated_by;
    }

    public void setLastUpdatedBy(int last_updated_by) {
        this.last_updated_by = last_updated_by;
    }

    public LocalDateTime getLastUpdateDate() {
        return last_update_date;
    }

    public void setLastUpdateDate(LocalDateTime last_update_date) {
        this.last_update_date = last_update_date;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setActive(boolean active) {
        is_active = active;
    }
}