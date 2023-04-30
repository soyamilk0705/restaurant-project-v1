package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.Reservation;
import com.example.restaurantprojectv1.domain.entity.Restaurant;
import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.ReservationDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.ReservationRepository;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService{

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;


    public Long create(Long restaurantId, Long userId, ReservationDto.Request reservationDto) {
        var restaurant = getRestaurantData(restaurantId);
        var user = getUserData(userId);

        checkPersonNumber(reservationDto.getPersonCount(), restaurant.getLimitedPersonNumber());


        Reservation reservation = Reservation.builder()
                .personCount(reservationDto.getPersonCount())
                .reservationDate(LocalDate.parse(reservationDto.getReservationDate(), DateTimeFormatter.ISO_DATE))
                .reservationTime(reservationDto.getReservationTime())
                .demand(reservationDto.getDemand())
                .cancel(false)
                .restaurant(restaurant)
                .user(user)
                .build();

        reservationRepository.save(reservation);

        return reservation.getId();
    }


    public ReservationDto.Response read(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(r -> new ReservationDto.Response(r))
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));
    }

    public Page<ReservationDto.Response> readAll(Pageable pageable){
        return reservationRepository.findAll(pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    public Page<ReservationDto.Response> readAllSearch(String searchType, String keyword, Pageable pageable){
        switch (searchType) {
            case "매장명":
                return reservationRepository.findAllByRestaurantName(keyword, pageable)
                        .map(r -> new ReservationDto.Response(r));
            case "예약자명":
                return reservationRepository.findAllByNickname(keyword, pageable)
                        .map(r -> new ReservationDto.Response(r));
            case "예약날짜":
                return reservationRepository.findAllByReservationDateContaining(keyword, pageable)
                        .map(r -> new ReservationDto.Response(r));
        }

        return null;
    }


    public Page<ReservationDto.Response> myPageCurrentDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdCurrentDate(userId, pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    public Page<ReservationDto.Response> myPagePastDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdPastDate(userId, pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    public Long countByUserIdCurrentDate(Long userId){
        return reservationRepository.countByUserIdCurrentDate(userId);
    }



    public Long update(Long reservationId, ReservationDto.Request reservationDto) {
        return reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(reservation -> {
                    reservation.set(reservationDto);

                    reservationRepository.save(reservation);

                    return reservation.getId();
                })
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));
    }


    public void delete(Long reservationId) {
        reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(reservation -> {
                    reservation.setCancel(true);

                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));
    }







    public boolean checkPersonNumber(Integer requestPerson, Integer restaurantPerson){
        return requestPerson > restaurantPerson || requestPerson < 1;
    }

    private Reservation checkCancel(Reservation reservation){
        if (reservation.isCancel()) {
            throw new DataNotFoundException("예약내역을 찾을 수 없습니다.");
        }
        return reservation;
    }

    public Restaurant getReservedRestaurant(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("예약 내역 없음"));

        return reservation.getRestaurant();
    }

    private Restaurant getRestaurantData(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("음식점 데이터 없음"));
    }

    private User getUserData(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("사용자 없음"));
    }
}

