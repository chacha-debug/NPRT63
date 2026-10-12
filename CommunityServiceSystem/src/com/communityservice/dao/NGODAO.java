package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.utils.AppLogger;
import com.communityservice.models.NGO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NGODAO implements INGODAO {

    @Override
    public boolean createNGOProfile(NGO ngo) {
        String query = "INSERT INTO ngos (user_id, org_name, registration_number, contact_person, contact_phone, verified_status, website) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ngo.getUserId());
            pstmt.setString(2, ngo.getOrgName());
            pstmt.setString(3, ngo.getRegistrationNumber());
            pstmt.setString(4, ngo.getContactPerson());
            pstmt.setString(5, ngo.getContactPhone());
            pstmt.setBoolean(6, ngo.isVerifiedStatus());
            pstmt.setString(7, ngo.getWebsite());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return false;
    }

    @Override
    public NGO getNGOById(int ngoId) {
        String query = "SELECT * FROM ngos WHERE ngo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ngoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractNGOFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return null;
    }

    @Override
    public NGO getNGOByUserId(int userId) {
        String query = "SELECT * FROM ngos WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractNGOFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return null;
    }

    @Override
    public List<NGO> getAllNGOs() {
        List<NGO> ngos = new ArrayList<>();
        String query = "SELECT * FROM ngos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ngos.add(extractNGOFromResultSet(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return ngos;
    }

    @Override
    public boolean updateNGOProfile(NGO ngo) {
        String query = "UPDATE ngos SET org_name = ?, registration_number = ?, contact_person = ?, contact_phone = ?, verified_status = ?, website = ? WHERE ngo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, ngo.getOrgName());
            pstmt.setString(2, ngo.getRegistrationNumber());
            pstmt.setString(3, ngo.getContactPerson());
            pstmt.setString(4, ngo.getContactPhone());
            pstmt.setBoolean(5, ngo.isVerifiedStatus());
            pstmt.setString(6, ngo.getWebsite());
            pstmt.setInt(7, ngo.getNgoId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return false;
    }

    @Override
    public boolean deleteNGOProfile(int ngoId) {
        String query = "DELETE FROM ngos WHERE ngo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ngoId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return false;
    }

    @Override
    public List<NGO> getPendingVerificationNGOs() {
        List<NGO> ngos = new ArrayList<>();
        String query = "SELECT n.* FROM ngos n JOIN users u ON u.user_id = n.user_id " +
                "WHERE n.verified_status = 0 AND u.is_verified = 1 " +
                "AND COALESCE(u.is_active, 1) = 1 AND COALESCE(u.is_deleted, 0) = 0 " +
                "ORDER BY n.ngo_id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ngos.add(extractNGOFromResultSet(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return ngos;
    }

    @Override
    public boolean verifyNGO(int ngoId) {
        return updateVerificationStatus(ngoId, true);
    }

    @Override
    public boolean updateVerificationStatus(int ngoId, boolean verified) {
        String query = "UPDATE ngos SET verified_status = ? WHERE ngo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, verified);
            pstmt.setInt(2, ngoId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in NGODAO", e);
        }
        return false;
    }

    @Override
    public boolean updateRegistrationDetails(int userId, String operatingRegion, String orgScale, String mission, String focusAreas, String preferredSkills) {
        String sql = "UPDATE ngos SET operating_region=?, org_scale=?, mission_statement=?, focus_areas=?, preferred_skills=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, operatingRegion); ps.setString(2, orgScale); ps.setString(3, mission); ps.setString(4, focusAreas); ps.setString(5, preferredSkills); ps.setInt(6, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { AppLogger.error("Failed to save NGO registration details", e); return false; }
    }

    private NGO extractNGOFromResultSet(ResultSet rs) throws SQLException {
        NGO ngo = new NGO();
        ngo.setNgoId(rs.getInt("ngo_id"));
        ngo.setUserId(rs.getInt("user_id"));
        ngo.setOrgName(rs.getString("org_name"));
        ngo.setRegistrationNumber(rs.getString("registration_number"));
        ngo.setContactPerson(rs.getString("contact_person"));
        ngo.setContactPhone(rs.getString("contact_phone"));
        ngo.setVerifiedStatus(rs.getBoolean("verified_status"));
        ngo.setWebsite(rs.getString("website"));
        return ngo;
    }
}
