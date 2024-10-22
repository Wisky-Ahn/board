package com.example.board.service;

import com.example.board.entity.UploadFile;
import com.example.board.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    UploadFileRepository uploadFileRepository;

    // 영상 저장 경로 설정
    private final static Path videoLocation = Paths.get("/Users/anhyeonjun/Desktop/sw 2024/board/upload/video");

    public UploadFile store(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            // MIME 타입이 video인지 확인
            String contentType = file.getContentType();
            if (!contentType.startsWith("video/")) {
                throw new Exception("This is not a video file: " + file.getOriginalFilename());
            }

            String saveFileName = fileSave(videoLocation.toString(), file);
            UploadFile saveFile = UploadFile.builder()
                    .fileName(file.getOriginalFilename())
                    .saveFileName(saveFileName)
                    .contentType(file.getContentType())
                    .size(file.getResource().contentLength())
                    .registerDate(LocalDateTime.now())
                    .filePath(videoLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName)
                    .build();

            uploadFileRepository.save(saveFile);
            return saveFile;

        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public UploadFile load(Long fileId) {
        return uploadFileRepository.findById(fileId).get();
    }

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }
}
