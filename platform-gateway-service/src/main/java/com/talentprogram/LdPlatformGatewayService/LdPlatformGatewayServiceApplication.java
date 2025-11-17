package com.talentprogram.LdPlatformGatewayService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAutoConfiguration 
public class LdPlatformGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdPlatformGatewayServiceApplication.class, args);
	}

}
