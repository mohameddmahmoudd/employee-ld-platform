package com.talentprogram.LdPlatformUserService.service;

import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import io.jsonwebtoken.security.InvalidKeyException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import com.talentprogram.LdPlatformUserService.dto.LoginRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.LoginResponseDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.entity.User;

@Slf4j
@Service
public class LoginService 
{
    private final UserRepository users;
    private final JwtService jwtService;

    public LoginService(UserRepository users, JwtService jwtService) {
        this.users = users;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) throws ResponseStatusException, InvalidKeyException, NoSuchFieldException, SecurityException, IllegalAccessException{

        log.debug("Attempting login for user: {}", request.username());

        User user = users.findByUsername(request.username())
        .orElseThrow(() -> {
            log.info("Login attempt failed for username: {}, user may not be found or is invalid"
            , request.username());
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        });
        
        log.debug("User found: {}", user.getUsername());

        if (!user.getPassword().equals(request.password())) {
            log.info("Invalid password for username: {}", request.username());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        UserDTO userView = new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getTitle(),
            user.getManager() != null ? user.getManager().getId() : null,
            user.getRoles()
        );

        log.debug("Login successful for user: {}", user.getUsername());

        return new LoginResponseDTO(
            jwtService.generateToken(Map.of("roles", user.getRoles()), user),
            userView
        );

    } 

    public LoginResponseDTO buildLoginResponse(UserDTO userDto) throws InvalidKeyException, ResponseStatusException, NoSuchFieldException, SecurityException, IllegalAccessException {
        log.debug("Building login response for user ID: {}", userDto.id());
        User user = users.findById(userDto.id())
            .orElseThrow(() -> { log.info("User not found with ID: {}", userDto.id());
             return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");});
        LoginRequestDTO request = new LoginRequestDTO(user.getUsername(), user.getPassword());
        return login(request);
    }
}
