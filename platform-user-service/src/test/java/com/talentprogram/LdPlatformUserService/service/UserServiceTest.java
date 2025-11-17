package com.talentprogram.LdPlatformUserService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.talentprogram.LdPlatformUserService.dto.UserUpdateInfoDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setUsername("current");
        user.setFullName("Current User");
        user.setTitle("Engineer");
        user.setRoles(new HashSet<>(Set.of(role("EMPLOYEE"))));
    }

    @SuppressWarnings("null")
    @Test
    void updateUserChangesBasicFields() {
        UserUpdateInfoDTO update = new UserUpdateInfoDTO(1L, "newUser", "New Name", "Lead");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("newUser")).thenReturn(false);

        Optional<User> result = userService.updateUser(1L, update);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("newUser");
        verify(userRepository).save(user);
    }

    @SuppressWarnings("null")
    @Test
    void updateUserThrowsWhenUsernameAlreadyExists() {
        UserUpdateInfoDTO update = new UserUpdateInfoDTO(1L, "duplicate", "New Name", "Lead");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("duplicate")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, update))
            .isInstanceOf(IllegalArgumentException.class);
        verify(userRepository, never()).save(any());
    }
                             
    @SuppressWarnings("null")
    @Test
    void updateUserRoleReplacesRoles() {
        Role admin = role("ADMIN");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(admin));
        List<String> assigned = userService.updateUserRole(1L, List.of("admin"));

        assertThat(assigned).containsExactly("ADMIN");
        assertThat(user.getRoles()).containsExactly(admin);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserRoleThrowsForUnknownRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("MANAGER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserRole(1L, List.of("MANAGER")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("null")
    @Test
    void updateUserManagerClearsWhenZero() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUserManager(1L, 0L);

        assertThat(user.getManager()).isNull();
        verify(userRepository).save(user);
 }

    @Test
    void updateUserManagerAssignsManager() {
        User manager = new User();
        ReflectionTestUtils.setField(manager, "id", 2L);
        manager.setUsername("manager");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(manager));

        userService.updateUserManager(1L, 2L);

        assertThat(user.getManager()).isEqualTo(manager);
    }

    @SuppressWarnings("null")
    @Test
    void updateUserPasswordEncodesBeforeSave() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encoded");

        userService.updateUserPassword(1L, "newPass");

        assertThat(user.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(user);
    }

    @Test
    void getAllRolesReturnsNames() {
        when(roleRepository.findAll()).thenReturn(List.of(role("EMPLOYEE"), role("MANAGER")));

        assertThat(userService.getAllRoles()).containsExactly("EMPLOYEE", "MANAGER");
    }

    @Test
    void updateUserThrowsWhenUserMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, new UserUpdateInfoDTO(1L, "u", "n", "t")))
            .isInstanceOf(EntityNotFoundException.class);
    }

    private Role role(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }
}
