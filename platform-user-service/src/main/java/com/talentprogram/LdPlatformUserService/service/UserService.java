package com.talentprogram.LdPlatformUserService.service;

import org.springframework.stereotype.Service;

import com.talentprogram.LdPlatformUserService.repos.UserRepository;

@Service
public class UserService 
{
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    
}
