package com.moda.moda_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ModaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModaApiApplication.class, args);
	}

}
