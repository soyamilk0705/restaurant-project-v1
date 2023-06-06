package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.Review;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class ReviewDto {

    @Getter
    @Setter
    public static class Request{
        private Long id;

        @NotBlank(message = "필수 정보입니다.")
        private String food;

        @Min(1)
        @Max(5)
        @NotNull(message = "필수 정보입니다.")
        private int score;

        private String description;
    }

    @Getter
    public static class Response{
        private Long id;
        private String restaurantName;
        private String nickname;
        private String food;
        private int score;
        private String description;
        private LocalDate updatedAt;
        private List<ReviewFileDto.Response> fileList;

        // Entity -> Dto
        public Response(Review review) {
            this.id = review.getId();
            this.restaurantName = review.getRestaurant().getRestaurantName();
            this.nickname = review.getUser().getNickname();
            this.food = review.getMenuItem().getFood();
            this.score = review.getScore();
            this.description = review.getDescription();
            this.updatedAt = review.getUpdatedAt().toLocalDate();
            this.fileList = ReviewFileDto.entityListToDtoList(review.getReviewFileList());
        }

    }

}
