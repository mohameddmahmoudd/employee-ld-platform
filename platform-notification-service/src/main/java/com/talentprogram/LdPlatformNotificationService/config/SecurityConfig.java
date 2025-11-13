package com.talentprogram.LdPlatformNotificationService.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.talentprogram.LdPlatformNotificationService.security.JwtService;

import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                .exceptionHandling(c -> c.authenticationEntryPoint(authEntryPoint()).accessDeniedHandler(accessDeniedHandler())
                )
                .build();
    }


    /****** Exception handlings ******/

    @Bean
    AuthenticationEntryPoint authEntryPoint() {
        return (request, response, ex) -> {
            var pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            pd.setTitle("Unauthorized");
            pd.setDetail("Valid authentication required");
            writeProblem(response, pd, HttpStatus.UNAUTHORIZED);
        };
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            var pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
            pd.setTitle("Forbidden");
            pd.setDetail("You do not have permission to access this resource.");
            writeProblem(response, pd, HttpStatus.FORBIDDEN);
        };
    }

    private void writeProblem(HttpServletResponse res, ProblemDetail pd, HttpStatus status) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/problem+json");
        res.getWriter().write(new ObjectMapper().writeValueAsString(pd));
    }



}
