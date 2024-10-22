package com.example.board.controller;

import com.example.board.entity.UploadFile;
import com.example.board.service.ImageService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
public class ImageController {

	@Autowired
	ImageService imageService;  // 이미지 파일 처리 서비스

	@Autowired
	ResourceLoader resourceLoader;  // 파일을 리소스로 로딩하는 도구


	/**
	 * 이미지 업로드를 처리하는 메서드
	 * @param file 업로드된 파일
	 * @return 업로드 성공 시 이미지의 경로 반환
//	 */

	@PostMapping("image")
	public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
		try {
			// 이미지 파일을 저장하고, 업로드된 파일 정보를 받아옴
			UploadFile uploadFile = imageService.store(file);
			log.info("File upload successful, image URL: {}", uploadFile);  // 로그 추가
			// 저장된 이미지의 URL을 반환
			return ResponseEntity.ok().body("/image/" + uploadFile.getId());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("File upload failed: ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * 업로드된 이미지를 보여주는 메서드
	 * @param fileId 이미지 파일 ID
	 * @return 이미지 파일을 리소스로 반환
	 */
	@GetMapping("image/{fileId}")
	public ResponseEntity<?> serveFile(@PathVariable(name = "fileId") Long fileId) {
		try {
			// fileId로 저장된 파일 정보를 가져옴
			UploadFile uploadFile = imageService.load(fileId);

			// 파일 경로로 Resource 객체 생성
			Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());

			// 리소스가 존재하지 않으면 404 오류 반환
			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			// 파일의 MIME 타입에 맞게 파일 리소스를 반환
			return ResponseEntity.ok()
					.contentType(org.springframework.http.MediaType.parseMediaType(uploadFile.getContentType()))
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("파일을 불러오는 중 문제가 발생했습니다.");
		}
	}
}
