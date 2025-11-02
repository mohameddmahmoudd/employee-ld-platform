package com.talentprogram.LdPlatformUserService.dto;
import java.util.Set;
import com.talentprogram.LdPlatformUserService.model.Role;

public record UserViewDTO(
  Long id,
  String name,
  String title,
  Long managerId,
  Set<Role> roles
)
{
    
}
