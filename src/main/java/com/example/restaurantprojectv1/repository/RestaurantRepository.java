package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.dao.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r.limitedPersonNumber from Restaurant r where r.id = :restaurantId")
    Integer getLimitedPersonNumber(@Param("restaurantId") Long restaurantId);

    List<Restaurant> findAllByRestaurantNameContaining(String keyword);

    List<Restaurant> findByAddressContainingAndRestaurantNameContaining(String city, String keyword);

    List<Restaurant> findByAddressContainingAndAddressContainingAndRestaurantNameContaining(String city, String country, String keyword);

}
