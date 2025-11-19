package com.example.demo.controller.admin;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.ServiceEntity;
import com.example.demo.service.ServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/service")
public class ServiceAdminController {

    @Autowired
    private ServiceEntityService serviceEntityService;

    /**
     * ➕ Create a new service (supports optional image upload)
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(
            @RequestParam("name") String name,
            @RequestParam(value = "presentServiceId", required = false) Long presentServiceId,
            @RequestPart(value = "thaimail", required = false) MultipartFile thaimail
    ) {
        ServiceEntity entity = new ServiceEntity();
        entity.setName(name);
        entity.setPresentServiceId(presentServiceId);

        return ResponseHelper.success(
                serviceEntityService.createWithImage(entity, thaimail),
                "Service created successfully with image"
        );
    }

    /**
     * 🧾 Get all services
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseHelper.success(
                serviceEntityService.getAll(),
                "All services fetched successfully"
        );
    }

    /**
     * ✏️ Update an existing service (supports optional image re-upload)
     */
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "presentServiceId", required = false) Long presentServiceId,
            @RequestPart(value = "thaimail", required = false) MultipartFile thaimail
    ) {
        ServiceEntity entity = new ServiceEntity();
        entity.setName(name);
        entity.setPresentServiceId(presentServiceId);

        ServiceEntity updated = serviceEntityService.update(id, entity, thaimail);
        return ResponseHelper.success(updated, "Service updated successfully");
    }

    /**
     * ❌ Delete service
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        serviceEntityService.delete(id);
        return ResponseHelper.success(null, "Service deleted successfully");
    }

    /**
     * 🔍 Get service by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseHelper.success(
                serviceEntityService.getById(id),
                "Service fetched successfully"
        );
    }
}
