package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Test
    void shouldMapSessionToDto() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("pass1")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("Bob")
                .lastName("Johnson")
                .password("pass2")
                .admin(false)
                .build();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("Morning yoga")
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Yoga Session", dto.getName());
        assertEquals("Morning yoga", dto.getDescription());
        assertEquals(1L, dto.getTeacher_id());
        assertEquals(2, dto.getUsers().size());
        assertTrue(dto.getUsers().contains(1L));
        assertTrue(dto.getUsers().contains(2L));
    }

    @Test
    void shouldMapDtoToSession() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        User user = User.builder()
                .id(1L)
                .email("user@test.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("pass")
                .admin(false)
                .build();

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");
        dto.setDate(new Date());
        dto.setDescription("Description");
        dto.setTeacher_id(1L);
        dto.setUsers(Arrays.asList(1L));

        // Act
        Session session = sessionMapper.toEntity(dto);

        // Assert
        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("Description", session.getDescription());
        assertNotNull(session.getTeacher());
        assertEquals(1L, session.getTeacher().getId());
        assertEquals(1, session.getUsers().size());
    }

    @Test
    void shouldMapListOfSessions() {
        // Arrange
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Desc 1")
                .users(new ArrayList<>())
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Session 2")
                .date(new Date())
                .description("Desc 2")
                .users(new ArrayList<>())
                .build();

        List<Session> sessions = Arrays.asList(session1, session2);

        // Act
        List<SessionDto> dtos = sessionMapper.toDto(sessions);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Session 1", dtos.get(0).getName());
        assertEquals("Session 2", dtos.get(1).getName());
    }

    @Test
    void shouldMapListOfDtos() {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(Teacher.builder().id(1L).build());

        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        dto1.setName("Session 1");
        dto1.setDate(new Date());
        dto1.setDescription("Desc");
        dto1.setTeacher_id(1L);
        dto1.setUsers(new ArrayList<>());

        SessionDto dto2 = new SessionDto();
        dto2.setId(2L);
        dto2.setName("Session 2");
        dto2.setDate(new Date());
        dto2.setDescription("Desc");
        dto2.setTeacher_id(1L);
        dto2.setUsers(new ArrayList<>());

        List<SessionDto> dtos = Arrays.asList(dto1, dto2);

        // Act
        List<Session> sessions = sessionMapper.toEntity(dtos);

        // Assert
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        assertEquals("Session 1", sessions.get(0).getName());
        assertEquals("Session 2", sessions.get(1).getName());
    }

    @Test
    void shouldHandleNullSession() {
        // Act
        SessionDto dto = sessionMapper.toDto((Session) null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldHandleNullDto() {
        // Act
        Session session = sessionMapper.toEntity((SessionDto) null);

        // Assert
        assertNull(session);
    }

    @Test
    void shouldHandleEmptyUsersList() {
        // Arrange
        Session session = Session.builder()
                .id(1L)
                .name("Empty Session")
                .date(new Date())
                .description("No users")
                .users(new ArrayList<>())
                .build();

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }

    @Test
    void shouldHandleSessionWithoutTeacher() {
        // Arrange
        Session session = Session.builder()
                .id(1L)
                .name("No Teacher Session")
                .date(new Date())
                .description("Description")
                .teacher(null)
                .users(new ArrayList<>())
                .build();

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
    }
}