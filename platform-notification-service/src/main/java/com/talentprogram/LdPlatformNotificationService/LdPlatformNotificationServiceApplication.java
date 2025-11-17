package com.talentprogram.LdPlatformNotificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import java.time.Clock;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAutoConfiguration 
public class LdPlatformNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdPlatformNotificationServiceApplication.class, args);
	}

	@Bean
  	public Clock clock() {
    return Clock.systemUTC();
  }
}

