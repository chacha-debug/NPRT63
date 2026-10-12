/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.FeedbackEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackEntryDAO {

    public boolean addFeedbackEntry(FeedbackEntry feedback) {

        String sql = "INSERT INTO feedback_entries "
                   + "(volunteer_id, opportunity_id, rating, comment) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, feedback.getVolunteerID());
            statement.setInt(2, feedback.getOppID());
            statement.setInt(3, feedback.getRating());
            statement.setString(4, feedback.getComment());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        feedback.setFeedbackID(keys.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error adding feedback: "
                    + e.getMessage());
        }

        return false;
    }

    public FeedbackEntry getFeedbackEntryByID(int feedback_id) {

        String sql = "SELECT * FROM feedback_entries "
                   + "WHERE feedback_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedback_id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                FeedbackEntry feedback = new FeedbackEntry();

                feedback.setFeedbackID(rs.getInt("feedback_id"));
                feedback.setVolunteerID(rs.getInt("volunteer_id"));
                feedback.setOppID(rs.getInt("opportunity_id"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setComment(rs.getString("comment"));

                Timestamp timestamp =
                        rs.getTimestamp("submission_date");

                if (timestamp != null) {
                    feedback.setSubmissionDate(
                            timestamp.toLocalDateTime()
                    );
                }

                return feedback;
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error finding feedback: "
                    + e.getMessage());
        }

        return null;
    }

    public boolean updateFeedbackEntry(FeedbackEntry feedback) {

        String sql = "UPDATE feedback_entries SET "
                   + "rating = ?, comment = ? "
                   + "WHERE feedback_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedback.getRating());
            statement.setString(2, feedback.getComment());
            statement.setInt(3, feedback.getFeedbackID());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error updating feedback: "
                    + e.getMessage());
        }

        return false;
    }

    public boolean deleteFeedbackEntry(int feedback_id) {

        String sql = "DELETE FROM feedback_entries "
                   + "WHERE feedback_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedback_id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error deleting feedback: "
                    + e.getMessage());
        }

        return false;
    }

    public List<FeedbackEntry> getAllFeedbackEntries() {

        List<FeedbackEntry> feedbackList = new ArrayList<>();

        String sql = "SELECT * FROM feedback_entries";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                FeedbackEntry feedback = new FeedbackEntry();

                feedback.setFeedbackID(rs.getInt("feedback_id"));
                feedback.setVolunteerID(rs.getInt("volunteer_id"));
                feedback.setOppID(rs.getInt("opportunity_id"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setComment(rs.getString("comment"));

                Timestamp timestamp =
                        rs.getTimestamp("submission_date");

                if (timestamp != null) {
                    feedback.setSubmissionDate(
                            timestamp.toLocalDateTime()
                    );
                }

                feedbackList.add(feedback);
            }

        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Error getting feedback entries: "
                    + e.getMessage());
        }

        return feedbackList;
    }
}
