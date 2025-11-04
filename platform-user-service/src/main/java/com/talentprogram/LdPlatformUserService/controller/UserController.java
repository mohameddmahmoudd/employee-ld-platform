package com.talentprogram.LdPlatformUserService.controller;

import org.springframework.web.bind.annotation.RestController;
import com.talentprogram.LdPlatformUserService.service.UserService;
@RestController
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    
}
