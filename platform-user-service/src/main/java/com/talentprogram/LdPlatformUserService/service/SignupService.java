package com.talentprogram.LdPlatformUserService.service;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformUserService.dto.SignUpRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.model.Role;
import com.talentprogram.LdPlatformUserService.model.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final UserRepository users;
    private final RoleRepository roles;

    public SignupService(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @Transactional
    public UserDTO signup(SignUpRequestDTO request)
    {
            if (users.existsByUsername(request.username()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "name already exists");

        Role defaultRole = roles.findByName("GUEST")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "default role not found"));

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setFullName(request.fullName());
        newUser.setPassword(request.password());
        newUser.setTitle(request.title());
        newUser.setRoles(Set.of(defaultRole));
        newUser.setIsEnabled(true);
        newUser.setLearningPoints(0);

        users.save(newUser);

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
