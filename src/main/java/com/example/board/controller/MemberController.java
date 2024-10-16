package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.service.BoardService;
import com.example.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    //신규생성 화면
    @GetMapping("/signup")
    public String signup(){
        log.info("signup");
        return "/signup";
    }

    //신규저장
    @PostMapping("/signup")
    public String register(MemberDTO dto, RedirectAttributes redirectAttributes){
        log.info("register dto:" + dto);
        String email = memberService.register(dto);
        if(email!=null && email.isEmpty()==false){
            redirectAttributes.addFlashAttribute("msg", "회원가입이 되었습니다");
        }else{
            redirectAttributes.addFlashAttribute("msg", "회원가입을 하지 못했습니다");
        }
        return "redirect:/login";
    }

    //로그인 화면
    @GetMapping("/login")
    public String login(){
        log.info("login");
        return "/login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String doLogin(MemberDTO dto, RedirectAttributes redirectAttributes, HttpServletRequest request){
        log.info("doLogin dto:" + dto);
        MemberDTO memberDTO = memberService.login(dto);
        if(memberDTO==null){
            redirectAttributes.addFlashAttribute("msg", "로그인을 하지 못했습니다");
            return "redirect:/login";
        }

        //세션정보 저장
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("member", memberDTO);
        return "redirect:/list";
    }

    //로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        log.info("logout");

        HttpSession httpSession = request.getSession();
        httpSession.invalidate(); //세션정보 초기화
        return "redirect:/list";
    }
}
