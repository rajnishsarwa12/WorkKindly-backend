package com.example.demo.service;

import com.example.demo.model.entity.Admin;
import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    Admin createAdmin(Admin admin);
    Admin getAdminByUsername(String username);
}
