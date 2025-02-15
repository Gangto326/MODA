package com.moda.moda_api.user.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.moda.moda_api.user.domain.User;

import java.time.LocalDateTime;

/**
 * 사용자 정보의 데이터베이스 매핑을 위한 JPA 엔티티 클래스입니다.
 * 실제 데이터베이스 테이블과 매핑되며, JPA 관련 설정을 포함합니다.
 *
 * @NoArgsConstructor(access = AccessLevel.PROTECTED)는 JPA 스펙을 만족시키면서
 * 무분별한 객체 생성을 막기 위해 protected 생성자를 제공합니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String hashedPassword;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "nickname", length = 100, nullable = false)
    private String nickname;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}