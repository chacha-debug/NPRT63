package com.communityservice.tools;
import com.communityservice.DatabaseConnection;
import com.communityservice.utils.PasswordHasher;
import java.awt.GridLayout;
import java.sql.*;
import javax.swing.*;
public final class InitialAdminSeeder {
 private InitialAdminSeeder() {}
 public static void main(String[] args) {
  JTextField name=new JTextField(24), email=new JTextField(24), employee=new JTextField(24);
  JPasswordField password=new JPasswordField(24), confirm=new JPasswordField(24);
  JPanel panel=new JPanel(new GridLayout(0,1,4,4));
  panel.add(new JLabel("Full name *")); panel.add(name); panel.add(new JLabel("Email address *")); panel.add(email);
  panel.add(new JLabel("Employee / Staff number *")); panel.add(employee); panel.add(new JLabel("Password *")); panel.add(password);
  panel.add(new JLabel("Confirm password *")); panel.add(confirm);
  panel.add(new JLabel("Password: 8+ characters, uppercase, lowercase, number and special character."));
  if(JOptionPane.showConfirmDialog(null,panel,"Create Administrator",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return;
  String fullName=name.getText().trim(), emailAddress=email.getText().trim().toLowerCase(), employeeNumber=employee.getText().trim();
  char[] pchars=password.getPassword(), cchars=confirm.getPassword(); String plain=new String(pchars), repeated=new String(cchars);
  try {
   if(fullName.isBlank()||emailAddress.isBlank()||employeeNumber.isBlank()||plain.isBlank()||repeated.isBlank()){showError("All fields are required.");return;}
   if(!emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){showError("Enter a valid email address.");return;}
   String strengthError=PasswordHasher.getPasswordStrengthError(plain); if(strengthError!=null){showError(strengthError);return;}
   if(!plain.equals(repeated)){showError("The passwords do not match.");return;}
   String hash=PasswordHasher.hashPassword(plain); if(hash==null){showError("The password could not be secured.");return;}
   try(Connection conn=DatabaseConnection.getConnection()){
    if(conn==null){showError("Could not connect to the database.");return;} boolean oldAutoCommit=conn.getAutoCommit();
    try { conn.setAutoCommit(false);
     if(exists(conn,"SELECT 1 FROM users WHERE email = ?",emailAddress))throw new SQLException("An account with this email already exists.");
     if(exists(conn,"SELECT 1 FROM administrators WHERE employee_number = ?",employeeNumber))throw new SQLException("That employee/staff number is already assigned.");
     boolean firstAdmin=!existsAnyAdmin(conn);
     int userId; String userSql="INSERT INTO users (full_name,email,password_hash,role,is_verified,is_active,admin_approval_status) VALUES (?,?,?,'Administrator',TRUE,?,?)";
     try(PreparedStatement ps=conn.prepareStatement(userSql,Statement.RETURN_GENERATED_KEYS)){ps.setString(1,fullName);ps.setString(2,emailAddress);ps.setString(3,hash);ps.setBoolean(4,firstAdmin);ps.setString(5,firstAdmin?"APPROVED":"PENDING");if(ps.executeUpdate()!=1)throw new SQLException("Administrator user could not be created.");try(ResultSet keys=ps.getGeneratedKeys()){if(!keys.next())throw new SQLException("Administrator user ID was not returned.");userId=keys.getInt(1);}}
     try(PreparedStatement ps=conn.prepareStatement("INSERT INTO administrators (user_id,permissions,department,employee_number) VALUES (?,?,?,?)")){ps.setInt(1,userId);ps.setString(2,firstAdmin?"SUPER_ADMIN":"STANDARD_ADMIN");ps.setString(3,"Administration");ps.setString(4,employeeNumber);if(ps.executeUpdate()!=1)throw new SQLException("Administrator profile could not be created.");}
     conn.commit();
     String msg=firstAdmin?"Initial Super Administrator created successfully.\nYou can now sign in with this administrator email and password.":"Administrator account created and submitted for approval.\nThe Super Administrator must approve it under Admin Users before it can sign in.";
     JOptionPane.showMessageDialog(null,msg,"Administrator Created",JOptionPane.INFORMATION_MESSAGE);
    } catch(Exception ex){try{conn.rollback();}catch(SQLException ignored){}showError(ex.getMessage()==null?"Administrator creation failed.":ex.getMessage());}
    finally{try{conn.setAutoCommit(oldAutoCommit);}catch(SQLException ignored){}}
   } catch(SQLException ex){showError("Database error: "+ex.getMessage());}
  } finally {java.util.Arrays.fill(pchars,'\0');java.util.Arrays.fill(cchars,'\0');}
 }
 private static boolean existsAnyAdmin(Connection conn)throws SQLException{try(PreparedStatement ps=conn.prepareStatement("SELECT 1 FROM users WHERE role='Administrator' AND admin_approval_status='APPROVED' LIMIT 1");ResultSet rs=ps.executeQuery()){return rs.next();}}
 private static boolean exists(Connection conn,String sql,String value)throws SQLException{try(PreparedStatement ps=conn.prepareStatement(sql)){ps.setString(1,value);try(ResultSet rs=ps.executeQuery()){return rs.next();}}}
 private static void showError(String message){JOptionPane.showMessageDialog(null,message,"Cannot Create Administrator",JOptionPane.ERROR_MESSAGE);}
}
