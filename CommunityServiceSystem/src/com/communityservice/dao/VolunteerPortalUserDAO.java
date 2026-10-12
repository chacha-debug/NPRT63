/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.PortalUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class VolunteerPortalUserDAO {

   

    public PortalUser addUser(PortalUser users) {

        String sql = "INSERT INTO users "
                   + "(full_name, email, password_hash, role, profile_pic, last_login, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(1, users.getFullName());
            statement.setString(2, users.getEmail());
            statement.setString(3, users.getPasswordHash());
            statement.setString(4, users.getRole());
            statement.setString(5, users.getProfilePic());

            if (users.getLastLogin() != null) {

                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                users.getLastLogin()
                        )
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setBoolean(
                    7,
                    users.is_active()
            );

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet generatedKeys =
                        statement.getGeneratedKeys();

                if (generatedKeys.next()) {

                    int generatedID =
                            generatedKeys.getInt(1);

                    users.setUserID(generatedID);

                    return users;
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error adding users: "
                    + e.getMessage()
            );
        }

        return null;
    }



    public PortalUser getUserByID(int user_id) {

        String sql =
                "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                PortalUser users = new PortalUser();

                users.setUserID(
                        resultSet.getInt("user_id")
                );

                users.setFullName(
                        resultSet.getString("full_name")
                );

                users.setEmail(
                        resultSet.getString("email")
                );

                users.setPasswordHash(
                        resultSet.getString("password_hash")
                );

                users.setRole(
                        resultSet.getString("role")
                );

                users.setProfilePic(
                        resultSet.getString("profile_pic")
                );

                if (resultSet.getTimestamp(
                        "date_registered") != null) {

                    users.setDateRegistered(
                            resultSet
                                    .getTimestamp(
                                            "date_registered"
                                    )
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp(
                        "last_login") != null) {

                    users.setLastLogin(
                            resultSet
                                    .getTimestamp(
                                            "last_login"
                                    )
                                    .toLocalDateTime()
                    );
                }

                users.setActive(
                        resultSet.getBoolean(
                                "is_active"
                        )
                );

                return users;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error finding users: "
                    + e.getMessage()
            );
        }

        return null;
    }



    public boolean updateUser(PortalUser users) {

        String sql =
                "UPDATE users SET "
                + "full_name = ?, "
                + "email = ?, "
                + "password_hash = ?, "
                + "role = ?, "
                + "profile_pic = ?, "
                + "last_login = ?, "
                + "is_active = ? "
                + "WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    users.getFullName()
            );

            statement.setString(
                    2,
                    users.getEmail()
            );

            statement.setString(
                    3,
                    users.getPasswordHash()
            );

            statement.setString(
                    4,
                    users.getRole()
            );

            statement.setString(
                    5,
                    users.getProfilePic()
            );

            if (users.getLastLogin() != null) {

                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                users.getLastLogin()
                        )
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setBoolean(
                    7,
                    users.is_active()
            );

            statement.setInt(
                    8,
                    users.getUserID()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating users: "
                    + e.getMessage()
            );

            return false;
        }
    }


   
    public boolean deleteUser(int user_id) {

        String sql =
                "DELETE FROM users WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);

            int rowsDeleted =
                    statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting users: "
                    + e.getMessage()
            );

            return false;
        }
    }
}