package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 📝 Blog title */
    @Column(nullable = false)
    private String title;

    /** 📄 Blog content (HTML or plain text) */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** ✍️ Author name */
    private String author;

    /** 🔗 Slug (URL-friendly title) */
    @Column(unique = true)
    private String slug;

    /** 🖼️ Banner headline text */
    private String bannerTxt;

    /** 📸 Banner image path */
    private String banner;

    /** ⚙️ Blog status (Published / Draft) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlogStatus status = BlogStatus.DRAFT;

    /** 🕒 Created timestamp */
    private LocalDateTime createdAt = LocalDateTime.now();

    /** 🕓 Updated timestamp */
    private LocalDateTime updatedAt = LocalDateTime.now();

    /** 🔍 SEO Meta fields */
    private String metaTitle;
    @Column(length = 500)
    private String metaDescription;
    private String metaKeywords;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum BlogStatus {
        PUBLISHED,
        DRAFT
    }
}
