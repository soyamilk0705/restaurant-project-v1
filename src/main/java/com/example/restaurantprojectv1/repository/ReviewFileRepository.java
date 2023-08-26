package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.ReviewFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewFileRepository extends JpaRepository<ReviewFile, Long> {
    void deleteByFileName(String fileName);
}
