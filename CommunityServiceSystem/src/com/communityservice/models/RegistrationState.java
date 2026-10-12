package com.communityservice.models;

/** Central state for the public registration wizard. */
public final class RegistrationState {
    private String role = "Volunteer";
    private String fullName = "";
    private String email = "";
    private String password = "";
    private String organizationName = "";
    private String registrationNumber = "";
    private String race = "";
    private String gender = "";
    private String dateOfBirth = "";

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role == null ? "Volunteer" : role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName == null ? "" : fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? "" : email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password == null ? "" : password; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String v) { organizationName = v == null ? "" : v; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String v) { registrationNumber = v == null ? "" : v; }
    public String getRace() { return race; }
    public void setRace(String v) { race = v == null ? "" : v; }
    public String getGender() { return gender; }
    public void setGender(String v) { gender = v == null ? "" : v; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String v) { dateOfBirth = v == null ? "" : v; }
    public void clearSensitive() { password = ""; }
    public void clear() { role="Volunteer"; fullName=""; email=""; password=""; organizationName=""; registrationNumber=""; race=""; gender=""; dateOfBirth=""; }
}
