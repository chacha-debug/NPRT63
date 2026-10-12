package com.communityservice.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import com.communityservice.dao.UserDAO;
import com.communityservice.models.User;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Administrator dashboard shell: a top bar (brand + page title + account
 * icon), a left sidebar (Dashboard / Users / Reports / Policies / Logs /
 * Verify NGOs / Logout), and a CardLayout content area — embedded as a
 * single card in the host app's own CardLayout. Styled identically to the
 * NGO and volunteer dashboards (same sidebar width, same RoundedNavButton,
 * same RoundPersonIcon) so all three feel like one consistent app.
 */
public class AdminDashboardPanel extends JPanel {

    private final int user_id;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel pageTitleLabel;
    private final Map<String, RoundedNavButton> navigationButtons = new LinkedHashMap<>();

    private Runnable logoutAction;
    private AdminLogsPanel logsPanel;
    private NgoVerificationPanel ngoVerificationPanel;
    private com.communityservice.dao.NGODAO verificationNgoDAO;

    public AdminDashboardPanel(int user_id) {
        this.user_id = user_id;
        setLayout(new BorderLayout());
        setBackground(UIColors.BACKGROUND);

        add(createTopBar(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);

        showPage("dashboard");
    }

    public void setLogoutListener(Runnable logoutAction) {
        this.logoutAction = logoutAction;
    }

    public int getUserID() {
        return user_id;
    }

    public void showPage(String page) {
        cardLayout.show(contentPanel, page);
        for (Map.Entry<String, RoundedNavButton> entry : navigationButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(page));
        }
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(pageTitleFor(page));
        }
        if ("logs".equals(page) && logsPanel != null) logsPanel.refreshLogs();
        if ("verifyNgos".equals(page) && ngoVerificationPanel != null && verificationNgoDAO != null)
            ngoVerificationPanel.setNGOs(verificationNgoDAO.getPendingVerificationNGOs());
    }

    private String pageTitleFor(String page) {
        switch (page) {
            case "users": return "User Management";
            case "reports": return "Reports";
            case "policies": return "System Policies";
            case "logs": return "Audit Logs";
            case "verifyNgos": return "Verify NGOs";
            case "settings": return "Settings";
            default: return "Dashboard";
        }
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIColors.PRIMARY);
        topBar.setPreferredSize(new Dimension(0, 62));

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        JLabel brand = new JLabel("Community Service+");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Arial", Font.BOLD, 20));
        brandRow.add(brand);
        topBar.add(brandRow, BorderLayout.WEST);

        pageTitleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        pageTitleLabel.setForeground(Color.WHITE);
        pageTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(pageTitleLabel, BorderLayout.CENTER);

        JPanel accountRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accountRow.setOpaque(false);
        accountRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        JLabel roleLabel = new JLabel(getDisplayName());
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        accountRow.add(roleLabel);
        accountRow.add(new RoundPersonIcon(34, Color.WHITE, UIColors.PRIMARY));
        topBar.add(accountRow, BorderLayout.EAST);

        return topBar;
    }

    private String getDisplayName() {
        try {
            User user = new UserDAO().getUserById(user_id);
            if (user != null && user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
                return user.getFullName().trim();
            }
        } catch (Exception ignored) { }
        return "Administrator";
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIColors.PRIMARY);
        sidebar.setPreferredSize(new Dimension(195, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        sidebar.add(createSidebarButton("Dashboard", "dashboard"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Users", "users"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Reports", "reports"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Policies", "policies"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Logs", "logs"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Verify NGOs", "verifyNgos"));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(createSidebarButton("Settings", "settings"));
        sidebar.add(Box.createVerticalGlue());

        RoundedNavButton logoutButton = createSidebarButton("Logout", null);
        logoutButton.addActionListener(e -> {
            if (logoutAction != null) {
                logoutAction.run();
            }
        });
        sidebar.add(logoutButton);

        return sidebar;
    }

    private RoundedNavButton createSidebarButton(String text, String page) {
        RoundedNavButton button = new RoundedNavButton(text);

        if (page != null) {
            button.addActionListener(e -> showPage(page));
            navigationButtons.put(page, button);
        }

        return button;
    }

    private JPanel createContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIColors.BACKGROUND);

        contentPanel.add(new AdminMainPanel(this, user_id), "dashboard");
        contentPanel.add(new AdminUsersPanel(user_id), "users");
        contentPanel.add(new AdminReportsPanel(user_id), "reports");
        contentPanel.add(new AdminPolicyPanel(user_id), "policies");
        logsPanel = new AdminLogsPanel();
        contentPanel.add(logsPanel, "logs");
        contentPanel.add(createNgoVerificationPage(), "verifyNgos");
        contentPanel.add(new SharedSettingsPanel(user_id), "settings");

        return contentPanel;
    }

    private JPanel createNgoVerificationPage() {
        com.communityservice.dao.NGODAO ngoDAO = new com.communityservice.dao.NGODAO();
        verificationNgoDAO = ngoDAO;
        com.communityservice.dao.UserDAO userDAO = new com.communityservice.dao.UserDAO();
        com.communityservice.dao.AuditLogDAO auditLogDAO = new com.communityservice.dao.AuditLogDAO();
        NgoVerificationPanel panel = new NgoVerificationPanel();
        ngoVerificationPanel = panel;
        panel.setNGOs(ngoDAO.getPendingVerificationNGOs());
        panel.setApproveListener(ngoId -> {
            panel.setBusy(true);
            new javax.swing.SwingWorker<Boolean, Void>() {
                com.communityservice.models.NGO ngo;
                com.communityservice.models.User user;
                boolean mailSent;
                @Override protected Boolean doInBackground() {
                    ngo = ngoDAO.getNGOById(ngoId);
                    if (ngo == null) return false;
                    user = userDAO.getUserById(ngo.getUserId());
                    boolean updated = ngoDAO.verifyNGO(ngoId);
                    if (!updated) return false;
                    logNgoAction(auditLogDAO, ngoDAO, ngoId, "Approve NGO");
                    if (user != null) mailSent = com.communityservice.utils.EmailSender.sendNgoApprovalNotice(user.getEmail(), ngo.getOrgName());
                    return true;
                }
                @Override protected void done() {
                    panel.setBusy(false); panel.setNGOs(ngoDAO.getPendingVerificationNGOs());
                    try { if (get()) {
                        String msg = "Organisation approved successfully." + (mailSent ? " Approval email sent." : " The approval was saved, but the notification email could not be sent.");
                        javax.swing.JOptionPane.showMessageDialog(panel, msg, "NGO Approval", mailSent ? javax.swing.JOptionPane.INFORMATION_MESSAGE : javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else javax.swing.JOptionPane.showMessageDialog(panel, "The organisation could not be approved.", "Approval Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) { com.communityservice.utils.AppLogger.error("NGO approval failed", ex); }
                }
            }.execute();
        });
        panel.setRejectListener((ngoId, reason) -> {
            panel.setBusy(true);
            new javax.swing.SwingWorker<Boolean, Void>() {
                com.communityservice.models.NGO ngo;
                com.communityservice.models.User user;
                boolean mailSent;
                @Override protected Boolean doInBackground() {
                    ngo = ngoDAO.getNGOById(ngoId); if (ngo == null) return false;
                    user = userDAO.getUserById(ngo.getUserId());
                    // Preserve the account as a rejected/inactive record so its status remains auditable.
                    boolean removed = userDAO.softDeleteUser(ngo.getUserId(), user_id, "NGO registration rejected: " + reason);
                    if (!removed) return false;
                    logNgoActionWithDetails(auditLogDAO, ngoId, "Reject NGO", "Reject NGO — " + ngo.getOrgName() + ". Reason: " + reason);
                    if (user != null) mailSent = com.communityservice.utils.EmailSender.sendNgoRejectionNotice(user.getEmail(), ngo.getOrgName(), reason);
                    return true;
                }
                @Override protected void done() {
                    panel.setBusy(false); panel.setNGOs(ngoDAO.getPendingVerificationNGOs());
                    try { if (get()) {
                        String msg="Organisation rejected."+(mailSent?" Notification email sent.":" The decision was saved, but the notification email could not be sent.");
                        javax.swing.JOptionPane.showMessageDialog(panel,msg,"NGO Rejection",mailSent?javax.swing.JOptionPane.INFORMATION_MESSAGE:javax.swing.JOptionPane.WARNING_MESSAGE);
                    } } catch(Exception ex){com.communityservice.utils.AppLogger.error("NGO rejection failed",ex);}
                }
            }.execute();
        });
        return panel;
    }

    private void logNgoAction(com.communityservice.dao.AuditLogDAO auditLogDAO,
                               com.communityservice.dao.NGODAO ngoDAO, int ngoId, String action) {
        com.communityservice.models.NGO ngo = ngoDAO.getNGOById(ngoId);
        com.communityservice.models.AuditLog log = new com.communityservice.models.AuditLog();
        log.setUserID(user_id);
        log.setAction(action);
        log.setEntityType("NGO");
        log.setEntityID(ngoId);
        log.setIpAddress("local");
        log.setDetails(action + (ngo != null ? (" — " + ngo.getOrgName()) : ""));
        log.setStatus("Success");
        auditLogDAO.addAuditLog(log);
    }

    private void logNgoActionWithDetails(com.communityservice.dao.AuditLogDAO auditLogDAO, int ngoId, String action, String details) {
        com.communityservice.models.AuditLog log = new com.communityservice.models.AuditLog();
        log.setUserID(user_id); log.setAction(action); log.setEntityType("NGO"); log.setEntityID(ngoId);
        log.setIpAddress("local"); log.setDetails(details); log.setStatus("Success"); auditLogDAO.addAuditLog(log);
    }
}
