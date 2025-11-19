package com.example.demo.controller.client;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.ServiceEntity;
import com.example.demo.service.ServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/service")
public class ServiceClientController {

    @Autowired
    private ServiceEntityService serviceEntityService;

    /**
     * 🌐 Get all available services (public)
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllServices() {
        return ResponseHelper.success(
                serviceEntityService.getAll(),
                "All services fetched successfully"
        );
    }

    /**
     * 🧭 Get all main categories (where presentServiceId is null)
     */
    @GetMapping("/category")
    public ResponseEntity<Object> getMainCategories() {
        List<ServiceEntity> mainCategories = serviceEntityService.getAllMainCategories();
        return ResponseHelper.success(mainCategories, "All main categories fetched successfully");
    }

    /**
     * 🗂 Get all subcategories under a parent category
     */
    @GetMapping("/subcategory/{id}")
    public ResponseEntity<Object> getSubcategories(@PathVariable Long id) {
        List<ServiceEntity> subcategories = serviceEntityService.getSubcategories(id);
        return ResponseHelper.success(subcategories, "All subcategories fetched successfully");
    }

    /**
     * 🔍 Get service details by ID (public)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getServiceById(@PathVariable Long id) {
        return ResponseHelper.success(
                serviceEntityService.getById(id),
                "Service details fetched successfully"
        );
    }
}
