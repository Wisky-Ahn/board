package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

public interface MemberService {
    // 등록
    String register(MemberDTO dto);

    // 로그인
    MemberDTO login(MemberDTO dto);
    
    //dto => entity 변환
    default Member dtoToEntity(MemberDTO dto){
        Member entity = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .pw(dto.getPw())
                .build();
        return entity;
    }

    //entity => dto 변환
    default MemberDTO entityToDto(Member entity){
        MemberDTO dto = MemberDTO.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                //.pw(entity.getPw())
                .build();
        return dto;
    }

    //entity리스트 => dto리스트 변환
    default List<MemberDTO> toList(List<Member> list){
        return list.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    }
}
