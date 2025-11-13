package com.talentprogram.LdPlatformUserService.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import com.talentprogram.LdPlatformUserService.dto.UserUpdateInfoDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class UserService 
{
    private final UserRepository users;
    private final RoleRepository roles;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles, BCryptPasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);
        return users.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);
        return users.findByUsername(username);
    }

    @Transactional
    public Optional<User> updateUser(Long id, UserUpdateInfoDTO entity) {
        
        log.debug("Updating user with id: {}", id);
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("user"));

        if (user != null) {
            user.setUsername(entity.username());
            user.setFullName(entity.fullName());
            user.setTitle(entity.title());
            users.save(user);
        }
        log.debug("User with id: {} updated successfully", id);
        return Optional.of(user);
    }

    public void deleteUser(Long id) {
        users.deleteById(id);
        log.debug("User with id: {} deleted successfully", id);
    }

    @Transactional
    public List<String> updateUserRole(Long id, List<String> role) {
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));

        /*Initializing a new set before clearing */
        if (user.getRoles()==null) user.setRoles(new HashSet<>());
        user.getRoles().clear();
        log.debug("Cleared roles for user");

        for (String roleName : role) {
            String normalized = roleName == null ? "" : roleName.trim().toUpperCase();
            Role roleEntity = roles.findByName(normalized)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + roleName));
            user.getRoles().add(roleEntity);
        }
        log.debug("Assigned new roles for user");

        users.save(user);

        log.debug("Roles updated successfully for user with id: {}", id);
        return user.getRoles().stream().map(Role::getName).toList();
    }

    @Transactional
    public Optional<User> updateUserManager(Long id, Long managerId) {

        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));

        user.setManager(users.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager " + managerId + " not found")));
        users.save(user);

        log.debug("Manager updated successfully for user with id: {}", id);
        return Optional.of(user);
    }

    public void updateUserPassword(Long id, String newPassword) {
        User user = users.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setPassword(passwordEncoder.encode(newPassword));
        users.save(user);
        log.debug("Password updated successfully for user with id: {}", id);
    }

    public List<String> getAllRoles() {
        return roles.findAll().stream().map(Role::getName).toList();
    }

}