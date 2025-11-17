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
  private long expiration;

  /**
   * Token issuer (iss).
   */
  private String issuer ;

  // getters/setters
  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }
  public long getExpiration() { return expiration; }
  public void setExpiration(long expiration) { this.expiration = expiration; }
  public String getIssuer() { return issuer; }
  public void setIssuer(String issuer) { this.issuer = issuer; }
    
}
