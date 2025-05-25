package com.example.medisyncbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = "com.example.medisyncbackend.Models")
@EnableJpaRepositories(basePackages = "com.example.medisyncbackend.Repository")
public class MediSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediSyncApplication.class, args);
	}
}
