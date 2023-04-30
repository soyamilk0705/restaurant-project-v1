package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.MenuItem;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MenuItemDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request{
        private Long id;

        @NotBlank(message = "필수 정보 입니다.")
        private String food;

        @NotNull(message = "필수 정보 입니다.")
        private Integer price;

        private String description;

        @Builder
        public Request(String food, Integer price, String description){
            this.food = food;
            this.price = price;
            this.description = description;
        }
    }


    @Getter
    public static class Response{
        private Long id;
        private String food;
        private Integer price;
        private String description;
        private List<MenuItemFileDto.Response> fileList;

        public Response(MenuItem menuItem) {
            this.id = menuItem.getId();
            this.food = menuItem.getFood();
            this.price = menuItem.getPrice();
            this.description = menuItem.getDescription();
            this.fileList = MenuItemFileDto.entityListToDtoList(menuItem.getMenuItemFileList());
        }
    }
}
