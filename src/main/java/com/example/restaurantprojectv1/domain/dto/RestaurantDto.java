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

    @NotBlank(message = "매장 이름은 필수 정보입니다.")
    private String restaurantName;

    @NotBlank(message = "주소는 필수 정보입니다.")
    private String address;

    private String information;

    @NotBlank(message = "전화 번호는 필수 정보입니다.")
    private String phoneNumber;

    @NotNull(message = "좌석 규모는 필수 정보입니다.")
    private Integer limitedPersonNumber;

    @NotBlank(message = "영업 시작시간은 필수 정보입니다.")
    private String startTime;

    @NotBlank(message = "영업 마감시간은 필수 정보입니다.")
    private String endTime;

    @ColumnDefault("0")
    private boolean packaging;

    @ColumnDefault("0")
    private boolean delivery;

    @ColumnDefault("0")
    private boolean parking;


}
