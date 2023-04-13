package com.example.restaurantprojectv1.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponseDto {

    private Long id;

    private String food;

    private Integer price;

    private String description;

    private String filename;

    private String filepath;
}
