package com.example.restaurantprojectv1.domain.dao;

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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private Integer score;

    private String filename;

    private String filepath;

    private String description;

    @CreatedDate
    private LocalDateTime registeredAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}
