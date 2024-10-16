package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            int num = (int) Math.ceil(Math.random()*10);
            String email = "user"+num+"@gmail.com";
            Optional<Member> optionalMemeber = memberRepository.findById(email);
            if(optionalMemeber.isPresent()){
                Board entity = Board.builder()
                        .title("Title"+i)
                        .content("Content " + i)
                        .member(optionalMemeber.get())
                        .build();
                boardRepository.save(entity);
            }
        });
    }

    @Test
    public void updateBoard(){
        //수정 테스트
        Optional<Board> optionalBoard = boardRepository.findById(1L);
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            board.setTitle("change Title");
            board.setContent("change Content");
            boardRepository.save(board);
        }
    }

    @Test
    public void queryDSLTest(){
        //키워드 "1" 이 타이틀에 포함된 데이터 검색
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        QBoard qBoard = QBoard.board;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qBoard.title.contains(keyword);
        builder.and(expression);
        Page<Board> result = boardRepository.findAll(builder, pageable);
        result.stream().forEach(board -> {
            System.out.println(board);
        });
    }

    @Test
    public void queryDSLTest2() {
        //키워드 "1" 이 타이틀, 내용, 작성자에 포함된 데이터 검색
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        QBoard qBoard = QBoard.board;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle =  qBoard.title.contains(keyword);
        BooleanExpression exContent =  qBoard.content.contains(keyword);
        BooleanExpression exWriter =  qBoard.member.name.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent).or(exWriter);
        builder.and(exAll);
        builder.and(qBoard.id.gt(0L));
        Page<Board> result = boardRepository.findAll(builder, pageable);
        result.stream().forEach(board -> {
            System.out.println(board);
        });
    }
}
