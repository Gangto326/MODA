package com.moda.moda_api.common.config;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.moda.moda_api.crawling.domain.model.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	private final ObjectMapper objectMapper;

	@Bean
	public RedisTemplate<String, List<Url>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, List<Url>> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// Key에 대한 직렬화 설정 (StringSerializer)
		template.setKeySerializer(new StringRedisSerializer());

		// Value에 대한 직렬화 설정 (Jackson JSON Serializer)
		Jackson2JsonRedisSerializer<List<Url>> serializer =
			new Jackson2JsonRedisSerializer<>(objectMapper.getTypeFactory().constructCollectionType(List.class, Url.class));

		template.setValueSerializer(serializer);

		return template;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
		config.setPassword(password);
		return new LettuceConnectionFactory(config);
	}
}