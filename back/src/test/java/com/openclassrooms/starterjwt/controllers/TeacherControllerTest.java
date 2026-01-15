package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Test
    void findById_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService, teacherMapper);
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(1L);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findById_shouldReturn200_whenFound() throws Exception {
        Teacher teacher = new Teacher();
        when(teacherService.findById(1L)).thenReturn(teacher);

        // on ne veut pas "tester DTO", donc on évite d'asserter le contenu
        when(teacherMapper.toDto(teacher)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk());

        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void findAll_shouldReturn200() throws Exception {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk());

        verify(teacherService).findAll();
        verify(teacherMapper).toDto(anyList());
    }
}
