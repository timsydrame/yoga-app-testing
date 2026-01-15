package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        // Injecter les valeurs de configuration via réflexion
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyForJwtTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000); // 24h
    }

    @Test
    void shouldGenerateJwtToken() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password"
        );
        when(auth.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(auth);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void shouldValidateJwtToken() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password"
        );
        when(auth.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(auth);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void shouldGetUserNameFromJwtToken() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password"
        );
        when(auth.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(auth);

        // Act
        String email = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("test@test.com", email);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        // Arrange - Token avec expiration immédiate
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000);

        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password"
        );
        when(auth.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(auth);

        // Reset to normal expiration
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void shouldReturnFalseForEmptyToken() {
        // Arrange
        String emptyToken = "";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }
}