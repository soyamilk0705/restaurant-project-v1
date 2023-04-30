package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.MenuItemFile;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemFileDto {

    @Getter
    public static class Response{
        private Long id;
        private String fileName;
        private String filePath;

        public Response(MenuItemFile menuItemFile){
            this.id = menuItemFile.getId();
            this.fileName = menuItemFile.getFileName();
            this.filePath = menuItemFile.getFilePath();
        }
    }

    public static List<Response> entityListToDtoList(List<MenuItemFile> menuItemFileList){
        return menuItemFileList.stream()
                .map(m -> new Response(m))
                .collect(Collectors.toList());
    }
}
