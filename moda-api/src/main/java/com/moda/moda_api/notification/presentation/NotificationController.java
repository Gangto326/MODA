package com.moda.moda_api.notification.presentation;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.notification.application.NotificationApplicationService;
import com.moda.moda_api.notification.application.NotificationResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationApplicationService notificationService;

	@PostMapping("/token")
	public ResponseEntity<Void> registerToken(
		@UserId String userId,
		@RequestBody FCMTokenRequest request) {
		notificationService.registerToken(userId , request.getToken());
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<List<NotificationResponse>> getNotifications(
		@UserId String userId,
		@RequestParam(defaultValue = "false") boolean unreadOnly) {
		List<NotificationResponse> notifications =
			notificationService.getUserNotifications(userId, unreadOnly);
		return ResponseEntity.ok(notifications);
	}

	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
		notificationService.markAsRead(notificationId);
		return ResponseEntity.ok().build();
	}
}