package com.example.board.controller;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.Board;
import com.example.board.service.BoardService;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class BoardController {

    private BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    //메인 목록화면
    @GetMapping("/")
    public String main(){
        log.info("list");
        return "redirect:/list";
    }

    //목록 화면
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list pageRequestDTO:{}", pageRequestDTO);

        //검색어 디폴트
        PageRequestDTO requestDTO = pageRequestDTO;
        requestDTO.setType(requestDTO.getType()==null?"a":requestDTO.getType());

        PageResultDTO<BoardDTO, Board> result = boardService.getListV2(pageRequestDTO);
        model.addAttribute("result", result);
        model.addAttribute("request", requestDTO);
        return "/list";
    }

    //신규생성 화면
    @GetMapping("/write")
    public String write(@SessionAttribute(name = "member", required = false) MemberDTO member){
        log.info("write");
        if(member==null){
            return "redirect:/login";
        }
        return "/write";
    }

    //신규저장
    @PostMapping("/write")
    public String doWrite(BoardDTO dto, RedirectAttributes redirectAttributes
            , @SessionAttribute(name = "member", required = false) MemberDTO member){
        log.info("doWrite dto:" + dto);
        if(member==null){
            return "redirect:/login";
        }
        //로그인 세션정보로 작성자 주입
        dto.setEmail(member.getEmail());
        Long id = boardService.register(dto);
        if(id!=null && id>0L){
            redirectAttributes.addFlashAttribute("msg", "등록 되었습니다");
        }
        return "redirect:/list";
    }

    //상세조회 화면
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        log.info("detail");
        BoardDTO boardDTO = boardService.findById(id, true);
        if(boardDTO==null){
            return "redirect:/list";
        }
        model.addAttribute("dto", boardDTO);
        return "/detail";
    }

    //수정 화면
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model
            , @SessionAttribute(name = "member", required = false) MemberDTO member){
        log.info("modify");
        if(member==null){
            return "redirect:/login";
        }
        BoardDTO boardDTO = boardService.findById(id, false);
        if(boardDTO==null){
            return "redirect:/list";
        }
        model.addAttribute("dto", boardDTO);
        return "/modify";
    }

    //수정
    @PostMapping("/modify")
    public String update(BoardDTO dto){
        log.info("update dto:{}", dto);
        BoardDTO boardDTO = boardService.update(dto);
        if(boardDTO==null || boardDTO.getId()==null){
            return "redirect:/list";
        }
        return "redirect:/detail/"+dto.getId();
    }

    //삭제
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Object> remove(@RequestBody Map<String, Object> map){
        log.info("remove map:{}", map);
        int tempId = (int) map.get("id");
        Long id = Long.valueOf(tempId);
        if(id==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제를 하지 못했습니다.");
        }

        int result = boardService.remove(id);
        HashMap res = new HashMap();
        res.put("result", result);
        if(result>0){
            res.put("msg", "삭제가 되었습니다.");
        }else{
            res.put("msg", "삭제를 하지 못했습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
