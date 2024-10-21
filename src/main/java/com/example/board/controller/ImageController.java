package com.example.board.controller;

import com.example.board.entity.UploadFile;
import com.example.board.service.ImageService;
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
public class ImageController {

	@Autowired ImageService imageService;

	@Autowired
	ResourceLoader resourceLoader;

	@PostMapping("image")
	public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
		try {
			UploadFile uploadFile = imageService.store(file);
			return ResponseEntity.ok().body("/image/" + uploadFile.getId());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	// 여기서부터 추가된 부분입니다
	@GetMapping("image/{fileId}")
	//이거 @PathVariable를 @PathVariable(name = "fileId")로 해야 파일 가지고옴
	public ResponseEntity<?> serveFile(@PathVariable(name = "fileId") Long fileId) {
		try {
			// fileId로 파일 정보 가져오기
			UploadFile uploadFile = imageService.load(fileId);

			// 파일 경로를 Resource 객체로 로드
			Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());

			// 리소스가 존재하지 않으면 404 반환
			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			// 파일의 MIME 타입을 설정하여 이미지를 반환
			return ResponseEntity.ok()
					.contentType(org.springframework.http.MediaType.parseMediaType(uploadFile.getContentType()))
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("파일을 불러오는 중 문제가 발생했습니다.");
		}
	}
	// 추가된 부분 끝
}
