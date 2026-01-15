package com.openclassrooms.starterjwt.controllers;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SessionService sessionService;

    @MockBean
    SessionMapper sessionMapper;

    @Test
    void findById_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(1L);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    void findById_shouldReturn200_whenFound() throws Exception {
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);

        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void findAll_shouldReturn200() throws Exception {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    void create_shouldReturn200() throws Exception {
        // On envoie du JSON minimal (pas besoin de connaitre tous les champs)
        String json =
                "{"
                        + "\"name\":\"Yoga morning\","
                        + "\"date\":1700000000000,"
                        + "\"description\":\"A simple session\","
                        + "\"teacher_id\":1,"
                        + "\"users\":[]"
                        + "}";

        Session entity = new Session();
        Session saved = new Session();

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(entity);
        when(sessionService.create(entity)).thenReturn(saved);
        when(sessionMapper.toDto(saved)).thenReturn(new SessionDto());

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(sessionMapper).toEntity(any(SessionDto.class));
        verify(sessionService).create(entity);
        verify(sessionMapper).toDto(saved);
    }

    @Test
    void update_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(put("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    void update_shouldReturn400_whenBodyInvalid() throws Exception {
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }


    @Test
    void delete_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(1L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void delete_shouldReturn200_whenFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(new Session());

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    void participate_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

    @Test
    void participate_shouldReturn200_whenOk() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void noLongerParticipate_shouldReturn400_whenIdNotNumeric() throws Exception {
        mockMvc.perform(delete("/api/session/abc/participate/2"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

    @Test
    void noLongerParticipate_shouldReturn200_whenOk() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 2L);
    }
}
