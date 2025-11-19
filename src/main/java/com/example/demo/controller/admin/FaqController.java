package com.example.demo.controller.admin;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.Faq;
import com.example.demo.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminFaqController") // 👈 Unique bean name
@RequestMapping("/api/admin/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // ➕ Create new FAQ
    @PostMapping("/add")
    public ResponseEntity<Object> addFaq(@RequestBody Faq faq) {
        try {
            Faq createdFaq = faqService.createFaq(faq);
            return ResponseHelper.success(createdFaq, "FAQ created successfully");
        } catch (Exception e) {
            return ResponseHelper.error("Error creating FAQ: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 📝 Update FAQ
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFaq(@PathVariable Long id, @RequestBody Faq faq) {
        try {
            Faq updated = faqService.updateFaq(id, faq);
            return ResponseHelper.success(updated, "FAQ updated successfully");
        } catch (Exception e) {
            return ResponseHelper.error("Error updating FAQ: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ❌ Delete FAQ (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFaq(@PathVariable Long id) {
        try {
            faqService.deleteFaq(id);
            return ResponseHelper.success(null, "FAQ deleted successfully");
        } catch (Exception e) {
            return ResponseHelper.error("Error deleting FAQ: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 📋 Get all FAQs
    @GetMapping("/all")
    public ResponseEntity<Object> getAllFaqs() {
        return ResponseHelper.success(faqService.getAllFaqs(), "All FAQs fetched successfully");
    }
}
