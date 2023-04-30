package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dto.SessionDto;
import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private SessionService sessionService;

    private SessionDto sessionDto = new SessionDto();
    private Long sessionId;

    @BeforeEach
    void before(){
        sessionDto = SessionDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password1("password")
                .password2("password")
                .phoneNumber("010-1111-1111")
                .build();

        sessionId = sessionService.join(sessionDto);
    }

    @Test
    @DisplayName("회원 정보 읽기 실패 - 데이터 없음")
    void myPage_found_fail() {
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> userService.read(1004L));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", fail.getMessage());
    }

    @Test
    @DisplayName("회원 정보 읽기 실패 - 탈퇴한 회원")
    void myPage_unregister_fail() {
        // given
        userService.delete(sessionId);

        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> userService.read(sessionId));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", fail.getMessage());
    }


    @Test
    @DisplayName("회원 정보 수정 성공")
    void edit_success(){
        // given
        UserDto.Request user = createUserRequestDto();
        user.setNickname("수정");
        user.setPhoneNumber("010-9999-9999");

        // when
        userService.edit(user);

        // then
        User savedUser = userRepository.findByEmailAndUnregisteredAtIsNull(user.getEmail()).orElse(null);

        assertEquals(user.getNickname(), savedUser.getNickname());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void delete_success(){
        // given
        // when
        userService.delete(sessionId);

        // then
        User savedUser = userRepository.findById(sessionId).orElse(null);

        assertEquals("탈퇴한 회원", savedUser.getNickname());
        assertNotNull(savedUser.getUnregisteredAt());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 이미 탈퇴한 회원")
    void delete_fail(){
        // given
        userService.delete(sessionId);

        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> userService.delete(sessionId));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", fail.getMessage());
    }




    private UserDto.Request createUserRequestDto() {
        return UserDto.Request.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password1("password")
                .password2("password")
                .phoneNumber("010-1111-1111")
                .build();
    }
}