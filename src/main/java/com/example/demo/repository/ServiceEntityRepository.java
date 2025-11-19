package com.example.demo.repository;

import com.example.demo.model.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {

    // 🟢 Get all main categories (no parent)
    List<ServiceEntity> findByPresentServiceIdIsNull();

    // 🟢 Get all subcategories under a parent
    List<ServiceEntity> findByPresentServiceId(Long presentServiceId);
}
