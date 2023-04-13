package com.example.restaurantprojectv1.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ROLE_ADMIN(0, "관리자"),
    ROLE_USER(1, "사용자")
    ;

    private Integer id;

    private String description;
}
