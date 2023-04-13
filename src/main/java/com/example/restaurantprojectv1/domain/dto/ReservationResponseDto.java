package com.example.restaurantprojectv1.domain.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long id;

    private String restaurantName;

    private String nickname;

    private String phoneNumber;

    private Integer personCount;

    private String reservationDate;

    private String reservationTime;

    private String demand;

    private boolean cancel;



}
