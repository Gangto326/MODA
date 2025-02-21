package com.moda.moda_api.notification.presentation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import com.moda.moda_api.notification.domain.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationApplicationService notificationService;

	// @PostMapping("")
	// public ResponseEntity<?> testSendNotification(){
	// 	String userId = "user";
	// 	NotificationType notificationType = NotificationType.card;
	// 	String contentId = "contentId";
	// 	String content = "내용물";
	// 	notificationService.sendNotification(userId,notificationType,contentId,content);
	// 	return ResponseEntity.ok().build();
	//
	// }

	@GetMapping("token")
	public ResponseEntity<Set<String>> getTokenByUserId(@UserId String userId){
		Set<String> userToken = notificationService.getTokenByUserId("user");
		return ResponseEntity.ok(userToken);
	}
	@PostMapping("/token")  // URL 일치시키기
	public ResponseEntity<Void> registerToken(
		@UserId String userId,
		@RequestBody FCMTokenRequest request) {

		log.info("FCM token registration request received for user: {}", userId);
		// 토큰 값은 로그에 출력하지 않음

		notificationService.registerToken(userId, request.getToken());
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
