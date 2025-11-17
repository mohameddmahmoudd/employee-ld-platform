package com.talentprogram.LdPlatformGatewayService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {
  private String secret;
  private String header = "Authorization";

  private String rolesClaim = "roles";
  private String usernameClaim = "username";
  private String userIdSource = "sub";


  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }

  public String getHeader() { return header; }
  public void setHeader(String header) { this.header = header; }

  public String getRolesClaim() { return rolesClaim; }
  public void setRolesClaim(String rolesClaim) { this.rolesClaim = rolesClaim; }

  public String getUsernameClaim() { return usernameClaim; }
  public void setUsernameClaim(String usernameClaim) { this.usernameClaim = usernameClaim; }

  public String getUserIdSource() { return userIdSource; }
  public void setUserIdSource(String userIdSource) { this.userIdSource = userIdSource; }

}
