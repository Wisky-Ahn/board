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
public class ImageService {

	@Autowired UploadFileRepository uploadFileRepository;

	private final static Path rootLocation = Paths.get("C://upload", "board");

	public UploadFile store(MultipartFile file) throws Exception {
		//		 fileName : 예류2.jpg
		//		 filePath : d:/images/uuid-예류2.jpg
		//		 saveFileName : uuid-예류2.png
		//		 contentType : image/jpeg
		//		 size : 4994942
		//		 registerDate : 2020-02-06 22:29:57.748
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty file " + file.getOriginalFilename());
			}

			String saveFileName = fileSave(rootLocation.toString(), file);
			UploadFile saveFile = UploadFile.builder()
			.fileName(file.getOriginalFilename())
			.saveFileName(saveFileName)
			.contentType(file.getContentType())
			.size(file.getResource().contentLength())
			.registerDate(LocalDateTime.now())
			// C:/upload/board/e722fb7d-abb0-47c4-8994-ff07a98f1bec2.jpg
			.filePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName).build();
			uploadFileRepository.save(saveFile);
			return saveFile;

		} catch(IOException e) {
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
