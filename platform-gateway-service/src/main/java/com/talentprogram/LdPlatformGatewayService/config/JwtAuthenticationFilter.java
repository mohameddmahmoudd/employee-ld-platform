package com.talentprogram.LdPlatformGatewayService.config;

import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.talentprogram.LdPlatformGatewayService.service.JwtService;
import org.springframework.stereotype.Component;
import com.talentprogram.LdPlatformGatewayService.service.JwtService.JwtPrincipal;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    
  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
      this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    
    var path = exchange.getRequest().getPath().value();
    
    if (path.startsWith("/auth/") || path.startsWith("/actuator")) {
      return chain.filter(exchange);
    }

    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
      return chain.filter(exchange);
    }

    String token = auth.substring(7);

    return jwtService.authenticate(token)
      .flatMap(authn -> {
        var principal = (JwtPrincipal) authn.getPrincipal();

        ServerHttpRequest mutated = exchange.getRequest().mutate()
          .headers(h -> {
            if (principal.userId() != null) h.set("X-User-Id", String.valueOf(principal.userId()));
            h.set("X-Username", principal.username());
            h.set("X-Roles", String.join(",", principal.roles()));
          })
          .build();

        var mutatedExchange = exchange.mutate().request(mutated).build();
        return chain.filter(mutatedExchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authn));
      })
      .onErrorResume(e -> chain.filter(exchange));
  }
  
}
