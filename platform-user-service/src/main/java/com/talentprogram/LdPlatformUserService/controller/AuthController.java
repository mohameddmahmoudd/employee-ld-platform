package com.talentprogram.LdPlatformUserService.controller;

import com.talentprogram.LdPlatformUserService.dto.LoginRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.LoginResponseDTO;
import com.talentprogram.LdPlatformUserService.dto.SignUpRequestDTO;
import com.talentprogram.LdPlatformUserService.service.LoginService;
import com.talentprogram.LdPlatformUserService.service.SignupService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.CREATED;
import jakarta.validation.Valid;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;


@RestController

public class AuthController
{
    private final SignupService signupService;
    private final LoginService loginService;

    public AuthController(SignupService signupService, LoginService loginService) {
        this.signupService = signupService;
        this.loginService = loginService;
    }

    @PostMapping("auth/signup")
    @ResponseStatus(CREATED)
    public UserDTO signup(@Valid @RequestBody SignUpRequestDTO entity) {        
        return signupService.signup(entity);
    }
    
    @PostMapping("auth/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO entity) {
        return loginService.login(entity);
    }
    
    
}
