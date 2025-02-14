package com.moda.moda_api.follow.infrastructure.repository;

import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.follow.domain.FollowRepository;
import com.moda.moda_api.follow.infrastructure.entity.FollowEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FollowRepository 인터페이스의 JPA 구현체입니다.
 * 실제 데이터베이스 작업을 수행하며, Spring Data JPA를 사용합니다.
 */
@Repository
@RequiredArgsConstructor
public class JpaFollowRepository implements FollowRepository {

    // private final SpringDataFollowRepository repository;
    //
    // @Override
    // public Follow save(Follow follow) {
    //     FollowEntity entity = FollowEntity.fromDomain(follow);
    //     FollowEntity savedEntity = repository.save(entity);
    //     return savedEntity.toDomain();
    // }
    //
    // @Override
    // public Follow findById(String followId) {
    //     return repository.findById(followId)
    //             .map(FollowEntity::toDomain)
    //             .orElse(null);
    // }
    //
    // @Override
    // public List<Follow> findAllFollowersByUserId(String userId) {
    //     // findByFollowingId -> findFollowersWithProfile로 변경
    //     return repository.findFollowersWithProfile(userId)
    //             .stream()
    //             .map(FollowEntity::toDomain)
    //             .collect(Collectors.toList());
    // }
    //
    // @Override
    // public List<Follow> findAllFollowingsByUserId(String userId) {
    //     // findByFollowerId -> findFollowingsWithProfile로 변경
    //     return repository.findFollowingsWithProfile(userId)
    //             .stream()
    //             .map(FollowEntity::toDomain)
    //             .collect(Collectors.toList());
    // }
    //
    // @Override
    // public Follow findByFollowerIdAndFollowingId(String followerId, String followingId) {
    //     FollowEntity entity = repository.findByFollowerIdAndFollowingId(followerId, followingId);
    //     return entity != null ? entity.toDomain() : null;
    // }
    //
    // @Override
    // public void delete(String followId) {
    //     repository.deleteById(followId);
    // }
    //
    // @Override
    // public long countFollowersByUserId(String userId) {
    //     return repository.countByFollowingId(userId);
    // }
    //
    // @Override
    // public long countFollowingsByUserId(String userId) {
    //     return repository.countByFollowerId(userId);
    // }
    //
    // @Override
    // public List<Follow> searchFollowersByKeyword(String userId, String keyword) {
    //     return repository.searchFollowersByKeyword(userId, keyword)
    //             .stream()
    //             .map(FollowEntity::toDomain)
    //             .collect(Collectors.toList());
    // }
    //
    // @Override
    // public List<Follow> searchFollowingsByKeyword(String userId, String keyword) {
    //     return repository.searchFollowingsByKeyword(userId, keyword)
    //             .stream()
    //             .map(FollowEntity::toDomain)
    //             .collect(Collectors.toList());
    // }
}