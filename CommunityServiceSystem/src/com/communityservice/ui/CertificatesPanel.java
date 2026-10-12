package com.communityservice.ui;

import com.communityservice.dao.CertificateDAO;
import com.communityservice.models.Certificate;
import com.communityservice.models.PortalVolunteer;
import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.utils.AppLogger;
import com.communityservice.utils.CertificateService;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Lists certificates earned by the logged-in volunteer and opens their PDFs. */
public class CertificatesPanel extends JPanel {
    private final int userId;
    private final CertificateDAO certificateDAO = new CertificateDAO();
    private final DefaultListModel<Certificate> model = new DefaultListModel<>();
    private final JList<Certificate> list = new JList<>(model);
    private final JButton openButton = AppTheme.createPrimaryButton("Open PDF");
    private final JLabel hint = new JLabel("Select a certificate to open its PDF.");

    public CertificatesPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout(12, 12));
        setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("My Certificates");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIColors.TEXT_DARK);
        JLabel subtitle = new JLabel("Certificates earned after completed community-service events.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UIColors.TEXT_GREY);
        heading.add(title); heading.add(Box.createVerticalStrut(4)); heading.add(subtitle);
        add(heading, BorderLayout.NORTH);

        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setFixedCellHeight(56);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object value, int index, boolean selected, boolean focus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(l, value, index, selected, focus);
                Certificate c = (Certificate) value;
                String number = c.getCertificationNumber() == null ? "Certificate #" + c.getCertID() : c.getCertificationNumber();
                String date = c.getIssueDate() == null ? "Date unavailable" : c.getIssueDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
                label.setText(number + "   |   Issued " + date);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return label;
            }
        });
        list.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) updateActionState(); });
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 8));
        bottom.setOpaque(false);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(UIColors.TEXT_GREY);
        bottom.add(hint, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton refresh = AppTheme.createPrimaryButton("Refresh");
        refresh.setPreferredSize(new Dimension(120, 40));
        openButton.setPreferredSize(new Dimension(140, 40));
        refresh.addActionListener(e -> loadCertificates());
        openButton.addActionListener(e -> openSelected());
        actions.add(refresh); actions.add(openButton);
        bottom.add(actions, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
        loadCertificates();
    }

    private void updateActionState() {
        openButton.setEnabled(list.getSelectedValue() != null);
        hint.setText(list.getSelectedValue() == null ? "Select a certificate to open its PDF." : "Ready to open the selected certificate.");
    }

    private void loadCertificates() {
        model.clear();
        PortalVolunteer volunteer = new VolunteerPortalVolunteerDAO().getVolunteerByUserID(userId);
        if (volunteer == null) { hint.setText("Your volunteer profile could not be found."); updateActionState(); return; }
        List<Certificate> certificates = certificateDAO.getCertificatesForVolunteer(volunteer.getVolunteerID());
        for (Certificate certificate : certificates) model.addElement(certificate);
        if (model.isEmpty()) hint.setText("No certificates yet. Certificates appear after completed attendance is recorded.");
        updateActionState();
    }

    private void openSelected() {
        Certificate selected = list.getSelectedValue();
        if (selected == null) return;
        openButton.setEnabled(false);
        openButton.setText("Opening...");
        hint.setText("Preparing your certificate...");
        new SwingWorker<Path, Void>() {
            @Override protected Path doInBackground() {
                String stored = selected.getPdfURL();
                if (stored != null && !stored.isBlank()) {
                    Path p = Paths.get(stored);
                    if (Files.isRegularFile(p)) return p;
                }
                return CertificateService.rebuildPdf(selected);
            }
            @Override protected void done() {
                openButton.setText("Open PDF");
                try {
                    Path path = get();
                    if (path == null || !Files.isRegularFile(path)) {
                        hint.setText("The PDF could not be prepared. Please try Refresh or contact support.");
                        return;
                    }
                    if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                        hint.setText("PDF saved at: " + path.toAbsolutePath());
                        JOptionPane.showMessageDialog(CertificatesPanel.this,
                                "The PDF was created, but this computer cannot open files automatically.\n\nSaved at:\n" + path.toAbsolutePath(),
                                "Certificate Ready", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    Desktop.getDesktop().open(path.toFile());
                    hint.setText("Opened " + path.getFileName());
                } catch (Exception ex) {
                    AppLogger.error("Could not open certificate PDF", ex);
                    hint.setText("The certificate could not be opened. Please try again.");
                    JOptionPane.showMessageDialog(CertificatesPanel.this,
                            "The certificate PDF could not be opened.\n" + ex.getMessage(), "Open Failed", JOptionPane.ERROR_MESSAGE);
                } finally { updateActionState(); }
            }
        }.execute();
    }

    @Override public String toString() { return "My Certificates"; }
}
