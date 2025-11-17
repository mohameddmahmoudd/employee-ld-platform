package com.talentprogram.LdPlatformUserService.dto;
import java.util.Set;

public record UserDTO(
  Long id,
  String username,
  String fullName,
  String title,
  Long managerId,
  Set<String> roles /*roles review TODO*/
)
{
    
}
