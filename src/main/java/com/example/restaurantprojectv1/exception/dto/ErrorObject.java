package com.example.restaurantprojectv1.exception.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ErrorObject {

    // 에러가 난 field (ex: email, name, password...)
    private String field;

    private String message;

}
