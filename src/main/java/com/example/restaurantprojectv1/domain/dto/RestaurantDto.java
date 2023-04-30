package com.example.restaurantprojectv1.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  RestaurantDto {

    private Long id;

    @NotBlank(message = "필수 정보입니다.")
    private String restaurantName;

    @NotBlank(message = "필수 정보입니다.")
    private String city;

    @NotBlank(message = "필수 정보입니다.")
    private String country;

    @NotBlank(message = "필수 정보입니다.")
    private String detailAddress;

    private String information;

    @NotBlank(message = "필수 정보입니다.")
    private String phoneNumber;

    @NotNull(message = "필수 정보입니다.")
    private Integer limitedPersonNumber;

    @NotBlank(message = "필수 정보입니다.")
    private String startTime;

    @NotBlank(message = "필수 정보입니다.")
    private String endTime;

    @ColumnDefault("0")
    private boolean packaging;

    @ColumnDefault("0")
    private boolean delivery;

    @ColumnDefault("0")
    private boolean parking;


}
