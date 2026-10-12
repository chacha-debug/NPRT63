package com.communityservice.dao;

import com.communityservice.DatabaseConnection;
import com.communityservice.models.User;
import com.communityservice.utils.AppLogger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    /** After this many consecutive failed logins, the account is locked out. */
    private static final int MAX_FAILED_ATTEMPTS = 5;
    /** How long an account stays locked once it hits the failed-attempt threshold. */
    private static final int LOCKOUT_MINUTES = 15;
    /** How long a verification/registration code remains valid. */
    private static final int CODE_VALID_MINUTES = 15;
    private static final int MAX_CODE_ATTEMPTS = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    @Override
    public int registerUser(User user) {
        String query = "INSERT INTO users (full_name, email, password_hash, role, is_verified, verification_code, "
                + "verification_code_expiry, verification_resend_available_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + CODE_VALID_MINUTES
                + " MINUTE), DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + RESEND_COOLDOWN_SECONDS + " SECOND))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setBoolean(5, user.isVerified());
            pstmt.setString(6, user.getVerificationCode());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to register user " + user.getEmail(), e);
        }
        return -1;
    }

    @Override
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to fetch user by id " + userId, e);
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to fetch user by email " + email, e);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to fetch all users", e);
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ?, role = ?, is_verified = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole());
            pstmt.setBoolean(4, user.isVerified());
            pstmt.setInt(5, user.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to update user " + user.getUserId(), e);
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to delete user " + userId, e);
        }
        return false;
    }

    @Override
    public boolean setUserActiveStatus(int userId, boolean active) {
        String query = "UPDATE users SET is_active = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, active);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to set active status for user " + userId, e);
        }
        return false;
    }

    @Override
    public boolean softDeleteUser(int userId, Integer deletedByAdminId, String reason) {
        // A removed account is also deactivated, so anything that only checks
        // is_active (rather than is_deleted) still fails safe.
        String query = "UPDATE users SET is_deleted = TRUE, is_active = FALSE, deleted_at = CURRENT_TIMESTAMP, "
                + "deleted_by = ?, deletion_reason = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (deletedByAdminId != null) {
                pstmt.setInt(1, deletedByAdminId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, reason);
            pstmt.setInt(3, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to remove user " + userId, e);
        }
        return false;
    }

    @Override
    public boolean restoreUser(int userId) {
        String query = "UPDATE users SET is_deleted = FALSE, is_active = TRUE, deleted_at = NULL, "
                + "deleted_by = NULL, deletion_reason = NULL WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to restore user " + userId, e);
        }
        return false;
    }

    @Override
    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to authenticate user " + email, e);
        }
        return null;
    }

    @Override
    public void updateLastLogin(int userId) {
        String query = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error("Failed to update last login for user " + userId, e);
        }
    }

    @Override
    public boolean updateVerificationCode(String email, String newCode) {
        String query = "UPDATE users SET verification_code = ?, verification_code_expiry = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL "
                + CODE_VALID_MINUTES + " MINUTE), verification_attempts = 0, "
                + "verification_resend_available_at = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + RESEND_COOLDOWN_SECONDS + " SECOND) "
                + "WHERE email = ? AND (verification_resend_available_at IS NULL OR verification_resend_available_at <= CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newCode);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to update verification code for " + email, e);
        }
        return false;
    }

    @Override
    public boolean verifyEmail(String email, String code) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String check = "SELECT verification_code, verification_code_expiry, verification_attempts, "
                    + "(verification_code_expiry > CURRENT_TIMESTAMP) AS code_is_valid "
                    + "FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    int attempts = rs.getInt("verification_attempts");
                    Timestamp expiry = rs.getTimestamp("verification_code_expiry");
                    String stored = rs.getString("verification_code");
                    if (attempts >= MAX_CODE_ATTEMPTS || expiry == null || !rs.getBoolean("code_is_valid")) {
                        return false;
                    }
                    if (stored != null && stored.equals(code)) {
                        try (PreparedStatement update = conn.prepareStatement(
                                "UPDATE users SET is_verified = TRUE, verification_code = NULL, "
                                + "verification_code_expiry = NULL, verification_attempts = 0 WHERE email = ?")) {
                            update.setString(1, email);
                            return update.executeUpdate() > 0;
                        }
                    }
                }
            }
            try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE users SET verification_attempts = verification_attempts + 1 "
                    + "WHERE email = ? AND verification_attempts < ?")) {
                update.setString(1, email);
                update.setInt(2, MAX_CODE_ATTEMPTS);
                update.executeUpdate();
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to verify email " + email, e);
        }
        return false;
    }

    @Override
    public int getVerificationAttemptsRemaining(String email) {
        return getAttemptsRemaining(email, "verification_attempts");
    }

    @Override
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to check whether email exists: " + email, e);
        }
        return false;
    }

    @Override
    public boolean isAccountLocked(String email) {
        String query = "SELECT 1 FROM users WHERE email = ? AND lockout_until IS NOT NULL AND lockout_until > CURRENT_TIMESTAMP";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to check lock status for " + email, e);
        }
        return false;
    }

    @Override
    public Timestamp getLockoutExpiry(String email) {
        String query = "SELECT lockout_until FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("lockout_until");
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to fetch lockout expiry for " + email, e);
        }
        return null;
    }

    @Override
    public void recordFailedLogin(String email) {
        // Single atomic UPDATE: bump the counter, and only set a lockout
        // window once the *new* count reaches the threshold.
        String query = "UPDATE users SET failed_login_attempts = failed_login_attempts + 1, "
                + "lockout_until = CASE WHEN failed_login_attempts + 1 >= ? "
                + "THEN DATE_ADD(CURRENT_TIMESTAMP, INTERVAL ? MINUTE) ELSE lockout_until END "
                + "WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, MAX_FAILED_ATTEMPTS);
            pstmt.setInt(2, LOCKOUT_MINUTES);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error("Failed to record failed login for " + email, e);
        }
    }

    @Override
    public void recordSuccessfulLogin(int userId) {
        String query = "UPDATE users SET failed_login_attempts = 0, lockout_until = NULL WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error("Failed to reset failed-login counter for user " + userId, e);
        }
    }

    @Override
    public boolean createResetToken(String email, String token, Timestamp expiry) {
        String query = "UPDATE users SET reset_token = ?, reset_token_expiry = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL "
                + CODE_VALID_MINUTES + " MINUTE), reset_attempts = 0, "
                + "reset_resend_available_at = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + RESEND_COOLDOWN_SECONDS + " SECOND) "
                + "WHERE email = ? AND (reset_resend_available_at IS NULL OR reset_resend_available_at <= CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, token);
            // Use the database clock for expiry so JVM/DB timezone differences cannot
            // make a newly-issued code appear expired.
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to create reset token for " + email, e);
        }
        return false;
    }

    @Override
    public User validateResetToken(String email, String token) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND reset_token_expiry > CURRENT_TIMESTAMP";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return null;
                    int attempts = rs.getInt("reset_attempts");
                    String stored = rs.getString("reset_token");
                    if (attempts >= MAX_CODE_ATTEMPTS) return null;
                    if (stored != null && stored.equals(token)) {
                        return extractUserFromResultSet(rs);
                    }
                }
            }
            try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE users SET reset_attempts = reset_attempts + 1 "
                    + "WHERE email = ? AND reset_attempts < ?")) {
                update.setString(1, email);
                update.setInt(2, MAX_CODE_ATTEMPTS);
                update.executeUpdate();
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to validate reset token", e);
        }
        return null;
    }

    @Override
    public boolean resetPassword(String email, String token, String newPasswordHash) {
        String query = "UPDATE users SET password_hash = ?, reset_token = NULL, reset_token_expiry = NULL, "
                + "reset_attempts = 0, reset_resend_available_at = NULL WHERE email = ? AND reset_token = ? "
                + "AND reset_token_expiry > CURRENT_TIMESTAMP AND reset_attempts < ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPasswordHash);
            pstmt.setString(2, email);
            pstmt.setString(3, token);
            pstmt.setInt(4, MAX_CODE_ATTEMPTS);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to reset password via token", e);
        }
        return false;
    }

    @Override
    public int getResetAttemptsRemaining(String email) {
        return getAttemptsRemaining(email, "reset_attempts");
    }

    private int getAttemptsRemaining(String email, String column) {
        if (!"verification_attempts".equals(column) && !"reset_attempts".equals(column)) return 0;
        String query = "SELECT " + column + " FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Math.max(0, MAX_CODE_ATTEMPTS - rs.getInt(1));
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to fetch code attempts", e);
        }
        return 0;
    }

    @Override
    public boolean setAdminApprovalPending(int userId) {
        String query = "UPDATE users SET admin_approval_status = 'PENDING', is_active = FALSE WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to mark admin account pending", e);
            return false;
        }
    }

    @Override
    public String getAdminApprovalStatus(int userId) {
        String query = "SELECT admin_approval_status FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            AppLogger.error("Failed to read admin approval status", e);
            return null;
        }
    }

    @Override
    public boolean approveAdmin(int userId) {
        String query = "UPDATE users SET admin_approval_status = 'APPROVED', is_active = TRUE WHERE user_id = ? AND role = 'Administrator'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to approve admin account", e);
            return false;
        }
    }

    @Override
    public boolean rejectAdmin(int userId) {
        String query = "UPDATE users SET admin_approval_status = 'REJECTED', is_active = FALSE WHERE user_id = ? AND role = 'Administrator'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to reject admin account", e);
            return false;
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPasswordHash) {
        String query = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            AppLogger.error("Failed to update password for user " + userId, e);
        }
        return false;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setVerificationCode(rs.getString("verification_code"));
        user.setActive(rs.getBoolean("is_active"));
        user.setDeleted(rs.getBoolean("is_deleted"));
        user.setDeletedAt(rs.getTimestamp("deleted_at"));
        int deletedBy = rs.getInt("deleted_by");
        user.setDeletedBy(rs.wasNull() ? null : deletedBy);
        user.setDeletionReason(rs.getString("deletion_reason"));
        return user;
    }
}
