package com.gaduationproject.cre8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class Cre8Application {

	public static void main(String[] args) {
		SpringApplication.run(Cre8Application.class, args);
	}

}
