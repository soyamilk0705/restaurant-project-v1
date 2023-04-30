package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


public class UserDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request{
        // 회원 가입 외에 는 password1, password2 필수 값 아니기 때문에 만듦

        private Long id;

        @NotBlank(message = "필수 정보입니다.")
        private String email;

        private String password1;

        private String password2;

        @NotBlank(message = "필수 정보입니다.")
        private String nickname;

        @NotBlank(message = "필수 정보입니다.")
        private String phoneNumber;


        @Builder
        public Request(String email, String password1, String password2, String nickname, String phoneNumber){
            this.email = email;
            this.password1 = password1;
            this.password2 = password2;
            this.nickname = nickname;
            this.phoneNumber = phoneNumber;
        }
    }


    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String email;
        private String password1;
        private String password2;
        private String nickname;
        private String phoneNumber;

        public Response(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.password1 = null;
            this.password2 = null;
            this.nickname = user.getNickname();
            this.phoneNumber = user.getPhoneNumber();
        }
    }


}
