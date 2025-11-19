package com.example.demo.service;

import com.example.demo.helper.ImageHelper;
import com.example.demo.model.entity.BlogEntity;
import com.example.demo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    private static final String UPLOAD_DIR = "uploads/blogs/";

    /**
     * 🔧 Save uploaded image as WebP (compressed) and return relative path.
     */
    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            // Create directory if missing
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // Save original temporary file
            String originalName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File tempFile = new File(dir, originalName);
            file.transferTo(tempFile);

            // Generate .webp filename
            String webpName = originalName.replaceAll("\\.[^.]+$", "") + ".webp";
            File webpFile = new File(dir, webpName);

            // Compress to WebP using helper
            ImageHelper.compressToWebP(tempFile, webpFile, 0.8f);

            // Remove original temp file
            tempFile.delete();

            // Return WebP relative path
            return "/uploads/blogs/" + webpName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    /**
     * ➕ Create new blog
     */
    public BlogEntity create(BlogEntity blog, MultipartFile imageFile) {
        if (blog.getSlug() == null || blog.getSlug().isEmpty()) {
            blog.setSlug(generateSlug(blog.getTitle()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            blog.setBanner(saveFile(imageFile));
        }
        return blogRepository.save(blog);
    }

    /**
     * ✏️ Update existing blog
     */
    public BlogEntity update(Long id, BlogEntity updated, MultipartFile imageFile) {
        BlogEntity existing = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setAuthor(updated.getAuthor());
        existing.setBannerTxt(updated.getBannerTxt());
        existing.setStatus(updated.getStatus());
        existing.setMetaTitle(updated.getMetaTitle());
        existing.setMetaDescription(updated.getMetaDescription());
        existing.setMetaKeywords(updated.getMetaKeywords());
        existing.setSlug(updated.getSlug() != null ? updated.getSlug() : existing.getSlug());

        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setBanner(saveFile(imageFile));
        }

        return blogRepository.save(existing);
    }

    /**
     * ❌ Delete blog
     */
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * 📚 Get all blogs
     */
    public List<BlogEntity> getAll() {
        return blogRepository.findAll();
    }

    /**
     * 🔍 Get blog by ID
     */
    public BlogEntity getById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    /**
     * 🔍 Get blog by slug
     */
    public BlogEntity getBySlug(String slug) {
        return blogRepository.findBySlug(slug);
    }

    /**
     * 📑 Get blogs with pagination and optional search
     */
    public Page<BlogEntity> getAllWithPagination(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (search == null || search.trim().isEmpty()) {
            return blogRepository.findAll(pageable);
        } else {
            return blogRepository.findBySearch(search.trim().toLowerCase(), pageable);
        }
    }

    /**
     * 🕒 Get latest N published blogs
     */
    public List<BlogEntity> getLatestBlogs(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return blogRepository.findByStatusOrderByCreatedAtDesc(BlogEntity.BlogStatus.PUBLISHED, pageable);
    }

    /**
     * 🔗 Generate a slug from title
     */
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
