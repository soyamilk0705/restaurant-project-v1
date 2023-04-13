package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto myPage(Long id) {
        return userRepository.findById(id)
                .map(this::checkUnregisteredAt)
                .map(this::entityToDto)
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }


    public void edit(UserDto userRequestDto) {
        User user = userRepository.findByEmailAndUnregisteredAtIsNull(userRequestDto.getEmail())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

        if (StringUtils.hasLength(userRequestDto.getPassword1())){
            String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword1());
            user.setPassword(encodedPassword);
        }

        user.setNickname(userRequestDto.getNickname())
            .setPhoneNumber(userRequestDto.getPhoneNumber());

        userRepository.save(user);
    }


    public void delete(Long id) {
        userRepository.findById(id)
                .map(this::checkUnregisteredAt)
                .map(user -> {
                    user.setUnregisteredAt(LocalDateTime.now())
                                    .setNickname("탈퇴한 회원");

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }



    public UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password1(null)
                .password2(null)
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    private User checkUnregisteredAt(User user){
        if (!ObjectUtils.isEmpty(user.getUnregisteredAt())){
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }
}
