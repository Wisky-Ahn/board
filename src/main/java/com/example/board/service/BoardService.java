package com.example.board.service;

import com.example.board.entity.Board;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    // 등록
    Long register(BoardDTO dto);

    //목록
    PageResultDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO);
    PageResultDTO<BoardDTO, Board> getListV2(PageRequestDTO requestDTO);

    //상세조회
    BoardDTO findById(Long id, boolean hit);

    //수정
    BoardDTO update(BoardDTO dto);

    //삭제
    int remove(Long id);
    
    //dto => entity 변환
    default Board dtoToEntity(BoardDTO dto){
        Board entity = Board.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .hit(dto.getHit())
                .url(dto.getUrl())
                .build();
        return entity;
    }

    //entity => dto 변환
    default BoardDTO entityToDto(Board entity){
        BoardDTO dto = BoardDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .hit(entity.getHit())
                .url((entity.getUrl()))
                .email(entity.getMember().getEmail())
                .writer(entity.getMember().getName())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }

    //entity리스트 => dto리스트 변환
    default List<BoardDTO> toList(List<Board> list){
        return list.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    }
}
