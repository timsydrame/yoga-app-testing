package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void create_shouldSaveAndReturnSession() {
        Session session = new Session();
        Session saved = new Session();

        when(sessionRepository.save(session)).thenReturn(saved);

        Session result = sessionService.create(session);

        assertSame(saved, result);
        verify(sessionRepository).save(session);
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void findAll_shouldReturnRepositoryList() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertSame(sessions, result);
        verify(sessionRepository).findAll();
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void getById_shouldReturnSession_whenFound() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertSame(session, result);
        verify(sessionRepository).findById(1L);
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void getById_shouldReturnNull_whenNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
        verify(sessionRepository).findById(1L);
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void update_shouldSetIdAndSave() {
        Session session = new Session();
        Session saved = new Session();

        when(sessionRepository.save(any(Session.class))).thenReturn(saved);

        Session result = sessionService.update(10L, session);

        assertSame(saved, result);
        // on vérifie que l’objet passé au save a bien l’id = 10
        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());

        Session passed = captor.getValue();
        assertEquals(10L, getField(passed, "id"));

        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void participate_shouldThrowNotFound_whenSessionOrUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void participate_shouldThrowBadRequest_whenAlreadyParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        setField(user, "id", userId);

        Session session = new Session();
        setUsers(session, new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void participate_shouldAddUserAndSave_whenNotParticipatingYet() {
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        setField(user, "id", userId);

        Session session = new Session();
        setUsers(session, new ArrayList<>()); // personne inscrit

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        assertEquals(1, session.getUsers().size());
        assertSame(user, session.getUsers().get(0));

        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));

        verify(sessionRepository).findById(1L);
        verify(sessionRepository, never()).save(any());
        verifyNoInteractions(userRepository);
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequest_whenUserNotParticipating() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        setUsers(session, new ArrayList<>()); // liste vide => user pas dedans

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any());
        verifyNoInteractions(userRepository);
    }

    @Test
    void noLongerParticipate_shouldRemoveUserAndSave_whenUserParticipating() {
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        setField(user, "id", userId);

        User other = new User();
        setField(other, "id", 99L);

        Session session = new Session();
        setUsers(session, new ArrayList<>(Arrays.asList(user, other)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        assertEquals(1, session.getUsers().size());
        assertEquals(99L, (Long) getField(session.getUsers().get(0), "id"));

        verify(sessionRepository).save(session);
        verifyNoInteractions(userRepository);
    }

    // ---- helpers (pour éviter dépendre de setters Lombok) ----
    private static void setUsers(Session session, List<User> users) {
        try {
            // si setUsers existe, on l’utilise
            session.getClass().getMethod("setUsers", List.class).invoke(session, users);
        } catch (Exception e) {
            // sinon on force via reflection
            setField(session, "users", users);
        }
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

    private static Object getField(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get field '" + fieldName + "'", e);
        }
    }
}
