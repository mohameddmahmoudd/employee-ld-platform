package com.talentprogram.LdPlatformUserService.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import com.talentprogram.LdPlatformUserService.config.JwtConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import io.jsonwebtoken.Claims;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.lang.reflect.Field;

@Service
public class JwtService 
{
  private final SecretKey key;
  private final long expiration;

  public JwtService(JwtConfig props) {
    this.expiration = props.getExpiration();
    this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
  }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) throws InvalidKeyException, NoSuchFieldException, SecurityException, IllegalAccessException {
        return buildToken(extraClaims, userDetails, expiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        final String usernameClaim = extractUsername(token);

        boolean usernameMatches = usernameClaim != null && usernameClaim.equals(userDetails.getUsername());
        boolean subjectMatchesId = false;

        if (subject != null) {
            String userId = resolveUserId(userDetails);
            subjectMatchesId = userId != null && subject.equals(userId);
        }

        return (usernameMatches || subjectMatchesId) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        String idStr = claims.getSubject();
        return Long.parseLong(idStr);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) throws IllegalAccessException, NoSuchFieldException, SecurityException {
        Field idField = userDetails.getClass().getDeclaredField("id"); // works for private fields
        idField.setAccessible(true);
        Object idValue = idField.get(userDetails);
        return Jwts
                .builder()
                .subject(String.valueOf(idValue))
                .claims(extraClaims)
                .claim("username", userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }
    

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String resolveUserId(UserDetails userDetails) {
        try {
            Field idField = userDetails.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(userDetails);
            return idValue != null ? idValue.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return null;
        }
    }

}
