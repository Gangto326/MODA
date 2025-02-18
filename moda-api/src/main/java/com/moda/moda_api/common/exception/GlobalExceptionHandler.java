package com.moda.moda_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.moda.moda_api.card.exception.ApiResponse;
import com.moda.moda_api.card.exception.DuplicateCardException;
import com.moda.moda_api.card.exception.DuplicateUrlException;
import com.moda.moda_api.notification.application.NotificationService;
import com.moda.moda_api.notification.domain.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
	private final NotificationService notificationService;

	@ExceptionHandler(DuplicateCardException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<Void> handleDuplicateCardException(DuplicateCardException e) {
		log.error("Duplicate card error occurred: {}", e.getMessage());

		try {
			notificationService.sendErrorNotification(e.getUserId(), e.getMessage());
		} catch (Exception ex) {
			log.error("Failed to send FCM notification", ex);
		}

		return ApiResponse.error(e.getMessage());
	}

	@ExceptionHandler(ContentExtractionException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse<Void> handleContentExtractionException(ContentExtractionException e) {
		log.error("Content extraction failed: {}", e.getMessage());

		try {
			notificationService.sendErrorNotification(e.getUserId(), e.getMessage());
		} catch (Exception ex) {
			log.error("Failed to send FCM notification", ex);
		}

		return ApiResponse.error(e.getMessage());
	}
	@ExceptionHandler(DuplicateUrlException.class)
	@ResponseStatus(HttpStatus.CONFLICT) // INTERNAL_SERVER_ERROR 대신 CONFLICT가 더 적절합니다
	public ApiResponse<Void> handleUrlException(DuplicateUrlException  e) {
		log.error("Content extraction failed: {}", e.getMessage());

		try {
			notificationService.sendErrorNotification(e.getUserId(), e.getMessage());
		} catch (Exception ex) {
			log.error("Failed to send FCM notification", ex);
		}

		return ApiResponse.error(e.getMessage());
	}

}