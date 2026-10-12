/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankingDAO {

    public List<Object[]> getVolunteerRanking() {

        List<Object[]> ranking =
                new ArrayList<>();

        String sql =
                "SELECT "
                + "v.volunteer_id, "
                + "u.full_name, "
                + "COALESCE(SUM("
                + "CASE "
                + "WHEN a.status = 'Present' "
                + "THEN a.hours_awarded "
                + "ELSE 0 "
                + "END"
                + "), 0) AS total_hours, "
                + "COUNT("
                + "CASE "
                + "WHEN a.status = 'Present' "
                + "AND a.hours_awarded > 0 "
                + "THEN a.attendance_id "
                + "END"
                + ") AS completedEvents "
                + "FROM volunteers v "
                + "INNER JOIN users u "
                + "ON v.user_id = u.user_id "
                + "LEFT JOIN attendance_records a "
                + "ON v.volunteer_id = a.volunteer_id "
                + "GROUP BY "
                + "v.volunteer_id, u.full_name "
                + "ORDER BY total_hours DESC, "
                + "completedEvents DESC";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            int rank = 1;

            while (resultSet.next()) {

                ranking.add(
                        new Object[]{

                                rank,

                                resultSet.getInt(
                                        "volunteer_id"
                                ),

                                resultSet.getString(
                                        "full_name"
                                ),

                                resultSet.getBigDecimal(
                                        "total_hours"
                                ),

                                resultSet.getInt(
                                        "completedEvents"
                                )
                        }
                );

                rank++;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting volunteers ranking: "
                    + e.getMessage()
            );
        }

        return ranking;
    }
}