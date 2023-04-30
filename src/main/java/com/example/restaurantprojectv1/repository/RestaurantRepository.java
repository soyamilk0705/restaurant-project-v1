package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.Restaurant;
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

    // 도시, 음식점 이름 검색
    @Query("select r from Restaurant r where r.city = :city and r.restaurantName like %:keyword%")
    List<Restaurant> searchCityAndRestaurantName(@Param("city") String city, @Param("keyword") String keyword);

    // 도시, 지역, 음식점 이름 검색
    @Query("select r from Restaurant r " +
            "where r.city = :city " +
            "and r.country = :country " +
            "and r.restaurantName like %:keyword%")
    List<Restaurant> searchAddressAndRestaurantName(@Param("city") String city, @Param("country") String country, @Param("keyword") String keyword);

}
