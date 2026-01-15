package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapUserToDto() {
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

        // Act
        UserDto dto = userMapper.toDto(user);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertFalse(dto.isAdmin());
    }

    @Test
    void toEntity_shouldMapBasicFields() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("password123");
        dto.setAdmin(false);

        // Act
        User user = userMapper.toEntity(dto);

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldMapListOfUsers() {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("pass1")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("pass2")
                .admin(true)
                .build();

        List<User> users = Arrays.asList(user1, user2);

        // Act
        List<UserDto> dtos = userMapper.toDto(users);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("user1@test.com", dtos.get(0).getEmail());
        assertEquals("user2@test.com", dtos.get(1).getEmail());
    }

    @Test
    void listMapping_shouldWork() {
        // Arrange
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("user1@test.com");
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setPassword("password1");
        dto1.setAdmin(false);

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("user2@test.com");
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setPassword("password2");
        dto2.setAdmin(true);

        List<UserDto> dtos = Arrays.asList(dto1, dto2);

        // Act
        List<User> users = userMapper.toEntity(dtos);

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1@test.com", users.get(0).getEmail());
        assertEquals("user2@test.com", users.get(1).getEmail());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Doe", users.get(0).getLastName());
        assertEquals("password1", users.get(0).getPassword());
        assertEquals("Jane", users.get(1).getFirstName());
        assertEquals("Smith", users.get(1).getLastName());
        assertEquals("password2", users.get(1).getPassword());
    }

    @Test
    void shouldHandleNullUser() {
        // Act
        UserDto dto = userMapper.toDto((User) null); // ← Cast explicite

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldHandleNullDto() {
        // Act
        User user = userMapper.toEntity((UserDto) null); // ← Cast explicite

        // Assert
        assertNull(user);
    }

    @Test
    void shouldHandleEmptyList() {
        // Arrange
        List<User> emptyList = Arrays.asList();

        // Act
        List<UserDto> dtos = userMapper.toDto(emptyList);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void shouldMapAdminUser() {
        // Arrange
        User admin = User.builder()
                .id(1L)
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .password("admin123")
                .admin(true)
                .build();

        // Act
        UserDto dto = userMapper.toDto(admin);

        // Assert
        assertNotNull(dto);
        assertTrue(dto.isAdmin());
        assertEquals("admin@test.com", dto.getEmail());
    }

    @Test
    void shouldMapUserWithTimestamps() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().minusDays(5);
        LocalDateTime updatedAt = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Act
        UserDto dto = userMapper.toDto(user);

        // Assert
        assertNotNull(dto);
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }
}