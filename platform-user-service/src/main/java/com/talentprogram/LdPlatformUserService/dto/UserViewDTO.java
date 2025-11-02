package com.talentprogram.LdPlatformUserService.dto;
import java.util.Set;
import com.talentprogram.LdPlatformUserService.model.Role;

public record UserViewDTO(
  Long id,
  String username,
  String fullName,
  String title,
  Long managerId,
  Set<Role> roles
)
{
    
}
