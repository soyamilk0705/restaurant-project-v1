package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.Pagination;
import com.example.restaurantprojectv1.domain.entity.PrincipalDetails;
import com.example.restaurantprojectv1.domain.dto.ReviewDto;
import com.example.restaurantprojectv1.service.MenuItemService;
import com.example.restaurantprojectv1.service.RestaurantService;
import com.example.restaurantprojectv1.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;


    /**
     * 리뷰 전체 보기
     */
    @GetMapping("/list")
    public String reviewList(Model model, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ReviewDto.Response> reviewDtoPage = reviewService.readAll(pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(reviewDtoPage.getPageable().getPageNumber(), reviewDtoPage.getTotalPages());

        model.addAttribute("reviewDtoPage", reviewDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "review/reviewList";
    }

    /**
     * 리뷰 전체 보기 - 검색
     */
    @GetMapping("/list/search")
    public String reviewListSearch(Model model, @RequestParam String keyword, @PageableDefault Pageable pageable){
        Page<ReviewDto.Response> reviewDtoPage = reviewService.readAllSearch(keyword, pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(reviewDtoPage.getPageable().getPageNumber(),  reviewDtoPage.getTotalPages());

        model.addAttribute("reviewDtoPage", reviewDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "review/reviewList";
    }

    /**
     * 리뷰 작성 전 매장 선택 화면
     */
    @GetMapping("/store")
    public String selectRestaurantPage(){
        return "review/reviewStore";
    }

    /**
     * 리뷰 작성 전 매장 선택
     */
    @GetMapping("/store.do")
    public String restaurantList(Model model,
                                 @RequestParam(required = false) String city,
                                 @RequestParam(required = false) String country,
                                 @RequestParam(required = false) String keyword){

        model.addAttribute("restaurantList", restaurantService.search(city, country, keyword));

        return "review/reviewStore";
    }

    /**
     * 리뷰 작성 화면
     */
    @GetMapping("/{restaurantId}")
    public String createPage(@PathVariable Long restaurantId, Model model){
        model.addAttribute("reviewDto", new ReviewDto.Request());
        model.addAttribute("restaurant", restaurantService.read(restaurantId));
        model.addAttribute("menuItemList", menuItemService.readAll());

        return "review/reviewForm";
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/{restaurantId}")
    public String create(@PathVariable Long restaurantId,
                         @AuthenticationPrincipal PrincipalDetails userDetails,
                         @Valid @ModelAttribute("reviewDto") ReviewDto.Request reviewDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws IOException {

        if (bindingResult.hasErrors()){
            return "review/reviewForm";
        }

        Long userId = userDetails.getUser().getId();
        reviewService.create(restaurantId, userId, reviewDto, file);

        model.addAttribute("message", "작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/review/list");

        return "message";
    }

    /**
     * 리뷰 수정 화면
     */
    @GetMapping("/modify/{reviewId}")
    public String updatePage(@PathVariable Long reviewId, Model model){
        model.addAttribute("reviewDto", reviewService.read(reviewId));
        model.addAttribute("menuItemList", menuItemService.readAll());

        return "review/reviewForm";
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/modify/{reviewId}")
    public String update(@PathVariable Long reviewId,
                         @Valid @ModelAttribute("reviewDto") ReviewDto.Request reviewDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws URISyntaxException, IOException {

        if (bindingResult.hasErrors()){
            return "review/reviewForm";
        }

        reviewService.update(reviewId, file, reviewDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/review");

        return "message";
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/delete/{reviewId}")
    public String delete(@PathVariable Long reviewId, Model model) {
        reviewService.delete(reviewId);

        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/review");

        return "message";
    }
}
