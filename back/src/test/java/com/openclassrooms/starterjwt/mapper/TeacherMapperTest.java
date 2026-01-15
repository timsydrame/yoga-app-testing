package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapTeacherToDto() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Act
        TeacherDto dto = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }

    @Test
    void shouldMapDtoToTeacher() {
        // Arrange
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");

        // Act
        Teacher teacher = teacherMapper.toEntity(dto);

        // Assert
        assertNotNull(teacher);
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    void shouldMapListOfTeachers() {
        // Arrange
        Teacher teacher1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(2L).firstName("Jane").lastName("Smith").build();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // Act
        List<TeacherDto> dtos = teacherMapper.toDto(teachers);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
        assertEquals("Jane", dtos.get(1).getFirstName());
    }

    @Test
    void shouldMapListOfDtos() {
        // Arrange
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");

        List<TeacherDto> dtos = Arrays.asList(dto1, dto2);

        // Act
        List<Teacher> teachers = teacherMapper.toEntity(dtos);

        // Assert
        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Jane", teachers.get(1).getFirstName());
    }

    @Test
    void shouldHandleNullTeacher() {
        // Act
        TeacherDto dto = teacherMapper.toDto((Teacher) null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldHandleNullDto() {
        // Act
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        // Assert
        assertNull(teacher);
    }
}