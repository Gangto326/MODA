package com.moda.moda_api.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 도메인 모델입니다.
 * 비즈니스 로직을 포함하는 풍부한 도메인 객체입니다.
 * JPA나 다른 기술에 의존하지 않는 순수한 도메인 객체입니다.
 */
@Getter
public class User {
    /**
     * 빌더 패턴을 사용한 User 객체 생성을 위한 생성자입니다.
     * createdAt은 객체 생성 시점에 자동으로 설정되며,
     * deletedAt은 비즈니스 메서드인 delete()를 통해서만 설정됩니다.
     *
     * @param userId 사용자 식별자
     * @param email 사용자 이메일
     * @param hashedPassword 암호화된 비밀번호
     * @param profileImage 프로필 이미지 URL
     * @param nickname 사용자 닉네임
     * @param status 사용자 상태
     */
    private UserId userId;
    private String email;
    private String hashedPassword;
    private String userName;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Builder
    public User(UserId userId, String email, String hashedPassword, String userName,
        String nickname, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.userId = userId;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userName = userName;
        this.nickname = nickname;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.deletedAt = deletedAt;
    }

    /**
     * 사용자의 비밀번호를 업데이트합니다.
     * 비밀번호는 이미 암호화되어 있어야 합니다.
     *
     * @param newPassword 새로운 암호화된 비밀번호
     */
    public void updatePassword(String newPassword) {
        this.hashedPassword = newPassword;
    }

    /**
     * 사용자 계정을 삭제 처리합니다.
     * 실제 삭제가 아닌 soft delete 방식을 사용합니다.
     * deletedAt 필드에 현재 시간을 설정합니다.
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 사용자 계정의 삭제 여부를 확인합니다.
     *
     * @return 삭제된 계정이면 true, 아니면 false
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

}