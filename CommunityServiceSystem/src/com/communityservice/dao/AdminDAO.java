package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.utils.AppLogger;
import com.communityservice.models.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO implements IAdminDAO {

    @Override
    public boolean createAdminProfile(Admin admin) {
        String query = "INSERT INTO administrators (user_id, permissions, department, employee_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, admin.getUserId());
            pstmt.setString(2, admin.getPermissions());
            pstmt.setString(3, admin.getDepartment());
            pstmt.setString(4, admin.getEmployeeNumber());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return false;
    }

    @Override
    public Admin getAdminById(int adminId) {
        String query = "SELECT * FROM administrators WHERE admin_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, adminId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdminFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return null;
    }

    @Override
    public Admin getAdminByUserId(int userId) {
        String query = "SELECT * FROM administrators WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdminFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return null;
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM administrators";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return admins;
    }

    @Override
    public boolean updateAdminProfile(Admin admin) {
        String query = "UPDATE administrators SET permissions = ?, department = ?, employee_number = ? WHERE admin_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, admin.getPermissions());
            pstmt.setString(2, admin.getDepartment());
            pstmt.setString(3, admin.getEmployeeNumber());
            pstmt.setInt(4, admin.getAdminId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return false;
    }

    @Override
    public boolean deleteAdminProfile(int adminId) {
        String query = "DELETE FROM administrators WHERE admin_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, adminId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in AdminDAO", e);
        }
        return false;
    }

    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setUserId(rs.getInt("user_id"));
        admin.setPermissions(rs.getString("permissions"));
        admin.setDepartment(rs.getString("department"));
        admin.setEmployeeNumber(rs.getString("employee_number"));
        return admin;
    }
}
