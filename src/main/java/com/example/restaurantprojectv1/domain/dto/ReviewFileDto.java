package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.ReviewFile;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewFileDto {

    @Getter
    public static class Response{
        private Long id;
        private String fileName;
        private String filePath;

        public Response(ReviewFile reviewFile){
            this.id = reviewFile.getId();
            this.fileName = reviewFile.getFileName();
            this.filePath = reviewFile.getFilePath();
        }

    }

    public static List<Response> entityListToDtoList(List<ReviewFile> fileList){
        return fileList.stream()
                .map(file -> new Response(file))
                .collect(Collectors.toList());
    }
}
