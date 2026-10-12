package com.communityservice.models;

import java.sql.Date;

public class Volunteer {
    private int volunteerId;
    private int userId;
    private double totalHours;
    private String preferences;
    private String academicYear;
    private Date dateOfBirth;
    private String race;
    private String gender;
    private String phoneNumber;
    private String phone;
    private String address;
    private String status;
    private int completedEventsCount;
    private String bio;
    private String skills;
    private String interests;
    private String availability;
    private String location;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Getters and Setters
    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
    
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
        if (this.phone == null) this.phone = phoneNumber;
    }

    public String getPhone() { return phone != null ? phone : phoneNumber; }
    public void setPhone(String phone) { 
        this.phone = phone; 
        this.phoneNumber = phone;
    }

    public String getAddress() { return address != null ? address : location; }
    public void setAddress(String address) { 
        this.address = address; 
        if (this.location == null) this.location = address;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getCompletedEventsCount() { return completedEventsCount; }
    public void setCompletedEventsCount(int completedEventsCount) { this.completedEventsCount = completedEventsCount; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
    
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { 
        this.location = location; 
        if (this.address == null) this.address = location;
    }
    
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
}