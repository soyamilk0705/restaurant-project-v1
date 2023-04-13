package com.example.restaurantprojectv1.domain.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReservationRequestDto {

    // @NotNull = null
    // @NotEmpty = null, ""
    // @NotBlank = null, "", " "

    private Long id;

    @NotNull(message = "필수 정보입니다.")
    private Integer personCount;

    @NotBlank(message = "필수 정보입니다.")
    private String reservationDate;

    @NotBlank(message = "필수 정보입니다.")
    private String reservationTime;

    private String demand;




}
