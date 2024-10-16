package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void insertMember(){
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member member = Member.builder().email("user"+i+"@gmail.com")
                    .name("user"+i)
                    .pw("user"+i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    @Transactional
    public void selectMember(){
        //cascade = {CascadeType.ALL} 자식 엔티티 조회 테스트
        Optional<Member> optionalMember = memberRepository.findById("user1@gmail.com");
        if(optionalMember.isPresent()){
            Member member = optionalMember.get();
            List<Board> boardList = member.getBoardList(); //내 작성글
            for(Board b : boardList){
                System.out.println(b);
            }
        }
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void addBookByMember(){
        //cascade = {CascadeType.ALL} 자식 엔티티 저장 테스트
        Optional<Member> optionalMember = memberRepository.findById("test1111");
        if(optionalMember.isPresent()){
            Member member = optionalMember.get();
            Board board = Board.builder().title("aaa").content("aaa").build();
            member.addBoard(board);
        }
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void deleteMember(){
        //cascade = {CascadeType.ALL} 자식 엔티티 삭제 테스트
        Optional<Member> optionalMember = memberRepository.findById("test1111");
        if(optionalMember.isPresent()){
            Member member = optionalMember.get();
            memberRepository.delete(member);
        }
    }
}
