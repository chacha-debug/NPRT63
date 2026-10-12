/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.VolunteerOpportunity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VolunteerOpportunityDAO {

    

    public VolunteerOpportunity addOpportunity(
            VolunteerOpportunity opportunity) {

        String sql =
                "INSERT INTO opportunities "
                + "(ngo_user_id, title, description, date, start_time, end_time, "
                + "location, max_slots, current_slots, category, status, "
                + "required_skills) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    opportunity.getUserID()
            );

            statement.setString(
                    2,
                    opportunity.getTitle()
            );

            statement.setString(
                    3,
                    opportunity.getDescription()
            );

            if (opportunity.getDate() != null) {

                statement.setDate(
                        4,
                        java.sql.Date.valueOf(
                                opportunity.getDate()
                        )
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.DATE
                );
            }

            if (opportunity.getStartTime() != null) {

                statement.setTime(
                        5,
                        java.sql.Time.valueOf(
                                opportunity.getStartTime()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.TIME
                );
            }

            if (opportunity.getEndTime() != null) {

                statement.setTime(
                        6,
                        java.sql.Time.valueOf(
                                opportunity.getEndTime()
                        )
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.TIME
                );
            }

            statement.setString(
                    7,
                    opportunity.getLocation()
            );

            statement.setInt(
                    8,
                    opportunity.getMaxSlots()
            );

            statement.setInt(
                    9,
                    opportunity.getCurrentSlots()
            );

            statement.setString(
                    10,
                    opportunity.getCategory()
            );

            statement.setString(
                    11,
                    opportunity.getStatus()
            );

            statement.setString(
                    12,
                    opportunity.getRequiredSkills()
            );

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet generatedKeys =
                        statement.getGeneratedKeys();

                if (generatedKeys.next()) {

                    opportunity.setOppID(
                            generatedKeys.getInt(1)
                    );

                    return opportunity;
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding opportunity: "
                    + e.getMessage()
            );
        }

        return null;
    }


    
    public VolunteerOpportunity getOpportunityByID(
            int opportunity_id) {

        String sql =
                "SELECT * FROM opportunities "
                + "WHERE opportunity_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    opportunity_id
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return mapResultSetToOpportunity(
                        resultSet
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding opportunity: "
                    + e.getMessage()
            );
        }

        return null;
    }


    
    public List<VolunteerOpportunity> getAllOpportunities() {

        List<VolunteerOpportunity> opportunities =
                new ArrayList<>();

        String sql =
                "SELECT * FROM opportunities "
                + "ORDER BY date ASC, start_time ASC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                opportunities.add(
                        mapResultSetToOpportunity(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting opportunities: "
                    + e.getMessage()
            );
        }

        return opportunities;
    }


   

    public List<VolunteerOpportunity>
            getOpenOpportunities() {

        List<VolunteerOpportunity> opportunities =
                new ArrayList<>();

        String sql =
                "SELECT * FROM opportunities "
                + "WHERE status = 'Open' "
                + "AND current_slots < max_slots "
                + "ORDER BY date ASC, start_time ASC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                opportunities.add(
                        mapResultSetToOpportunity(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting open opportunities: "
                    + e.getMessage()
            );
        }

        return opportunities;
    }


    
    public List<VolunteerOpportunity>
            getOpportunitiesByCategory(
                    String category) {

        List<VolunteerOpportunity> opportunities =
                new ArrayList<>();

        String sql =
                "SELECT * FROM opportunities "
                + "WHERE category = ? "
                + "ORDER BY date ASC, start_time ASC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    category
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                opportunities.add(
                        mapResultSetToOpportunity(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting opportunities by category: "
                    + e.getMessage()
            );
        }

        return opportunities;
    }


    

    public boolean updateOpportunity(
            VolunteerOpportunity opportunity) {

        String sql =
                "UPDATE opportunities SET "
                + "ngo_user_id = ?, "
                + "title = ?, "
                + "description = ?, "
                + "date = ?, "
                + "start_time = ?, "
                + "end_time = ?, "
                + "location = ?, "
                + "max_slots = ?, "
                + "current_slots = ?, "
                + "category = ?, "
                + "status = ?, "
                + "required_skills = ? "
                + "WHERE opportunity_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    opportunity.getUserID()
            );

            statement.setString(
                    2,
                    opportunity.getTitle()
            );

            statement.setString(
                    3,
                    opportunity.getDescription()
            );

            if (opportunity.getDate() != null) {

                statement.setDate(
                        4,
                        java.sql.Date.valueOf(
                                opportunity.getDate()
                        )
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.DATE
                );
            }

            if (opportunity.getStartTime() != null) {

                statement.setTime(
                        5,
                        java.sql.Time.valueOf(
                                opportunity.getStartTime()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.TIME
                );
            }

            if (opportunity.getEndTime() != null) {

                statement.setTime(
                        6,
                        java.sql.Time.valueOf(
                                opportunity.getEndTime()
                        )
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.TIME
                );
            }

            statement.setString(
                    7,
                    opportunity.getLocation()
            );

            statement.setInt(
                    8,
                    opportunity.getMaxSlots()
            );

            statement.setInt(
                    9,
                    opportunity.getCurrentSlots()
            );

            statement.setString(
                    10,
                    opportunity.getCategory()
            );

            statement.setString(
                    11,
                    opportunity.getStatus()
            );

            statement.setString(
                    12,
                    opportunity.getRequiredSkills()
            );

            statement.setInt(
                    13,
                    opportunity.getOppID()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating opportunity: "
                    + e.getMessage()
            );

            return false;
        }
    }


   
    public boolean deleteOpportunity(
            int opportunity_id) {

        String sql =
                "DELETE FROM opportunities "
                + "WHERE opportunity_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    opportunity_id
            );

            int rowsDeleted =
                    statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting opportunity: "
                    + e.getMessage()
            );

            return false;
        }
    }


   
    private VolunteerOpportunity
            mapResultSetToOpportunity(
                    ResultSet resultSet)
            throws SQLException {

        VolunteerOpportunity opportunity =
                new VolunteerOpportunity();

        opportunity.setOppID(
                resultSet.getInt("opportunity_id")
        );

        opportunity.setUserID(
                resultSet.getInt("ngo_user_id")
        );

        opportunity.setTitle(
                resultSet.getString("title")
        );

        opportunity.setDescription(
                resultSet.getString("description")
        );

        if (resultSet.getDate("date") != null) {

            opportunity.setDate(
                    resultSet
                            .getDate("date")
                            .toLocalDate()
            );
        }

        if (resultSet.getTime("start_time") != null) {

            opportunity.setStartTime(
                    resultSet
                            .getTime("start_time")
                            .toLocalTime()
            );
        }

        if (resultSet.getTime("end_time") != null) {

            opportunity.setEndTime(
                    resultSet
                            .getTime("end_time")
                            .toLocalTime()
            );
        }

        opportunity.setLocation(
                resultSet.getString("location")
        );

        opportunity.setMaxSlots(
                resultSet.getInt("max_slots")
        );

        opportunity.setCurrentSlots(
                resultSet.getInt("current_slots")
        );

        opportunity.setCategory(
                resultSet.getString("category")
        );

        opportunity.setStatus(
                resultSet.getString("status")
        );

        opportunity.setRequiredSkills(
                resultSet.getString("required_skills")
        );

        return opportunity;
    }
            public List<Object[]> getAvailableOpportunities() {

    List<Object[]> opportunities =
            new ArrayList<>();

    String sql =
            "SELECT opportunity_id, title, description, date, "
            + "start_time, end_time, location, max_slots, "
            + "current_slots, category, status, required_skills "
            + "FROM opportunities "
            + "WHERE status = 'Open' "
            + "AND current_slots < max_slots "
            + "AND (date IS NULL OR date >= CURDATE()) "
            + "ORDER BY date ASC, start_time ASC";

    try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery()
    ) {

        while (resultSet.next()) {

            opportunities.add(
                    new Object[]{

                            resultSet.getInt(
                                    "opportunity_id"
                            ),

                            resultSet.getString(
                                    "title"
                            ),

                            resultSet.getString(
                                    "description"
                            ),

                            resultSet.getDate(
                                    "date"
                            ),

                            resultSet.getTime(
                                    "start_time"
                            ),

                            resultSet.getTime(
                                    "end_time"
                            ),

                            resultSet.getString(
                                    "location"
                            ),

                            resultSet.getInt(
                                    "max_slots"
                            ),

                            resultSet.getInt(
                                    "current_slots"
                            ),

                            resultSet.getString(
                                    "category"
                            ),

                            resultSet.getString(
                                    "status"
                            ),

                            resultSet.getString(
                                    "required_skills"
                            )
                    }
            );
        }

    } catch (SQLException e) {

        com.communityservice.utils.AppLogger.error(
                "Error getting available opportunities: "
                + e.getMessage()
        );
    }

    return opportunities;
}
}