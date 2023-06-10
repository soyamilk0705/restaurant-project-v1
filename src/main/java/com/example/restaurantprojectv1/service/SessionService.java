package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dto.SessionDto;
import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.domain.enumclass.UserRole;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 체크
     */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email) || !StringUtils.hasLength(email);

    }

    /**
     * 회원가입
     */
    public Long join(SessionDto sessionDto) {
        User user = User.builder()
                .email(sessionDto.getEmail())
                .password(passwordEncoder.encode(sessionDto.getPassword1()))
                .nickname(sessionDto.getNickname())
                .phoneNumber(sessionDto.getPhoneNumber())
                .role(UserRole.ROLE_USER)
                .unregisteredAt(null).build();

        userRepository.save(user);

        return user.getId();
    }
}
