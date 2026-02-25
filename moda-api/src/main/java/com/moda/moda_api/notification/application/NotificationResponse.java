package com.moda.moda_api.notification.application;

import java.time.LocalDateTime;

import com.moda.moda_api.notification.domain.Notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
	private Long notificationId;
	private String type;
	private String content;
	private String contentId;
	private boolean isRead;
	private LocalDateTime sentAt;

	public static NotificationResponse from(Notification notification) {
		return NotificationResponse.builder()
			.notificationId(notification.getNotificationId())
			.type(notification.getType().getDescription())
			.content(notification.getContent())
			.contentId(notification.getContentId())
			.isRead(notification.isRead())
			.build();
	}
}

