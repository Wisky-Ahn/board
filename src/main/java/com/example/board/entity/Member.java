package com.example.board.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "boardList")
public class Member extends BaseEntity{
    @Id
    private String email;
    private String name;
    private String pw;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    //@ToString.Exclude
    private List<Board> boardList = new ArrayList<>();
    public void addBoard(Board board){
        boardList.add(board);
        board.setMember(this);
    }
 }
