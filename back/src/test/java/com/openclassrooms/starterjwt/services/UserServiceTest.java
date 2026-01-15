package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        Long id = 1L;

        userService.delete(id);

        verify(userRepository).deleteById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_shouldReturnUser_whenFound() {
        Long id = 1L;
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findById(id);

        assertNotNull(result);
        assertSame(user, result);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_shouldReturnNull_whenNotFound() {
        Long id = 999L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User result = userService.findById(id);

        assertNull(result);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }
}
