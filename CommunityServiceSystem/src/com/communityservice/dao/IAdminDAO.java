package com.communityservice.dao;

import com.communityservice.models.Admin;
import java.util.List;

public interface IAdminDAO {
    boolean createAdminProfile(Admin admin);
    Admin getAdminById(int adminId);
    Admin getAdminByUserId(int userId);
    List<Admin> getAllAdmins();
    boolean updateAdminProfile(Admin admin);
    boolean deleteAdminProfile(int adminId);
}
