package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.Pagination;
import com.example.restaurantprojectv1.domain.dao.PrincipalDetails;
import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.ReservationResponseDto;
import com.example.restaurantprojectv1.domain.dto.ReviewResponseDto;
import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.service.ReservationService;
import com.example.restaurantprojectv1.service.ReviewService;
import com.example.restaurantprojectv1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    // <User>
    // localhost:8080/user/edit
    // localhost:8080/user/mypage
    // localhost:8080/user/unregister


    private final UserService userService;
    private final ReservationService reservationService;
    private final ReviewService reviewService;


    @GetMapping("/edit")
    public String editPage(@AuthenticationPrincipal PrincipalDetails userDetails, Model model){
        Long userId = userDetails.getUser().getId();
        model.addAttribute("userDto", userService.myPage(userId));

        return "authentication/edit";
    }


    @PutMapping("/edit")
    public String edit(@Valid UserDto userDto, BindingResult bindingResult, Model model){
        // TODO: 탈퇴 버튼 jquery 보내는게 맞는지 확인

        if (!userDto.getPassword1().equals(userDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordIncorrect", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()){
            return "authentication/edit";
        }

        userService.edit(userDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/edit");

        return "message";
    }


    @GetMapping("/mypage/reservation")
    public String myPageReservation(Model model,
                                    @AuthenticationPrincipal PrincipalDetails userDetails,
                                    @PageableDefault(size = 5)
                                    @SortDefault.SortDefaults ({
                                            @SortDefault(sort = "reservationDate"),
                                            @SortDefault(sort = "reservationTime")
                                    }) Pageable pageable){

        User user = userDetails.getUser();

        Page<ReservationResponseDto> reservationDtoPage = reservationService.myPageCurrentDate(user.getId(), pageable);

        Pagination pagination =  new Pagination();
        pagination.setPagination(reservationDtoPage.getPageable().getPageNumber(), reservationDtoPage.getTotalPages());

        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("reviewCount", reviewService.countByUserId(user.getId()));
        model.addAttribute("reservationDtoPage", reservationDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "authentication/mypage/reservationTab";
    }

    @GetMapping("/mypage/past_reservation")
    public String myPagePastReservation(Model model,
                                        @AuthenticationPrincipal PrincipalDetails userDetails,
                                        @PageableDefault(size = 5)
                                        @SortDefault.SortDefaults ({
                                                @SortDefault(sort = "reservationDate"),
                                                @SortDefault(sort = "reservationTime")
                                        }) Pageable pageable){

        User user = userDetails.getUser();

        Page<ReservationResponseDto> reservationDtoPage = reservationService.myPagePastDate(user.getId(), pageable);

        Pagination pagination =  new Pagination();
        pagination.setPagination(reservationDtoPage.getPageable().getPageNumber(), reservationDtoPage.getTotalPages());

        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("reservationCount", reservationService.countByUserIdCurrentDate(user.getId()));
        model.addAttribute("reviewCount", reviewService.countByUserId(user.getId()));
        model.addAttribute("reservationDtoPage", reservationDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "authentication/mypage/pastReservationTab";
    }

    @GetMapping("/mypage/review")
    public String myPageReview(Model model,
                               @AuthenticationPrincipal PrincipalDetails userDetails,
                               @PageableDefault(size = 5, sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable){
        User user = userDetails.getUser();

        Page<ReviewResponseDto> reviewDtoPage = reviewService.readAllByUserId(user.getId(), pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(reviewDtoPage.getPageable().getPageNumber(), reviewDtoPage.getTotalPages());

        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("reservationCount", reservationService.countByUserIdCurrentDate(user.getId()));
        model.addAttribute("reviewDtoPage", reviewDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "authentication/mypage/reviewTab";
    }



    @DeleteMapping("/unregister")
    public String unregister(@AuthenticationPrincipal PrincipalDetails userDetails, Model model) {
        Long userId = userDetails.getUser().getId();
        userService.delete(userId);

        model.addAttribute("message", "탈퇴가 완료되었습니다.");
        model.addAttribute("searchUrl", "/logout");

        return "message";
    }

}
