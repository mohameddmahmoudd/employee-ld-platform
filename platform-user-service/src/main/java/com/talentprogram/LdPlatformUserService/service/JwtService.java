// package com.talentprogram.LdPlatformUserService.service;

// import java.nio.charset.StandardCharsets;
// import java.time.Instant;
// import java.util.stream.Collectors;

// import javax.crypto.SecretKey;

// import com.talentprogram.LdPlatformUserService.config.JwtConfig;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import java.util.Date;
// import java.util.List;
// import com.talentprogram.LdPlatformUserService.model.User;

// @Service
// public class JwtService 
// {
//   private final SecretKey key;
//   private final long ttlMinutes;
//   private final String issuer;

//   public JwtService(JwtConfig props) {
//     this.ttlMinutes = props.getTtlMinutes();
//     this.issuer = props.getIssuer();
//     this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
//   }

//    public String createAccessToken(User u) {
    
//     var now = Instant.now();
//     var exp = now.plusSeconds(ttlMinutes * 60);

//     // Safely build roles list
//     List<String> roles = (u.getRoles() == null)
//         ? List.of()
//         : u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());

//     var builder = Jwts.builder()
//         .subject(String.valueOf(u.getId()))
//         .issuer(issuer)
//         .issuedAt(Date.from(now))
//         .expiration(Date.from(exp))
//         .claim("roles", roles);

//     // Optional, only if you truly need them (beware PII)
//     if (u.getName() != null)   builder.claim("name", u.getName());
//     if (u.getTitle() != null)  builder.claim("title", u.getTitle());
//     if (u.getManager() != null && u.getManager().getId() != null)
//       builder.claim("managerId", u.getManager().getId());

//     return builder.signWith(key).compact();
//    }
// }
