package com.moda.moda_api.user.infrastructure.repository;

import com.moda.moda_api.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA를 사용한 User 엔티티 레포지토리입니다.
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공받으며,
 * 필요한 커스텀 쿼리 메서드를 추가로 정의합니다.
 *
 * @see JpaRepository
 * @see UserEntity
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUserName(String userName);


    UserEntity findByEmail(String email);

    /**
     * 주어진 닉네임으로 사용자 엔티티를 찾습니다.
     * 메서드 이름 규칙에 따라 자동으로 쿼리가 생성됩니다.
     *
     * @param nickname 찾고자 하는 사용자의 닉네임
     * @return 해당 닉네임을 가진 UserEntity, 없으면 null
     */
    UserEntity findByNickname(String nickname);

    /**
     * 주어진 이메일이 데이터베이스에 존재하는지 확인합니다.
     * 회원가입 시 이메일 중복 체크 등에 사용됩니다.
     *
     * @param email 중복 체크할 이메일
     * @return 이메일이 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 주어진 닉네임이 데이터베이스에 존재하는지 확인합니다.
     * 회원가입 또는 프로필 수정 시 닉네임 중복 체크에 사용됩니다.
     *
     * @param nickname 중복 체크할 닉네임
     * @return 닉네임이 존재하면 true, 없으면 false
     */
    boolean existsByNickname(String nickname);


	boolean existsByUserName(String userName);


}