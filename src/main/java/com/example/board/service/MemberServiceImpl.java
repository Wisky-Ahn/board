package com.example.board.service;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.Member;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor //lombok 의존성 자동주입
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override public String register(MemberDTO dto) {
        //이메일 중복 예외처리
        if(memberRepository.findById(dto.getEmail()).isPresent()){
            return null;
        }
        //신규 등록
        Member member = dtoToEntity(dto);
        member = memberRepository.save(member);
        return member.getEmail();
    }

    @Override public MemberDTO login(MemberDTO dto) {

        Optional<Member> optionalMember = memberRepository.findByEmailAndPw(dto.getEmail(), dto.getPw());
        if(optionalMember.isPresent()){
            return entityToDto(optionalMember.get());
        }
        return null;
    }
}
