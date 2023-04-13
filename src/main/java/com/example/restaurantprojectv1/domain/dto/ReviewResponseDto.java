package com.example.restaurantprojectv1.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long id;

    private String restaurantName;

    private String nickname;

    private String food;

    private Integer score;

    private String description;

    private String filename;

    private String filepath;

    private LocalDate updatedAt;
}
