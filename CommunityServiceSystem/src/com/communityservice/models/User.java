package com.communityservice.models;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String passwordHash;
    private String role;
    private String profilePic;
    private Timestamp dateRegistered;
    private Timestamp lastLogin;
    private boolean isActive;
    private boolean isVerified;
    private String verificationCode;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    private boolean isDeleted;
    private Timestamp deletedAt;
    private Integer deletedBy;
    private String deletionReason;

    // Constructors
    public User() {}

    public User(String fullName, String email, String passwordHash, String role) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.password = passwordHash;
        this.role = role;
        this.isActive = true;
        this.isVerified = false;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
        if (this.password == null) this.password = passwordHash;
    }

    public String getPassword() { return password != null ? password : passwordHash; }
    public void setPassword(String password) { 
        this.password = password; 
        this.passwordHash = password;
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
    
    public Timestamp getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(Timestamp dateRegistered) { this.dateRegistered = dateRegistered; }
    
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    
    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    
    public Timestamp getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(Timestamp resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }

    /** True once an administrator has removed this account (soft delete: the row is kept, but sign-in is blocked). */
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }

    public Integer getDeletedBy() { return deletedBy; }
    public void setDeletedBy(Integer deletedBy) { this.deletedBy = deletedBy; }

    public String getDeletionReason() { return deletionReason; }
    public void setDeletionReason(String deletionReason) { this.deletionReason = deletionReason; }
}