/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.OpportunityApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Opportunity Applications.
 *
 * Handles:
 * - Creating volunteers applications
 * - Checking existing applications
 * - Getting applications
 * - Getting volunteers applications
 * - Updating application status
 * - Approving applications
 * - Rejecting applications
 * - Updating opportunity slot counts
 */
public class OpportunityApplicationDAO {

    // =========================================================
    // APPLY FOR OPPORTUNITY
    // =========================================================

    public boolean applyForOpportunity(
            int volunteer_id,
            int opportunity_id) {

        String checkSql =
                "SELECT application_id " +
                "FROM opportunity_applications " +
                "WHERE volunteer_id = ? " +
                "AND opportunity_id = ?";

        String opportunitySql =
                "SELECT max_slots, current_slots, status, date " +
                "FROM opportunities " +
                "WHERE opportunity_id = ?";

        String insertSql =
                "INSERT INTO opportunity_applications " +
                "(volunteer_id, opportunity_id, application_date, status, override_flag) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, 'Pending', 0)";

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            // -------------------------------------------------
            // CHECK WHETHER VOLUNTEER ALREADY APPLIED
            // -------------------------------------------------

            try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkSql)) {

                checkStatement.setInt(
                        1,
                        volunteer_id
                );

                checkStatement.setInt(
                        2,
                        opportunity_id
                );

                try (ResultSet resultSet =
                             checkStatement.executeQuery()) {

                    if (resultSet.next()) {

                        com.communityservice.utils.AppLogger.error(
                                "Volunteer has already applied."
                        );

                        return false;
                    }
                }
            }

            // -------------------------------------------------
            // CHECK OPPORTUNITY
            // -------------------------------------------------

            try (PreparedStatement opportunityStatement =
                         connection.prepareStatement(opportunitySql)) {

                opportunityStatement.setInt(
                        1,
                        opportunity_id
                );

                try (ResultSet resultSet =
                             opportunityStatement.executeQuery()) {

                    if (!resultSet.next()) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity not found."
                        );

                        return false;
                    }

                    int max_slots =
                            resultSet.getInt(
                                    "max_slots"
                            );

                    int current_slots =
                            resultSet.getInt(
                                    "current_slots"
                            );

                    String status =
                            resultSet.getString(
                                    "status"
                            );

                    Date opportunityDate =
                            resultSet.getDate(
                                    "date"
                            );

                    // -------------------------------------------------
                    // STATUS CHECK
                    // -------------------------------------------------

                    if (status == null
                            ||
                            !status.equalsIgnoreCase("Open")) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity is not open."
                        );

                        return false;
                    }

                    // -------------------------------------------------
                    // SLOT CHECK
                    // -------------------------------------------------

                    if (current_slots >= max_slots) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity is full."
                        );

                        return false;
                    }

                    // -------------------------------------------------
                    // DATE CHECK
                    // -------------------------------------------------

                    if (opportunityDate != null
                            &&
                            opportunityDate.toLocalDate()
                                    .isBefore(
                                            java.time.LocalDate.now()
                                    )) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity date has passed."
                        );

                        return false;
                    }
                }
            }

            // -------------------------------------------------
            // INSERT APPLICATION
            // -------------------------------------------------

            try (PreparedStatement insertStatement =
                         connection.prepareStatement(
                                 insertSql,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                insertStatement.setInt(
                        1,
                        volunteer_id
                );

                insertStatement.setInt(
                        2,
                        opportunity_id
                );

                int rowsInserted =
                        insertStatement.executeUpdate();

                if (rowsInserted > 0) {

                    com.communityservice.utils.AppLogger.error(
                            "Application submitted successfully."
                    );

                    return true;
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error applying for opportunity: "
                    +
                    e.getMessage()
            );

        } finally {

            /*
             * DatabaseConnection manages the connection.
             * We intentionally do not close it here if your
             * DatabaseConnection returns a shared connection.
             */
        }

        return false;
    }


    // =========================================================
    // ADD APPLICATION
    // =========================================================

    public boolean addApplication(
            OpportunityApplication application) {

        if (application == null) {
            return false;
        }

        return applyForOpportunity(
                application.getVolunteerID(),
                application.getOppID()
        );
    }


    // =========================================================
    // GET APPLICATION BY ID
    // =========================================================

    public OpportunityApplication getApplicationByID(
            int application_id) {

        String sql =
                "SELECT * " +
                "FROM opportunity_applications " +
                "WHERE application_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    application_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToApplication(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting application: "
                    +
                    e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // GET APPLICATION BY VOLUNTEER AND OPPORTUNITY
    // =========================================================

    public OpportunityApplication
            getApplicationByVolunteerAndOpportunity(
                    int volunteer_id,
                    int opportunity_id) {

        String sql =
                "SELECT * " +
                "FROM opportunity_applications " +
                "WHERE volunteer_id = ? " +
                "AND opportunity_id = ? " +
                "LIMIT 1";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            statement.setInt(
                    2,
                    opportunity_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToApplication(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error checking application: "
                    +
                    e.getMessage()
            );
        }

        return null;
    }


    // =========================================================
    // CHECK WHETHER VOLUNTEER HAS APPLIED
    // =========================================================

    public boolean hasApplied(
            int volunteer_id,
            int opportunity_id) {

        OpportunityApplication application =
                getApplicationByVolunteerAndOpportunity(
                        volunteer_id,
                        opportunity_id
                );

        return application != null;
    }


    // =========================================================
    // GET ALL APPLICATIONS
    // =========================================================

    public List<OpportunityApplication>
            getAllApplications() {

        List<OpportunityApplication> applications =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "a.*, " +

                "o.title AS opportunityTitle, " +
                "o.description AS opportunityDescription, " +
                "o.date AS opportunityDate, " +
                "o.start_time AS start_time, " +
                "o.end_time AS end_time, " +
                "o.location AS opportunityLocation, " +
                "o.max_slots AS max_slots, " +
                "o.current_slots AS current_slots, " +
                "o.category AS category, " +
                "o.status AS opportunityStatus, " +
                "o.required_skills AS required_skills " +

                "FROM opportunity_applications a " +

                "INNER JOIN opportunities o " +
                "ON a.opportunity_id = o.opportunity_id " +

                "ORDER BY a.application_date DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                applications.add(
                        mapResultSetToApplication(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting all applications: "
                    +
                    e.getMessage()
            );
        }

        return applications;
    }


    // =========================================================
    // GET APPLICATIONS BY VOLUNTEER ID
    // =========================================================

    public List<OpportunityApplication>
            getApplicationsByVolunteerID(
                    int volunteer_id) {

        List<OpportunityApplication> applications =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "a.*, " +

                "o.title AS opportunityTitle, " +
                "o.description AS opportunityDescription, " +
                "o.date AS opportunityDate, " +
                "o.start_time AS start_time, " +
                "o.end_time AS end_time, " +
                "o.location AS opportunityLocation, " +
                "o.max_slots AS max_slots, " +
                "o.current_slots AS current_slots, " +
                "o.category AS category, " +
                "o.status AS opportunityStatus, " +
                "o.required_skills AS required_skills " +

                "FROM opportunity_applications a " +

                "INNER JOIN opportunities o " +
                "ON a.opportunity_id = o.opportunity_id " +

                "WHERE a.volunteer_id = ? " +

                "ORDER BY a.application_date DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    applications.add(
                            mapResultSetToApplication(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting volunteers applications: "
                    +
                    e.getMessage()
            );
        }

        return applications;
    }


    // =========================================================
    // GET APPLICATIONS BY VOLUNTEER
    // =========================================================

    public List<OpportunityApplication>
            getApplicationsByVolunteer(
                    int volunteer_id) {

        return getApplicationsByVolunteerID(
                volunteer_id
        );
    }


    // =========================================================
    // GET PENDING APPLICATIONS
    // =========================================================

    public List<OpportunityApplication>
            getPendingApplications() {

        List<OpportunityApplication> applications =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "a.*, " +

                "o.title AS opportunityTitle, " +
                "o.description AS opportunityDescription, " +
                "o.date AS opportunityDate, " +
                "o.start_time AS start_time, " +
                "o.end_time AS end_time, " +
                "o.location AS opportunityLocation, " +
                "o.max_slots AS max_slots, " +
                "o.current_slots AS current_slots, " +
                "o.category AS category, " +
                "o.status AS opportunityStatus, " +
                "o.required_skills AS required_skills " +

                "FROM opportunity_applications a " +

                "INNER JOIN opportunities o " +
                "ON a.opportunity_id = o.opportunity_id " +

                "WHERE a.status = 'Pending' " +

                "ORDER BY a.application_date ASC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                applications.add(
                        mapResultSetToApplication(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting pending applications: "
                    +
                    e.getMessage()
            );
        }

        return applications;
    }


    // =========================================================
    // GET APPLICATIONS BY STATUS
    // =========================================================

    public List<OpportunityApplication>
            getApplicationsByStatus(
                    String status) {

        List<OpportunityApplication> applications =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "a.*, " +

                "o.title AS opportunityTitle, " +
                "o.description AS opportunityDescription, " +
                "o.date AS opportunityDate, " +
                "o.start_time AS start_time, " +
                "o.end_time AS end_time, " +
                "o.location AS opportunityLocation, " +
                "o.max_slots AS max_slots, " +
                "o.current_slots AS current_slots, " +
                "o.category AS category, " +
                "o.status AS opportunityStatus, " +
                "o.required_skills AS required_skills " +

                "FROM opportunity_applications a " +

                "INNER JOIN opportunities o " +
                "ON a.opportunity_id = o.opportunity_id " +

                "WHERE a.status = ? " +

                "ORDER BY a.application_date DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    applications.add(
                            mapResultSetToApplication(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error getting applications by status: "
                    +
                    e.getMessage()
            );
        }

        return applications;
    }


    // =========================================================
    // APPROVE APPLICATION
    // =========================================================

    public boolean approveApplication(
            int application_id,
            int reviewed_by) {

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            // -------------------------------------------------
            // GET APPLICATION
            // -------------------------------------------------

            String applicationSql =
                    "SELECT volunteer_id, opportunity_id, status " +
                    "FROM opportunity_applications " +
                    "WHERE application_id = ?";

            int volunteer_id;
            int opportunity_id;
            String currentStatus;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    applicationSql
                            )
            ) {

                statement.setInt(
                        1,
                        application_id
                );

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        com.communityservice.utils.AppLogger.error(
                                "Application not found."
                        );

                        return false;
                    }

                    volunteer_id =
                            resultSet.getInt(
                                    "volunteer_id"
                            );

                    opportunity_id =
                            resultSet.getInt(
                                    "opportunity_id"
                            );

                    currentStatus =
                            resultSet.getString(
                                    "status"
                            );
                }
            }

            // -------------------------------------------------
            // ONLY PENDING APPLICATIONS CAN BE APPROVED
            // -------------------------------------------------

            if (currentStatus == null
                    ||
                    !currentStatus.equalsIgnoreCase(
                            "Pending"
                    )) {

                com.communityservice.utils.AppLogger.error(
                        "Application is not pending."
                );

                return false;
            }

            // -------------------------------------------------
            // CHECK OPPORTUNITY SLOTS
            // -------------------------------------------------

            String opportunitySql =
                    "SELECT max_slots, current_slots, status " +
                    "FROM opportunities " +
                    "WHERE opportunity_id = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    opportunitySql
                            )
            ) {

                statement.setInt(
                        1,
                        opportunity_id
                );

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity not found."
                        );

                        return false;
                    }

                    int max_slots =
                            resultSet.getInt(
                                    "max_slots"
                            );

                    int current_slots =
                            resultSet.getInt(
                                    "current_slots"
                            );

                    String status =
                            resultSet.getString(
                                    "status"
                            );

                    if (status == null
                            ||
                            !status.equalsIgnoreCase(
                                    "Open"
                            )) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity is not open."
                        );

                        return false;
                    }

                    if (current_slots >= max_slots) {

                        com.communityservice.utils.AppLogger.error(
                                "Opportunity is full."
                        );

                        return false;
                    }
                }
            }

            // -------------------------------------------------
            // APPROVE APPLICATION
            // -------------------------------------------------

            String updateApplicationSql =
                    "UPDATE opportunity_applications " +
                    "SET status = 'Approved', " +
                    "reviewed_by = ?, " +
                    "reviewed_date = CURRENT_TIMESTAMP " +
                    "WHERE application_id = ? " +
                    "AND status = 'Pending'";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateApplicationSql
                            )
            ) {

                statement.setInt(
                        1,
                        reviewed_by
                );

                statement.setInt(
                        2,
                        application_id
                );

                int rowsUpdated =
                        statement.executeUpdate();

                if (rowsUpdated == 0) {

                    return false;
                }
            }

            // -------------------------------------------------
            // INCREASE CURRENT SLOTS
            // -------------------------------------------------

            String updateSlotsSql =
                    "UPDATE opportunities " +
                    "SET current_slots = current_slots + 1 " +
                    "WHERE opportunity_id = ? " +
                    "AND current_slots < max_slots";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateSlotsSql
                            )
            ) {

                statement.setInt(
                        1,
                        opportunity_id
                );

                int rowsUpdated =
                        statement.executeUpdate();

                if (rowsUpdated == 0) {

                    // Revert application status
                    String revertSql =
                            "UPDATE opportunity_applications " +
                            "SET status = 'Pending', " +
                            "reviewed_by = NULL, " +
                            "reviewed_date = NULL " +
                            "WHERE application_id = ?";

                    try (
                            PreparedStatement revertStatement =
                                    connection.prepareStatement(
                                            revertSql
                                    )
                    ) {

                        revertStatement.setInt(
                                1,
                                application_id
                        );

                        revertStatement.executeUpdate();
                    }

                    return false;
                }
            }

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            com.communityservice.utils.AppLogger.error(
                    "Application approved successfully."
            );

            return true;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error approving application: "
                    +
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    public boolean rejectApplication(
            int application_id,
            int reviewed_by,
            String rejection_reason) {

        String sql =
                "UPDATE opportunity_applications " +
                "SET status = 'Rejected', " +
                "rejection_reason = ?, " +
                "reviewed_by = ?, " +
                "reviewed_date = CURRENT_TIMESTAMP " +
                "WHERE application_id = ? " +
                "AND status = 'Pending'";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    rejection_reason
            );

            statement.setInt(
                    2,
                    reviewed_by
            );

            statement.setInt(
                    3,
                    application_id
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                com.communityservice.utils.AppLogger.error(
                        "Application rejected successfully."
                );

                return true;
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error rejecting application: "
                    +
                    e.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // UPDATE APPLICATION STATUS
    // =========================================================

    public boolean updateApplicationStatus(
            int application_id,
            String status,
            int reviewed_by) {

        if (status == null
                ||
                status.trim().isEmpty()) {

            return false;
        }

        String sql =
                "UPDATE opportunity_applications " +
                "SET status = ?, " +
                "reviewed_by = ?, " +
                "reviewed_date = CURRENT_TIMESTAMP " +
                "WHERE application_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            statement.setInt(
                    2,
                    reviewed_by
            );

            statement.setInt(
                    3,
                    application_id
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating application status: "
                    +
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // UPDATE APPLICATION
    // =========================================================

    public boolean updateApplication(
            OpportunityApplication application) {

        if (application == null) {
            return false;
        }

        String sql =
                "UPDATE opportunity_applications " +
                "SET volunteer_id = ?, " +
                "opportunity_id = ?, " +
                "application_date = ?, " +
                "status = ?, " +
                "override_flag = ?, " +
                "rejection_reason = ?, " +
                "reviewed_by = ?, " +
                "reviewed_date = ? " +
                "WHERE application_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    application.getVolunteerID()
            );

            statement.setInt(
                    2,
                    application.getOppID()
            );

            if (application.getApplicationDate() != null) {

                statement.setTimestamp(
                        3,
                        Timestamp.valueOf(
                                application.getApplicationDate()
                        )
                );

            } else {

                statement.setNull(
                        3,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setString(
                    4,
                    application.getStatus()
            );

            statement.setBoolean(
                    5,
                    application.isOverrideFlag()
            );

            statement.setString(
                    6,
                    application.getRejectionReason()
            );

            if (application.getReviewedBy() != null) {

                statement.setInt(
                        7,
                        application.getReviewedBy()
                );

            } else {

                statement.setNull(
                        7,
                        java.sql.Types.INTEGER
                );
            }

            if (application.getReviewedDate() != null) {

                statement.setTimestamp(
                        8,
                        Timestamp.valueOf(
                                application.getReviewedDate()
                        )
                );

            } else {

                statement.setNull(
                        8,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setInt(
                    9,
                    application.getAppID()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error updating application: "
                    +
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    /** Allows a volunteer to withdraw only a pending application. */
    public boolean withdrawApplication(int applicationId, int volunteerId) {
        String sql = "DELETE FROM opportunity_applications WHERE application_id = ? AND volunteer_id = ? AND status = 'Pending'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.setInt(2, volunteerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.communityservice.utils.AppLogger.error("Failed to withdraw application " + applicationId, e);
            return false;
        }
    }

    // DELETE APPLICATION
    // =========================================================

    public boolean deleteApplication(
            int application_id) {

        String sql =
                "DELETE FROM opportunity_applications " +
                "WHERE application_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    application_id
            );

            int rowsDeleted =
                    statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error deleting application: "
                    +
                    e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // COUNT PENDING APPLICATIONS
    // =========================================================

    public int countPendingApplications() {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunity_applications " +
                "WHERE status = 'Pending'";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error counting pending applications: "
                    +
                    e.getMessage()
            );
        }

        return 0;
    }


    // =========================================================
    // COUNT VOLUNTEER APPLICATIONS
    // =========================================================

    public int countApplicationsByVolunteer(
            int volunteer_id) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM opportunity_applications " +
                "WHERE volunteer_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    volunteer_id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            com.communityservice.utils.AppLogger.error(
                    "Error counting volunteers applications: "
                    +
                    e.getMessage()
            );
        }

        return 0;
    }


    // =========================================================
    // MAP RESULT SET TO MODEL
    // =========================================================

    private OpportunityApplication
            mapResultSetToApplication(
                    ResultSet resultSet)
            throws SQLException {

        OpportunityApplication application =
                new OpportunityApplication();


        // =====================================================
        // APPLICATION INFORMATION
        // =====================================================

        application.setAppID(
                resultSet.getInt(
                        "application_id"
                )
        );

        application.setVolunteerID(
                resultSet.getInt(
                        "volunteer_id"
                )
        );

        application.setOppID(
                resultSet.getInt(
                        "opportunity_id"
                )
        );


        Timestamp applicationTimestamp =
                resultSet.getTimestamp(
                        "application_date"
                );

        if (applicationTimestamp != null) {

            application.setApplicationDate(
                    applicationTimestamp.toLocalDateTime()
            );
        }


        application.setStatus(
                resultSet.getString(
                        "status"
                )
        );


        application.setOverrideFlag(
                resultSet.getBoolean(
                        "override_flag"
                )
        );


        application.setRejectionReason(
                resultSet.getString(
                        "rejection_reason"
                )
        );


        int reviewed_by =
                resultSet.getInt(
                        "reviewed_by"
                );

        if (!resultSet.wasNull()) {

            application.setReviewedBy(
                    reviewed_by
            );
        }


        Timestamp reviewedTimestamp =
                resultSet.getTimestamp(
                        "reviewed_date"
                );

        if (reviewedTimestamp != null) {

            application.setReviewedDate(
                    reviewedTimestamp.toLocalDateTime()
            );
        }


        // =====================================================
        // OPPORTUNITY INFORMATION
        // =====================================================

        try {

            application.setOpportunityTitle(
                    resultSet.getString(
                            "opportunityTitle"
                    )
            );

            application.setOpportunityDescription(
                    resultSet.getString(
                            "opportunityDescription"
                    )
            );


            Date opportunityDate =
                    resultSet.getDate(
                            "opportunityDate"
                    );

            if (opportunityDate != null) {

                application.setOpportunityDate(
                        opportunityDate.toLocalDate()
                );
            }


            Time start_time =
                    resultSet.getTime(
                            "start_time"
                    );

            if (start_time != null) {

                application.setStartTime(
                        start_time.toLocalTime()
                );
            }


            Time end_time =
                    resultSet.getTime(
                            "end_time"
                    );

            if (end_time != null) {

                application.setEndTime(
                        end_time.toLocalTime()
                );
            }


            application.setOpportunityLocation(
                    resultSet.getString(
                            "opportunityLocation"
                    )
            );


            int max_slots =
                    resultSet.getInt(
                            "max_slots"
                    );

            if (!resultSet.wasNull()) {

                application.setMaxSlots(
                        max_slots
                );
            }


            int current_slots =
                    resultSet.getInt(
                            "current_slots"
                    );

            if (!resultSet.wasNull()) {

                application.setCurrentSlots(
                        current_slots
                );
            }


            application.setCategory(
                    resultSet.getString(
                            "category"
                    )
            );


            application.setOpportunityStatus(
                    resultSet.getString(
                            "opportunityStatus"
                    )
            );


            application.setRequiredSkills(
                    resultSet.getString(
                            "required_skills"
                    )
            );

        } catch (SQLException ignored) {

            /*
             * The additional opportunity fields are not
             * present when this method is used by a simple
             * SELECT * FROM opportunity_applications query.
             *
             * The basic application information has already
             * been mapped above.
             */
        }


        return application;
    }
}