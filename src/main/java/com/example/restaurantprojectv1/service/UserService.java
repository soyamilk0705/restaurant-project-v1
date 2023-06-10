package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 읽기
     */
    public UserDto.Response read(Long id) {
        return userRepository.findById(id)
                .map(this::checkUnregisteredAt)
                .map(r -> new UserDto.Response(r))
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }


    /**
     * 회원 정보 수정
     */
    public void edit(UserDto.Request userDto) {
        User user = userRepository.findByEmailAndUnregisteredAtIsNull(userDto.getEmail())
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

        if (StringUtils.hasLength(userDto.getPassword1())){
            String encodedPassword = passwordEncoder.encode(userDto.getPassword1());
            user.setPassword(encodedPassword);
        }

        user.set(userDto);
    }


    /**
     * 회원 탈퇴
     */
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .map(this::checkUnregisteredAt)
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

        user.setUnregister();
    }


    /**
     * 탈퇴한 회원인지 확인
     */
    private User checkUnregisteredAt(User user){
        if (!ObjectUtils.isEmpty(user.getUnregisteredAt())){
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }
}
