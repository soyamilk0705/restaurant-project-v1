package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.dao.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByUserId(Long userId, Pageable pageable);

    Page<Review> findAllByDescriptionContaining(String keyword, Pageable pageable);

    Long countByUserId(Long userId);
}
