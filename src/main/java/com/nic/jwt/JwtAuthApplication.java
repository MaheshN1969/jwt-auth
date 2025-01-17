package com.nic.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthApplication.class, args);
		
		System.out.println("<========================================= APPLICATION STARTED =========================================>");
	}

}
