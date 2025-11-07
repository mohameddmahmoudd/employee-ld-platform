package com.talentprogram.LdPlatformNotificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAutoConfiguration 
public class LdPlatformNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdPlatformNotificationServiceApplication.class, args);
	}

}
