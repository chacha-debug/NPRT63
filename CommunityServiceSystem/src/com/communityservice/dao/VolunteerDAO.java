package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.utils.AppLogger;
import com.communityservice.models.Volunteer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO implements IVolunteerDAO {

    @Override
    public boolean createVolunteerProfile(Volunteer volunteer) {
        String query = "INSERT INTO volunteers (user_id, phone_number, location, skills, interests, availability, bio, date_of_birth, race, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, volunteer.getUserId());
            pstmt.setString(2, volunteer.getPhoneNumber());
            pstmt.setString(3, volunteer.getLocation());
            pstmt.setString(4, volunteer.getSkills());
            pstmt.setString(5, volunteer.getInterests());
            pstmt.setString(6, volunteer.getAvailability());
            pstmt.setString(7, volunteer.getBio());
            pstmt.setDate(8, volunteer.getDateOfBirth());
            pstmt.setString(9, volunteer.getRace());
            pstmt.setString(10, volunteer.getGender());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return false;
    }

    @Override
    public Volunteer getVolunteerById(int volunteerId) {
        String query = "SELECT * FROM volunteers WHERE volunteer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, volunteerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractVolunteerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return null;
    }

    @Override
    public Volunteer getVolunteerByUserId(int userId) {
        String query = "SELECT * FROM volunteers WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractVolunteerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return null;
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        List<Volunteer> volunteers = new ArrayList<>();
        String query = "SELECT * FROM volunteers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                volunteers.add(extractVolunteerFromResultSet(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return volunteers;
    }

    @Override
    public boolean updateVolunteerProfile(Volunteer volunteer) {
        String query = "UPDATE volunteers SET phone_number = ?, location = ?, skills = ?, interests = ?, availability = ?, bio = ? WHERE volunteer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, volunteer.getPhoneNumber());
            pstmt.setString(2, volunteer.getLocation());
            pstmt.setString(3, volunteer.getSkills());
            pstmt.setString(4, volunteer.getInterests());
            pstmt.setString(5, volunteer.getAvailability());
            pstmt.setString(6, volunteer.getBio());
            pstmt.setInt(7, volunteer.getVolunteerId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return false;
    }

    @Override
    public boolean deleteVolunteerProfile(int volunteerId) {
        String query = "DELETE FROM volunteers WHERE volunteer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, volunteerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return false;
    }

    @Override
    public List<Volunteer> getVolunteersBySkills(String skills) {
        List<Volunteer> volunteers = new ArrayList<>();
        String query = "SELECT * FROM volunteers WHERE skills LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + skills + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    volunteers.add(extractVolunteerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return volunteers;
    }

    @Override
    public List<Volunteer> getVolunteersByAvailability(String availability) {
        List<Volunteer> volunteers = new ArrayList<>();
        String query = "SELECT * FROM volunteers WHERE availability = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, availability);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    volunteers.add(extractVolunteerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Database operation failed in VolunteerDAO", e);
        }
        return volunteers;
    }

    private Volunteer extractVolunteerFromResultSet(ResultSet rs) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerId(rs.getInt("volunteer_id"));
        volunteer.setUserId(rs.getInt("user_id"));
        volunteer.setTotalHours(rs.getDouble("total_hours"));
        volunteer.setPreferences(rs.getString("preferences"));
        volunteer.setAcademicYear(rs.getString("academic_year"));
        volunteer.setDateOfBirth(rs.getDate("date_of_birth"));
        volunteer.setRace(rs.getString("race"));
        volunteer.setGender(rs.getString("gender"));
        volunteer.setPhoneNumber(rs.getString("phone_number"));
        volunteer.setCompletedEventsCount(rs.getInt("completed_events_count"));
        volunteer.setBio(rs.getString("bio"));
        volunteer.setSkills(rs.getString("skills"));
        volunteer.setInterests(rs.getString("interests"));
        volunteer.setAvailability(rs.getString("availability"));
        volunteer.setLocation(rs.getString("location"));
        volunteer.setEmergencyContactName(rs.getString("emergency_contact_name"));
        volunteer.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        return volunteer;
    }
}
