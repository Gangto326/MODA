package com.moda.moda_api.notification.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;

	@Column(nullable = false)
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@Column(nullable = false)
	private String contentId;

	@Column(nullable = false)
	private String content;

	private boolean isRead = false;

	private LocalDateTime createdAt;
	@Builder
	public Notification(String userId, NotificationType type, String contentId, String content) {
		this.userId = userId;
		this.type = type;
		this.contentId = contentId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
	}
	public void markAsRead() {
		this.isRead = true;
	}
}