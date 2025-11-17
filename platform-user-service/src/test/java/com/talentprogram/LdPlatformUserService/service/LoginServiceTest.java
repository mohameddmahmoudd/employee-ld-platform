package com.talentprogram.LdPlatformUserService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformUserService.dto.LoginRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.LoginResponseDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        ReflectionTestUtils.setField(existingUser, "id", 42L);
        existingUser.setUsername("mohamed123");
        existingUser.setFullName("Mohamed Mahmoud");
        existingUser.setPassword("hashed");
        existingUser.setRoles(Set.of(role("EMPLOYEE")));
    }

    @Test
    void loginReturnsTokenWhenCredentialsMatch() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("mohamed123", "secret");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(request.password(), existingUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn("token-value");

        LoginResponseDTO response = loginService.login(request);

        assertThat(response.token()).isEqualTo("token-value");
        assertThat(response.user().username()).isEqualTo("mohamed123");
    }

    @Test
    void loginFailsWhenUserMissing() {
        LoginRequestDTO request = new LoginRequestDTO("ghost", "secret");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(request))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode")
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginFailsWhenPasswordMismatch() {
        LoginRequestDTO request = new LoginRequestDTO("mohamed123", "bad");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(request.password(), existingUser.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> loginService.login(request))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode")
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void buildLoginResponseUsesRepositoryLookup() throws Exception {
        UserDTO dto = new UserDTO(42L, "mohamed123", "Mohamed", "Engineer", null, Set.of("EMPLOYEE"));
        when(userRepository.findById(42L)).thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn("token");

        LoginResponseDTO response = loginService.buildLoginResponse(dto);

        assertThat(response.user()).isSameAs(dto);
        assertThat(response.token()).isEqualTo("token");
    }

    @Test
    void buildLoginResponseThrowsWhenUserMissing() {
        UserDTO dto = new UserDTO(99L, "ghost", "Ghost", "", null, Set.of());
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.buildLoginResponse(dto))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode")
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private Role role(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }
}
