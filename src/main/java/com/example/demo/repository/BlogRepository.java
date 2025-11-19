package com.example.demo.repository;

import com.example.demo.model.entity.BlogEntity;
import com.example.demo.model.entity.BlogEntity.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {

    BlogEntity findBySlug(String slug);

    // 🔍 Search query by title, metaTitle, metaDescription, or metaKeywords
    @Query("""
           SELECT b FROM BlogEntity b 
           WHERE LOWER(b.title) LIKE %:search% 
           OR LOWER(b.metaTitle) LIKE %:search%
           OR LOWER(b.metaDescription) LIKE %:search%
           OR LOWER(b.metaKeywords) LIKE %:search%
           """)
    Page<BlogEntity> findBySearch(String search, Pageable pageable);

    // 🕒 Get latest published blogs
    List<BlogEntity> findByStatusOrderByCreatedAtDesc(BlogStatus status, Pageable pageable);
}
