package com.moda.moda_api.follow.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.user.infrastructure.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * 팔로우 정보의 데이터베이스 매핑을 위한 JPA 엔티티 클래스입니다.
 * 실제 데이터베이스 테이블과 매핑되며, JPA 관련 설정을 포함합니다.
 */
@Entity
@Table(name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"follower_id", "following_id"}
                )
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowEntity {

    @Id
    @Column(name = "follow_id")
    private String followId;

    @Column(name = "follower_id", nullable = false)
    private String followerId;

    @Column(name = "following_id", nullable = false)
    private String followingId;

    @Column(name = "followed_at", nullable = false)
    private LocalDateTime followedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", insertable = false, updatable = false)
    private UserEntity following;

    // Entity -> Domain
    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     * 인프라스트럭처 계층과 도메인 계층의 경계에서 사용됩니다.
     */
    public Follow toDomain() {
        return Follow.builder()
                .followId(this.followId)
                .followerId(this.followerId)
                .followingId(this.followingId)
                .followerNickname(this.follower != null ? this.follower.getNickname() : null)
                .followerProfileImage(this.follower != null ? this.follower.getProfileImage() : null)
                .followingNickname(this.following != null ? this.following.getNickname() : null)
                .followingProfileImage(this.following != null ? this.following.getProfileImage() : null)
                .build();
    }

    // Domain -> Entity
    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     * 인프라스트럭처 계층과 도메인 계층의 경계에서 사용됩니다.
     */
    public static FollowEntity fromDomain(Follow follow) {
        FollowEntity entity = new FollowEntity();
        entity.followId = follow.getFollowId();
        entity.followerId = follow.getFollowerId();
        entity.followingId = follow.getFollowingId();
        entity.followedAt = follow.getFollowedAt();
        return entity;
    }
}