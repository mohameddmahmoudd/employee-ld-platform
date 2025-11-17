package com.talentprogram.LdPlatformGatewayService.service;

import com.talentprogram.LdPlatformGatewayService.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Collections;
import java.util.List;

@Service
public class JwtService {

  private final JwtConfig cfg;
  private final Clock clock;

  public JwtService(JwtConfig cfg, Clock clock) {
    this.cfg = cfg;
    this.clock = clock;
  }

  public Mono<UsernamePasswordAuthenticationToken> authenticate(String token) {
      
    Claims c = parse(token);
    
    validate(c);

    Long userId = toLong(c.get(cfg.getUserIdSource()));
      
    String username = c.get(cfg.getUsernameClaim(), String.class);

    List<String> roles = getRoles(c);

    List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
    
    JwtPrincipal principal = new JwtPrincipal(userId, username, roles);
    
    return Mono.just(new UsernamePasswordAuthenticationToken(principal, token, authorities));

    }


  private Claims parse(String token) {
    SecretKey key = Keys.hmacShaKeyFor(cfg.getSecret().getBytes(StandardCharsets.UTF_8));
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  private void validate(Claims c) {
    if (c.getExpiration() != null && c.getExpiration().toInstant().isBefore(clock.instant())) {
      throw new JwtException("Token expired");
    }
  }

  @SuppressWarnings("unchecked")
  private List<String> getRoles(Claims c) {
    Object val = c.get(cfg.getRolesClaim());
    if (val instanceof List<?> l) {
      return (List<String>) l;
    }
    return Collections.emptyList();
  }

  private static Long toLong(Object o) {
    if (o == null) return null;
    if (o instanceof Number n) return n.longValue();
    if (o instanceof String s && !s.isBlank()) return Long.valueOf(s);
    throw new IllegalArgumentException("Unsupported user id type: " + o);
  }

  public record JwtPrincipal(Long userId, String username, List<String> roles) {}
}
