package com.example.demo.controller.admin;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.Admin;
import com.example.demo.model.dto.AdminAuthRequestDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.service.AdminAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AuthController {

    @Autowired
    private AdminAuthService adminauthservice;

    @Autowired
    private JwtUtil jwtUtil; // ✅ Injected bean

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AdminAuthRequestDTO loginRequest) {
        try {
            Optional<Admin> adminOpt = adminauthservice.findByEmail(loginRequest.getEmail());

            if (adminOpt.isEmpty()) {
                return ResponseHelper.error("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }

            Admin admin = adminOpt.get();
            boolean passwordMatch = adminauthservice.checkPassword(
                    loginRequest.getPassword(),
                    admin.getPassword()
            );

            if (!passwordMatch) {
                return ResponseHelper.error("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }

            // ✅ Use instance method
            String token = jwtUtil.generateToken(admin.getEmail());

            var responseData = new java.util.HashMap<String, Object>();
            responseData.put("token", token);
            responseData.put("email", admin.getEmail());
            responseData.put("username", admin.getUsername());

            return ResponseHelper.success(responseData, "Login successful");
        } catch (Exception e) {
            return ResponseHelper.error("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------- VERIFY TOKEN ----------------
    @GetMapping("/verify")
    public ResponseEntity<Object> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseHelper.error("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.replace("Bearer ", "");

            boolean isValid = jwtUtil.validateToken(token); // ✅ validateToken returns boolean
            String email = jwtUtil.extractUsername(token);  // ✅ extract email if valid

            if (!isValid || email == null) {
                return ResponseHelper.error("Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            return ResponseHelper.success(email, "Token is valid");
        } catch (Exception e) {
            return ResponseHelper.error("Error verifying token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // ---------------- Password Encoder (for testing) ----------------
    @GetMapping("/encode/{password}")
    public String encode(@PathVariable String password) {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(password);
    }
}
