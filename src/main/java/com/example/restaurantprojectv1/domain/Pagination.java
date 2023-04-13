package com.example.restaurantprojectv1.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

    private Integer nowPage;

    private Integer startPage;

    private Integer endPage;


    public void setPagination(int pageNumber, int totalPages){
        this.nowPage = pageNumber + 1;
        this.startPage = Math.max(this.nowPage - 4, 1);
        this.endPage = Math.min(this.nowPage + 5, totalPages);
    }


}
