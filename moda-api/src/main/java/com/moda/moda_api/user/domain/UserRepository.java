package com.moda.moda_api.user.domain;

import java.util.Optional;

/**
 * 사용자 도메인을 위한 리포지토리 인터페이스입니다.
 * 실제 구현체(JpaUserRepository)와 도메인 모델 사이의 추상화 계층입니다.
 * 특정 기술(JPA 등)에 의존하지 않는 순수한 인터페이스입니다.
 */
public interface UserRepository {
    // 해당 Id로 User찾기
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    // ID로 사용자 찾기
    // 반환값: 해당 ID의 사용자가 있으면 User 객체, 없으면 null
    Optional<User> findById(String userId);

    // 이메일로 사용자 찾기
    // 새로운 사용자 저장 또는 기존 사용자 정보 업데이트
    // 반환값: 저장된 User 객체
    User save(User user);

    // 이메일 중복 확인
    // 반환값: 이메일이 존재하면 true, 없으면 false
    boolean existsByEmail(String email);

    // 닉네임 중복 확인
    // 반환값: 닉네임이 존재하면 true, 없으면 false
    boolean existsByNickname(String nickname);

    // 유저 로그인 아이디 중복 확인
    boolean existsByUserName(String userName);

}