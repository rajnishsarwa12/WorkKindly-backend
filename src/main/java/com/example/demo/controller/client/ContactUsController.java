package com.example.demo.controller.client;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.ContactUs;
import com.example.demo.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/client/contact")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> submitContact(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("message") String message,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment
    ) {
        ContactUs contactUs = new ContactUs();
        contactUs.setFirstName(firstName);
        contactUs.setLastName(lastName);
        contactUs.setEmail(email);
        contactUs.setPhone(phone);
        contactUs.setMessage(message);

        return ResponseHelper.success(
                contactUsService.createWithAttachment(contactUs, attachment),
                "Message submitted successfully!"
        );
    }
}
