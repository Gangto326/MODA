package com.moda.moda_api.follow.application;

import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.follow.domain.FollowRepository;
import com.moda.moda_api.follow.application.response.FollowResponse;
import com.moda.moda_api.follow.application.response.FollowerResponse;
import com.moda.moda_api.follow.application.response.FollowingResponse;
import com.moda.moda_api.follow.presentation.request.FollowRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private FollowMapper followMapper;

    @InjectMocks
    private FollowService followService;

    private Follow follow;
    private FollowRequest request;
    private String followerId;
    private String followingId;

    @BeforeEach
    void setUp() {
        followerId = "user-1";
        followingId = "user-2";

        request = FollowRequest.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        follow = Follow.builder()
                .followId(UUID.randomUUID().toString())
                .followerId(followerId)
                .followingId(followingId)
                .followerNickname("팔로워")
                .followerProfileImage("follower.jpg")
                .followingNickname("팔로잉")
                .followingProfileImage("following.jpg")
                .build();
    }

    // 팔로우 관계를 생성할 수 있다
    @Test
    void followTest() {
        // Given
        FollowResponse expectedResponse = FollowResponse.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followingId(follow.getFollowingId())
                .followedAt(follow.getFollowedAt())
                .build();

        when(followRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .thenReturn(null);
        when(followRepository.save(any(Follow.class))).thenReturn(follow);
        when(followMapper.toFollowResponse(follow)).thenReturn(expectedResponse);

        // When
        FollowResponse response = followService.follow(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getFollowerId()).isEqualTo(followerId);
        assertThat(response.getFollowingId()).isEqualTo(followingId);
        verify(followRepository).save(any(Follow.class));
    }

    // 자기 자신을 팔로우할 수 없다
    @Test
    void followSelfTest() {
        // Given
        FollowRequest selfRequest = FollowRequest.builder()
                .followerId(followerId)
                .followingId(followerId)
                .build();

        // When & Then
        assertThatThrownBy(() -> followService.follow(selfRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신을 팔로우할 수 없습니다.");
    }

    // 이미 팔로우한 사용자를 다시 팔로우할 수 없다
    @Test
    void followDuplicateTest() {
        // Given
        when(followRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .thenReturn(follow);

        // When & Then
        assertThatThrownBy(() -> followService.follow(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 팔로우한 사용자입니다.");
    }

    // 팔로우를 해제할 수 있다
    @Test
    void unfollowTest() {
        // Given
        when(followRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .thenReturn(follow);

        // When
        followService.unfollow(request);

        // Then
        verify(followRepository).delete(follow.getFollowId());
    }

    // 존재하지 않는 팔로우 관계를 해제할 수 없다
    @Test
    void unfollowNonExistentTest() {
        // Given
        when(followRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> followService.unfollow(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("팔로우 관계가 존재하지 않습니다.");
    }

    // 팔로워 목록을 조회할 수 있다
    @Test
    void getFollowersTest() {
        // Given
        List<Follow> followers = Arrays.asList(follow);
        FollowerResponse followerResponse = FollowerResponse.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followerNickname(follow.getFollowerNickname())
                .followerProfileImage(follow.getFollowerProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();

        when(followRepository.findAllFollowersByUserId(followingId)).thenReturn(followers);
        when(followMapper.toFollowerResponse(follow)).thenReturn(followerResponse);

        // When
        List<FollowerResponse> responses = followService.getFollowers(followingId);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getFollowerId()).isEqualTo(followerId);
    }

    // 팔로잉 목록을 조회할 수 있다
    @Test
    void getFollowingsTest() {
        // Given
        List<Follow> followings = Arrays.asList(follow);
        FollowingResponse followingResponse = FollowingResponse.builder()
                .followId(follow.getFollowId())
                .followingId(follow.getFollowingId())
                .followingNickname(follow.getFollowingNickname())
                .followingProfileImage(follow.getFollowingProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();

        when(followRepository.findAllFollowingsByUserId(followerId)).thenReturn(followings);
        when(followMapper.toFollowingResponse(follow)).thenReturn(followingResponse);

        // When
        List<FollowingResponse> responses = followService.getFollowings(followerId);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getFollowingId()).isEqualTo(followingId);
    }

    // 팔로워를 검색할 수 있다
    @Test
    void searchFollowersTest() {
        // Given
        String keyword = "팔로워";
        List<Follow> followers = Arrays.asList(follow);
        FollowerResponse followerResponse = FollowerResponse.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followerNickname(follow.getFollowerNickname())
                .followerProfileImage(follow.getFollowerProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();

        when(followRepository.searchFollowersByKeyword(followingId, keyword)).thenReturn(followers);
        when(followMapper.toFollowerResponse(follow)).thenReturn(followerResponse);

        // When
        List<FollowerResponse> responses = followService.searchFollowers(followingId, keyword);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getFollowerNickname()).contains(keyword);
    }

    // 팔로잉을 검색할 수 있다
    @Test
    void searchFollowingsTest() {
        // Given
        String keyword = "팔로잉";
        List<Follow> followings = Arrays.asList(follow);
        FollowingResponse followingResponse = FollowingResponse.builder()
                .followId(follow.getFollowId())
                .followingId(follow.getFollowingId())
                .followingNickname(follow.getFollowingNickname())
                .followingProfileImage(follow.getFollowingProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();

        when(followRepository.searchFollowingsByKeyword(followerId, keyword)).thenReturn(followings);
        when(followMapper.toFollowingResponse(follow)).thenReturn(followingResponse);

        // When
        List<FollowingResponse> responses = followService.searchFollowings(followerId, keyword);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getFollowingNickname()).contains(keyword);
    }

    // 팔로워 수를 조회할 수 있다
    @Test
    void getFollowerCountTest() {
        // Given
        long expectedCount = 5;
        when(followRepository.countFollowersByUserId(followingId)).thenReturn(expectedCount);

        // When
        long count = followService.getFollowerCount(followingId);

        // Then
        assertThat(count).isEqualTo(expectedCount);
    }

    // 팔로잉 수를 조회할 수 있다
    @Test
    void getFollowingCountTest() {
        // Given
        long expectedCount = 3;
        when(followRepository.countFollowingsByUserId(followerId)).thenReturn(expectedCount);

        // When
        long count = followService.getFollowingCount(followerId);

        // Then
        assertThat(count).isEqualTo(expectedCount);
    }
}