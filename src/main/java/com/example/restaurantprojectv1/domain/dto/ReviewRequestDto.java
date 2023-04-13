package com.example.restaurantprojectv1.domain.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReviewRequestDto {

    private Long id;

    @NotBlank(message = "필수 정보입니다.")
    private String food;

    @Min(1)
    @Max(5)
    @NotNull(message = "필수 정보입니다.")
    private Integer score;

    private String description;

}
