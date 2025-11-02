package com.talentprogram.LdPlatformUserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAutoConfiguration 
public class LdPlatformUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdPlatformUserServiceApplication.class, args);
	}

}
