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

    /**
     * 예약 생성
     */
    public Long create(Long restaurantId, Long userId, ReservationDto.Request reservationDto) {
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("음식점 데이터 없음"));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("사용자 없음"));

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

    /**
     * 예약 읽기
     */
    public ReservationDto.Response read(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .map(r -> new ReservationDto.Response(r))
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));
    }

    /**
     * 예약 전체 보기
     */
    public Page<ReservationDto.Response> readAll(Pageable pageable){
        return reservationRepository.findAll(pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    /**
     * 예약 전체 보기 - 검색
     */
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

    /**
     * 예약 수정
     */
    public void update(Long reservationId, ReservationDto.Request reservationDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));

        reservation.set(reservationDto);
    }

    /**
     * 예약 삭제
     */
    public void delete(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .map(this::checkCancel)
                .orElseThrow(() -> new DataNotFoundException("예약내역을 찾을 수 없습니다."));

        reservation.setCancel(true);
    }

    /**
     * 마이페이지 - 현재 예약 내역
     */
    public Page<ReservationDto.Response> myPageCurrentDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdCurrentDate(userId, pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    /**
     * 마이페이지 - 과거 예약 내역
     */
    public Page<ReservationDto.Response> myPagePastDate(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserIdPastDate(userId, pageable)
                .map(r -> new ReservationDto.Response(r));
    }

    /**
     * 마이페이지 - 현재 예약 내역 갯수
     */
    public Long countByUserIdCurrentDate(Long userId){
        return reservationRepository.countByUserIdCurrentDate(userId);
    }

    /**
     * 예약된 음식점 정보 가져오기
     */
    public Restaurant getReservedRestaurant(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DataNotFoundException("예약 내역 없음"));

        return reservation.getRestaurant();
    }


    /**
     * 제한 인원 수 체크
     */
    public boolean checkPersonNumber(Integer requestPerson, Integer restaurantPerson){
        return requestPerson > restaurantPerson || requestPerson < 1;
    }

    /**
     * 취소된 예약인지 확인
     */
    private Reservation checkCancel(Reservation reservation){
        if (reservation.isCancel()) {
            throw new DataNotFoundException("예약내역을 찾을 수 없습니다.");
        }
        return reservation;
    }

}

