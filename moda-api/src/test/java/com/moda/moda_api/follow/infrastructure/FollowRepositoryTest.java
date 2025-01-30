package com.moda.moda_api.follow.infrastructure;

import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.follow.domain.FollowRepository;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FollowRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        // 기존 데이터 정리
        entityManager.createQuery("delete from FollowEntity").executeUpdate();
        entityManager.createQuery("delete from UserEntity").executeUpdate();
        entityManager.flush();

        // 테스트용 사용자 생성
        user1 = User.builder()
                .userId("test-user-1")
                .email("user1@example.com")
                .password("password123")
                .profileImage("profile1.jpg")
                .nickname("user1")
                .status("ACTIVE")
                .build();

        user2 = User.builder()
                .userId("test-user-2")
                .email("user2@example.com")
                .password("password123")
                .profileImage("profile2.jpg")
                .nickname("user2")
                .status("ACTIVE")
                .build();

        user3 = User.builder()
                .userId("test-user-3")
                .email("user3@example.com")
                .password("password123")
                .profileImage("profile3.jpg")
                .nickname("user3")
                .status("ACTIVE")
                .build();

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        entityManager.flush();
        entityManager.clear();
    }

    // 팔로우 관계를 저장할 수 있다
    @Test
    void saveFollowTest() {
        // Given
        String followId = UUID.randomUUID().toString();
        Follow follow = Follow.builder()
                .followId(followId)
                .followerId(user1.getUserId())
                .followingId(user2.getUserId())
                .build();

        // When
        Follow savedFollow = followRepository.save(follow);

        // Then
        assertThat(savedFollow).isNotNull();
        assertThat(savedFollow.getFollowerId()).isEqualTo(user1.getUserId());
        assertThat(savedFollow.getFollowingId()).isEqualTo(user2.getUserId());
    }

    // 팔로워 목록을 조회할 수 있다
    @Test
    void findAllFollowersByUserIdTest() {
        // Given
        String follow1Id = UUID.randomUUID().toString();
        String follow2Id = UUID.randomUUID().toString();

        Follow follow1 = Follow.builder()
                .followId(follow1Id)
                .followerId(user1.getUserId())
                .followingId(user3.getUserId())
                .build();

        Follow follow2 = Follow.builder()
                .followId(follow2Id)
                .followerId(user2.getUserId())
                .followingId(user3.getUserId())
                .build();

        followRepository.save(follow1);
        followRepository.save(follow2);

        entityManager.flush();
        entityManager.clear();

        // When
        List<Follow> followers = followRepository.findAllFollowersByUserId(user3.getUserId());

        // Then
        assertThat(followers).hasSize(2);
        assertThat(followers).extracting("followerId")
                .containsExactlyInAnyOrder(user1.getUserId(), user2.getUserId());
    }

    // 팔로잉 목록을 조회할 수 있다
    @Test
    void findAllFollowingsByUserIdTest() {
        // Given
        String follow1Id = UUID.randomUUID().toString();
        String follow2Id = UUID.randomUUID().toString();

        Follow follow1 = Follow.builder()
                .followId(follow1Id)
                .followerId(user1.getUserId())
                .followingId(user2.getUserId())
                .build();

        Follow follow2 = Follow.builder()
                .followId(follow2Id)
                .followerId(user1.getUserId())
                .followingId(user3.getUserId())
                .build();

        followRepository.save(follow1);
        followRepository.save(follow2);

        entityManager.flush();
        entityManager.clear();

        // When
        List<Follow> followings = followRepository.findAllFollowingsByUserId(user1.getUserId());

        // Then
        assertThat(followings).hasSize(2);
        assertThat(followings).extracting("followingId")
                .containsExactlyInAnyOrder(user2.getUserId(), user3.getUserId());
    }

    // 특정 팔로우 관계를 조회할 수 있다
    @Test
    void findByFollowerIdAndFollowingIdTest() {
        // Given
        String followId = UUID.randomUUID().toString();
        Follow follow = Follow.builder()
                .followId(followId)
                .followerId(user1.getUserId())
                .followingId(user2.getUserId())
                .build();

        followRepository.save(follow);

        entityManager.flush();
        entityManager.clear();

        // When
        Follow foundFollow = followRepository.findByFollowerIdAndFollowingId(
                user1.getUserId(), user2.getUserId());

        // Then
        assertThat(foundFollow).isNotNull();
        assertThat(foundFollow.getFollowerId()).isEqualTo(user1.getUserId());
        assertThat(foundFollow.getFollowingId()).isEqualTo(user2.getUserId());
    }

    // 팔로워 수를 조회할 수 있다
    @Test
    void countFollowersByUserIdTest() {
        // Given
        String follow1Id = UUID.randomUUID().toString();
        String follow2Id = UUID.randomUUID().toString();

        Follow follow1 = Follow.builder()
                .followId(follow1Id)
                .followerId(user1.getUserId())
                .followingId(user3.getUserId())
                .build();

        Follow follow2 = Follow.builder()
                .followId(follow2Id)
                .followerId(user2.getUserId())
                .followingId(user3.getUserId())
                .build();

        followRepository.save(follow1);
        followRepository.save(follow2);

        entityManager.flush();
        entityManager.clear();

        // When
        long followerCount = followRepository.countFollowersByUserId(user3.getUserId());

        // Then
        assertThat(followerCount).isEqualTo(2);
    }

    // 팔로잉 수를 조회할 수 있다
    @Test
    void countFollowingsByUserIdTest() {
        // Given
        String follow1Id = UUID.randomUUID().toString();
        String follow2Id = UUID.randomUUID().toString();

        Follow follow1 = Follow.builder()
                .followId(follow1Id)
                .followerId(user1.getUserId())
                .followingId(user2.getUserId())
                .build();

        Follow follow2 = Follow.builder()
                .followId(follow2Id)
                .followerId(user1.getUserId())
                .followingId(user3.getUserId())
                .build();

        followRepository.save(follow1);
        followRepository.save(follow2);

        entityManager.flush();
        entityManager.clear();

        // When
        long followingCount = followRepository.countFollowingsByUserId(user1.getUserId());

        // Then
        assertThat(followingCount).isEqualTo(2);
    }
}