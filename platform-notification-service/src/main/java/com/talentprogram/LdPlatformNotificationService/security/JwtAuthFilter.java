package com.talentprogram.LdPlatformNotificationService.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Objects;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException , java.io.IOException
    {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                
                Claims claims = jwtService.parseToken(token);

                Long uid = null;
                Object rawUid = claims.getSubject();
                /*Long userId = Long.valueOf(jwt.getSubject()); 
                 String username = jwt.getClaimAsString("username"); */
                /*String uid handling */
                
                if (rawUid instanceof Number n)
                    uid = n.longValue();
                else if (rawUid instanceof String s && !s.isBlank())
                    uid = Long.valueOf(s);

                String username = claims.get("username").toString(); 

                List<String> roles = switch (claims.get("roles")) {
                    case List<?> l -> l.stream().map(Objects::toString).toList();
                    case String s ->
                        java.util.Arrays.stream(s.split(",")).map(String::trim).filter(t -> !t.isEmpty()).toList();
                    default -> List.of();
                };

                List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

                JwtPrincipal principal = new JwtPrincipal(uid, username, roles);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        chain.doFilter(request, response);
    }

}
