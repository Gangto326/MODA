package com.moda.moda_api.user.infrastructure.repository;

import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.infrastructure.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * UserRepository 인터페이스의 JPA 구현체입니다.
 * 실제 데이터베이스 작업을 수행하며, Spring Data JPA를 사용합니다.
 *
 * @RequiredArgsConstructor는 final 필드를 위한 생성자를 자동으로 생성합니다.
 * Spring의 생성자 주입을 위해 사용됩니다.
 */
@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    /**
     * Spring Data JPA 리포지토리입니다.
     * final 키워드와 @RequiredArgsConstructor를 통해 생성자 주입이 이루어집니다.
     */
    private final SpringDataUserRepository repository;

    /**
     * ID로 사용자를 조회합니다.
     * UserEntity를 도메인 모델로 변환하여 반환합니다.
     */
    @Override
    public User findById(String id) {
        UserEntity entity = repository.findById(id).orElse(null);
        return entity != null ? entity.toDomain() : null;
    }

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 사용자의 이메일
     * @return 조회된 사용자, 없으면 null
     */
    @Override
    public User findByEmail(String email) {
        UserEntity entity = repository.findByEmail(email);
        return entity != null ? entity.toDomain() : null;
    }

    /**
     * 사용자 정보를 저장하거나 업데이트합니다.
     *
     * @param user 저장할 사용자 도메인 객체
     * @return 저장된 사용자 도메인 객체
     */
    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        UserEntity savedEntity = repository.save(entity);
        return savedEntity.toDomain();
    }

    /**
     * 닉네임으로 사용자를 조회합니다.
     *
     * @param nickname 조회할 사용자의 닉네임
     * @return 조회된 사용자, 없으면 null
     */
    @Override
    public User findByNickname(String nickname) {
        UserEntity entity = repository.findByNickname(nickname);
        return entity != null ? entity.toDomain() : null;
    }

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param email 중복 검사할 이메일
     * @return 중복된 이메일이 있으면 true, 없으면 false
     */
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     *
     * @param nickname 중복 검사할 닉네임
     * @return 중복된 닉네임이 있으면 true, 없으면 false
     */
    @Override
    public boolean existsByNickname(String nickname) {
        return repository.existsByNickname(nickname);
    }

    /**
     * 이메일과 상태로 사용자를 조회합니다.
     * 주로 로그인 시 활성 상태의 사용자만 조회하는 용도로 사용됩니다.
     *
     * @param email 조회할 사용자의 이메일
     * @param status 조회할 사용자의 상태
     * @return 조회된 사용자, 없으면 null
     */
    @Override
    public User findByEmailAndStatus(String email, String status) {
        UserEntity entity = repository.findByEmailAndStatus(email, status);
        return entity != null ? entity.toDomain() : null;
    }
}