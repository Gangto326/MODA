package com.moda.moda_api.common.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
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
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageStorageService {
	private final S3Client s3Client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	/**
	 * S3에 업로드 하기 위한 메서드
	 * @param imageUrl 업로드할 이미지의 URL 주소
	 * @return S3에 업로드된 이미지의 URL 주소
	 */
	public String uploadImageFromurl(String imageUrl) {
		try {
			byte[] imageData = downloadImageFromUrl(imageUrl);

			// fileName을 아무 UUID로 생성하자.
			String fileName = UUID.randomUUID() + ".jpg";

			// S3에 저장될 경로
			String key = "images/blogPost/" + fileName;

			PutObjectRequest request = PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)  // key에 파일명 포함
				.contentType("image/jpeg")
				.build();

			s3Client.putObject(request, RequestBody.fromBytes(imageData));

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
			String modifiedUrl = imageUrl;
			if (imageUrl.contains("_blur")) {
				// _blur 제거 후 type 파라미터 변경
				modifiedUrl = imageUrl.replace("_blur", "")
					.substring(0, imageUrl.indexOf("type=")) + "type=w800";
			}

			URL url = new URL(modifiedUrl);

			System.out.println(modifiedUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			connection.setInstanceFollowRedirects(true);

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

	private String getExtension(String filename) {
		return Optional.ofNullable(filename)
			.filter(f -> f.contains("."))
			.map(f -> "." + f.substring(f.lastIndexOf(".") + 1))
			.orElse(".jpg");
	}

	public String uploadMultipartFile(MultipartFile file) {
		try {
			// fileName을 UUID로 생성
			String fileName = UUID.randomUUID() + getExtension(file.getOriginalFilename());

			// S3에 저장될 경로
			String key = "images/capture/" + fileName;

			PutObjectRequest request = PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.contentType(file.getContentType())
				.build();

			s3Client.putObject(request,
				RequestBody.fromBytes(file.getBytes()));

			// URL 생성
			return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s",
				bucket, key);

		} catch (S3Exception | IOException e) {
			log.error("Error uploading file to S3: {}", e.getMessage());
			throw new RuntimeException("Failed to upload image to S3", e);
		}
	}

}
