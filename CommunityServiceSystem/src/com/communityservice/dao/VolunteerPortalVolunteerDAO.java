/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.PortalVolunteer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VolunteerPortalVolunteerDAO {

    
    public PortalVolunteer addVolunteer(PortalVolunteer volunteers) {

        String sql = "INSERT INTO volunteers "
                   + "(user_id, total_hours, preferences, academic_year, "
                   + "date_of_birth, phone_number, completed_events_count) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    volunteers.getUserID()
            );

            statement.setBigDecimal(
                    2,
                    volunteers.getTotalHours()
            );

            statement.setString(
                    3,
                    volunteers.getPreferences()
            );

            statement.setString(
                    4,
                    volunteers.getAcademicYear()
            );

            if (volunteers.getDateOfBirth() != null) {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                volunteers.getDateOfBirth()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            statement.setString(
                    6,
                    volunteers.getPhoneNumber()
            );

            statement.setInt(
                    7,
                    volunteers.getCompletedEventsCount()
            );

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet generatedKeys =
                        statement.getGeneratedKeys();

                if (generatedKeys.next()) {

                    int generatedID =
                            generatedKeys.getInt(1);

                    volunteers.setVolunteerID(
                            generatedID
                    );

                    return volunteers;
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding volunteers: "
                    + e.getMessage()
            );
        }

        return null;
    }


   

    public PortalVolunteer getVolunteerByID(int volunteer_id) {

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

                PortalVolunteer volunteers =
                        new PortalVolunteer();

                volunteers.setVolunteerID(
                        resultSet.getInt(
                                "volunteer_id"
                        )
                );

                volunteers.setUserID(
                        resultSet.getInt(
                                "user_id"
                        )
                );

                volunteers.setTotalHours(
                        resultSet.getBigDecimal(
                                "total_hours"
                        )
                );

                volunteers.setPreferences(
                        resultSet.getString(
                                "preferences"
                        )
                );

                volunteers.setAcademicYear(
                        resultSet.getString(
                                "academic_year"
                        )
                );

                if (resultSet.getDate(
                        "date_of_birth"
                ) != null) {

                    volunteers.setDateOfBirth(
                            resultSet
                                    .getDate(
                                            "date_of_birth"
                                    )
                                    .toLocalDate()
                    );
                }

                volunteers.setPhoneNumber(
                        resultSet.getString(
                                "phone_number"
                        )
                );

                volunteers.setCompletedEventsCount(
                        resultSet.getInt(
                                "completed_events_count"
                        )
                );

                return volunteers;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding volunteers: "
                    + e.getMessage()
            );
        }

        return null;
    }


    

    public boolean updateVolunteer(
            PortalVolunteer volunteers) {

        String sql =
                "UPDATE volunteers SET "
                + "user_id = ?, "
                + "total_hours = ?, "
                + "preferences = ?, "
                + "academic_year = ?, "
                + "date_of_birth = ?, "
                + "phone_number = ?, "
                + "completed_events_count = ? "
                + "WHERE volunteer_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    volunteers.getUserID()
            );

            statement.setBigDecimal(
                    2,
                    volunteers.getTotalHours()
            );

            statement.setString(
                    3,
                    volunteers.getPreferences()
            );

            statement.setString(
                    4,
                    volunteers.getAcademicYear()
            );

            if (volunteers.getDateOfBirth() != null) {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                volunteers.getDateOfBirth()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }

            statement.setString(
                    6,
                    volunteers.getPhoneNumber()
            );

            statement.setInt(
                    7,
                    volunteers.getCompletedEventsCount()
            );

            statement.setInt(
                    8,
                    volunteers.getVolunteerID()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating volunteers: "
                    + e.getMessage()
            );

            return false;
        }
    }


   
    public boolean deleteVolunteer(int volunteer_id) {

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
                    "Error deleting volunteers: "
                    + e.getMessage()
            );

            return false;
        }
    }
    public PortalVolunteer getVolunteerByUserID(int user_id) {

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

            PortalVolunteer volunteers =
                    new PortalVolunteer();

            volunteers.setVolunteerID(
                    resultSet.getInt("volunteer_id")
            );

            volunteers.setUserID(
                    resultSet.getInt("user_id")
            );

            volunteers.setTotalHours(
                    resultSet.getBigDecimal("total_hours")
            );

            volunteers.setPreferences(
                    resultSet.getString("preferences")
            );

            volunteers.setAcademicYear(
                    resultSet.getString("academic_year")
            );

            if (resultSet.getDate("date_of_birth") != null) {

                volunteers.setDateOfBirth(
                        resultSet.getDate("date_of_birth")
                                .toLocalDate()
                );
            }

            volunteers.setPhoneNumber(
                    resultSet.getString("phone_number")
            );

            volunteers.setCompletedEventsCount(
                    resultSet.getInt("completed_events_count")
            );

            return volunteers;
        }

    } catch (SQLException e) {

        com.communityservice.utils.AppLogger.error(
                "Error finding volunteers by users ID: "
                + e.getMessage()
        );
    }

    return null;
}
 public List<PortalVolunteer> getAllVolunteers() {

    List<PortalVolunteer> volunteers =
            new ArrayList<>();

    String sql =
            "SELECT * FROM volunteers "
            + "ORDER BY total_hours DESC";

    try (
            Connection connection =
                    DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
    ) {

        ResultSet resultSet =
                statement.executeQuery();

        while (resultSet.next()) {

            PortalVolunteer vol =
                    new PortalVolunteer();

            vol.setVolunteerID(
                    resultSet.getInt(
                            "volunteer_id"
                    )
            );

            vol.setUserID(
                    resultSet.getInt(
                            "user_id"
                    )
            );

            vol.setTotalHours(
                    resultSet.getBigDecimal(
                            "total_hours"
                    )
            );

            vol.setCompletedEventsCount(
                    resultSet.getInt(
                            "completed_events_count"
                    )
            );

         

            volunteers.add(
                    vol
            );
        }

    } catch (SQLException e) {

        com.communityservice.utils.AppLogger.error(
                "Error getting volunteers: "
                + e.getMessage()
        );
    }

    return volunteers;
}
}