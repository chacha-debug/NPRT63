package com.communityservice.tools;

import com.communityservice.DatabaseConnection;
import com.communityservice.utils.PasswordHasher;
import java.sql.*;
import javax.swing.*;

/** Repairs an existing administrator row created manually/with an old password format. */
public final class LegacyAdminRepair {
    private LegacyAdminRepair() {}
    public static void main(String[] args) {
        JTextField email = new JTextField(24);
        JPasswordField password = new JPasswordField(24);
        JPasswordField confirm = new JPasswordField(24);
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Existing administrator email")); p.add(email);
        p.add(new JLabel("Set a new password")); p.add(password);
        p.add(new JLabel("Confirm new password")); p.add(confirm);
        if (JOptionPane.showConfirmDialog(null,p,"Repair Legacy Administrator",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION) return;
        String e=email.getText().trim().toLowerCase(), a=new String(password.getPassword()), b=new String(confirm.getPassword());
        if (e.isEmpty() || !a.equals(b)) { error("Enter the admin email and matching passwords."); return; }
        String strength=PasswordHasher.getPasswordStrengthError(a); if(strength!=null){error(strength);return;}
        try(Connection c=DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            int userId;
            try(PreparedStatement q=c.prepareStatement("SELECT user_id, role FROM users WHERE LOWER(email)=?")){q.setString(1,e);try(ResultSet rs=q.executeQuery()){if(!rs.next()){error("No account exists with that email.");return;}userId=rs.getInt(1);}}
            try(PreparedStatement u=c.prepareStatement("UPDATE users SET password_hash=?, role='Administrator', is_verified=TRUE, is_active=TRUE, is_deleted=FALSE, admin_approval_status='APPROVED' WHERE user_id=?")){u.setString(1,PasswordHasher.hashPassword(a));u.setInt(2,userId);u.executeUpdate();}
            try(PreparedStatement q=c.prepareStatement("SELECT 1 FROM administrators WHERE user_id=?")){q.setInt(1,userId);try(ResultSet rs=q.executeQuery()){if(!rs.next()){try(PreparedStatement i=c.prepareStatement("INSERT INTO administrators(user_id,permissions,department,employee_number) VALUES (?,'SUPER_ADMIN','Administration',?)")){i.setInt(1,userId);i.setString(2,"LEGACY-"+userId);i.executeUpdate();}}}}
            c.commit(); JOptionPane.showMessageDialog(null,"Administrator repaired. Sign in using the new password.");
        } catch(Exception ex){error("Could not repair administrator: "+ex.getMessage());}
    }
    private static void error(String m){JOptionPane.showMessageDialog(null,m,"Admin Repair",JOptionPane.ERROR_MESSAGE);}
}
