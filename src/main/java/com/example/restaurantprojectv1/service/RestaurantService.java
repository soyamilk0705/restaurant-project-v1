package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.Restaurant;
import com.example.restaurantprojectv1.domain.dto.*;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Long create(RestaurantDto restaurantDto) {

        Restaurant restaurant = Restaurant.builder()
                .restaurantName(restaurantDto.getRestaurantName())
                .address(restaurantDto.getAddress())
                .phoneNumber(restaurantDto.getPhoneNumber())
                .limitedPersonNumber(restaurantDto.getLimitedPersonNumber())
                .information(restaurantDto.getInformation())
                .startTime(restaurantDto.getStartTime())
                .endTime(restaurantDto.getEndTime())
                .packaging(restaurantDto.isPackaging())
                .delivery(restaurantDto.isDelivery())
                .parking(restaurantDto.isParking())
                .build();

        restaurantRepository.save(restaurant);

        return restaurant.getId();

    }


    public RestaurantDto read(Long id) {
        return restaurantRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new DataNotFoundException("음식점을 찾을 수 없습니다."));
    }


    public List<RestaurantDto> readAll(String city, String country, String keyword){
        List<Restaurant> restaurantList;

        if (city == null){
            // keyword 만 있을 경우
            restaurantList = restaurantRepository.findAllByRestaurantNameContaining(keyword);
        } else if(country.equals("전체")){
            // city, keyword 있을 경우
            restaurantList = restaurantRepository.findByAddressContainingAndRestaurantNameContaining(city, keyword);
        } else{
            // city, country, keyword 있을 경우
            restaurantList = restaurantRepository.findByAddressContainingAndAddressContainingAndRestaurantNameContaining(city, country, keyword);
        }

        return restaurantList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

    }


    public Long update(Long id, RestaurantDto restaurantDto) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setRestaurantName(restaurantDto.getRestaurantName())
                            .setAddress(restaurantDto.getAddress())
                            .setPhoneNumber(restaurantDto.getPhoneNumber())
                            .setStartTime(restaurantDto.getStartTime())
                            .setEndTime(restaurantDto.getEndTime())
                            .setLimitedPersonNumber(restaurantDto.getLimitedPersonNumber())
                            .setPackaging(restaurantDto.isPackaging())
                            .setDelivery(restaurantDto.isDelivery())
                            .setParking(restaurantDto.isParking())
                            .setInformation(restaurantDto.getInformation());

                    restaurantRepository.save(restaurant);

                    return restaurant.getId();
                })
                .orElseThrow(() -> new DataNotFoundException("음식점을 찾을 수 없습니다."));
    }


    public void delete(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("음식점을 찾을 수 없습니다."));

        restaurantRepository.delete(restaurant);
    }


    private RestaurantDto entityToDto(Restaurant restaurant) {
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .information(restaurant.getInformation())
                .phoneNumber(restaurant.getPhoneNumber())
                .limitedPersonNumber(restaurant.getLimitedPersonNumber())
                .startTime(restaurant.getStartTime())
                .endTime(restaurant.getEndTime())
                .packaging(restaurant.isPackaging())
                .delivery(restaurant.isDelivery())
                .parking(restaurant.isParking()).build();
    }

}
