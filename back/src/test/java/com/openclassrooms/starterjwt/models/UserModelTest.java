package com.openclassrooms.starterjwt.models;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void shouldCreateUserWithBuilder() {
        // Arrange & Act
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        // Arrange & Act
        LocalDateTime now = LocalDateTime.now();

        User user = new User(
                1L,
                "test@test.com",
                "Doe",
                "John",
                "password123",
                false,
                now,
                now
        );

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldCreateUserWithRequiredArgsConstructor() {
        // Arrange & Act
        User user = new User(
                "test@test.com",
                "Doe",
                "John",
                "password123",
                false
        );

        // Assert
        assertNull(user.getId()); // ID not set yet
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        // Arrange & Act
        User user = new User();

        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getEmail());
    }

    @Test
    void shouldSetAndGetPropertiesWithChaining() {
        // Arrange & Act
        LocalDateTime now = LocalDateTime.now();

        User user = new User()
                .setId(1L)
                .setEmail("test@test.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(true)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldTestEqualsWithSameId() {
        // Arrange
        User user1 = new User()
                .setId(1L)
                .setEmail("test1@test.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("pass1")
                .setAdmin(false);

        User user2 = new User()
                .setId(1L)
                .setEmail("test2@test.com")
                .setFirstName("Jane")
                .setLastName("Smith")
                .setPassword("pass2")
                .setAdmin(true);

        // Act & Assert
        assertEquals(user1, user2); // Equals only on ID due to @EqualsAndHashCode(of = {"id"})
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldTestNotEqualsWithDifferentId() {
        // Arrange
        User user1 = new User()
                .setId(1L)
                .setEmail("test@test.com");

        User user2 = new User()
                .setId(2L)
                .setEmail("test@test.com");

        // Act & Assert
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldTestEqualsWithNullId() {
        // Arrange
        User user1 = new User().setEmail("test1@test.com");
        User user2 = new User().setEmail("test2@test.com");

        // Act & Assert
        assertEquals(user1, user2); // Both have null ID
    }

    @Test
    void shouldTestNotEqualsWithNull() {
        // Arrange
        User user = new User().setId(1L);

        // Act & Assert
        assertNotEquals(user, null);
    }

    @Test
    void shouldTestNotEqualsWithDifferentClass() {
        // Arrange
        User user = new User().setId(1L);
        String notAUser = "Not a user";

        // Act & Assert
        assertNotEquals(user, notAUser);
    }

    @Test
    void shouldTestToString() {
        // Arrange
        User user = new User()
                .setId(1L)
                .setEmail("test@test.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(false);

        // Act
        String toString = user.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("test@test.com"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("password123"));
        assertTrue(toString.contains("admin=false"));
    }

    @Test
    void shouldTestGetterAndSetter() {
        // Arrange
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        // Act
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldReturnTrueForAdmin() {
        // Arrange & Act
        User admin = new User()
                .setEmail("admin@test.com")
                .setFirstName("Admin")
                .setLastName("User")
                .setPassword("admin123")
                .setAdmin(true);

        // Assert
        assertTrue(admin.isAdmin());
    }

    @Test
    void shouldReturnFalseForNonAdmin() {
        // Arrange & Act
        User user = new User()
                .setEmail("user@test.com")
                .setFirstName("Regular")
                .setLastName("User")
                .setPassword("user123")
                .setAdmin(false);

        // Assert
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldAllowChainedSetters() {
        // Arrange & Act
        User user = new User()
                .setId(1L)
                .setEmail("chained@test.com")
                .setFirstName("Chain")
                .setLastName("Test")
                .setPassword("chain123")
                .setAdmin(false);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("chained@test.com", user.getEmail());
        assertEquals("Chain", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertFalse(user.isAdmin());
    }
}