package com.example.demo.controller.admin;

import com.example.demo.helper.ResponseHelper;
import com.example.demo.model.entity.BlogEntity;
import com.example.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/blog")
public class BlogAdminController {

    @Autowired
    private BlogService blogService;

    /** ➕ Create new blog (supports image upload) */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "slug", required = false) String slug,
            @RequestParam(value = "bannerTxt", required = false) String bannerTxt,
            @RequestParam(value = "status", required = false) BlogEntity.BlogStatus status,
            @RequestParam(value = "metaTitle", required = false) String metaTitle,
            @RequestParam(value = "metaDescription", required = false) String metaDescription,
            @RequestParam(value = "metaKeywords", required = false) String metaKeywords,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        BlogEntity blog = new BlogEntity();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthor(author);
        blog.setSlug(slug);
        blog.setBannerTxt(bannerTxt);
        blog.setStatus(status != null ? status : BlogEntity.BlogStatus.DRAFT);
        blog.setMetaTitle(metaTitle);
        blog.setMetaDescription(metaDescription);
        blog.setMetaKeywords(metaKeywords);

        return ResponseHelper.success(
                blogService.create(blog, image),
                "Blog created successfully"
        );
    }

    /** 🧾 Get all blogs */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseHelper.success(blogService.getAll(), "All blogs fetched successfully");
    }

    /** 🔍 Get blog by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseHelper.success(blogService.getById(id), "Blog fetched successfully");
    }

    /** 🔍 Get blog by slug */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Object> getBySlug(@PathVariable String slug) {
        return ResponseHelper.success(blogService.getBySlug(slug), "Blog fetched successfully");
    }

    /** ✏️ Update blog */
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "slug", required = false) String slug,
            @RequestParam(value = "bannerTxt", required = false) String bannerTxt,
            @RequestParam(value = "status", required = false) BlogEntity.BlogStatus status,
            @RequestParam(value = "metaTitle", required = false) String metaTitle,
            @RequestParam(value = "metaDescription", required = false) String metaDescription,
            @RequestParam(value = "metaKeywords", required = false) String metaKeywords,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        BlogEntity blog = new BlogEntity();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthor(author);
        blog.setSlug(slug);
        blog.setBannerTxt(bannerTxt);
        blog.setStatus(status != null ? status : BlogEntity.BlogStatus.DRAFT);
        blog.setMetaTitle(metaTitle);
        blog.setMetaDescription(metaDescription);
        blog.setMetaKeywords(metaKeywords);

        return ResponseHelper.success(
                blogService.update(id, blog, image),
                "Blog updated successfully"
        );
    }

    /** ❌ Delete blog */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseHelper.success(null, "Blog deleted successfully");
    }
}
