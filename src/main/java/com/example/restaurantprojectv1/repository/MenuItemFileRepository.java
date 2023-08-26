package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.MenuItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemFileRepository extends JpaRepository<MenuItemFile, Long> {
    void deleteByFileName(String fileName);
}
