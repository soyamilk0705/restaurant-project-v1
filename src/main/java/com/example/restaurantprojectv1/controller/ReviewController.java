package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.Pagination;
import com.example.restaurantprojectv1.domain.dao.PrincipalDetails;
import com.example.restaurantprojectv1.domain.dto.MenuItemResponseDto;
import com.example.restaurantprojectv1.domain.dto.RestaurantDto;
import com.example.restaurantprojectv1.domain.dto.ReviewRequestDto;
import com.example.restaurantprojectv1.domain.dto.ReviewResponseDto;
import com.example.restaurantprojectv1.service.MenuItemService;
import com.example.restaurantprojectv1.service.RestaurantService;
import com.example.restaurantprojectv1.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;


    @GetMapping("/list")
    public String reviewList(Model model, @PageableDefault(size = 5, sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ReviewResponseDto> reviewDtoPage = reviewService.readAll(pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(reviewDtoPage.getPageable().getPageNumber(), reviewDtoPage.getTotalPages());

        model.addAttribute("reviewDtoPage", reviewDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "review/reviewList";
    }

    @GetMapping("/list/search")
    public String reviewListSearch(Model model, @RequestParam String keyword, @PageableDefault Pageable pageable){
        Page<ReviewResponseDto> reviewDtoPage = reviewService.readAllSearch(keyword, pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(reviewDtoPage.getPageable().getPageNumber(),  reviewDtoPage.getTotalPages());

        model.addAttribute("reviewDtoPage", reviewDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "review/reviewList";
    }

    @GetMapping("/store")
    public String selectRestaurantPage(){
        return "review/reviewStore";
    }

    @GetMapping("/store.do")
    public String restaurantList(Model model,
                                 @RequestParam(required = false) String city,
                                 @RequestParam(required = false) String country,
                                 @RequestParam(required = false) String keyword){

        model.addAttribute("restaurantList", restaurantService.readAll(city, country, keyword));

        return "review/reviewStore";
    }

    @GetMapping("/{restaurantId}")
    public String read(@PathVariable Long restaurantId, Model model){
        model.addAttribute("reviewRequestDto", new ReviewRequestDto());
        model.addAttribute("restaurant", restaurantService.read(restaurantId));
        model.addAttribute("menuItemList", menuItemService.readAll());

        return "review/reviewForm";
    }

    @PostMapping("/{restaurantId}")
    public String create(@PathVariable Long restaurantId,
                         @AuthenticationPrincipal PrincipalDetails userDetails,
                         @Valid ReviewRequestDto reviewRequestDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws IOException {

        if (bindingResult.hasErrors()){
            return "review/reviewForm";
        }

        Long userId = userDetails.getUser().getId();
        reviewService.create(restaurantId, userId, reviewRequestDto, file);

        model.addAttribute("message", "작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/review/list");

        return "message";
    }

    @GetMapping("/modify/{reviewId}")
    public String updatePage(@PathVariable Long reviewId, Model model){
        model.addAttribute("reviewRequestDto", reviewService.read(reviewId));
        model.addAttribute("menuItemList", menuItemService.readAll());

        return "review/reviewForm";
    }

    @PutMapping("/modify/{reviewId}")
    public String update(@PathVariable Long reviewId,
                         @Valid ReviewRequestDto reviewRequestDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws URISyntaxException, IOException {

        if (bindingResult.hasErrors()){
            return "review/reviewForm";
        }

        reviewService.update(reviewId, file, reviewRequestDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/review");

        return "message";
    }

    @DeleteMapping("/delete/{reviewId}")
    public String delete(@PathVariable Long reviewId, Model model) {
        reviewService.delete(reviewId);

        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/review");

        return "message";
    }
}
