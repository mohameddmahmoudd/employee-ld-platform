package com.talentprogram.LdPlatformUserService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "security.jwt")
@Configuration
public class JwtConfig 
{
    private String secret;

  /**
   * Access token TTL in minutes.
   */
  private long ttlMinutes;

  /**
   * Token issuer (iss).
   */
  private String issuer ;

  // getters/setters
  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }
  public long getTtlMinutes() { return ttlMinutes; }
  public void setTtlMinutes(long ttlMinutes) { this.ttlMinutes = ttlMinutes; }
  public String getIssuer() { return issuer; }
  public void setIssuer(String issuer) { this.issuer = issuer; }
    
}
