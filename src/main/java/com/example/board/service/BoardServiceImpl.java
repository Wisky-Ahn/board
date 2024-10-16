package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.QBoard;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor //의존성 자동주입
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO dto) {
        Long id = null;
        Optional<Member> optionalMember = memberRepository.findById(dto.getEmail());
        if(optionalMember.isPresent()){
            Board entity = dtoToEntity(dto);
            //Member 주입
            Member member = optionalMember.get();
            entity.setMember(member);
            entity = boardRepository.save(entity);
            id = entity.getId();
        }
        return id;
    }

    @Override
    public PageResultDTO<BoardDTO, Board> getList(
            PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        Function<Board, BoardDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<BoardDTO, Board> getListV2(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());

        Page<Board> result = boardRepository.findAll(getSearch(requestDTO), pageable);

        Function<Board, BoardDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    @Override public BoardDTO findById(Long id, boolean hit) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if(boardOptional.isPresent()){
            Board board = boardOptional.get();
            if(hit){
                board.setHit(board.getHit()+1); //조회수 추가
                board = boardRepository.save(board);
            }
            return entityToDto(board);
        }
        return null;
    }

    @Override public BoardDTO update(BoardDTO dto) {
        Optional<Board> boardOptional = boardRepository.findById(dto.getId());
        if(boardOptional.isPresent()){
            Board entity = boardOptional.get();
            //수정항목만 업데이트
            entity.setTitle(dto.getTitle());
            entity.setContent(dto.getContent());
            entity.setUrl(dto.getUrl());
            entity = boardRepository.save(entity);
            return entityToDto(entity);
        }
        return null;
    }

    @Override public int remove(Long id) {
        int result = 0;
        try{
            boardRepository.deleteById(id);
            result = 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public BooleanBuilder getSearch(PageRequestDTO requestDto){
        String type = requestDto.getType();
        type = type!=null&&type.isEmpty()==false ? type : "a";

        //엔티티 검색
        QBoard qBoard = QBoard.board;
        String keyword = requestDto.getKeyword();
        keyword = keyword!=null&&keyword.isEmpty()==false ? keyword : "";
        BooleanBuilder builder = new BooleanBuilder();
        switch (type){
            case "a": {
                BooleanExpression allExp = qBoard.title.contains(keyword);
                BooleanExpression contentExp =  qBoard.content.contains(keyword);
                BooleanExpression writerExp =  qBoard.member.name.contains(keyword);
                builder.and(allExp.or(contentExp).or(writerExp));
                break;
            }
            case "t":
                BooleanExpression titleExp =  qBoard.title.contains(keyword);
                builder.and(titleExp);
                break;
            case "c":
                BooleanExpression contentExp =  qBoard.content.contains(keyword);
                builder.and(contentExp);
                break;
            case "w":
                BooleanExpression writerExp =  qBoard.member.name.contains(keyword);
                builder.and(writerExp);
                break;
        }
        builder.and(qBoard.id.gt(0L));
        return builder;
    }
}
