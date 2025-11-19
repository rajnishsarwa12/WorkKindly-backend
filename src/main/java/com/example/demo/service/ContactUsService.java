package com.example.demo.service;

import com.example.demo.helper.ImageHelper;
import com.example.demo.model.entity.ContactUs;
import com.example.demo.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    private static final String UPLOAD_DIR = "uploads/contact/";

    // ✉️ Save new contact message (client)
    public ContactUs create(ContactUs contactUs) {
        contactUs.setStatus("PENDING");
        return contactUsRepository.save(contactUs);
    }

    // ✉️ Save new contact message with image compression & WebP conversion
    public ContactUs createWithAttachment(ContactUs contactUs, MultipartFile attachment) {
        try {
            if (attachment != null && !attachment.isEmpty()) {
                // Ensure upload directory exists
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Original filename and paths
                String originalName = System.currentTimeMillis() + "_" + attachment.getOriginalFilename();
                Path tempFilePath = uploadPath.resolve(originalName);

                // Save the original uploaded file temporarily
                Files.copy(attachment.getInputStream(), tempFilePath);

                // Define output WebP file
                String webpName = originalName.replaceAll("\\.(png|jpg|jpeg|gif)$", "") + ".webp";
                Path webpFilePath = uploadPath.resolve(webpName);

                // Compress & convert to WebP
                ImageHelper.compressToWebP(
                        tempFilePath.toFile(),
                        webpFilePath.toFile(),
                        0.8f // compression quality (80%)
                );

                // Delete original file to save space
                Files.deleteIfExists(tempFilePath);

                // Save final path in DB
                contactUs.setAttachment("/" + UPLOAD_DIR + webpName);
            }

            contactUs.setStatus("PENDING");
            return contactUsRepository.save(contactUs);

        } catch (IOException e) {
            throw new RuntimeException("Error while uploading/processing file: " + e.getMessage(), e);
        }
    }

    // 🧾 Admin: get all messages
    public List<ContactUs> getAll() {
        return contactUsRepository.findAll();
    }

    // 🔄 Admin: update status (pending → solved)
    public ContactUs updateStatus(Long id, String status) {
        ContactUs contact = contactUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setStatus(status.toUpperCase());
        return contactUsRepository.save(contact);
    }

    // ❌ Admin: delete contact
    public void delete(Long id) {
        contactUsRepository.deleteById(id);
    }
}
