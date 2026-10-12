package com.communityservice.models;

public class Admin {
    private int adminId;
    private int userId;
    private String permissions;
    private String department;
    private String employeeNumber;

    public Admin() {}

    public Admin(int userId, String permissions) {
        this.userId = userId;
        this.permissions = permissions;
    }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
}
