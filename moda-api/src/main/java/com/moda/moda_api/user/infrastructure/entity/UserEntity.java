package com.moda.moda_api.user.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "nickname", length = 100, nullable = false)
    private String nickname;

    @Column(name = "role", length = 10, nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Entity -> Domain
    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     * 인프라스트럭처 계층과 도메인 계층의 경계에서 사용됩니다.
     *
     * @return 변환된 User 도메인 객체
     */
    public User toDomain() {
        return User.withId(
                this.userId,
                this.email,
                this.password,
                this.profileImage,
                this.nickname,
                this.role,
                this.createdAt,
                this.deletedAt
        );
    }

    // Domain -> Entity
    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     * 인프라스트럭처 계층과 도메인 계층의 경계에서 사용됩니다.
     *
     * @param user 변환할 User 도메인 객체
     * @return 변환된 UserEntity
     */
    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.userId = user.getUserId();
        entity.email = user.getEmail();
        entity.password = user.getPassword();
        entity.profileImage = user.getProfileImage();
        entity.nickname = user.getNickname();
        entity.role = user.getRole();
        entity.createdAt = user.getCreatedAt();
        entity.deletedAt = user.getDeletedAt();
        return entity;
    }
}