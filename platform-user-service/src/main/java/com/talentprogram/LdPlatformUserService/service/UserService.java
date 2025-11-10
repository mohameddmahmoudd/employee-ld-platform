package com.talentprogram.LdPlatformUserService.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentprogram.LdPlatformUserService.model.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import  java.util.Set;
import com.talentprogram.LdPlatformUserService.model.Role;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import java.util.List;

@Service
public class UserService 
{
    private final UserRepository users;
    private final RoleRepository roles;

    public UserService(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    public Optional<User> getUserById(Long id) {
        return Optional.of(users.findById(id).orElse(null));
    }

    @Transactional
    public Optional<User> updateUser(Long id, UserDTO entity) {
        
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("user"));

        if (user != null) {
            user.setUsername(entity.username());
            user.setFullName(entity.fullName());
            user.setTitle(entity.title());
            users.save(user);
        }
        return Optional.of(user);
    }

    public void deleteUser(Long id) {
        users.deleteById(id);
    }

    @Transactional
    public List<String> updateUserRole(Long id, String role) {
        User user = users.findById(id).orElse(null);
        String normalized = role == null ? "" : role.trim().toUpperCase();
        
        Role roleEntity = this.roles.findByName(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + role));
        
        Role NewUserRole = this.roles.findByName("NEWUSER").orElseThrow();
        
        if (user != null) 
        {
            if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
            }
            if(user.getRoles().contains(NewUserRole)) {
                user.getRoles().remove(NewUserRole);
            }
            user.getRoles().add(roleEntity);
            users.save(user);
        }
        return user.getRoles().stream().map(Role::getName).toList();
    }

    @Transactional
    public Optional<User> updateUserManager(Long id, Long managerId) {
        
        User user = users.findById(id).orElse(null);
        
        if (user != null && (managerId == null || !managerId.equals(id))) {
            user.setManager(users.findById(managerId).orElse(null));
            users.save(user);
        }
        else if (user != null && managerId == null) {
            user.setManager(null);
            users.save(user);
        }
        else
        {
            throw new EntityNotFoundException("User not found");
        }

        return Optional.of(user);
    }

}