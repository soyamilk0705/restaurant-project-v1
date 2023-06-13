package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.Restaurant;
import com.example.restaurantprojectv1.domain.dto.RestaurantDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RestaurantServiceTests {

    @Autowired private RestaurantService restaurantService;
    @Autowired private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("음식점 생성 성공")
    void create(){
        // given
        RestaurantDto restaurantDto = createRestaurantDto();

        // when
        Long restaurantId = restaurantService.create(restaurantDto);

        // then
        Restaurant savedRestaurant = restaurantRepository.findById(restaurantId).orElseGet(null);

        assertEquals(restaurantDto.getRestaurantName(), savedRestaurant.getRestaurantName());
        assertEquals(restaurantDto.getDetailAddress(), savedRestaurant.getDetailAddress());
    }

    @Test
    @DisplayName("메뉴 읽기 실패")
    void read_fail(){
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> restaurantService.read(1004L));

        // then
        assertEquals("음식점을 찾을 수 없습니다.", fail.getMessage());
    }


    @Test
    @DisplayName("음식점 수정 성공")
    void update() {
        // given
        RestaurantDto restaurantDto = createRestaurantDto();
        Long createdId = restaurantService.create(restaurantDto);

        restaurantDto.setRestaurantName("수정");
        restaurantDto.setDetailAddress("수정");

        // when
        restaurantService.update(createdId, restaurantDto);

        // then
        Restaurant savedRestaurant = restaurantRepository.findById(createdId).orElse(null);

        assertEquals(restaurantDto.getRestaurantName(), savedRestaurant.getRestaurantName());
        assertEquals(restaurantDto.getDetailAddress(), savedRestaurant.getDetailAddress());
    }

    @Test
    @DisplayName("메뉴 삭제 실패")
    void delete_fail() {
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> restaurantService.delete(1004L));

        // then
        assertEquals("음식점을 찾을 수 없습니다.", fail.getMessage());
    }

    private RestaurantDto createRestaurantDto(){
        return RestaurantDto.builder()
                .restaurantName("restaurantTest")
                .city("서울특별시")
                .country("강남구")
                .detailAddress("신사동")
                .information("restaurantTest")
                .limitedPersonNumber(10)
                .phoneNumber("010-0000-0000")
                .startTime(LocalTime.now().toString())
                .endTime(LocalTime.now().toString())
                .delivery(false)
                .packaging(false)
                .parking(false)
                .build();

    }
}