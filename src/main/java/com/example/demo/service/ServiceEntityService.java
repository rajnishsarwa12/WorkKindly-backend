package com.example.demo.service;

import com.example.demo.model.entity.ServiceEntity;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ServiceEntityService {

    @Autowired
    private ServiceRepository serviceRepository;

    private static final String UPLOAD_DIR = "uploads/services/";

    /**
     * ➕ Create new service (with or without image)
     */
    public ServiceEntity createWithImage(ServiceEntity entity, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                String filePath = saveImage(image);
                entity.setThaimail(filePath); // store path in DB
            }
            return serviceRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving service: " + e.getMessage());
        }
    }

    /**
     * ➕ Create new service (simple JSON, no file)
     */
    public ServiceEntity create(ServiceEntity entity) {
        return serviceRepository.save(entity);
    }

    /**
     * 🧾 Get all services
     */
    public List<ServiceEntity> getAll() {
        return serviceRepository.findAll();
    }

    /**
     * 🔍 Get service by ID
     */
    public ServiceEntity getById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    /**
     * ✏️ Update existing service (with or without new image)
     */
    public ServiceEntity update(Long id, ServiceEntity updatedEntity, MultipartFile newImage) {
        ServiceEntity existing = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        existing.setName(updatedEntity.getName());
        existing.setPresentServiceId(updatedEntity.getPresentServiceId());

        try {
            if (newImage != null && !newImage.isEmpty()) {
                String filePath = saveImage(newImage);
                existing.setThaimail(filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating service image: " + e.getMessage());
        }

        return serviceRepository.save(existing);
    }

    /**
     * ❌ Delete service
     */
    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    /**
     * 🖼️ Utility: Save uploaded image and return file path
     */
    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Unique filename (timestamp + original)
        String originalName = image.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + (originalName != null ? originalName : "image");
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path for serving later
        return "/uploads/services/" + fileName;
    }

    public List<ServiceEntity> getAllMainCategories() {
        return serviceEntityRepository.findByPresentServiceIdIsNull();
    }

    public List<ServiceEntity> getSubcategories(Long parentId) {
        return serviceEntityRepository.findByPresentServiceId(parentId);
    }

}
