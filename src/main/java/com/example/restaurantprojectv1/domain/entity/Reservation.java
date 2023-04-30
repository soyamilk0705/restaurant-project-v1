package com.example.restaurantprojectv1.domain.entity;

import com.example.restaurantprojectv1.domain.dto.ReservationDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer personCount;

    private LocalDate reservationDate;

    private String reservationTime;

    private String demand;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime registeredAt;

    @ColumnDefault("0")
    private boolean cancel;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Reservation(Integer personCount, LocalDate reservationDate, String reservationTime, String demand,
                       boolean cancel, Restaurant restaurant, User user){
        this.personCount = personCount;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.demand = demand;
        this.cancel = cancel;
        this.restaurant = restaurant;
        this.user = user;

    }

    public void set(ReservationDto.Request reservationDto){
        this.personCount = reservationDto.getPersonCount();
        this.reservationDate = LocalDate.parse(reservationDto.getReservationDate(), DateTimeFormatter.ISO_DATE);
        this.reservationTime = reservationDto.getReservationTime();
        this.demand = reservationDto.getDemand();
    }

    public void setCancel(boolean cancel){
        this.cancel = cancel;
    }

}
