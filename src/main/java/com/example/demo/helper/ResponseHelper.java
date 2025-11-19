package com.example.demo.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {

    // ✅ Success Response (with custom message)
    public static ResponseEntity<Object> success(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", HttpStatus.OK.value());  // 200
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Success Response (default message)
    public static ResponseEntity<Object> success(Object data) {
        return success(data, "Request processed successfully");
    }

    // ❌ Error Response (with custom status)
    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("status", status.value());
        response.put("message", message);
        response.put("data", null);
        return new ResponseEntity<>(response, status);
    }

    // ❌ Error Response (default 500 Internal Server Error)
    public static ResponseEntity<Object> error(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
