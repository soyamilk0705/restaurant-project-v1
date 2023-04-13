package com.example.restaurantprojectv1.controller;

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

    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("userDto", new UserDto());
        return "authentication/join";
    }


    @PostMapping("/join")
    public String join(@Valid UserDto userDto, BindingResult bindingResult, Model model) {

        if (!StringUtils.hasLength(userDto.getPassword1())) {
            bindingResult.rejectValue("password1", "passwordNull", "필수 정보입니다.");
        }

        if (!StringUtils.hasLength(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordNull", "필수 정보입니다.");
        }

        if (!userDto.getPassword1().equals(userDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "authentication/join";
        }

        sessionService.join(userDto);

        model.addAttribute("message", "회원가입이 완료되었습니다.");
        model.addAttribute("searchUrl", "/home");

        return "message";
    }

    @GetMapping("/checkEmailDuplicate")
    @ResponseBody
    public boolean checkEmailDuplicate(@RequestParam("email") String email){
        return sessionService.checkEmailDuplicate(email);
    }

    @GetMapping("/login")
    public String loginPage(){
        return "authentication/login";
    }


}
