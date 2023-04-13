package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class SessionServiceTests {

    @Autowired SessionService sessionService;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void join(){
        // given
        UserDto user = createUserRequestDto();

        // when
        sessionService.join(user);

        // then
        User savedUser = userRepository.findByEmailAndUnregisteredAtIsNull(user.getEmail()).get();

        assertEquals(user.getNickname(), savedUser.getNickname());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }


    private UserDto createUserRequestDto() {
        return UserDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password1("password")
                .password2("password")
                .phoneNumber("010-1111-1111")
                .build();
    }
}