package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_shouldReturn200_andJwtResponse_whenOk_andAdminTrue() throws Exception {
        String json =
                "{"
                        + "\"email\":\"admin@mail.com\","
                        + "\"password\":\"password123\""
                        + "}";

        UserDetailsImpl principal = UserDetailsImpl.builder()
                .id(1L)
                .username("admin@mail.com")
                .firstName("Fatou")
                .lastName("Drame")
                .password("encoded")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt-token");

        User user = new User();
        user.setAdmin(true);
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Fatou"))
                .andExpect(jsonPath("$.lastName").value("Drame"))
                .andExpect(jsonPath("$.admin").value(true));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(any(Authentication.class));
        verify(userRepository).findByEmail("admin@mail.com");
    }

    @Test
    void login_shouldReturn200_andAdminFalse_whenUserNotFoundInDb() throws Exception {
        String json =
                "{"
                        + "\"email\":\"user@mail.com\","
                        + "\"password\":\"password123\""
                        + "}";

        UserDetailsImpl principal = UserDetailsImpl.builder()
                .id(2L)
                .username("user@mail.com")
                .firstName("User")
                .lastName("Test")
                .password("encoded")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt-token");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin").value(false));

        verify(userRepository).findByEmail("user@mail.com");
    }

    @Test
    void login_shouldReturn400_whenBodyInvalid() throws Exception {
        // NotBlank sur email/password => 400 si manquant
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationManager, jwtUtils, userRepository);
    }

    @Test
    void register_shouldReturn400_whenEmailAlreadyTaken() throws Exception {
        String json =
                "{"
                        + "\"email\":\"test@mail.com\","
                        + "\"firstName\":\"Fatou\","
                        + "\"lastName\":\"Drame\","
                        + "\"password\":\"password123\""
                        + "}";

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        verify(userRepository).existsByEmail("test@mail.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldReturn200_whenOk() throws Exception {
        String json =
                "{"
                        + "\"email\":\"new@mail.com\","
                        + "\"firstName\":\"Fatou\","
                        + "\"lastName\":\"Drame\","
                        + "\"password\":\"password123\""
                        + "}";

        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPwd");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(userRepository).existsByEmail("new@mail.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldReturn400_whenBodyInvalid() throws Exception {
        // Validation SignupRequest => 400 si JSON incomplet
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userRepository, passwordEncoder);
    }
}
