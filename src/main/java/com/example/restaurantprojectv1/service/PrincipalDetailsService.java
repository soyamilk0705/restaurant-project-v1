package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.PrincipalDetails;
import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmailAndUnregisteredAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new PrincipalDetails(user);
    }
}
