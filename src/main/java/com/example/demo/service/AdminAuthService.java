package com.example.demo.service;

import com.example.demo.model.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminAuthService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 🔹 Find admin by email
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    // 🔹 Verify password (raw vs hashed)
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password from DB: " + encodedPassword);
        boolean match = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("Password Match: " + match);
        return match;
    }

    // 🔹 Encode password when creating admin
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
