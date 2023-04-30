package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByFood(String food);
}
