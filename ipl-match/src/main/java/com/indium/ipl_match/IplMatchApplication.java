package com.indium.ipl_match;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IplMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(IplMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
