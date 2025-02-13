package com.moda.moda_api.notification.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.moda.moda_api.notification.domain.NotificationType;
import com.moda.moda_api.notification.presentation.FCMTokenRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationApplicationService {
	private final NotificationService notificationService;
	private final FCMTokenService fcmTokenService;

	// FCM 토큰 등록
	public void registerToken(String userId, String token) {

		fcmTokenService.saveToken(userId, token);
	}

	// 알림 전송
	public void sendNotification(String userId, NotificationType type, String contentId, String content) {

		notificationService.sendNotification(userId, type, contentId, content);
	}

	// 사용자의 알림 목록 조회
	public List<NotificationResponse> getUserNotifications(String userId, boolean unreadOnly) {
		return notificationService.getNotifications(userId, unreadOnly)
			.stream()
			.map(NotificationResponse::from)
			.collect(Collectors.toList());
	}

	public Set<String> getTokenByUserId(String userId){
		return fcmTokenService.getUserTokens(userId);
	}

	// 알림 읽음 처리
	public void markAsRead(Long notificationId) {
		notificationService.markAsRead(notificationId);
	}
}