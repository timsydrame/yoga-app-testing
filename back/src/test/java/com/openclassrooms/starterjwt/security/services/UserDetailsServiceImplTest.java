package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());

        verify(userRepository, times(1)).findByEmail("test@test.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown@test.com")
        );

        assertTrue(exception.getMessage().contains("User Not Found with email: unknown@test.com"));
        verify(userRepository, times(1)).findByEmail("unknown@test.com");
    }

    @Test
    void shouldLoadAdminUser() {
        User admin = User.builder()
                .id(1L)
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .password("admin123")
                .admin(true)
                .build();

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@test.com");

        assertNotNull(userDetails);
        assertEquals("admin@test.com", userDetails.getUsername());

        // IMPORTANT: le service ne remplit pas admin => null attendu
        assertNull(((UserDetailsImpl) userDetails).getAdmin());

        verify(userRepository).findByEmail("admin@test.com");
    }


    @Test
    void shouldLoadUserWithAllFields() {
        LocalDateTime created = LocalDateTime.now().minusDays(10);
        LocalDateTime updated = LocalDateTime.now();

        User user = User.builder()
                .id(5L)
                .email("complete@test.com")
                .firstName("Complete")
                .lastName("User")
                .password("completePass")
                .admin(false)
                .createdAt(created)
                .updatedAt(updated)
                .build();

        when(userRepository.findByEmail("complete@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("complete@test.com");
        UserDetailsImpl impl = (UserDetailsImpl) userDetails;

        assertEquals(5L, impl.getId());
        assertEquals("complete@test.com", impl.getUsername());
        assertEquals("Complete", impl.getFirstName());
        assertEquals("User", impl.getLastName());
        assertEquals("completePass", impl.getPassword());

        // le service ne remplit pas admin
        assertNull(impl.getAdmin());
    }


    @Test
    void shouldHandleMultipleCallsForSameUser() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .build();

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails1 = userDetailsService.loadUserByUsername("test@test.com");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("test@test.com");

        // Assert
        assertNotNull(userDetails1);
        assertNotNull(userDetails2);
        assertEquals(userDetails1.getUsername(), userDetails2.getUsername());

        verify(userRepository, times(2)).findByEmail("test@test.com");
    }

    @Test
    void shouldReturnUserDetailsWithCorrectInterface() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("interface@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        when(userRepository.findByEmail("interface@test.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("interface@test.com");

        // Assert
        assertTrue(userDetails instanceof UserDetails);
        assertTrue(userDetails instanceof UserDetailsImpl);
    }
}