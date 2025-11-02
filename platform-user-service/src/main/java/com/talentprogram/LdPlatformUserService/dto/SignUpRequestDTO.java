package com.talentprogram.LdPlatformUserService.dto;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDTO(@NotBlank String username, @NotBlank String fullName, @NotBlank String password, @NotBlank String title)
{

}

