package com.example.restaurantprojectv1.controller;

import com.example.restaurantprojectv1.domain.dto.MenuItemDto;
import com.example.restaurantprojectv1.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;


    @GetMapping("/menu/list")
    public String menuListPage(Model model){
        model.addAttribute("menuItemList", menuItemService.readAll());
        return "menu/menuList";
    }

    @GetMapping("/menu/detail")
    public String read(@RequestParam Long id, Model model) {
        model.addAttribute("menuItem", menuItemService.read(id));
        return "menu/menuDetail";
    }

    @GetMapping("/admin/menu/register")
    public String createPage(Model model){
        model.addAttribute("menuItemDto", new MenuItemDto.Request());
        return "menu/menuForm";
    }

    @PostMapping("/admin/menu/register")
    public String create(@Valid @ModelAttribute(name = "menuItemDto") MenuItemDto.Request menuItemDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws IOException {

        if (bindingResult.hasErrors()){
            return "menu/menuForm";
        }

        menuItemService.create(file, menuItemDto);

        model.addAttribute("message", "작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/menu/list");

        return "message";
    }

    @GetMapping("/admin/menu/modify/{id}")
    public String updatePage(@PathVariable Long id, Model model){
        model.addAttribute("menuItemDto", menuItemService.read(id));
        return "menu/menuForm";
    }

    @PutMapping("/admin/menu/modify/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute(name = "menuItemDto") MenuItemDto.Request menuItemDto,
                         BindingResult bindingResult,
                         MultipartFile file,
                         Model model) throws IOException {

        if (bindingResult.hasErrors()){
            return "menu/menuForm";
        }

        menuItemService.update(id, file, menuItemDto);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/menu/list");

        return "message";
    }

    @DeleteMapping("/admin/menu/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        menuItemService.delete(id);

        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/menu/list");

        return "message";
    }


}
