package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @Rollback(value = false)
    public void registerTest(){
        MemberDTO dto = MemberDTO.builder()
                .email("test@gmail.com")
                .name("test")
                .pw("1234")
                .build();
        memberService.register(dto);
    }
}
