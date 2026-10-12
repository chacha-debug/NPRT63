/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.models;

/**
 *
 * @author Admin
 */
public class UserProfile {
    
    private int volunteer_id;
    private int user_id;
    private String bio;
    private String interests;
    private String availability;
    private String location;
    private String emergency_contact_name;
    
    
    public UserProfile() {
}
  public UserProfile(int volunteer_id, int user_id, String bio,
                   String interests, String availability,
                   String location, String emergency_contact_name) {

    this.volunteer_id = volunteer_id;
    this.user_id = user_id;
    this.bio = bio;
    this.interests = interests;
    this.availability = availability;
    this.location = location;
    this.emergency_contact_name = emergency_contact_name;
}  

    public int getProfileID() {
        return volunteer_id;
    }

    public void setProfileID(int volunteer_id) {
        this.volunteer_id = volunteer_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmergencyContact() {
        return emergency_contact_name;
    }

    public void setEmergencyContact(String emergency_contact_name) {
        this.emergency_contact_name = emergency_contact_name;
    }
}
