package com.mwm.bioplanta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BioplantaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BioplantaApplication.class, args);
	}

}
