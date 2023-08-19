package com.example.restaurantprojectv1.domain.entity;

import com.example.restaurantprojectv1.domain.dto.UserDto;
import com.example.restaurantprojectv1.domain.enumclass.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @CreatedDate
    private LocalDateTime registeredAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime unregisteredAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updatedAt;


    @JsonManagedReference // JPA 연관관계에서 양방향 매핑 선언 후 발생하는 infinite recursion 발생 해결 위해 사용
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList = new ArrayList<>();


    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, String phoneNumber, UserRole role, LocalDateTime unregisteredAt){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.unregisteredAt = unregisteredAt;
    }

    public void setPassword(String encodedPassword){
        this.password = encodedPassword;
    }

    public void set(UserDto.Request userDto){
        this.nickname = userDto.getNickname();
        this.phoneNumber = userDto.getPhoneNumber();
    }

    public void setUnregister(){
        this.unregisteredAt = LocalDateTime.now();
        this.nickname = "탈퇴한 회원";
    }


    // 테스트용
    public User(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

}
