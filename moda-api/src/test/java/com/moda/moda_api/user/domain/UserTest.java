package com.moda.moda_api.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId("test-id")
                .email("test@example.com")
                .password("password123")
                .profileImage("profile.jpg")
                .nickname("testUser")
                .status("ACTIVE")
                .build();
    }

    // User 생성 시 createdAt이 자동으로 설정되는지 확인하는 테스트
    @Test
    void createUserTest() {
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getDeletedAt()).isNull();
        assertThat(user.isDeleted()).isFalse();
    }

    // withId 정적 팩토리 메서드로 User 객체가 정상적으로 생성되는지 확인하는 테스트
    @Test
    void createUserWithStaticFactoryMethodTest() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime deletedAt = LocalDateTime.now();

        // When
        User userFromFactory = User.withId(
                "test-id",
                "test@example.com",
                "password123",
                "profile.jpg",
                "testUser",
                "ACTIVE",
                createdAt,
                deletedAt
        );

        // Then
        assertThat(userFromFactory.getUserId()).isEqualTo("test-id");
        assertThat(userFromFactory.getDeletedAt()).isEqualTo(deletedAt);
        assertThat(userFromFactory.isDeleted()).isTrue();
    }

    // 프로필 정보(닉네임, 이미지) 업데이트가 정상적으로 동작하는지 확인하는 테스트
    @Test
    void updateProfileTest() {
        // When
        String newNickname = "newNickname";
        String newProfileImage = "new-profile.jpg";
        user.updateProfile(newNickname, newProfileImage);

        // Then
        assertThat(user.getNickname()).isEqualTo(newNickname);
        assertThat(user.getProfileImage()).isEqualTo(newProfileImage);
        assertThat(user.getCreatedAt()).isNotNull();  // 다른 필드는 변경되지 않아야 함
    }

    // 비밀번호 업데이트가 정상적으로 동작하는지 확인하는 테스트
    @Test
    void updatePasswordTest() {
        // When
        String newPassword = "newPassword123";
        user.updatePassword(newPassword);

        // Then
        assertThat(user.getPassword()).isEqualTo(newPassword);
        assertThat(user.getCreatedAt()).isNotNull();  // 다른 필드는 변경되지 않아야 함
    }

    // 계정 삭제(soft delete)가 정상적으로 동작하는지 확인하는 테스트
    @Test
    void deleteTest() {
        // Given
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.getDeletedAt()).isNull();

        // When
        user.delete();

        // Then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.getDeletedAt()).isNotNull();
        assertThat(user.getUserId()).isEqualTo("test-id");  // 다른 정보는 유지되어야 함
    }
}