package com.example.board.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "member")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1500)
    private String content;
    @Builder.Default
    private int hit = 0;
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)  //FetchType.LAZY (지연로딩)
    @JoinColumn(name="member_email")
    private Member member;
}
