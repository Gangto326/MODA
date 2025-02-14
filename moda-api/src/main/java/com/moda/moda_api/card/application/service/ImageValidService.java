package com.moda.moda_api.card.application.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageValidService {

	private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
	private final long MAX_SIZE = 10 * 1024 * 1024; // 10MB

	public boolean validateFile(MultipartFile file) throws IllegalArgumentException {
		// 파일 크기 검사
		if (file.getSize() > MAX_SIZE) {
			throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
		}

		// 파일 확장자 검사
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
			throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
		}

		// 파일 타입 검사
		String contentType = file.getContentType();
		if (!contentType.startsWith("image/")) {
			throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
		}
		return false;
	}
}
