package com.example.demo.controller.client;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("clientFaqController") // 👈 Unique bean name
@RequestMapping("/api/client/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // 🌍 Get all active FAQs (public endpoint)
    @GetMapping("/active")
    public ResponseEntity<Object> getActiveFaqs() {
        return ResponseHelper.success(faqService.getActiveFaqs(), "Active FAQs fetched successfully");
    }
}
