package com.example.restaurantprojectv1.domain.dao;

import com.example.restaurantprojectv1.domain.enumclass.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @JsonSerialize(using = LocalDateTimeSerializer.class)   // LocalDateTime 직렬화 역직렬화 오류 해결 위해 사용
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registeredAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime unregisteredAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;


    @JsonManagedReference // JPA 연관관계에서 양방향 매핑 선언 후 발생하는 infinite recursion 발생 해결 위해 사용
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList;


    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList;


}
