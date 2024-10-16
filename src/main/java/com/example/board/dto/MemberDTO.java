package com.example.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO {
    private String email;
    private String name;
    private String pw;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
