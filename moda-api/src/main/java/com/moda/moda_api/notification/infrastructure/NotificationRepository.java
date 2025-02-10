package com.moda.moda_api.notification.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.notification.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
	List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);
}