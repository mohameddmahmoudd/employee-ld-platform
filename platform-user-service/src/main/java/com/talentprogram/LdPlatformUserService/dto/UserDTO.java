package com.talentprogram.LdPlatformUserService.dto;
import java.util.Set;

import com.talentprogram.LdPlatformUserService.entity.Role;

public record UserDTO(
  Long id,
  String username,
  String fullName,
  String title,
  Long managerId,
  Set<Role> roles /*roles review TODO*/
)
{
    
}
