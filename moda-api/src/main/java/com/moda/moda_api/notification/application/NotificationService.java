package com.moda.moda_api.notification.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.moda.moda_api.notification.domain.Notification;
import com.moda.moda_api.notification.domain.NotificationType;
import com.moda.moda_api.notification.infrastructure.NotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final FCMTokenService fcmTokenService;
	private final FirebaseMessaging firebaseMessaging;
	private final NotificationRepository notificationRepository;

	// 알림 저장 및 FCM 전송
	@Transactional
	public void sendNotification(String userId, NotificationType type, String contentId, String content) {
		// 1. FCM 발송
		sendFCMNotification(userId, type, content);
	}

	// FCM 발송
	private void sendFCMNotification(String userId, NotificationType type, String content) {
		// 유저 토큰 가져오기.
		Set<String> tokens = fcmTokenService.getUserTokens(userId);

		tokens.forEach(token -> {
			Message message = Message.builder()
				.setToken(token)
				.setNotification(com.google.firebase.messaging.Notification.builder()
					.setTitle(type.getDescription())
					.setBody(content)
					.setImage("https://a805bucket.s3.ap-northeast-2.amazonaws.com/images/logo/download.jpg")
					.build())
				.setAndroidConfig(AndroidConfig.builder()
					.setTtl(3600 * 1000)
					.setNotification(AndroidNotification.builder()
						.setIcon("moda_logo")
						.setColor("#FFFFFF")
						.setClickAction("OPEN_ACTIVITY")
						.setDefaultVibrateTimings(true)
						.setDefaultSound(true)
						.setNotificationCount(1)
						.build())
					.build())
				.build();
			try {
				firebaseMessaging.send(message);
			} catch (FirebaseMessagingException e) {
				handleFCMException(e, token);
			}
		});
	}

	private void handleFCMException(FirebaseMessagingException e, String token) {
		if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
			fcmTokenService.removeToken(token);
		}
		log.error("FCM 발송 실패", e);
	}


	// 알림 조회
	public List<Notification> getNotifications(String userId, boolean unreadOnly) {
		return unreadOnly ?
			notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId) :
			notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	// 읽음 처리
	@Transactional
	public void markAsRead(Long notificationId) {
		notificationRepository.findById(notificationId)
			.ifPresent(Notification::markAsRead);
	}
}