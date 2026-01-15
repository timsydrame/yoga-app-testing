package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void findById_shouldReturn400_whenIdIsNotNumeric() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void findById_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());

        verify(userService).findById(1L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void findById_shouldReturn200_whenUserFound() throws Exception {
        User user = new User();
        when(userService.findById(1L)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(new UserDto());

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void delete_shouldReturn400_whenIdIsNotNumeric() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void delete_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());

        verify(userService).findById(1L);
        verify(userService, never()).delete(any());
    }

    @Test
    @WithMockUser(username = "other@mail.com")
    void delete_shouldReturn401_whenAuthenticatedUserIsNotOwner() throws Exception {
        User user = new User();
        setField(user, "email", "owner@mail.com");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService).findById(1L);
        verify(userService, never()).delete(any());
    }

    @Test
    @WithMockUser(username = "owner@mail.com")
    void delete_shouldReturn200_andDelete_whenAuthenticatedUserIsOwner() throws Exception {
        User user = new User();
        setField(user, "email", "owner@mail.com");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set field '" + fieldName + "'", e);
        }
    }
}
