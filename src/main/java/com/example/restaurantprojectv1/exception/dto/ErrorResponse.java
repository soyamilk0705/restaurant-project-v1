package com.example.restaurantprojectv1.exception.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ErrorResponse {

    private String statusCode;

    private String message;

    private String requestUrl;


}
