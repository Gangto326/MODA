package com.moda.moda_api.user.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId("test-id-1")
                .email("test@example.com")
                .password("password123")
                .profileImage("profile.jpg")
                .nickname("testUser")
                .status("ACTIVE")
                .build();

        userRepository.save(testUser);
    }

    @Test
    void findByIdTest() {
        // When
        User foundUser = userRepository.findById("test-id-1");

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getNickname()).isEqualTo("testUser");
    }

    @Test
    void updateProfileTest() {
        // When
        testUser.updateProfile("newNickname", "new-profile.jpg");
        User updatedUser = userRepository.save(testUser);

        // Then
        assertThat(updatedUser.getNickname()).isEqualTo("newNickname");
        assertThat(updatedUser.getProfileImage()).isEqualTo("new-profile.jpg");
    }

    @Test
    void updatePasswordTest() {
        // When
        testUser.updatePassword("newPassword123");
        User updatedUser = userRepository.save(testUser);

        // Then
        assertThat(updatedUser.getPassword()).isEqualTo("newPassword123");
    }

    @Test
    void deleteUserTest() {
        // When
        testUser.delete();
        User deletedUser = userRepository.save(testUser);

        // Then
        assertThat(deletedUser.isDeleted()).isTrue();
        assertThat(deletedUser.getDeletedAt()).isNotNull();
    }

    @Test
    void existsByEmailTest() {
        // When
        boolean exists = userRepository.existsByEmail("test@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void existsByNicknameTest() {
        // When
        boolean exists = userRepository.existsByNickname("testUser");
        boolean notExists = userRepository.existsByNickname("nonexistentUser");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}