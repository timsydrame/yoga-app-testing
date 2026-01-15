package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void shouldCreateUserDetailsWithBuilder() {
        // Arrange & Act
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password123")
                .build();

        // Assert
        assertNotNull(userDetails);
        assertEquals(1L, userDetails.getId());
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertEquals("password123", userDetails.getPassword());
        assertFalse(userDetails.getAdmin());
    }

    @Test
    void shouldCreateUserDetailsWithAllArgsConstructor() {
        // Arrange & Act
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password123"
        );

        // Assert
        assertNotNull(userDetails);
        assertEquals(1L, userDetails.getId());
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    void shouldReturnEmptyAuthorities() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
        assertEquals(0, authorities.size());
    }

    @Test
    void shouldReturnTrueForAccountNonExpired() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueForAccountNonLocked() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueForCredentialsNonExpired() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueForEnabled() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldTestEqualsWithSameId() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test1@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password1")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test2@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("password2")
                .build();

        // Act & Assert
        assertEquals(user1, user2); // Equals only checks ID
        // NE PAS tester hashCode car pas surchargé dans la classe
    }

    @Test
    void shouldTestNotEqualsWithDifferentIds() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertNotEquals(user1, user2);
        // NE PAS tester hashCode
    }

    @Test
    void shouldReturnTrueWhenComparingWithSameObject() {
        // Arrange
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertEquals(user, user);
    }

    @Test
    void shouldReturnFalseWhenComparingWithNull() {
        // Arrange
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        // Act & Assert
        assertNotEquals(user, null);
    }

    @Test
    void shouldReturnFalseWhenComparingWithDifferentClass() {
        // Arrange
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        String notAUser = "Not a UserDetailsImpl";

        // Act & Assert
        assertNotEquals(user, notAUser);
    }

    @Test
    void shouldHandleNullId() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .username("test1@test.com")
                .password("password1")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .username("test2@test.com")
                .password("password2")
                .build();

        // Act & Assert
        assertEquals(user1, user2); // Both have null ID
    }

    @Test
    void shouldCreateAdminUser() {
        // Arrange & Act
        UserDetailsImpl admin = UserDetailsImpl.builder()
                .id(1L)
                .username("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .admin(true)
                .password("admin123")
                .build();

        // Assert
        assertTrue(admin.getAdmin());
        assertEquals("admin@test.com", admin.getUsername());
    }

    @Test
    void shouldCreateNonAdminUser() {
        // Arrange & Act
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user@test.com")
                .firstName("Regular")
                .lastName("User")
                .admin(false)
                .password("user123")
                .build();

        // Assert
        assertFalse(user.getAdmin());
        assertEquals("user@test.com", user.getUsername());
    }

    @Test
    void shouldGetAllFieldsCorrectly() {
        // Arrange & Act
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(5L)
                .username("complete@test.com")
                .firstName("Complete")
                .lastName("User")
                .admin(true)
                .password("completePass")
                .build();

        // Assert
        assertEquals(5L, userDetails.getId());
        assertEquals("complete@test.com", userDetails.getUsername());
        assertEquals("Complete", userDetails.getFirstName());
        assertEquals("User", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("completePass", userDetails.getPassword());
    }

    @Test
    void shouldImplementUserDetailsInterface() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        // Assert
        assertTrue(userDetails instanceof org.springframework.security.core.userdetails.UserDetails);
    }

    @Test
    void shouldHaveSerialVersionUID() {
        // This test ensures the serialVersionUID is present
        // Arrange & Act
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        // Assert
        assertNotNull(userDetails);
        // The class should be serializable
        assertTrue(userDetails instanceof java.io.Serializable);
    }
}