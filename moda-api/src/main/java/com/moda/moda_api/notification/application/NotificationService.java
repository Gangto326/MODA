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
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardContentType;
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


	// FCM 발송
	@Transactional
	public void sendFCMNotification(String userId, NotificationType type, Card card) {
		// 유저 토큰 가져오기.
		Set<String> tokens = fcmTokenService.getUserTokens(userId);
		String imageUrl = card.getTypeId().equals(1) ?
			String.format("https://img.youtube.com/vi/%s/default.jpg", card.getThumbnailUrl()) : card.getThumbnailUrl();

		System.out.println("userId : " + userId);
		System.out.println("NotificationType : " +type);
		System.out.println();
		System.out.println(imageUrl);
		tokens.forEach(System.out::println);

		System.out.println(imageUrl);
		tokens.forEach(token -> {
			Message message = Message.builder()
				.setToken(token)
				.putData("cardId", String.valueOf(card.getCardId().getValue()))  // cardId를 문자열로 명확히 전달
				.putData("imageUrl", imageUrl)
				.putData("title", card.getTitle())
				.setNotification(com.google.firebase.messaging.Notification.builder()
					.setTitle("카드가 생성이 되었습니다. 모다모다~")
					.setBody(card.getTitle())
					.build())
				.setAndroidConfig(AndroidConfig.builder()
					.setTtl(3600 * 1000)
					.setPriority(AndroidConfig.Priority.HIGH)
					.setNotification(AndroidNotification.builder()
						.setColor("#FFFFFF")
						.setIcon("icon_round")
						.setImage(imageUrl)  // **이미지 URL 추가**
						.setClickAction("OPEN_ACTIVITY")
						.setDefaultVibrateTimings(true)
						.setDefaultSound(true)
						.setNotificationCount(1)
						.build())
					.build())
				.build();
			try {
				System.out.println("메시지 전송 시도: " + token);
				String response = firebaseMessaging.send(message);
				System.out.println("전송 성공. Response: " + response);
			} catch (FirebaseMessagingException e) {
				System.out.println("전송 실패. 에러: " + e.getMessage());
				e.printStackTrace();
				handleFCMException(e, token);
			}
		});
	}

	@Transactional
	public void sendErrorNotification(String userId, String errorMessage) {
		// 유저 토큰 가져오기
		Set<String> tokens = fcmTokenService.getUserTokens(userId);

		tokens.forEach(token -> {
			Message message = Message.builder()
				.setToken(token)
				.setNotification(com.google.firebase.messaging.Notification.builder()
					.setTitle(errorMessage)
					.build())
				.setAndroidConfig(AndroidConfig.builder()
					.setTtl(3600 * 1000)
					.setPriority(AndroidConfig.Priority.HIGH)
					.setNotification(AndroidNotification.builder()
						.setColor("#FFFFFF")
						.setIcon("icon_round")
						.setImage("https://a805bucket.s3.ap-northeast-2.amazonaws.com/images/logo/naverLogo.jpg")
						.setClickAction("OPEN_ACTIVITY")
						.setDefaultVibrateTimings(true)
						.setDefaultSound(true)
						.setNotificationCount(1)
						.build())
					.build())
				.build();
			try {
				System.out.println("크롤링 실패 알림 전송 시도: " + token);
				String response = firebaseMessaging.send(message);
				System.out.println("전송 성공. Response: " + response);
			} catch (FirebaseMessagingException e) {
				System.out.println("전송 실패. 에러: " + e.getMessage());
				e.printStackTrace();
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