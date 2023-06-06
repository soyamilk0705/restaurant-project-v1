package com.example.restaurantprojectv1.domain.entity;

import com.example.restaurantprojectv1.domain.dto.MenuItemDto;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String food;

    private int price;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @OneToMany(mappedBy = "menuItem", orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", orphanRemoval = true)
    private List<MenuItemFile> menuItemFileList = new ArrayList<>();

    @Builder
    public MenuItem(String food, int price, String description){
        this.food = food;
        this.price = price;
        this.description = description;
    }


    public void set(MenuItemDto.Request menuItemDto){
        this.food = menuItemDto.getFood();
        this.price = menuItemDto.getPrice();
        this.description = menuItemDto.getDescription();
    }
}
