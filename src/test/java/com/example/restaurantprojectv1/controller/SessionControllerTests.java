package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class SessionControllerTests {

    @Autowired private WebApplicationContext wac;
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }


    @Test
    @DisplayName("로그인 실패")
    void loginFail() throws Exception {
        mockMvc.perform(post("/login.do")
                        .param("username", "fail@gmail.com")
                        .param("password", "fail"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());

    }


    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception{
        mockMvc.perform(post("/logout")
                        .param("email","user1@gmail.com")
                        .param("password","asdf"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andExpect(redirectedUrl("/home"));

    }


    private UserDto.Request createUserDto(){
        return UserDto.Request.builder()
                .email("user@gmail.com")
                .password1("user")
                .password2("user")
                .nickname("user")
                .phoneNumber("010-0000-0000")
                .build();
    }

    private String toJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

}