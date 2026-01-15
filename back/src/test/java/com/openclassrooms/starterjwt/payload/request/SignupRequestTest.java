package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    @Test
    void shouldSetAndGetFields() {
        SignupRequest req = new SignupRequest();
        req.setEmail("user@mail.com");
        req.setFirstName("Fatou");
        req.setLastName("Drame");
        req.setPassword("password123");

        assertEquals("user@mail.com", req.getEmail());
        assertEquals("Fatou", req.getFirstName());
        assertEquals("Drame", req.getLastName());
        assertEquals("password123", req.getPassword());
    }
}
