/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class PortalUser {

    private int user_id;
    private String full_name;
    private String email;
    private String password_hash;
    private String role;
    private String profilePic;
    private LocalDateTime dateRegistered;
    private LocalDateTime lastLogin;
    private boolean is_active;

    public PortalUser() {
    }

    public PortalUser(int user_id, String full_name, String email,
                String password_hash, String role, String profilePic,
                LocalDateTime dateRegistered, LocalDateTime lastLogin,
                boolean is_active) {

        this.user_id = user_id;
        this.full_name = full_name;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.profilePic = profilePic;
        this.dateRegistered = dateRegistered;
        this.lastLogin = lastLogin;
        this.is_active = is_active;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
public boolean is_active() {
    return is_active;
}

public void setActive(boolean active) {
    this.is_active = active;
}
}