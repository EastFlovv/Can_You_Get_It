package com.allback.cygiuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CygiUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiUserApplication.class, args);
	}

}
