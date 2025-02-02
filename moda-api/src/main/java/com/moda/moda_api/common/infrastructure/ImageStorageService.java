package com.moda.moda_api.common.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageStorageService {
	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;



	/**
	 * S3에 업로드 하기 위한 메서드
	 * @param imageUrl 업로드할 이미지의 URL 주소
	 * @return S3에 업로드된 이미지의 URL 주소
	 */
	public String uploadImage(String imageUrl) {
		try {
			byte[] imageData = downloadImageFromUrl(imageUrl);

			// fileName을 아무 UUID로 생성하자.
			String fileName = UUID.randomUUID() + ".jpg";

			// S3에 저장될 경로
			String key = "images/" + fileName;

			PutObjectRequest request = PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)  // key에 파일명 포함
				.contentType("image/jpeg")
				.build();

			s3Client.putObject(request,
				RequestBody.fromBytes(imageData));

			// URL 생성 (key에 파일명 포함)
			return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s",
				bucket, key);

		} catch (S3Exception e) {
			log.error("Error uploading file to S3: {}", e.getMessage());
			throw new RuntimeException("Failed to upload image to S3", e);
		}
	}

	// 이미지 외부 url로 부터 이미지 다운로드
	public byte[] downloadImageFromUrl(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// 이미지 다운로드
			try (InputStream inputStream = connection.getInputStream();
				 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				return outputStream.toByteArray();
			}

		} catch (IOException e) {
			log.error("이미지 다운로드 실패  URL: {}", e.getMessage());
			throw new RuntimeException("이미지 다운로드 실패 URL", e);
		}
	}

}
