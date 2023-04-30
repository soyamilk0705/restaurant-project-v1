package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dto.SessionDto;
import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class SessionServiceTests {

    @Autowired SessionService sessionService;
    @Autowired UserRepository userRepository;

    // TODO

    @Test
    @DisplayName("이메일 중복 검사 실패")
    void check_email_duplicate_fail(){
        // given
        SessionDto user = createSessionDto();
        sessionService.join(user);

        // when
        boolean result = sessionService.checkEmailDuplicate("test@gmail.com");

        // then
        assertEquals(true, result);
    }


    @Test
    @DisplayName("회원가입 성공")
    void join_success(){
        // given
        SessionDto user = createSessionDto();

        // when
        sessionService.join(user);

        // then
        User savedUser = userRepository.findByEmailAndUnregisteredAtIsNull(user.getEmail()).get();

        assertEquals(user.getNickname(), savedUser.getNickname());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }


    private SessionDto createSessionDto() {
        return SessionDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password1("password")
                .password2("password")
                .phoneNumber("010-1111-1111")
                .build();
    }
}