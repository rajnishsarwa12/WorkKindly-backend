package com.example.demo.controller.admin;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contact")
public class ContactUsAdminController {

    @Autowired
    private ContactUsService contactUsService;

    // 🧾 Get all contact messages
    @GetMapping("/all")
    public ResponseEntity<Object> getAllContacts() {
        return ResponseHelper.success(contactUsService.getAll(), "All contact messages fetched");
    }

    // 🔄 Update contact status
    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseHelper.success(contactUsService.updateStatus(id, status), "Status updated successfully");
    }

    // ❌ Delete contact
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteContact(@PathVariable Long id) {
        contactUsService.delete(id);
        return ResponseHelper.success(null, "Contact deleted successfully");
    }
}
