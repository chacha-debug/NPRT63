/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class ProfileFieldsDAO {

    
    public UserProfile addUserProfile(UserProfile profile) {

        String sql = "INSERT INTO volunteers "
                   + "(user_id, bio, interests, availability, location, emergency_contact_name) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    profile.getUserID()
            );

            statement.setString(
                    2,
                    profile.getBio()
            );

            statement.setString(
                    3,
                    profile.getInterests()
            );

            statement.setString(
                    4,
                    profile.getAvailability()
            );

            statement.setString(
                    5,
                    profile.getLocation()
            );

            statement.setString(
                    6,
                    profile.getEmergencyContact()
            );

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet generatedKeys =
                        statement.getGeneratedKeys();

                if (generatedKeys.next()) {

                    int generatedID =
                            generatedKeys.getInt(1);

                    profile.setProfileID(
                            generatedID
                    );

                    return profile;
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding users profile: "
                    + e.getMessage()
            );
        }

        return null;
    }


   
    public UserProfile getUserProfileByID(int volunteer_id) {

        String sql =
                "SELECT * FROM volunteers WHERE volunteer_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                UserProfile profile =
                        new UserProfile();

                profile.setProfileID(
                        resultSet.getInt(
                                "volunteer_id"
                        )
                );

                profile.setUserID(
                        resultSet.getInt(
                                "user_id"
                        )
                );

                profile.setBio(
                        resultSet.getString(
                                "bio"
                        )
                );

                profile.setInterests(
                        resultSet.getString(
                                "interests"
                        )
                );

                profile.setAvailability(
                        resultSet.getString(
                                "availability"
                        )
                );

                profile.setLocation(
                        resultSet.getString(
                                "location"
                        )
                );

                profile.setEmergencyContact(
                        resultSet.getString(
                                "emergency_contact_name"
                        )
                );

                return profile;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding users profile: "
                    + e.getMessage()
            );
        }

        return null;
    }


    
    public boolean updateUserProfile(
            UserProfile profile) {

        String sql =
                "UPDATE volunteers SET "
                + "user_id = ?, "
                + "bio = ?, "
                + "interests = ?, "
                + "availability = ?, "
                + "location = ?, "
                + "emergency_contact_name = ? "
                + "WHERE volunteer_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    profile.getUserID()
            );

            statement.setString(
                    2,
                    profile.getBio()
            );

            statement.setString(
                    3,
                    profile.getInterests()
            );

            statement.setString(
                    4,
                    profile.getAvailability()
            );

            statement.setString(
                    5,
                    profile.getLocation()
            );

            statement.setString(
                    6,
                    profile.getEmergencyContact()
            );

            statement.setInt(
                    7,
                    profile.getProfileID()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating users profile: "
                    + e.getMessage()
            );

            return false;
        }
    }



    public boolean deleteUserProfile(int volunteer_id) {

        String sql =
                "DELETE FROM volunteers WHERE volunteer_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            int rowsDeleted =
                    statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting users profile: "
                    + e.getMessage()
            );

            return false;
        }
    }
    public UserProfile getUserProfileByUserID(int user_id) {

    String sql =
            "SELECT * FROM volunteers WHERE user_id = ?";

    try (Connection connection =
                 DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, user_id);

        ResultSet resultSet =
                statement.executeQuery();

        if (resultSet.next()) {

            UserProfile profile =
                    new UserProfile();

            profile.setProfileID(
                    resultSet.getInt("volunteer_id")
            );

            profile.setUserID(
                    resultSet.getInt("user_id")
            );

            profile.setBio(
                    resultSet.getString("bio")
            );

            profile.setInterests(
                    resultSet.getString("interests")
            );

            profile.setAvailability(
                    resultSet.getString("availability")
            );

            profile.setLocation(
                    resultSet.getString("location")
            );

            profile.setEmergencyContact(
                    resultSet.getString("emergency_contact_name")
            );

            return profile;
        }

    } catch (SQLException e) {

        com.communityservice.utils.AppLogger.error(
                "Error finding users profile by users ID: "
                + e.getMessage()
        );
    }

    return null;
}
}
