package com.talentprogram.LdPlatformUserService.dto;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDTO(@NotBlank String name, @NotBlank String password, @NotBlank String title)
{

}

