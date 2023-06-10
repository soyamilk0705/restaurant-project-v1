package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.dto.SessionDto;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    /**
     * 회원 가입 화면
     */
    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("sessionDto", new SessionDto());
        return "authentication/join";
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public String join(@Valid SessionDto sessionDto, BindingResult bindingResult, Model model) {
        if (!sessionDto.getPassword1().equals(sessionDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "authentication/join";
        }

        sessionService.join(sessionDto);

        model.addAttribute("message", "회원가입이 완료되었습니다.");
        model.addAttribute("searchUrl", "/home");

        return "message";
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/checkEmailDuplicate")
    @ResponseBody
    public boolean checkEmailDuplicate(@RequestParam("email") String email){
        return sessionService.checkEmailDuplicate(email);
    }

    /**
     * 로그인 화면
     */
    @GetMapping("/login")
    public String loginPage(){
        return "authentication/login";
    }


}
