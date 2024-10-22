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
    private String videoUrl; // 영상 URL 추가

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private String writer;
    private String email;

    // 본문(content)의 첫 번째 줄만 반환하는 메서드 추가
    public String getFirstLineOfContent() {
        if (this.content != null && !this.content.isEmpty()) {
            // 줄바꿈 기준으로 첫 번째 줄만 반환
            String[] lines = this.content.split("\n");
            return lines.length > 0 ? lines[0] : this.content;
        }
        return "";
    }
}
