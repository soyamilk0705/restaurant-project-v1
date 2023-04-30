package com.example.restaurantprojectv1.domain.dto;

import com.example.restaurantprojectv1.domain.entity.Reservation;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request{
        private Long id;

        @Min(1)
        @NotNull(message = "필수 정보입니다.")
        private Integer personCount;

        @NotNull(message = "필수 정보입니다.")
        private String reservationDate;

        @NotBlank(message = "필수 정보입니다.")
        private String reservationTime;

        private String demand;


        @Builder
        public Request(Integer personCount, String reservationDate, String reservationTime, String demand){
            this.personCount = personCount;
            this.reservationDate = reservationDate;
            this.reservationTime = reservationTime;
            this.demand = demand;
        }
    }


    @Getter
    public static class Response{
        private Long id;
        private String restaurantName;
        private String nickname;
        private String phoneNumber;
        private Integer personCount;
        private String reservationDate;
        private String reservationTime;
        private String demand;
        private boolean cancel;

        public Response(Reservation reservation) {
            this.id = reservation.getId();
            this.restaurantName = reservation.getRestaurant().getRestaurantName();
            this.nickname = reservation.getUser().getNickname();
            this.phoneNumber = reservation.getUser().getPhoneNumber();
            this.personCount = reservation.getPersonCount();
            this.reservationDate = reservation.getReservationDate().toString();
            this.reservationTime = reservation.getReservationTime();
            this.demand = reservation.getDemand();
            this.cancel = reservation.isCancel();
        }
    }

}
