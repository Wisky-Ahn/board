package com.example.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private String url;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private String writer;
    private String email;
}
