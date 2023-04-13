package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.Reservation;
import com.example.restaurantprojectv1.domain.dao.Restaurant;
import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.ReservationRequestDto;
import com.example.restaurantprojectv1.domain.dto.ReservationResponseDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.ReservationRepository;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService{

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;


    public Long create(Long restaurantId, Long userId, ReservationRequestDto request) {
        var restaurant = getRestaurantData(restaurantId);
        var user = getUserData(userId);

        checkPersonNumber(request.getPersonCount(), restaurant.getLimitedPersonNumber());


        Reservation reservation = Reservation.builder()
                .personCount(request.getPersonCount())
                .reservationDate(LocalDate.parse(request.getReservationDate(), DateTimeFormatter.ISO_DATE))
                .reservationTime(request.getReservationTime())
                .demand(request.getDemand())
                .cancel(false)
                .restaurant(restaurant)
                .user(user)
                .build();

        reservationRepository.save(reservation);

        return reservation.getId();
    }


    public ReservationResponseDto read(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(this::entityToDto)
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));
    }

    public Page<ReservationResponseDto> readAll(Pageable pageable){
        return reservationRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    public Page<ReservationResponseDto> readAllSearch(String searchType, String keyword, Pageable pageable){
        switch (searchType) {
            case "매장명":
                return reservationRepository.findAllByRestaurantName(keyword, pageable)
                        .map(this::entityToDto);
            case "예약자명":
                return reservationRepository.findAllByNickname(keyword, pageable)
                        .map(this::entityToDto);
            case "전화번호":
                return reservationRepository.findAllByPhoneNumber(keyword, pageable)
                        .map(this::entityToDto);
            case "예약날짜":
                return reservationRepository.findAllByReservationDateContaining(keyword, pageable)
                        .map(this::entityToDto);
            case "예약시간":
                return reservationRepository.findAllByReservationTimeContaining(keyword, pageable)
                        .map(this::entityToDto);
            case "요구사항":
                return reservationRepository.findAllByDemandContaining(keyword, pageable)
                        .map(this::entityToDto);
        }

        return null;
    }


    public Page<ReservationResponseDto> myPageCurrentDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdCurrentDate(userId, pageable)
                .map(this::entityToDto);
    }

    public Page<ReservationResponseDto> myPagePastDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdPastDate(userId, pageable)
                .map(this::entityToDto);
    }

    public Long countByUserIdCurrentDate(Long userId){
        return reservationRepository.countByUserIdCurrentDate(userId);
    }



    public Long update(Long reservationId, ReservationRequestDto request) {
        return reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(reservation -> {
                    reservation.setPersonCount(request.getPersonCount())
                            .setReservationDate(LocalDate.parse(request.getReservationDate(), DateTimeFormatter.ISO_DATE))
                            .setReservationTime(request.getReservationTime())
                            .setDemand(request.getDemand());

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




    private ReservationResponseDto entityToDto(Reservation reservation) {
        return ReservationResponseDto.builder()
                .id(reservation.getId())
                .restaurantName(reservation.getRestaurant().getRestaurantName())
                .nickname(reservation.getUser().getNickname())
                .phoneNumber(reservation.getUser().getPhoneNumber())
                .personCount(reservation.getPersonCount())
                .reservationDate(reservation.getReservationDate().toString())
                .reservationTime(reservation.getReservationTime())
                .demand(reservation.getDemand())
                .cancel(reservation.isCancel()).build();
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

