package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.Pagination;
import com.example.restaurantprojectv1.domain.entity.PrincipalDetails;
import com.example.restaurantprojectv1.domain.entity.Restaurant;
import com.example.restaurantprojectv1.domain.dto.ReservationDto;
import com.example.restaurantprojectv1.domain.dto.RestaurantDto;
import com.example.restaurantprojectv1.service.ReservationService;
import com.example.restaurantprojectv1.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@Controller
@RequiredArgsConstructor
public class ReservationController{

    private final ReservationService reservationService;
    private final RestaurantService restaurantService;

    @GetMapping("/reservation/store")
    public String reservationStorePage(){
        return "reservation/reservationStore";
    }

    @GetMapping("/reservation/store.do")
    public String reservationStore(Model model,
                                   @RequestParam(required = false) String city,
                                   @RequestParam(required = false) String country,
                                   @RequestParam(required = false) String keyword){

        model.addAttribute("restaurantList", restaurantService.readAll(city, country, keyword));

        return "reservation/reservationStore";
    }

    @GetMapping("/reservation/{restaurantId}")
    public String createPage(Model model, @PathVariable Long restaurantId){

        model.addAttribute("restaurant", restaurantService.read(restaurantId));
        model.addAttribute("reservationDto", new ReservationDto.Request());

        return "reservation/reservationWrite";
    }

    @PostMapping("/reservation/{restaurantId}")
    public String create(@PathVariable Long restaurantId,
                         @AuthenticationPrincipal PrincipalDetails userDetails,
                         @Valid @ModelAttribute(name = "reservationDto") ReservationDto.Request reservationDto,
                         BindingResult bindingResult,
                         Model model) {

        RestaurantDto restaurant = restaurantService.read(restaurantId);

        if (bindingResult.hasErrors()){
            model.addAttribute("restaurant", restaurant);
            return "reservation/reservationWrite";
        }

        if (reservationService.checkPersonNumber(reservationDto.getPersonCount(), restaurant.getLimitedPersonNumber())){
            bindingResult.rejectValue("personCount", "overPersonCount","수용인원을 초과했습니다.");

            model.addAttribute("restaurant", restaurant);
            return "reservation/reservationWrite";
        }


        Long userId = userDetails.getUser().getId();
        reservationService.create(restaurantId, userId, reservationDto);

        model.addAttribute("message", "예약이 완료되었습니다.");
        model.addAttribute("searchUrl", "/reservation/store");

        return "message";

    }

    @GetMapping("/reservation/modify/{reservationId}")
    public String updatePage(@PathVariable Long reservationId, Model model){

        Restaurant restaurant = reservationService.getReservedRestaurant(reservationId);

        model.addAttribute("reservationDto", reservationService.read(reservationId));
        model.addAttribute("limitedPersonNumber", restaurant.getLimitedPersonNumber());

        return "reservation/reservationEdit";
    }

    @PutMapping("/reservation/modify/{reservationId}")
    public String update(@PathVariable Long reservationId,
                         @Valid @ModelAttribute("reservationDto") ReservationDto.Request reservationDto,
                         BindingResult bindingResult,
                         Model model) throws URISyntaxException {

        Restaurant restaurant = reservationService.getReservedRestaurant(reservationId);

        if (bindingResult.hasErrors()){
            model.addAttribute("restaurant", restaurant);
            return "reservation/reservationEdit";
        }

        if (reservationService.checkPersonNumber(reservationDto.getPersonCount(), restaurant.getLimitedPersonNumber())){
            bindingResult.rejectValue("personCount", "수용인원을 초과했습니다.");

            model.addAttribute("restaurant", restaurant);
            return "reservation/reservationEdit";
        }

        reservationService.update(reservationId, reservationDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/reservation");

        return "message";
    }


    @DeleteMapping("/reservation/delete/{reservationId}")
    public String delete(@PathVariable Long reservationId, Model model) {
        reservationService.delete(reservationId);

        model.addAttribute("message", "예약 취소가 완료되었습니다.");
        model.addAttribute("searchUrl", "/user/mypage/reservation");

        return "message";
    }


    @GetMapping("/admin/reservation/list")
    public String readAll(Model model, @PageableDefault(size = 5)
                            @SortDefault.SortDefaults({
                                @SortDefault(sort = "reservationDate"),
                                @SortDefault(sort = "reservationTime")
                            }) Pageable pageable){

        Page<ReservationDto.Response> responseDtoPage = reservationService.readAll(pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(responseDtoPage.getPageable().getPageNumber(), responseDtoPage.getTotalPages());

        model.addAttribute("reservationDtoPage", responseDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "reservation/reservationList";
    }

    @GetMapping("/admin/reservation/list/search")
    public String readAllSearch(Model model,
                                @RequestParam String searchType,
                                @RequestParam String keyword,
                                @PageableDefault(size = 5)
                                    @SortDefault.SortDefaults({
                                        @SortDefault(sort = "reservationDate"),
                                        @SortDefault(sort = "reservationTime")
                                    }) Pageable pageable){

        Page<ReservationDto.Response> responseDtoPage = reservationService.readAllSearch(searchType, keyword, pageable);

        Pagination pagination = new Pagination();
        pagination.setPagination(responseDtoPage.getPageable().getPageNumber(), responseDtoPage.getTotalPages());

        model.addAttribute("reservationDtoPage", responseDtoPage);
        model.addAttribute("nowPage", pagination.getNowPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "reservation/reservationListSearch";
    }

}
