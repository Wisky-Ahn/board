package com.example.board.controller;

import com.example.board.entity.UploadFile;
import com.example.board.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ResourceLoader resourceLoader;

    // 이미지와 비디오 업로드를 모두 처리하는 엔드포인트
    @PostMapping("/media")
    public ResponseEntity<?> uploadMedia(@RequestParam("file") MultipartFile file) {
        try {
            // 파일의 contentType을 기반으로 이미지 또는 비디오 구분
            String contentType = file.getContentType();
            List<String> imageTypes = Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE);
            List<String> videoTypes = Arrays.asList("video/mpeg", "video/mp4", "video/quicktime");
            List<String> audioTypes = Arrays.asList(
                    "audio/mpeg",  // mp3
                    "audio/mp3",   // mp3
                    "audio/wav",   // wav
                    "audio/m4a"    // m4a 추가
            );

            if (imageTypes.contains(contentType)) {
                // 이미지 파일인 경우
                UploadFile uploadedFile = mediaService.storeImage(file);
                return ResponseEntity.ok("/image/" + uploadedFile.getId());
            } else if (videoTypes.contains(contentType)) {
                // 비디오 파일인 경우
                UploadFile uploadedFile = mediaService.storeVideo(file);
                return ResponseEntity.ok("/video/" + uploadedFile.getId());
            } else if (audioTypes.contains(contentType)) {
                // 오디오 파일인 경우
                UploadFile uploadedFile = mediaService.storeAudio(file);
                return ResponseEntity.ok("/audio/" + uploadedFile.getId());
            } else {
                // 지원되지 않는 파일 타입인 경우
                return ResponseEntity.badRequest().body("지원되지 않는 파일 형식입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 중 에러 발생");
        }
    }


    // 미디어 파일 제공 (이미지, 비디오, 오디오에 대한 요청 처리)
    @GetMapping("/media/{fileId}")
    public ResponseEntity<?> serveMedia(@PathVariable(name = "fileId") Long fileId) {
        try {
            UploadFile uploadFile = mediaService.load(fileId);

            // 파일 경로로 Resource 객체 생성
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            log.info("파일 경로: {}", uploadFile.getFilePath());


            // 리소스가 존재하지 않으면 404 반환
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // MIME 타입을 확인하여 적절하게 파일을 반환
            String contentType = uploadFile.getContentType();
            log.info("파일 ContentType: {}", uploadFile.getContentType());


            if (contentType.startsWith("image/")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // 이미지일 경우
                        .body(resource);
            } else if (contentType.startsWith("video/")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("video/mp4")) // 비디오일 경우
                        .body(resource);
            } else if (contentType.startsWith("audio/")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/mpeg")) // 오디오일 경우
                        .body(resource);
            } else {
                return ResponseEntity.status(415).body("지원하지 않는 파일 형식입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일을 불러오는 중 문제가 발생했습니다.");
        }
    }

}
