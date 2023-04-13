package com.example.restaurantprojectv1.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequestDto {

    private Long id;

    @NotBlank(message = "필수 정보 입니다.")
    private String food;

    @NotNull(message = "필수 정보 입니다.")
    private Integer price;

    private String description;

}
