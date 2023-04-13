package com.example.restaurantprojectv1.domain.dao;

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
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;

    private String address;

    private String information;

    private String phoneNumber;

    private Integer limitedPersonNumber;

    private String startTime;

    private String endTime;

    private boolean packaging;

    private boolean delivery;

    private boolean parking;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime registeredAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;


    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Reservation> reservationList;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Review> reviewList;

    // TODO: 예약내역 검색에서 아무것도 없으면 pagination 안보이게 하기
}
