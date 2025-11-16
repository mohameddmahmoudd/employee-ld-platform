package com.talentprogram.LdPlatformUserService.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentprogram.LdPlatformUserService.dto.LoginRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.LoginResponseDTO;
import com.talentprogram.LdPlatformUserService.dto.SignUpRequestDTO;
import com.talentprogram.LdPlatformUserService.dto.UserDTO;
import com.talentprogram.LdPlatformUserService.service.LoginService;
import com.talentprogram.LdPlatformUserService.service.SignupService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupService signupService;

    @MockBean
    private LoginService loginService;

    @Test
    void signupReturnsLoginResponse() throws Exception {
        SignUpRequestDTO request = new SignUpRequestDTO("mohamed", "Mohamed", "pass", "Engineer");
        UserDTO userDto = new UserDTO(1L, "mohamed", "Mohamed", "Engineer", null, Set.of("GUEST"));
        LoginResponseDTO response = new LoginResponseDTO("token", userDto);
        when(signupService.signup(any(SignUpRequestDTO.class))).thenReturn(userDto);
        when(loginService.buildLoginResponse(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void loginDelegatesToService() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("mohamed", "pass");
        UserDTO userDto = new UserDTO(1L, "mohamed", "Mohamed", "Engineer", null, Set.of());
        LoginResponseDTO response = new LoginResponseDTO("token", userDto);
        when(loginService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
