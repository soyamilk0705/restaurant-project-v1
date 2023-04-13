package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.dto.*;
import com.example.restaurantprojectv1.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RestaurantController{

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant/list")
    public String readAllPage(){
        return "restaurant/restaurantList";
    }

    @GetMapping("/restaurant/list.do")
    public String readAll(Model model,
                         @RequestParam(required = false) String city,
                         @RequestParam(required = false) String country,
                         @RequestParam(required = false) String keyword){

        model.addAttribute("restaurantList", restaurantService.readAll(city, country, keyword));

        return "restaurant/restaurantList";
    }

    @GetMapping("/restaurant/detail")
    public String read(Model model, @RequestParam Long id){
        model.addAttribute("restaurant", restaurantService.read(id));
        return "restaurant/restaurantDetail";
    }


    @GetMapping("/admin/restaurant/register")
    public String createPage(Model model){
        model.addAttribute("restaurantDto", new RestaurantDto());
        return "restaurant/restaurantForm";
    }


    @PostMapping("/admin/restaurant/register")
    public String create(@Valid RestaurantDto restaurantDto, BindingResult bindingResult, Model model) throws URISyntaxException {

        if (bindingResult.hasErrors()){
            return "restaurant/restaurantForm";
        }

        restaurantService.create(restaurantDto);

        model.addAttribute("message", "작성이 완료되었습니다..");
        model.addAttribute("searchUrl", "/restaurant/list");

        return "message";
    }


    @GetMapping("/admin/restaurant/modify/{id}")
    public String updatePage(@PathVariable Long id, Model model){
        model.addAttribute("restaurantDto", restaurantService.read(id));
        return "restaurant/restaurantForm";
    }

    @PutMapping("/admin/restaurant/modify/{id}")
    public String update(@PathVariable Long id,
                         @Valid RestaurantDto restaurantDto,
                         BindingResult bindingResult,
                         Model model) throws URISyntaxException {

        if (bindingResult.hasErrors()){
            return "restaurant/restaurantForm";
        }

        restaurantService.update(id, restaurantDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/restaurant/list");
        return "message";
    }

    @DeleteMapping("/admin/restaurant/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        restaurantService.delete(id);

        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/restaurant/list");

        return "message";
    }

}
