package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.domain.enumclass.UserRole;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email) || !StringUtils.hasLength(email);

    }

    public Long join(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .role(UserRole.ROLE_USER)
                .registeredAt(LocalDateTime.now())
                .unregisteredAt(null).build();

        userRepository.save(user);

        return user.getId();
    }
}
