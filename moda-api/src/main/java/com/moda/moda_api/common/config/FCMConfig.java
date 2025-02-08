package com.moda.moda_api.common.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {
	@Value("${fcm.credentials.path}")
	private String credentialsPath;

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials credentials = GoogleCredentials
			.fromStream(new ClassPathResource(credentialsPath).getInputStream());
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(credentials)
			.build();

		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
		return FirebaseMessaging.getInstance();
	}


}
