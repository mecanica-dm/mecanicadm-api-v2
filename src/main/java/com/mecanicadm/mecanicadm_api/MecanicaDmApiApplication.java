package com.mecanicadm.mecanicadm_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MecanicaDmApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MecanicaDmApiApplication.class, args);
	}

}
