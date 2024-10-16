package com.example.board.dto;


import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    //검색용
    private String type;
    private String keyword;

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }
}
