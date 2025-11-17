package com.talentprogram.LdPlatformUserService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.talentprogram.LdPlatformUserService.dto.SignUpRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.entity.Role;
import com.talentprogram.LdPlatformUserService.entity.User;
import com.talentprogram.LdPlatformUserService.repos.RoleRepository;
import com.talentprogram.LdPlatformUserService.repos.UserRepository;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupService signupService;

    private SignUpRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new SignUpRequestDTO("mohamed", "Mohamed Mahmoud", "pass", "Engineer");
    }

    @SuppressWarnings("null")
    @Test
    void signupPersistsUserWithDefaultRole() {
        Role guest = new Role();
        guest.setName("GUEST");
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(roleRepository.findByName("GUEST")).thenReturn(Optional.of(guest));
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");

        UserDTO result = signupService.signup(request);

        assertThat(result.username()).isEqualTo("mohamed");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(saved.getRoles()).containsExactly(guest);
    }

    @Test
    void signupFailsWhenUsernameExists() {
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        assertThatThrownBy(() -> signupService.signup(request))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode")
            .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void signupFailsWhenDefaultRoleMissing() {
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(roleRepository.findByName("GUEST")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> signupService.signup(request))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode")
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
