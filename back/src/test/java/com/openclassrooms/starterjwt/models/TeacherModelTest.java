package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherModelTest {

    @Test
    void shouldSetAndGetFields_andChainAccessors() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Fatou")
                .setLastName("Drame")
                .setCreatedAt(now)
                .setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Fatou", teacher.getFirstName());
        assertEquals("Drame", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void shouldCreateTeacherWithBuilder() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Assert
        assertNotNull(teacher);
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void shouldCreateTeacherWithNoArgsConstructor() {
        // Act
        Teacher teacher = new Teacher();

        // Assert
        assertNotNull(teacher);
        assertNull(teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
    }

    @Test
    void equalsAndHashCode_shouldUseIdOnly() {
        Teacher t1 = new Teacher().setId(1L).setFirstName("A").setLastName("B");
        Teacher t2 = new Teacher().setId(1L).setFirstName("X").setLastName("Y");

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        Teacher t1 = new Teacher().setId(1L).setFirstName("John");
        Teacher t2 = new Teacher().setId(2L).setFirstName("John");

        assertNotEquals(t1, t2);
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Teacher teacher = new Teacher().setId(1L);

        assertNotEquals(teacher, null);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Teacher teacher = new Teacher().setId(1L);
        String notATeacher = "Not a teacher";

        assertNotEquals(teacher, notATeacher);
    }

    @Test
    void equals_shouldReturnTrueForSameObject() {
        Teacher teacher = new Teacher().setId(1L).setFirstName("John");

        assertEquals(teacher, teacher);
    }

    @Test
    void equals_shouldHandleNullIds() {
        Teacher t1 = new Teacher().setFirstName("John");
        Teacher t2 = new Teacher().setFirstName("Jane");

        assertEquals(t1, t2); // Both have null ID
    }

    @Test
    void toString_shouldContainTeacherName() {
        // Arrange
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe");

        // Act
        String toString = teacher.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
    }

    @Test
    void shouldUpdateTimestamps() {
        // Arrange
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        // Act
        Teacher teacher = new Teacher()
                .setCreatedAt(created)
                .setUpdatedAt(updated);

        // Assert
        assertEquals(created, teacher.getCreatedAt());
        assertEquals(updated, teacher.getUpdatedAt());
        assertTrue(teacher.getUpdatedAt().isAfter(teacher.getCreatedAt()));
    }

    @Test
    void shouldSetFirstNameCorrectly() {
        // Arrange
        Teacher teacher = new Teacher();

        // Act
        teacher.setFirstName("Alice");

        // Assert
        assertEquals("Alice", teacher.getFirstName());
    }

    @Test
    void shouldSetLastNameCorrectly() {
        // Arrange
        Teacher teacher = new Teacher();

        // Act
        teacher.setLastName("Smith");

        // Assert
        assertEquals("Smith", teacher.getLastName());
    }

    @Test
    void shouldChainMultipleSetters() {
        // Act
        Teacher teacher = new Teacher()
                .setId(5L)
                .setFirstName("Marie")
                .setLastName("Curie");

        // Assert
        assertEquals(5L, teacher.getId());
        assertEquals("Marie", teacher.getFirstName());
        assertEquals("Curie", teacher.getLastName());
    }

    @Test
    void shouldHandleNullFirstName() {
        // Arrange
        Teacher teacher = new Teacher()
                .setFirstName(null)
                .setLastName("Doe");

        // Assert
        assertNull(teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    void shouldHandleNullLastName() {
        // Arrange
        Teacher teacher = new Teacher()
                .setFirstName("John")
                .setLastName(null);

        // Assert
        assertEquals("John", teacher.getFirstName());
        assertNull(teacher.getLastName());
    }

    @Test
    void shouldHandleNullTimestamps() {
        // Arrange
        Teacher teacher = new Teacher()
                .setCreatedAt(null)
                .setUpdatedAt(null);

        // Assert
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }
}