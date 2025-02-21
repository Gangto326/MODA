package com.moda.moda_api.user.infrastructure.repository;

import java.util.Optional;

import com.moda.moda_api.user.application.UserMapper;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.infrastructure.entity.UserEntity;
import com.moda.moda_api.user.infrastructure.mapper.UserEntityMapper;

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
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	private final UserEntityMapper userEntityMapper;


	@Override
	public Optional<User> findByUserName(String userName) {
		return Optional.ofNullable(userJpaRepository.findByUserName(userName))
			.map(userEntityMapper::toDomain);
	}


	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(userJpaRepository.findByEmail(email))
			.map(userEntityMapper::toDomain);
	}

	/**
	 * ID로 사용자를 조회합니다.
	 * UserEntity를 도메인 모델로 변환하여 반환합니다.
	 */
	@Override
	public Optional<User> findById(String userId) {
		return userJpaRepository.findById(userId)
			.map(userEntityMapper::toDomain);
	}

	/**
	 * 사용자 정보를 저장하거나 업데이트합니다.
	 *
	 * @param user 저장할 사용자 도메인 객체
	 * @return 저장된 사용자 도메인 객체
	 */
	@Override
	public User save(User user) {
		UserEntity entity = userEntityMapper.fromDomain(user);
		return userEntityMapper.toDomain(userJpaRepository.save(entity));
	}

	/**
	 * 이메일 중복 여부를 확인합니다.
	 *
	 * @param email 중복 검사할 이메일
	 * @return 중복된 이메일이 있으면 true, 없으면 false
	 */
	@Override
	public boolean existsByEmail(String email) {
		return userJpaRepository.existsByEmail(email);
	}
	/**
	 * 닉네임 중복 여부를 확인합니다.
	 *
	 * @param nickname 중복 검사할 닉네임
	 * @return 중복된 닉네임이 있으면 true, 없으면 false
	 */
	@Override
	public boolean existsByNickname(String nickname) {
		return userJpaRepository.existsByNickname(nickname);
	}

	@Override
	public boolean existsByUserName(String userName) {
		return userJpaRepository.existsByUserName(userName);
	}


}