package com.talentprogram.LdPlatformNotificationService.security;
import java.util.List;

public record JwtPrincipal
(Long userId,
 String username,
List<String> roles) 
{}