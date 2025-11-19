package com.example.demo.controller.client;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.BlogEntity;
import com.example.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogClientController {

    @Autowired
    private BlogService blogService;

    /**
     * 🧾 Get all blogs with pagination and search
     * Example:
     * /api/blog/list?page=0&size=10&search=java
     */
    @GetMapping("/list")
    public ResponseEntity<Object> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Page<BlogEntity> blogs = blogService.getAllWithPagination(page, size, search);
        return ResponseHelper.success(blogs, "Blogs fetched successfully");
    }

    /**
     * 🕒 Get latest 3 published blogs
     */
    @GetMapping("/latest")
    public ResponseEntity<Object> getLatestBlogs() {
        List<BlogEntity> latestBlogs = blogService.getLatestBlogs(3);
        return ResponseHelper.success(latestBlogs, "Latest blogs fetched successfully");
    }

    /**
     * 🔍 Get blog by slug (Public API)
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Object> getBySlug(@PathVariable String slug) {
        BlogEntity blog = blogService.getBySlug(slug);
        if (blog == null) {
            // ✅ FIX: Use HttpStatus.NOT_FOUND instead of integer 404
            return ResponseHelper.error("Blog not found with slug: " + slug, HttpStatus.NOT_FOUND);
        }
        return ResponseHelper.success(blog, "Blog fetched successfully");
    }
}
