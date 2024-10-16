package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class BoardServiceTest {

    @Autowired BoardService boardService;

    @Test
    @Rollback(value = false)
    public void registerTest(){
        BoardDTO dto = BoardDTO.builder()
                .title("Test Title")
                .content("Test Content")
                .email("test@gmail.com")
                .build();
        boardService.register(dto);
    }

    @Test
    public void getListTest(){
        PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<BoardDTO, Board> resultDTO = boardService.getList(requestDTO);
        for(BoardDTO dto : resultDTO.getDtoList()){
            System.out.println(dto);
        }
        int totalPage = resultDTO.getTotalPage(); //전체페이지
        int startPage = resultDTO.getStart(); //화면 시작페이지
        int endPage = resultDTO.getEnd(); //화면 끝페이지
        boolean prev = resultDTO.isPrev(); //이전
        boolean next = resultDTO.isNext();
        System.out.println("totalPage:" + totalPage );
        System.out.println("startPage:" + startPage );
        System.out.println("endPage:" + endPage );
        System.out.println("prev:" + prev );
        System.out.println("next:" + next );
        for(int i : resultDTO.getPageList()){
            System.out.println("page list num:" + i );
        }
    }
}
