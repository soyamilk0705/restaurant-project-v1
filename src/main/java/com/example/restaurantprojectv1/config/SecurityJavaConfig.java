package com.example.restaurantprojectv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**", "/reservation/**", "/review/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login.do")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}