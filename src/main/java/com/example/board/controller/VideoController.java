package com.example.board.controller;

import com.example.board.entity.UploadFile;
import com.example.board.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoController {

    @Autowired
    VideoService videoService;  // 비디오 파일 처리 서비스

    @Autowired
    ResourceLoader resourceLoader;  // 파일을 리소스로 로딩하는 도구

    /**
     * 비디오 업로드를 처리하는 메서드
     * @param file 업로드된 파일
     * @return 업로드 성공 시 비디오의 경로 반환
     */
    @PostMapping("video")  // 수정된 부분: 비디오 업로드를 처리하는 경로 설정
    public ResponseEntity<?> videoUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 비디오 파일을 저장하고, 업로드된 파일 정보를 받아옴
            UploadFile uploadFile = videoService.store(file);
            // 수정된 부분: 비디오의 URL을 Board 엔티티의 'videoUrl' 필드에 저장하기 위해 videoUrl을 반환
            return ResponseEntity.ok().body("/video/" + uploadFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 업로드된 비디오를 보여주는 메서드
     * @param fileId 비디오 파일 ID
     * @return 비디오 파일을 리소스로 반환
     */
    @GetMapping("video/{fileId}")  // 수정된 부분: 비디오 파일 로드를 처리하는 경로
    public ResponseEntity<?> serveFile(@PathVariable(name = "fileId") Long fileId) {
        try {
            // fileId로 저장된 파일 정보를 가져옴
            UploadFile uploadFile = videoService.load(fileId);

            // 수정된 부분: 해당 경로로부터 비디오를 불러옴
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());

            // 리소스가 존재하지 않으면 404 오류 반환
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 파일의 MIME 타입에 맞게 파일 리소스를 반환 (비디오 MIME 타입을 처리)
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(uploadFile.getContentType()))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일을 불러오는 중 문제가 발생했습니다.");
        }
    }
}
