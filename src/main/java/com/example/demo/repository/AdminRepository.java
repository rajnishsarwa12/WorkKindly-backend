package com.example.demo.repository;

import com.example.demo.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ✅ यह import जरूरी है

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
    Optional<Admin> findByEmail(String email);
}
