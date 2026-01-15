package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionModelTest {

    @Test
    void shouldSetAndGetFields_andChainAccessors() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();

        Teacher teacher = new Teacher().setId(10L).setFirstName("T").setLastName("L");

        Session session = new Session()
                .setId(1L)
                .setName("Yoga morning")
                .setDate(date)
                .setDescription("desc")
                .setTeacher(teacher)
                .setUsers(Arrays.asList())
                .setCreatedAt(now)
                .setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Yoga morning", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("desc", session.getDescription());
        assertSame(teacher, session.getTeacher());
        assertNotNull(session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void shouldCreateSessionWithBuilder() {
        // Arrange
        Date sessionDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher().setId(1L).setFirstName("John").setLastName("Doe");
        List<User> users = new ArrayList<>();

        // Act
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("Morning yoga")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Assert
        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals(sessionDate, session.getDate());
        assertEquals("Morning yoga", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }

    @Test
    void shouldCreateSessionWithNoArgsConstructor() {
        // Act
        Session session = new Session();

        // Assert
        assertNotNull(session);
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getTeacher());
    }

    @Test
    void equalsAndHashCode_shouldUseIdOnly() {
        Session s1 = new Session().setId(1L).setName("A");
        Session s2 = new Session().setId(1L).setName("B");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        Session s1 = new Session().setId(1L).setName("Yoga");
        Session s2 = new Session().setId(2L).setName("Yoga");

        assertNotEquals(s1, s2);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Session session = new Session().setId(1L);

        assertNotEquals(session, null);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Session session = new Session().setId(1L);
        String notASession = "Not a session";

        assertNotEquals(session, notASession);
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Session session = new Session().setId(1L).setName("Yoga");

        assertEquals(session, session);
    }

    @Test
    void equals_shouldHandleNullIds() {
        Session s1 = new Session().setName("Yoga");
        Session s2 = new Session().setName("Pilates");

        assertEquals(s1, s2); // Both have null ID
    }

    @Test
    void shouldHandleUsersListOperations() {
        // Arrange
        Session session = new Session().setUsers(new ArrayList<>());
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
                .admin(false)
                .build();

        // Act
        session.getUsers().add(user1);
        session.getUsers().add(user2);

        // Assert
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));

        // Remove
        session.getUsers().remove(user1);
        assertEquals(1, session.getUsers().size());
        assertFalse(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));
    }

    @Test
    void shouldSetAndGetTeacher() {
        // Arrange
        Session session = new Session();
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe");

        // Act
        session.setTeacher(teacher);

        // Assert
        assertNotNull(session.getTeacher());
        assertEquals(teacher, session.getTeacher());
        assertEquals("John", session.getTeacher().getFirstName());
    }

    @Test
    void shouldHandleNullTeacher() {
        // Arrange
        Session session = new Session()
                .setName("Session without teacher")
                .setTeacher(null);

        // Assert
        assertNull(session.getTeacher());
    }

    @Test
    void shouldHandleEmptyUsersList() {
        // Arrange
        Session session = new Session()
                .setUsers(new ArrayList<>());

        // Assert
        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void toString_shouldContainSessionName() {
        // Arrange
        Session session = new Session()
                .setId(1L)
                .setName("Yoga Session")
                .setDescription("Morning yoga");

        // Act
        String toString = session.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Yoga Session"));
    }

    @Test
    void shouldUpdateTimestamps() {
        // Arrange
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // Act
        Session session = new Session()
                .setCreatedAt(created)
                .setUpdatedAt(updated);

        // Assert
        assertEquals(created, session.getCreatedAt());
        assertEquals(updated, session.getUpdatedAt());
        assertTrue(session.getUpdatedAt().isAfter(session.getCreatedAt()));
    }

    @Test
    void shouldHandleMultipleTeacherChanges() {
        // Arrange
        Session session = new Session();
        Teacher teacher1 = new Teacher().setId(1L).setFirstName("John");
        Teacher teacher2 = new Teacher().setId(2L).setFirstName("Jane");

        // Act
        session.setTeacher(teacher1);
        assertEquals(teacher1, session.getTeacher());

        session.setTeacher(teacher2);

        // Assert
        assertEquals(teacher2, session.getTeacher());
        assertNotEquals(teacher1, session.getTeacher());
    }

    @Test
    void shouldSetDateCorrectly() {
        // Arrange
        Date now = new Date();
        Session session = new Session();

        // Act
        session.setDate(now);

        // Assert
        assertEquals(now, session.getDate());
    }

    @Test
    void shouldSetDescriptionCorrectly() {
        // Arrange
        Session session = new Session();
        String description = "This is a detailed session description";

        // Act
        session.setDescription(description);

        // Assert
        assertEquals(description, session.getDescription());
    }
}