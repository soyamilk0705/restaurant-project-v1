package com.example.restaurantprojectv1.domain.entity;

import com.example.restaurantprojectv1.domain.dto.RestaurantDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;

    private String city;

    private String country;

    private String detailAddress;

    private String information;

    private String phoneNumber;

    private int limitedPersonNumber;

    private String startTime;

    private String endTime;

    private boolean packaging;

    private boolean delivery;

    private boolean parking;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();



    @Builder
    public Restaurant(String restaurantName, String city, String country, String detailAddress, String information, String phoneNumber, int limitedPersonNumber, String startTime, String endTime, boolean packaging, boolean delivery, boolean parking) {
        this.restaurantName = restaurantName;
        this.city = city;
        this.country = country;
        this.detailAddress = detailAddress;
        this.information = information;
        this.phoneNumber = phoneNumber;
        this.limitedPersonNumber = limitedPersonNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.packaging = packaging;
        this.delivery = delivery;
        this.parking = parking;
    }

    public void set(RestaurantDto restaurantDto){
        this.restaurantName = restaurantDto.getRestaurantName();
        this.city = restaurantDto.getCity();
        this.country = restaurantDto.getCountry();
        this.detailAddress = restaurantDto.getDetailAddress();
        this.information = restaurantDto.getInformation();
        this.phoneNumber = restaurantDto.getPhoneNumber();
        this.startTime = restaurantDto.getStartTime();
        this.endTime = restaurantDto.getEndTime();
        this.limitedPersonNumber = restaurantDto.getLimitedPersonNumber();
        this.packaging = restaurantDto.isPackaging();
        this.delivery = restaurantDto.isDelivery();
        this.parking = restaurantDto.isParking();

    }

    // 테스트용
    @Builder
    public Restaurant(String restaurantName, Integer limitedPersonNumber){
        this.restaurantName = restaurantName;
        this.limitedPersonNumber = limitedPersonNumber;
    }

}
