package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.UserRepository;
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

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionService sessionService;

    @Test
    @DisplayName("메뉴 읽기 실패")
    void myPage_fail() throws IOException {
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> userService.myPage(1004L));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", fail.getMessage());
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void edit(){
        // given
        UserDto user = createUserRequestDto();
        sessionService.join(user);

        user.setNickname("수정")
                .setPhoneNumber("010-9999-9999");

        // when
        userService.edit(user);

        // then
        User savedUser = userRepository.findByEmailAndUnregisteredAtIsNull(user.getEmail()).get();

        assertEquals(user.getNickname(), savedUser.getNickname());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void delete(){
        // given
        UserDto user = createUserRequestDto();
        Long userId = sessionService.join(user);

        // when
        userService.delete(userId);

        // then
        User savedUser = userRepository.findById(userId).get();

        assertEquals("탈퇴한 회원", savedUser.getNickname());
        assertNotNull(savedUser.getUnregisteredAt());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 이미 탈퇴한 회원")
    void delete_fail(){
        // given
        UserDto user = createUserRequestDto();
        Long userId = sessionService.join(user);
        userService.delete(userId);

        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> userService.delete(userId));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", fail.getMessage());
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