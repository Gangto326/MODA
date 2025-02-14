package com.moda.moda_api.follow.presentation;

import com.moda.moda_api.follow.application.FollowService;
import com.moda.moda_api.follow.application.response.FollowResponse;
import com.moda.moda_api.follow.application.response.FollowerResponse;
import com.moda.moda_api.follow.application.response.FollowingResponse;
import com.moda.moda_api.follow.presentation.request.FollowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 팔로우 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    // private final FollowService followService;
    //
    // /**
    //  * 특정 사용자를 팔로우합니다.
    //  *
    //  * @param followingId 팔로우할 사용자의 ID
    //  * @return 생성된 팔로우 관계 정보
    //  */
    // @PostMapping("/{followingId}")
    // public ResponseEntity<FollowResponse> follow(
    //         @PathVariable String followingId,
    //         @RequestHeader("X-USER-ID") String followerId) {
    //     FollowRequest request = FollowRequest.builder()
    //             .followerId(followerId)
    //             .followingId(followingId)
    //             .build();
    //     FollowResponse response = followService.follow(request);
    //     return ResponseEntity.ok(response);
    // }
    //
    // /**
    //  * 특정 사용자를 언팔로우합니다.
    //  *
    //  * @param followingId 언팔로우할 사용자의 ID
    //  */
    // @DeleteMapping("/{followingId}")
    // public ResponseEntity<Void> unfollow(
    //         @PathVariable String followingId,
    //         @RequestHeader("X-USER-ID") String followerId) {
    //     FollowRequest request = FollowRequest.builder()
    //             .followerId(followerId)
    //             .followingId(followingId)
    //             .build();
    //     followService.unfollow(request);
    //     return ResponseEntity.ok().build();
    // }
    //
    // /**
    //  * 특정 사용자의 팔로워 목록을 조회합니다.
    //  *
    //  * @param userId 팔로워를 조회할 사용자의 ID
    //  * @return 팔로워 목록
    //  */
    // @GetMapping("/followers/{userId}")
    // public ResponseEntity<List<FollowerResponse>> getFollowers(
    //         @PathVariable String userId) {
    //     List<FollowerResponse> followers = followService.getFollowers(userId);
    //     return ResponseEntity.ok(followers);
    // }
    //
    // /**
    //  * 특정 사용자의 팔로잉 목록을 조회합니다.
    //  *
    //  * @param userId 팔로잉을 조회할 사용자의 ID
    //  * @return 팔로잉 목록
    //  */
    // @GetMapping("/followings/{userId}")
    // public ResponseEntity<List<FollowingResponse>> getFollowings(
    //         @PathVariable String userId) {
    //     List<FollowingResponse> followings = followService.getFollowings(userId);
    //     return ResponseEntity.ok(followings);
    // }
    //
    // /**
    //  * 특정 사용자의 팔로워를 검색합니다.
    //  *
    //  * @param userId 검색할 사용자의 ID
    //  * @param keyword 검색 키워드 (닉네임)
    //  * @return 검색된 팔로워 목록
    //  */
    // @GetMapping("/followers/{userId}/search")
    // public ResponseEntity<List<FollowerResponse>> searchFollowers(
    //         @PathVariable String userId,
    //         @RequestParam String keyword) {
    //     List<FollowerResponse> followers = followService.searchFollowers(userId, keyword);
    //     return ResponseEntity.ok(followers);
    // }
    //
    // /**
    //  * 특정 사용자의 팔로잉을 검색합니다.
    //  *
    //  * @param userId 검색할 사용자의 ID
    //  * @param keyword 검색 키워드 (닉네임)
    //  * @return 검색된 팔로잉 목록
    //  */
    // @GetMapping("/followings/{userId}/search")
    // public ResponseEntity<List<FollowingResponse>> searchFollowings(
    //         @PathVariable String userId,
    //         @RequestParam String keyword) {
    //     List<FollowingResponse> followings = followService.searchFollowings(userId, keyword);
    //     return ResponseEntity.ok(followings);
    // }
    //
    // /**
    //  * 특정 사용자의 팔로워 수를 조회합니다.
    //  *
    //  * @param userId 팔로워 수를 조회할 사용자의 ID
    //  * @return 팔로워 수
    //  */
    // @GetMapping("/followers/count/{userId}")
    // public ResponseEntity<Long> getFollowerCount(@PathVariable String userId) {
    //     long count = followService.getFollowerCount(userId);
    //     return ResponseEntity.ok(count);
    // }
    //
    // /**
    //  * 특정 사용자의 팔로잉 수를 조회합니다.
    //  *
    //  * @param userId 팔로잉 수를 조회할 사용자의 ID
    //  * @return 팔로잉 수
    //  */
    // @GetMapping("/followings/count/{userId}")
    // public ResponseEntity<Long> getFollowingCount(@PathVariable String userId) {
    //     long count = followService.getFollowingCount(userId);
    //     return ResponseEntity.ok(count);
    // }
}