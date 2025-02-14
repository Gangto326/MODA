package com.moda.moda_api.follow.application;

import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.follow.domain.FollowRepository;
import com.moda.moda_api.follow.application.response.FollowResponse;
import com.moda.moda_api.follow.application.response.FollowerResponse;
import com.moda.moda_api.follow.application.response.FollowingResponse;
import com.moda.moda_api.follow.presentation.request.FollowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Follow 도메인과 관련된 핵심 비즈니스 로직을 수행하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    // private final FollowRepository followRepository;
    // private final FollowMapper followMapper;

    /**
     * 특정 사용자를 팔로우합니다.
     *
     * @param request 팔로우 요청 정보
     * @return 생성된 팔로우 관계 정보
     * @throws IllegalArgumentException 자기 자신을 팔로우하거나, 이미 팔로우한 경우
     */
    // @Transactional
    // public FollowResponse follow(FollowRequest request) {
        // 자기 자신을 팔로우하는 경우 체크
        // if (request.getFollowerId().equals(request.getFollowingId())) {
        //     throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        // }
        //
        // // 이미 팔로우한 경우 체크
        // if (followRepository.findByFollowerIdAndFollowingId(
        //         request.getFollowerId(),
        //         request.getFollowingId()) != null) {
        //     throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        // }
        //
        // // 새로운 팔로우 관계 생성
        // Follow follow = Follow.builder()
        //         .followId(UUID.randomUUID().toString())
        //         .followerId(request.getFollowerId())
        //         .followingId(request.getFollowingId())
        //         .build();
        //
        // Follow savedFollow = followRepository.save(follow);
        // return followMapper.toFollowResponse(savedFollow);
    // }

    /**
     * 특정 사용자를 언팔로우합니다.
     *
     * @param request 언팔로우 요청 정보
     * @throws IllegalArgumentException 팔로우 관계가 존재하지 않는 경우
     */
    // @Transactional
    // public void unfollow(FollowRequest request) {
    //     Follow follow = followRepository.findByFollowerIdAndFollowingId(
    //             request.getFollowerId(),
    //             request.getFollowingId()
    //     );
    //
    //     if (follow == null) {
    //         throw new IllegalArgumentException("팔로우 관계가 존재하지 않습니다.");
    //     }
    //
    //     followRepository.delete(follow.getFollowId());
    // }
    //
    // /**
    //  * 특정 사용자의 팔로워 목록을 조회합니다.
    //  *
    //  * @param userId 팔로워를 조회할 사용자의 ID
    //  * @return 팔로워 목록
    //  */
    // public List<FollowerResponse> getFollowers(String userId) {
    //     return followRepository.findAllFollowersByUserId(userId)
    //             .stream()
    //             .map(followMapper::toFollowerResponse)
    //             .collect(Collectors.toList());
    // }
    //
    // /**
    //  * 특정 사용자의 팔로잉 목록을 조회합니다.
    //  *
    //  * @param userId 팔로잉을 조회할 사용자의 ID
    //  * @return 팔로잉 목록
    //  */
    // public List<FollowingResponse> getFollowings(String userId) {
    //     return followRepository.findAllFollowingsByUserId(userId)
    //             .stream()
    //             .map(followMapper::toFollowingResponse)
    //             .collect(Collectors.toList());
    // }
    //
    // /**
    //  * 특정 사용자의 팔로워를 검색합니다.
    //  *
    //  * @param userId 검색할 사용자의 ID
    //  * @param keyword 검색 키워드
    //  * @return 검색된 팔로워 목록
    //  */
    // public List<FollowerResponse> searchFollowers(String userId, String keyword) {
    //     return followRepository.searchFollowersByKeyword(userId, keyword)
    //             .stream()
    //             .map(followMapper::toFollowerResponse)
    //             .collect(Collectors.toList());
    // }
    //
    // /**
    //  * 특정 사용자의 팔로잉을 검색합니다.
    //  *
    //  * @param userId 검색할 사용자의 ID
    //  * @param keyword 검색 키워드
    //  * @return 검색된 팔로잉 목록
    //  */
    // public List<FollowingResponse> searchFollowings(String userId, String keyword) {
    //     return followRepository.searchFollowingsByKeyword(userId, keyword)
    //             .stream()
    //             .map(followMapper::toFollowingResponse)
    //             .collect(Collectors.toList());
    // }
    //
    // /**
    //  * 팔로워 수를 조회합니다.
    //  *
    //  * @param userId 팔로워 수를 조회할 사용자의 ID
    //  * @return 팔로워 수
    //  */
    // public long getFollowerCount(String userId) {
    //     return followRepository.countFollowersByUserId(userId);
    // }
    //
    // /**
    //  * 팔로잉 수를 조회합니다.
    //  *
    //  * @param userId 팔로잉 수를 조회할 사용자의 ID
    //  * @return 팔로잉 수
    //  */
    // public long getFollowingCount(String userId) {
    //     return followRepository.countFollowingsByUserId(userId);
    // }
}