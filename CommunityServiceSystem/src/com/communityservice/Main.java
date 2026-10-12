package com.communityservice;

import com.communityservice.dao.IAdminDAO;
import com.communityservice.dao.AdminDAO;
import com.communityservice.dao.INGODAO;
import com.communityservice.dao.IUserDAO;
import com.communityservice.dao.IVolunteerDAO;
import com.communityservice.dao.NGODAO;
import com.communityservice.dao.UserDAO;
import com.communityservice.dao.AuditLogDAO;
import com.communityservice.models.AuditLog;
import com.communityservice.dao.VolunteerDAO;
import com.communityservice.models.Admin;
import com.communityservice.models.NGO;
import com.communityservice.models.User;
import com.communityservice.models.RegistrationState;
import com.communityservice.models.Volunteer;
import com.communityservice.ui.*;
import com.communityservice.utils.EmailSender;
import com.communityservice.utils.AppLogger;
import com.communityservice.utils.PasswordHasher;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private NavigationBar topNavPanel;

    // Data access
    private final IUserDAO userDAO = new UserDAO();
    private final INGODAO ngoDAO = new NGODAO();
    private final IVolunteerDAO volunteerDAO = new VolunteerDAO();
    private final IAdminDAO adminDAO = new AdminDAO();

    // Session state
    private final RegistrationState registrationState = new RegistrationState();
    private User currentUser;              // signed-in user
    private String pendingVerificationEmail; // email awaiting registration verification
    private String pendingResetEmail;      // email that requested a password reset
    private String pendingResetCode;       // active reset code for that email
    private NGODashboardPanel ngoDashboardPanel; // lazily (re)created per NGO login, since it needs the real user id
    private VolunteerDashboardPanel volunteerDashboardPanel; // lazily (re)created per Volunteer login, same reason
    private AdminDashboardPanel adminDashboardPanel; // lazily (re)created per Administrator login, same reason

    // View Panels
    private LoginPanel loginPanel;
    private RegistrationPanel registerPanel;
    private RoleSelectionPanel roleSelectionPanel;
    private PreferencesPanel preferencesPanel;
    private ProfileSetupPanel profileSetupPanel;
    private ForgotPasswordPanel forgotPasswordPanel;
    private RegistrationVerificationPanel regVerifyPanel;
    private PasswordResetVerificationPanel passVerifyPanel;
    private ResetPasswordPanel resetPasswordPanel;
    private DashboardOverviewPanel dashboardPanel;
    private HelpPanel helpPanel;
    private AboutPanel aboutPanel;
    private WelcomePanel welcomePanel;
    private NgoSetupPanel ngoSetupPanel;
    private AdminSetupPanel adminSetupPanel;

    public Main() {
        setTitle("Community Service+");
        setIconImage(com.communityservice.ui.RoundPersonIcon.toImage(48, com.communityservice.ui.AppTheme.NAVY_PRIMARY, java.awt.Color.WHITE));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        topNavPanel = new NavigationBar();
        topNavPanel.setVisible(false);
        add(topNavPanel, BorderLayout.NORTH);

        initPanels();
        AppLogger.info(EmailSender.getConfigurationDiagnostic());

        add(mainContainer, BorderLayout.CENTER);

        cardLayout.show(mainContainer, "LOGIN");
    }

    private void initPanels() {
        // 1. Instantiate ALL panel objects
        loginPanel = new LoginPanel();
        registerPanel = new RegistrationPanel();
        roleSelectionPanel = new RoleSelectionPanel();
        preferencesPanel = new PreferencesPanel();
        profileSetupPanel = new ProfileSetupPanel();
        forgotPasswordPanel = new ForgotPasswordPanel();
        regVerifyPanel = new RegistrationVerificationPanel();
        passVerifyPanel = new PasswordResetVerificationPanel();
        resetPasswordPanel = new ResetPasswordPanel();
        dashboardPanel = new DashboardOverviewPanel();
        helpPanel = new HelpPanel();
        aboutPanel = new AboutPanel();
        welcomePanel = new WelcomePanel();
        ngoSetupPanel = new NgoSetupPanel();
        adminSetupPanel = new AdminSetupPanel();

        // 2. Add instantiated panels to CardLayout container
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(registerPanel, "REGISTER");
        mainContainer.add(roleSelectionPanel, "ROLE");
        mainContainer.add(preferencesPanel, "PREFERENCES");
        mainContainer.add(ngoSetupPanel, "NGO_SETUP");
        mainContainer.add(adminSetupPanel, "ADMIN_SETUP");
        mainContainer.add(profileSetupPanel, "PROFILE_SETUP");
        mainContainer.add(forgotPasswordPanel, "FORGOT_PASS");
        mainContainer.add(regVerifyPanel, "REGISTRATION_VERIFY");
        mainContainer.add(passVerifyPanel, "PASSWORD_VERIFY");
        mainContainer.add(resetPasswordPanel, "RESET_PASS");
        mainContainer.add(dashboardPanel, "DASHBOARD");
        mainContainer.add(helpPanel, "HELP");
        mainContainer.add(aboutPanel, "ABOUT");
        mainContainer.add(welcomePanel, "WELCOME");

        wireNavigation();
    }

    private void wireNavigation() {
    // --- 1. Login Listeners ---
    loginPanel.setLoginListener(e -> {
        final String email = loginPanel.getEmail();
        final String password = loginPanel.getPassword();
        loginPanel.setBusy(true);
        new javax.swing.SwingWorker<User, Void>() {
            @Override protected User doInBackground() {
                User user = userDAO.authenticateUser(email, PasswordHasher.hashPassword(password));
                if (user == null) userDAO.recordFailedLogin(email);
                return user;
            }
            @Override protected void done() {
                loginPanel.setBusy(false);
                try {
                    User user = get();
                    if (user == null) { loginPanel.clearPassword(); loginPanel.showStatus("Email or password is incorrect.", true); return; }
                    if (userDAO.isAccountLocked(email)) { loginPanel.showStatus("Sign-in is temporarily unavailable. Please try again later.", true); return; }
                    if (!user.isVerified()) { pendingVerificationEmail = user.getEmail(); cardLayout.show(mainContainer, "REGISTRATION_VERIFY"); return; }
                    if (user.isDeleted() || !user.isActive()) { loginPanel.clearPassword(); loginPanel.showStatus("This account is currently unavailable. Contact support if you need help.", true); return; }
                    if ("NGO".equalsIgnoreCase(user.getRole())) {
                        NGO ngo = ngoDAO.getNGOByUserId(user.getUserId());
                        if (ngo == null || !ngo.isVerifiedStatus()) {
                            loginPanel.clearPassword();
                            loginPanel.showStatus("Your organisation is awaiting administrator approval. You can sign in after it has been approved.", false);
                            return;
                        }
                    }
                    if ("Administrator".equalsIgnoreCase(user.getRole())) {
                        String approval = userDAO.getAdminApprovalStatus(user.getUserId());
                        if (!"APPROVED".equalsIgnoreCase(approval)) { loginPanel.showStatus("Administrator access is not currently available for this account.", true); return; }
                    }
                    userDAO.recordSuccessfulLogin(user.getUserId());
                    userDAO.updateLastLogin(user.getUserId());
                    recordAuditEvent(user.getUserId(), "LOGIN_SUCCESS", "USER", user.getUserId(),
                            "Successful sign-in as " + user.getRole(), "Success");
                    loginPanel.saveRememberedEmail();
                    currentUser = user;
                    loginPanel.clearFields();
                    showRoleDashboard(user);
                } catch (Exception ex) { AppLogger.error("Login failed", ex); loginPanel.showStatus("Sign-in is temporarily unavailable. Please try again.", true); }
            }
        }.execute();
    });
    loginPanel.setGoToRegisterListener(e -> { registrationState.clear(); registerPanel.clearFields(); roleSelectionPanel.clearFields(); registerPanel.setRole("Volunteer"); cardLayout.show(mainContainer, "REGISTER"); });
    loginPanel.setForgotPasswordListener(e -> {
        forgotPasswordPanel.setEmail(loginPanel.getEmail());
        cardLayout.show(mainContainer, "FORGOT_PASS");
    });
    loginPanel.setHelpListener(e -> cardLayout.show(mainContainer, "HELP"));
    loginPanel.setAboutListener(e -> cardLayout.show(mainContainer, "ABOUT"));
    helpPanel.setBackListener(e -> cardLayout.show(mainContainer, "LOGIN"));
    aboutPanel.setBackListener(e -> cardLayout.show(mainContainer, "LOGIN"));

    // --- 2. Registration Wizard: Account -> Role & Details -> Preferences/Profile -> Verification ---
    registerPanel.setNextListener(e -> {
        registrationState.setFullName(registerPanel.getNameInput());
        registrationState.setEmail(registerPanel.getEmailInput());
        registrationState.setPassword(registerPanel.getPasswordInput());
        roleSelectionPanel.setSelectedRole(registrationState.getRole());
        cardLayout.show(mainContainer, "ROLE");
    });
    registerPanel.setBackToLoginListener(e -> cardLayout.show(mainContainer, "LOGIN"));

    roleSelectionPanel.setContinueListener(e -> {
        registrationState.setRole(roleSelectionPanel.getSelectedRole());
        if ("NGO".equalsIgnoreCase(registrationState.getRole())) {
            registrationState.setOrganizationName(roleSelectionPanel.getOrgName());
            registrationState.setRegistrationNumber(roleSelectionPanel.getRegNumber());
            registrationState.setRace(""); registrationState.setGender(""); registrationState.setDateOfBirth("");
            cardLayout.show(mainContainer, "NGO_SETUP");
        } else {
            registrationState.setRace(roleSelectionPanel.getSelectedRace());
            registrationState.setGender(roleSelectionPanel.getGender());
            java.time.LocalDate dob = roleSelectionPanel.getDateOfBirth();
            registrationState.setDateOfBirth(dob == null ? "" : dob.toString());
            registrationState.setOrganizationName(""); registrationState.setRegistrationNumber("");
            cardLayout.show(mainContainer, "PREFERENCES");
        }
    });
    roleSelectionPanel.setBackListener(e -> {
        registerPanel.restoreInputs(registrationState.getFullName(), registrationState.getEmail());
        cardLayout.show(mainContainer, "REGISTER");
    });

    // Step 3 Back Buttons
    preferencesPanel.setBackListener(e -> cardLayout.show(mainContainer, "ROLE"));
    ngoSetupPanel.setBackListener(e -> cardLayout.show(mainContainer, "ROLE"));
    adminSetupPanel.setBackListener(e -> cardLayout.show(mainContainer, "ROLE"));

    // Step 3 Completion Next Route
    // Volunteers go to additional individual Profile Setup
    preferencesPanel.setFinishListener(e -> cardLayout.show(mainContainer, "PROFILE_SETUP"));
    
    // NGO & Admin proceed directly to Verification (bypassing duplicate Profile Setup)
    ngoSetupPanel.setSaveListener(e -> {
        ngoSetupPanel.setBusy(true);
        javax.swing.Timer t = new javax.swing.Timer(120, ev -> {
            ((javax.swing.Timer) ev.getSource()).stop();
            boolean ok = completeRegistration("NGO");
            ngoSetupPanel.setBusy(false);
            if (ok) cardLayout.show(mainContainer, "REGISTRATION_VERIFY");
        });
        t.setRepeats(false); t.start();
    });

    // Volunteer Profile Setup Back & Next Routing
    profileSetupPanel.setBackListener(e -> cardLayout.show(mainContainer, "PREFERENCES"));
    profileSetupPanel.setSaveProfileListener(e -> {
        profileSetupPanel.setBusy(true);
        javax.swing.Timer t = new javax.swing.Timer(120, ev -> {
            ((javax.swing.Timer) ev.getSource()).stop();
            boolean ok = completeRegistration("Volunteer");
            profileSetupPanel.setBusy(false);
            if (ok) cardLayout.show(mainContainer, "REGISTRATION_VERIFY");
        });
        t.setRepeats(false); t.start();
    });

    // Verification & Welcome
    regVerifyPanel.setVerifyListener(e -> {
        if (pendingVerificationEmail == null) {
            regVerifyPanel.showStatus("No pending registration was found. Please register again.", true);
            return;
        }
        final String email = pendingVerificationEmail;
        final String code = regVerifyPanel.getCodeInput();
        regVerifyPanel.setBusy(true);
        regVerifyPanel.showStatus("Verifying your code...", false);
        new javax.swing.SwingWorker<User, Void>() {
            private int remaining;
            @Override protected User doInBackground() {
                if (!userDAO.verifyEmail(email, code)) {
                    remaining = userDAO.getVerificationAttemptsRemaining(email);
                    return null;
                }
                User user = userDAO.getUserByEmail(email);
                if (user != null) userDAO.updateLastLogin(user.getUserId());
                return user;
            }
            @Override protected void done() {
                regVerifyPanel.setBusy(false);
                try {
                    User verifiedUser = get();
                    if (verifiedUser == null) {
                        regVerifyPanel.clearFields();
                        regVerifyPanel.showStatus(remaining > 0
                                ? "That code is incorrect or expired. " + remaining + " attempts remaining."
                                : "Too many incorrect attempts. Request a new code.", true);
                        return;
                    }
                    currentUser = verifiedUser;
                    pendingVerificationEmail = null;
                    regVerifyPanel.clearFields();
                    if ("NGO".equalsIgnoreCase(verifiedUser.getRole())) {
                        currentUser = null;
                        regVerifyPanel.showStatus("Email verified. Your organisation application has been submitted for administrator review. Preparing your confirmation email...", false);
                        final String organisationName = ngoDAO.getNGOByUserId(verifiedUser.getUserId()) != null
                                ? ngoDAO.getNGOByUserId(verifiedUser.getUserId()).getOrgName() : verifiedUser.getFullName();
                        new javax.swing.SwingWorker<Boolean, Void>() {
                            @Override protected Boolean doInBackground() {
                                return EmailSender.sendNgoPendingReviewNotice(verifiedUser.getEmail(), organisationName);
                            }
                            @Override protected void done() {
                                loginPanel.showStatus("Email verified successfully. Your organisation application is being reviewed. We will email you when it is approved or rejected.", false);
                                javax.swing.Timer next = new javax.swing.Timer(900, x -> {
                                    ((javax.swing.Timer)x.getSource()).stop();
                                    cardLayout.show(mainContainer, "LOGIN");
                                });
                                next.setRepeats(false); next.start();
                            }
                        }.execute();
                        return;
                    }
                    if ("Administrator".equalsIgnoreCase(verifiedUser.getRole())
                            && "PENDING".equalsIgnoreCase(userDAO.getAdminApprovalStatus(verifiedUser.getUserId()))) {
                        currentUser = null;
                        loginPanel.showStatus("Email verified. Administrator access is pending approval.", false);
                        cardLayout.show(mainContainer, "LOGIN");
                        return;
                    }
                    cardLayout.show(mainContainer, "WELCOME");
                } catch (Exception ex) {
                    AppLogger.error("Registration verification failed", ex);
                    regVerifyPanel.showStatus("Verification is temporarily unavailable. Please try again.", true);
                }
            }
        }.execute();
    });
    regVerifyPanel.setResendListener(e -> {
        if (pendingVerificationEmail == null) {
            regVerifyPanel.showStatus("No pending registration was found. Please register again.", true);
            return;
        }
        final String email = pendingVerificationEmail;
        regVerifyPanel.setBusy(true);
        regVerifyPanel.showStatus("Sending a new code...", false);
        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() {
                String newCode = PasswordHasher.generateVerificationCode();
                if (!userDAO.updateVerificationCode(email, newCode)) return false;
                return EmailSender.sendVerificationCode(email, newCode);
            }
            @Override protected void done() {
                regVerifyPanel.setBusy(false);
                try {
                    if (get()) { regVerifyPanel.startResendCooldown(60); regVerifyPanel.showStatus("A new verification code has been sent.", false); }
                    else regVerifyPanel.showStatus("A new code could not be sent yet. Check your connection or wait for the resend cooldown.", true);
                } catch (Exception ex) { AppLogger.error("Verification resend failed", ex); regVerifyPanel.showStatus("A new code could not be sent. Please try again.", true); }
            }
        }.execute();
    });
    regVerifyPanel.setBackListener(e -> {
        String role = roleSelectionPanel.getSelectedRole();
        if ("NGO".equalsIgnoreCase(role)) {
            cardLayout.show(mainContainer, "NGO_SETUP");
        } else {
            cardLayout.show(mainContainer, "PROFILE_SETUP");
        }
    });

    welcomePanel.setGoToDashboardListener(e -> {
        if (currentUser != null) {
            showRoleDashboard(currentUser);
        } else {
            topNavPanel.setVisible(true);
            cardLayout.show(mainContainer, "DASHBOARD");
        }
    });

    // --- 3. Password Recovery Flow ---
    forgotPasswordPanel.setSendResetLinkListener(e -> {
        final String email = forgotPasswordPanel.getEmail();
        forgotPasswordPanel.setBusy(true);
        forgotPasswordPanel.showStatus("Processing your request...", false);
        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() {
                if (!userDAO.emailExists(email)) return false;
                String code = PasswordHasher.generateVerificationCode();
                Timestamp expiry = Timestamp.from(Instant.now().plusSeconds(15 * 60));
                if (!userDAO.createResetToken(email, code, expiry)) return false;
                pendingResetEmail = email;
                pendingResetCode = code;
                return EmailSender.sendPasswordResetCode(email, code);
            }
            @Override protected void done() {
                forgotPasswordPanel.setBusy(false);
                // Deliberately identical outward response whether the account exists or mail succeeds.
                forgotPasswordPanel.showStatus("If an account exists for that email, a reset code has been sent.", false);
                forgotPasswordPanel.clearFields();
                cardLayout.show(mainContainer, "PASSWORD_VERIFY");
            }
        }.execute();
    });
    forgotPasswordPanel.setBackToLoginListener(e -> cardLayout.show(mainContainer, "LOGIN"));

    passVerifyPanel.setConfirmCodeListener(e -> {
        if (pendingResetEmail == null) { passVerifyPanel.showStatus("No pending password reset was found. Start again from Forgot Password.", true); return; }
        final String email = pendingResetEmail;
        final String code = passVerifyPanel.getCodeInput();
        passVerifyPanel.setBusy(true);
        passVerifyPanel.showStatus("Verifying your reset code...", false);
        new javax.swing.SwingWorker<User, Void>() {
            private int remaining;
            @Override protected User doInBackground() {
                User user = userDAO.validateResetToken(email, code);
                if (user == null) remaining = userDAO.getResetAttemptsRemaining(email);
                return user;
            }
            @Override protected void done() {
                passVerifyPanel.setBusy(false);
                try {
                    User user = get();
                    if (user == null) {
                        passVerifyPanel.clearFields();
                        passVerifyPanel.showStatus(remaining > 0
                                ? "That reset code is incorrect or expired. " + remaining + " attempts remaining."
                                : "Too many incorrect attempts. Request a new reset code.", true);
                        return;
                    }
                    pendingResetCode = code;
                    passVerifyPanel.clearFields();
                    cardLayout.show(mainContainer, "RESET_PASS");
                } catch (Exception ex) { AppLogger.error("Reset-code verification failed", ex); passVerifyPanel.showStatus("Verification is temporarily unavailable. Please try again.", true); }
            }
        }.execute();
    });
    passVerifyPanel.setResendListener(e -> {
        if (pendingResetEmail == null) {
            JOptionPane.showMessageDialog(this, "No pending password reset found.",
                    "Resend Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String code = PasswordHasher.generateVerificationCode();
        Timestamp expiry = Timestamp.from(Instant.now().plusSeconds(15 * 60));
        if (!userDAO.createResetToken(pendingResetEmail, code, expiry)) {
            JOptionPane.showMessageDialog(this,
                    "Please wait before requesting another reset code. The resend cooldown is still active.",
                    "Resend Cooldown", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        pendingResetCode = code;
        deliverResetCode(pendingResetEmail, code, true);
    });
    passVerifyPanel.setCancelListener(e -> {
        pendingResetEmail = null;
        pendingResetCode = null;
        passVerifyPanel.clearFields();
        cardLayout.show(mainContainer, "LOGIN");
    });

    resetPasswordPanel.setSubmitListener(e -> {
        if (pendingResetCode == null) {
            JOptionPane.showMessageDialog(this, "No pending password reset found. Please start again.",
                    "Reset Error", JOptionPane.ERROR_MESSAGE);
            cardLayout.show(mainContainer, "FORGOT_PASS");
            return;
        }
        String newHash = PasswordHasher.hashPassword(resetPasswordPanel.getNewPassword());
        boolean ok = userDAO.resetPassword(pendingResetEmail, pendingResetCode, newHash);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Could not reset the password. The code may have expired.",
                    "Reset Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        pendingResetEmail = null;
        pendingResetCode = null;
        resetPasswordPanel.clearFields();
        JOptionPane.showMessageDialog(this,
                "Your password has been reset successfully. You can now sign in with your new password.",
                "Password Reset Successful",
                JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainContainer, "LOGIN");
    });

    // --- 4. Top Navigation Bar ---
    topNavPanel.setDashboardListener(e -> cardLayout.show(mainContainer, "DASHBOARD"));
    topNavPanel.setProfileListener(e -> cardLayout.show(mainContainer, "PROFILE_SETUP"));
    topNavPanel.setLogoutListener(e -> handleLogout());
}

    /**
     * Persists the user (and role-specific profile) being registered, generates
     * a verification code, stores it, and emails it. Returns true on success.
     */
    private boolean completeRegistration(String uiRole) {
        String fullName = registrationState.getFullName();
        String email = registrationState.getEmail();
        String passwordHash = PasswordHasher.hashPassword(registrationState.getPassword());
        String verificationCode = PasswordHasher.generateVerificationCode();

        // An unverified row represents an in-progress registration, not a completed
        // account. If the user starts registration again with the same address,
        // replace that pending registration (role profiles cascade on delete) rather
        // than permanently blocking the email. Verified accounts remain protected.
        User existingUser = userDAO.getUserByEmail(email);
        if (existingUser != null) {
            if (existingUser.isVerified()) {
                JOptionPane.showMessageDialog(this,
                        "An account with this email already exists. Please sign in instead.",
                        "Email Already Registered", JOptionPane.ERROR_MESSAGE);
                cardLayout.show(mainContainer, "LOGIN");
                return false;
            }
            if (!userDAO.deleteUser(existingUser.getUserId())) {
                JOptionPane.showMessageDialog(this,
                        "We couldn't refresh your pending registration. Please try again.",
                        "Registration Pending", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // The users.role column is an ENUM('Volunteer','NGO','Administrator').
        String dbRole = "Admin".equalsIgnoreCase(uiRole) ? "Administrator" : uiRole;

        User user = new User(fullName, email, passwordHash, dbRole);
        user.setVerificationCode(verificationCode);

        int userId = userDAO.registerUser(user);
        if (userId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "We couldn't save your account. Please check the database connection and try again.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean profileCreated = true;
        if ("NGO".equalsIgnoreCase(uiRole)) {
            // ngos table has no address/contact_email columns, so only fields
            // that map to real columns (org_name, registration_number,
            // contact_person, contact_phone, verified_status, website) are saved.
            NGO ngo = new NGO(userId, registrationState.getOrganizationName(), fullName, ngoSetupPanel.getContactPhone());
            ngo.setRegistrationNumber(registrationState.getRegistrationNumber());
            ngo.setVerifiedStatus(false);
            profileCreated = ngoDAO.createNGOProfile(ngo);
            if (profileCreated) {
                profileCreated = ngoDAO.updateRegistrationDetails(userId, ngoSetupPanel.getOperatingRegion(), ngoSetupPanel.getOrgScale(), ngoSetupPanel.getMissionStatement(), String.join(", ", ngoSetupPanel.getFocusAreas()), String.join(", ", ngoSetupPanel.getRequiredSkills()));
            }
        } else if ("Volunteer".equalsIgnoreCase(uiRole)) {
            // volunteers table has no status column, so it's simply not set.
            Volunteer volunteer = new Volunteer();
            volunteer.setUserId(userId);
            volunteer.setPhoneNumber(profileSetupPanel.getPhoneNumber());
            volunteer.setRace(registrationState.getRace());
            volunteer.setGender(registrationState.getGender());
            if (!registrationState.getDateOfBirth().isBlank()) volunteer.setDateOfBirth(java.sql.Date.valueOf(registrationState.getDateOfBirth()));
            volunteer.setLocation(profileSetupPanel.getLocationInput());
            volunteer.setBio(profileSetupPanel.getBio());
            volunteer.setSkills(String.join(", ", preferencesPanel.getSkills()));
            volunteer.setInterests(String.join(", ", preferencesPanel.getSelectedCauses()));
            String availability = profileSetupPanel.getAvailability();
            volunteer.setAvailability(availability != null && !availability.isBlank()
                    ? availability : preferencesPanel.getAvailability());
            profileCreated = volunteerDAO.createVolunteerProfile(volunteer);
        }

        if (!profileCreated) {
            // Avoid leaving an account that can never complete registration because
            // its role-specific profile failed to persist.
            userDAO.deleteUser(userId);
            JOptionPane.showMessageDialog(this,
                    "Your account could not be completed because your profile details could not be saved. Nothing was kept; please try again.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        pendingVerificationEmail = email;
        deliverCode(email, verificationCode, false);

        // Clear every form used along the registration path now that their
        // data has been read and saved, so stale values aren't reused later.
        registrationState.clear();
        registerPanel.clearFields();
        roleSelectionPanel.clearFields();
        ngoSetupPanel.clearFields();
        adminSetupPanel.clearFields();
        preferencesPanel.clearFields();
        profileSetupPanel.clearFields();

        return true;
    }

    /** Sends a registration verification code by email. */
    private void deliverCode(String email, String code, boolean isResend) {
        boolean sent = EmailSender.sendVerificationCode(email, code);
        if (!sent) {
            String reason = EmailSender.getLastError();
            JOptionPane.showMessageDialog(this,
                    "Your registration was saved securely, but we could not deliver the verification email. "
                            + "You can use Resend Code after the cooldown.\n\n"
                            + reason,
                    "Verification Email Not Delivered", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Sends a password-reset code by email. */
    private void deliverResetCode(String email, String code, boolean isResend) {
        boolean sent = EmailSender.sendPasswordResetCode(email, code);
        if (!sent) {
            String reason = EmailSender.getLastError();
            JOptionPane.showMessageDialog(this,
                    "The reset request was saved, but we could not deliver the email. "
                            + "You can request another code after the cooldown.\n\n"
                            + reason,
                    "Reset Email Not Delivered", JOptionPane.WARNING_MESSAGE);
        }
    }



    /** Personalizes the dashboard greeting and stats for the signed-in user's role. */
    private void populateDashboard(User user) {
        dashboardPanel.setUserInfo(user.getFullName(), user.getRole());

        if ("Volunteer".equalsIgnoreCase(user.getRole())) {
            Volunteer volunteer = volunteerDAO.getVolunteerByUserId(user.getUserId());
            if (volunteer != null) {
                dashboardPanel.updateStats((int) Math.round(volunteer.getTotalHours()),
                        volunteer.getCompletedEventsCount(), 0);
                return;
            }
        }
        dashboardPanel.updateStats(0, 0, 0);
    }

    /**
     * Shows the right dashboard for the signed-in user's role. NGO accounts get
     * their own full-featured dashboard (events, applications, attendance,
     * reports) with its own internal navigation, so the shared top nav bar is
     * hidden while it's showing. Other roles use the generic dashboard.
     */
    private void showRoleDashboard(User user) {
        if ("NGO".equalsIgnoreCase(user.getRole())) {
            if (ngoDashboardPanel != null) {
                mainContainer.remove(ngoDashboardPanel);
            }
            ngoDashboardPanel = new NGODashboardPanel(user.getUserId());
            ngoDashboardPanel.setLogoutListener(this::handleLogout);
            mainContainer.add(ngoDashboardPanel, "NGO_DASHBOARD");
            topNavPanel.setVisible(false);
            cardLayout.show(mainContainer, "NGO_DASHBOARD");
            return;
        }

        if ("Volunteer".equalsIgnoreCase(user.getRole())) {
            if (volunteerDashboardPanel != null) {
                mainContainer.remove(volunteerDashboardPanel);
            }
            volunteerDashboardPanel = new VolunteerDashboardPanel(user.getUserId());
            volunteerDashboardPanel.setLogoutListener(this::handleLogout);
            mainContainer.add(volunteerDashboardPanel, "VOLUNTEER_DASHBOARD");
            topNavPanel.setVisible(false);
            cardLayout.show(mainContainer, "VOLUNTEER_DASHBOARD");
            return;
        }

        if ("Administrator".equalsIgnoreCase(user.getRole())) {
            if (adminDashboardPanel != null) {
                mainContainer.remove(adminDashboardPanel);
            }
            adminDashboardPanel = new AdminDashboardPanel(user.getUserId());
            adminDashboardPanel.setLogoutListener(this::handleLogout);
            mainContainer.add(adminDashboardPanel, "ADMIN_DASHBOARD");
            topNavPanel.setVisible(false);
            cardLayout.show(mainContainer, "ADMIN_DASHBOARD");
            return;
        }

        populateDashboard(user);
        topNavPanel.setVisible(true);
        topNavPanel.setAdminLinksVisible("Administrator".equalsIgnoreCase(user.getRole()));
        cardLayout.show(mainContainer, "DASHBOARD");
    }

    /** Signs the current user out and returns to the login screen — used by the top nav bar and any embedded dashboard. */
    private void handleLogout() {
        if (currentUser != null) recordAuditEvent(currentUser.getUserId(), "LOGOUT", "USER", currentUser.getUserId(), "User signed out", "Success");
        currentUser = null;
        topNavPanel.setAdminLinksVisible(false);
        topNavPanel.setVisible(false);
        cardLayout.show(mainContainer, "LOGIN");
    }

    public static void main(String[] args) {
        // The application is South Africa-specific. Set the JVM default before any
        // Swing component, DAO, logger or legacy Calendar/Date code is created.
        // Africa/Johannesburg is SAST (UTC+02:00) and has no daylight-saving shift.
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Africa/Johannesburg")));
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::closeConnection));
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
    private void recordAuditEvent(int actorUserId, String action, String entityType, int entityId, String details, String status) {
        try {
            AuditLog log = new AuditLog();
            log.setUserID(actorUserId); log.setAction(action); log.setEntityType(entityType); log.setEntityID(entityId);
            log.setIpAddress("local"); log.setDetails(details); log.setStatus(status);
            new AuditLogDAO().addAuditLog(log);
        } catch (RuntimeException ex) {
            AppLogger.error("Unable to persist audit event " + action, ex);
        }
    }

}