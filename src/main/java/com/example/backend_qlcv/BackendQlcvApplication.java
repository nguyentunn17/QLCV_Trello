package com.example.backend_qlcv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendQlcvApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendQlcvApplication.class, args);
	}

}
