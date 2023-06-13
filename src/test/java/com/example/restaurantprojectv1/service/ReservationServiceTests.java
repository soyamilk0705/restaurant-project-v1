package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.*;
import com.example.restaurantprojectv1.domain.dto.ReservationDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReservationServiceTests {
    @Autowired private ReservationService reservationService;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private UserRepository userRepository;


    private ReservationDto.Request reservationDto;
    private Restaurant restaurant;
    private User user;

    @BeforeEach
    void before(){
        restaurant = restaurantRepository.save(Restaurant.builder().limitedPersonNumber(10).build());
        user = userRepository.save(new User("test@gmail.com", "test"));

        reservationDto = ReservationDto.Request.builder()
                .personCount(1)
                .reservationTime(LocalTime.now().toString())
                .reservationDate(LocalDate.now().toString())
                .demand("test")
                .build();
    }

    @Test
    @DisplayName("예약 생성 성공")
    void create() {
        // given
        // when
        Long reservationId = reservationService.create(restaurant.getId(), user.getId(), reservationDto);

        // then
        Reservation savedReservation = reservationRepository.findById(reservationId).orElse(null);

        assertEquals(reservationDto.getPersonCount(), savedReservation.getPersonCount());
        assertEquals(reservationDto.getDemand(), savedReservation.getDemand());

    }

    @Test
    @DisplayName("리뷰 읽기 실패")
    void read_fail() throws IOException {
        // given
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () ->  reservationService.read(1004L));

        // then
        assertEquals("예약내역을 찾을 수 없습니다.", fail.getMessage());
    }


    @Test
    @DisplayName("예약 수정 성공")
    void update() {
        // given
        Long reservationId = reservationService.create(restaurant.getId(), user.getId(), reservationDto);
        reservationDto.setPersonCount(2);
        reservationDto.setDemand("수정");

        // when
        reservationService.update(reservationId, reservationDto);

        // then
        Reservation savedReservation = reservationRepository.findById(reservationId).orElse(null);

        assertEquals(reservationDto.getPersonCount(), savedReservation.getPersonCount());
        assertEquals(reservationDto.getDemand(), savedReservation.getDemand());

    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete(){
        // given
        Long reservationId = reservationService.create(restaurant.getId(), user.getId(), reservationDto);

        // when
        reservationService.delete(reservationId);

        // then
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () ->  reservationService.read(reservationId));

        assertEquals("예약내역을 찾을 수 없습니다.", fail.getMessage());

    }

    @Test
    @DisplayName("리뷰 삭제 실패")
    void delete_fail() throws IOException {
        // given
        Long reservationId = reservationService.create(restaurant.getId(), user.getId(), reservationDto);
        reservationService.delete(reservationId);

        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () ->  reservationService.delete(reservationId));

        // then
        assertEquals("예약내역을 찾을 수 없습니다.", fail.getMessage());
    }

    private MockMultipartFile createMockMultipartFile(){
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_test.jpg";

        MockMultipartFile file = new MockMultipartFile("images", filename, "image/jpg",  "test file".getBytes(StandardCharsets.UTF_8));

        return file;
    }
}