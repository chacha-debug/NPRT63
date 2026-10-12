package com.communityservice.models;

public class NGO {
    private int ngoId;
    private int userId;
    private String orgName;
    private String organizationName;
    private String registrationNumber;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private String phone;
    private String address;
    private boolean verifiedStatus;
    private String verificationStatus;
    private String website;

    // Default constructor
    public NGO() {}

    // Parameterized constructor
    public NGO(int userId, String orgName, String contactPerson, String contactPhone) {
        this.userId = userId;
        this.orgName = orgName;
        this.organizationName = orgName;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.phone = contactPhone;
        this.verifiedStatus = false;
    }

    // Getters and Setters
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { 
        this.orgName = orgName; 
        if (this.organizationName == null) this.organizationName = orgName;
    }

    public String getOrganizationName() { return organizationName != null ? organizationName : orgName; }
    public void setOrganizationName(String organizationName) { 
        this.organizationName = organizationName; 
        this.orgName = organizationName;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { 
        this.contactPhone = contactPhone; 
        if (this.phone == null) this.phone = contactPhone;
    }

    public String getPhone() { return phone != null ? phone : contactPhone; }
    public void setPhone(String phone) { 
        this.phone = phone; 
        this.contactPhone = phone;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isVerifiedStatus() { return verifiedStatus; }
    public void setVerifiedStatus(boolean verifiedStatus) { this.verifiedStatus = verifiedStatus; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}