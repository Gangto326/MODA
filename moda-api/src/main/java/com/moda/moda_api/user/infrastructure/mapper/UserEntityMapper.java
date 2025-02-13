package com.moda.moda_api.user.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.user.infrastructure.entity.UserEntity;

@Component
public class UserEntityMapper {
	public User toDomain(UserEntity entity) {
		return User.builder()
			.userId(new UserId(entity.getUserId()))
			.email(entity.getEmail())
			.password(entity.getPassword())
			.userName(entity.getUserName())
			.nickname(entity.getNickname())
			.createdAt(entity.getCreatedAt())
			.deletedAt(entity.getDeletedAt())
			.build();
	}

	public UserEntity fromDomain(User user) {
		return UserEntity.builder()
			.userId(user.getUserId().getValue())
			.email(user.getEmail())
			.password(user.getPassword())
			.userName(user.getUserName())
			.nickname(user.getNickname())
			.createdAt(user.getCreatedAt())
			.deletedAt(user.getDeletedAt())
			.build();
	}
}