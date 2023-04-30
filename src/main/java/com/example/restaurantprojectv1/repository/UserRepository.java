package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndUnregisteredAtIsNull(String email);

    boolean existsByEmail(String email);

}
