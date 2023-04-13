package com.example.restaurantprojectv1.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "필수 정보입니다.")
    private String email;

    private String password1;

    private String password2;

    @NotBlank(message = "필수 정보입니다.")
    private String nickname;

    @NotBlank(message = "필수 정보입니다.")
    private String phoneNumber;


}
