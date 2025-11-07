package com.talentprogram.LdPlatformNotificationService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.talentprogram.LdPlatformNotificationService.security.JwtService;
import com.talentprogram.LdPlatformNotificationService.security.JwtAuthFilter;

public @Configuration @EnableMethodSecurity class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity h, JwtService jwtService) throws Exception {
        return h.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/notifications").permitAll()
                        .requestMatchers("/notifications").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
