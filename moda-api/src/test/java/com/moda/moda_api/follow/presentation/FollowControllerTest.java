package com.moda.moda_api.follow.presentation;

import com.moda.moda_api.follow.application.FollowService;
import com.moda.moda_api.follow.application.response.FollowResponse;
import com.moda.moda_api.follow.application.response.FollowerResponse;
import com.moda.moda_api.follow.application.response.FollowingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FollowService followService;

    @InjectMocks
    private FollowController followController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(followController).build();
    }

    @Test
    void follow_Success() throws Exception {
        // Given
        String followerId = "user-1";
        String followingId = "user-2";

        FollowResponse response = FollowResponse.builder()
                .followId(UUID.randomUUID().toString())
                .followerId(followerId)
                .followingId(followingId)
                .followedAt(LocalDateTime.now())
                .build();

        given(followService.follow(any())).willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/follow/{followingId}", followingId)
                        .header("X-USER-ID", followerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerId").value(followerId))
                .andExpect(jsonPath("$.followingId").value(followingId));
    }

    @Test
    void unfollow_Success() throws Exception {
        // Given
        String followerId = "user-1";
        String followingId = "user-2";

        // When & Then
        mockMvc.perform(delete("/api/follow/{followingId}", followingId)
                        .header("X-USER-ID", followerId))
                .andExpect(status().isOk());
    }

    @Test
    void getFollowers_Success() throws Exception {
        // Given
        String userId = "user-1";
        List<FollowerResponse> followers = Arrays.asList(
                FollowerResponse.builder()
                        .followId(UUID.randomUUID().toString())
                        .followerId("follower-1")
                        .followerNickname("팔로워1")
                        .followerProfileImage("profile1.jpg")
                        .followedAt(LocalDateTime.now())
                        .build()
        );

        given(followService.getFollowers(userId)).willReturn(followers);

        // When & Then
        mockMvc.perform(get("/api/follow/followers/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].followerId").value("follower-1"))
                .andExpect(jsonPath("$[0].followerNickname").value("팔로워1"));
    }

    @Test
    void getFollowings_Success() throws Exception {
        // Given
        String userId = "user-1";
        List<FollowingResponse> followings = Arrays.asList(
                FollowingResponse.builder()
                        .followId(UUID.randomUUID().toString())
                        .followingId("following-1")
                        .followingNickname("팔로잉1")
                        .followingProfileImage("profile1.jpg")
                        .followedAt(LocalDateTime.now())
                        .build()
        );

        given(followService.getFollowings(userId)).willReturn(followings);

        // When & Then
        mockMvc.perform(get("/api/follow/followings/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].followingId").value("following-1"))
                .andExpect(jsonPath("$[0].followingNickname").value("팔로잉1"));
    }

    @Test
    void searchFollowers_Success() throws Exception {
        // Given
        String userId = "user-1";
        String keyword = "팔로워";
        List<FollowerResponse> followers = Arrays.asList(
                FollowerResponse.builder()
                        .followId(UUID.randomUUID().toString())
                        .followerId("follower-1")
                        .followerNickname("팔로워1")
                        .followerProfileImage("profile1.jpg")
                        .followedAt(LocalDateTime.now())
                        .build()
        );

        given(followService.searchFollowers(userId, keyword)).willReturn(followers);

        // When & Then
        mockMvc.perform(get("/api/follow/followers/{userId}/search", userId)
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].followerId").value("follower-1"))
                .andExpect(jsonPath("$[0].followerNickname").value("팔로워1"));
    }

    @Test
    void searchFollowings_Success() throws Exception {
        // Given
        String userId = "user-1";
        String keyword = "팔로잉";
        List<FollowingResponse> followings = Arrays.asList(
                FollowingResponse.builder()
                        .followId(UUID.randomUUID().toString())
                        .followingId("following-1")
                        .followingNickname("팔로잉1")
                        .followingProfileImage("profile1.jpg")
                        .followedAt(LocalDateTime.now())
                        .build()
        );

        given(followService.searchFollowings(userId, keyword)).willReturn(followings);

        // When & Then
        mockMvc.perform(get("/api/follow/followings/{userId}/search", userId)
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].followingId").value("following-1"))
                .andExpect(jsonPath("$[0].followingNickname").value("팔로잉1"));
    }

    @Test
    void getFollowerCount_Success() throws Exception {
        // Given
        String userId = "user-1";
        long count = 5L;

        given(followService.getFollowerCount(userId)).willReturn(count);

        // When & Then
        mockMvc.perform(get("/api/follow/followers/count/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getFollowingCount_Success() throws Exception {
        // Given
        String userId = "user-1";
        long count = 3L;

        given(followService.getFollowingCount(userId)).willReturn(count);

        // When & Then
        mockMvc.perform(get("/api/follow/followings/count/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}