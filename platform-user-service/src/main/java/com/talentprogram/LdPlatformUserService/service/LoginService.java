package com.talentprogram.LdPlatformUserService.service;

import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformUserService.dto.LoginRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.LoginResponseDTO;
import com.talentprogram.LdPlatformUserService.model.User;
import com.talentprogram.LdPlatformUserService.dto.UserViewDTO;

@Service
public class LoginService 
{
    private final UserRepository users;

    /* TODO Add JwtService */

    public LoginService(UserRepository users) {
        this.users = users;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        /* TODO Implement login logic */

        User user = users.findByUsername(request.name())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

        if (!user.getPassword().equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
        }

        UserViewDTO userView = new UserViewDTO(
            user.getId(),
            user.getFullName(),
            user.getUsername(),
            user.getTitle(),
            user.getManager() != null ? user.getManager().getId() : null,
            user.getRoles()
        );

        return new LoginResponseDTO(
        null,    
            userView
            /* TODO Add JWT token */
        );

    } 
}
