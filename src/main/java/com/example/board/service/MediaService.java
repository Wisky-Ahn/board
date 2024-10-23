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
public class MediaService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    private final static Path IMAGE_LOCATION = Paths.get("/Users/anhyeonjun/Desktop/sw 2024/board/upload/board");
    private final static Path VIDEO_LOCATION = Paths.get("/Users/anhyeonjun/Desktop/sw 2024/board/upload/video");
    private final static Path AUDIO_LOCATION = Paths.get("/Users/anhyeonjun/Desktop/sw 2024/board/upload/audio");

    public UploadFile storeImage(MultipartFile file) throws IOException {
        return storeFile(file, IMAGE_LOCATION);
    }

    public UploadFile storeVideo(MultipartFile file) throws IOException {
        return storeFile(file, VIDEO_LOCATION);
    }

    public UploadFile storeAudio(MultipartFile file) throws IOException {
        return storeFile(file, AUDIO_LOCATION);
    }

    private UploadFile storeFile(MultipartFile file, Path location) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일을 저장할 수 없습니다: " + file.getOriginalFilename());
        }

        String saveFileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        File targetFile = new File(location.toFile(), saveFileName);
        FileCopyUtils.copy(file.getBytes(), targetFile);

        UploadFile uploadedFile = UploadFile.builder()
                .fileName(file.getOriginalFilename())
                .saveFileName(saveFileName)
                .filePath(targetFile.getAbsolutePath())
                .contentType(file.getContentType())
                .registerDate(LocalDateTime.now())
                .size(file.getSize())
                .build();

        uploadFileRepository.save(uploadedFile);
        return uploadedFile;
    }

    public UploadFile load(Long fileId) {
        return uploadFileRepository.findById(fileId).orElse(null);
    }
}
