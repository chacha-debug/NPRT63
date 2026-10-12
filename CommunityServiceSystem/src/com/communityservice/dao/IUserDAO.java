package com.communityservice.dao;

import com.communityservice.models.User;
import java.sql.Timestamp;
import java.util.List;

public interface IUserDAO {
    // Basic CRUD Operations
    int registerUser(User user);
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    boolean setUserActiveStatus(int userId, boolean active);

    // Account removal (soft delete: kept for auditing, blocked from signing in)
    boolean softDeleteUser(int userId, Integer deletedByAdminId, String reason);
    boolean restoreUser(int userId);

    // Authentication & Verification
    User authenticateUser(String email, String password);
    void updateLastLogin(int userId);
    boolean updateVerificationCode(String email, String newCode);
    boolean verifyEmail(String email, String code);
    int getVerificationAttemptsRemaining(String email);
    boolean emailExists(String email);

    // Login lockout (brute-force protection)
    boolean isAccountLocked(String email);
    Timestamp getLockoutExpiry(String email);
    void recordFailedLogin(String email);
    void recordSuccessfulLogin(int userId);

    // Password Management
    boolean createResetToken(String email, String token, Timestamp expiry);
    User validateResetToken(String email, String token);
    boolean resetPassword(String email, String token, String newPasswordHash);
    int getResetAttemptsRemaining(String email);
    boolean updatePassword(int userId, String newPasswordHash);
    boolean setAdminApprovalPending(int userId);
    String getAdminApprovalStatus(int userId);
    boolean approveAdmin(int userId);
    boolean rejectAdmin(int userId);
}