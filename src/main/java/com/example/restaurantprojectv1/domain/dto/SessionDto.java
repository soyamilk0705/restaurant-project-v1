package com.example.restaurantprojectv1.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {

    // 회원가입 시 password1, password2 빈값으로 안받기 위해서 만듦

    private Long id;

    @NotBlank(message = "필수 정보입니다.")
    private String email;

    @NotBlank(message = "필수 정보입니다.")
    private String password1;

    @NotBlank(message = "필수 정보입니다.")
    private String password2;

    @NotBlank(message = "필수 정보입니다.")
    private String nickname;

    @NotBlank(message = "필수 정보입니다.")
    private String phoneNumber;

}