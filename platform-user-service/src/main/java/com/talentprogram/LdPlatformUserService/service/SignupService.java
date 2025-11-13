package com.talentprogram.LdPlatformUserService.service;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformUserService.dto.SignUpRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignupService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignupService(UserRepository users, RoleRepository roles, BCryptPasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO signup(SignUpRequestDTO request)
    {
        log.debug("Attempting signup for user: {}", request.username());
            if (users.existsByUsername(request.username()))
            {
                log.info("Signup attempt failed for username: {}, username already exists"
                , request.username());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
            }

        Role defaultRole = roles.findByName("GUEST")
            .orElseThrow(() ->{ log.info("Default role GUEST not found during signup for username: {}", request.username());
             return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found");});

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setTitle(request.title());
        newUser.setRoles(Set.of(defaultRole));
        newUser.setIsEnabled(true);
        newUser.setLearningPoints(0);

        users.save(newUser);

        log.debug("Signup successful for user: {}", request.username());

        UserDTO userView = new UserDTO(
            newUser.getId(),
            newUser.getUsername(),
            newUser.getFullName(),
            newUser.getTitle(),
            newUser.getManager() != null ? newUser.getManager().getId() : null,
            newUser.getRoles()
        );

        return userView;
    }
}
