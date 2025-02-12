package com.moda.moda_api.common.jwt;

import org.springframework.beans.factory.annotation.Value;

public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String secretKey;


}
