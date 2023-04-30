package com.example.restaurantprojectv1.repository;

import com.example.restaurantprojectv1.domain.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r left join r.user u where u.id = :userId and r.reservationDate > CURRENT_DATE")
    Page<Reservation> findAllByUserIdCurrentDate(@Param("userId") Long userId, Pageable pageable);

    @Query("select r from Reservation r left join r.user u where u.id = :userId and r.reservationDate < CURRENT_DATE")
    Page<Reservation> findAllByUserIdPastDate(@Param("userId") Long userId, Pageable pageable);

    @Query("select count(r) from Reservation r left join r.user u where u.id = :userId and r.reservationDate > CURRENT_DATE")
    Long countByUserIdCurrentDate(@Param("userId") Long userId);

    @Query("select r from Reservation r left join r.restaurant n where n.restaurantName like %:keyword%")
    Page<Reservation> findAllByRestaurantName(@Param("keyword") String keyword, Pageable pageable);

    @Query("select r from Reservation r left join r.user u where u.nickname like %:keyword%")
    Page<Reservation> findAllByNickname(@Param("keyword") String keyword, Pageable pageable);

    Page<Reservation> findAllByReservationDateContaining(String reservationDate, Pageable pageable);
}
