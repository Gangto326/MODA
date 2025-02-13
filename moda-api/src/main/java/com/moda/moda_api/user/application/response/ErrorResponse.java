package com.moda.moda_api.user.application.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private LocalDateTime timestamp = LocalDateTime.now();

	public ErrorResponse(String e){
		this.message = e;
	}

}
