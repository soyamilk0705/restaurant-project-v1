package com.example.restaurantprojectv1.domain.entity;

import com.example.restaurantprojectv1.domain.dto.ReviewDto;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private int score;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewFile> reviewFileList = new ArrayList<>();


    @Builder
    public Review(int score, String description, Restaurant restaurant, MenuItem menuItem, User user){
        this.score = score;
        this.description = description;
        this.restaurant = restaurant;
        this.menuItem = menuItem;
        this.user = user;

    }

    public void set(ReviewDto.Request reviewDto){
        this.score = reviewDto.getScore();
        this.description = reviewDto.getDescription();
    }

    public void setMenuItem(MenuItem menuItem){
        this.menuItem = menuItem;
    }
}
