package com.example.restaurantprojectv1.domain.dao;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String food;

    private Integer price;

    private String description;

    private String filename;

    private String filepath;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @OneToMany(mappedBy = "menuItem", orphanRemoval = true)
    private List<Review> reviewList;
}
