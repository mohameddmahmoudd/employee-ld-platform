package com.talentprogram.LdPlatformUserService.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordUpdateRequestDTO(@NotBlank String newPassword) 
{
}