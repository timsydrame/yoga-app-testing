package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void shouldSetAndGetFields() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@mail.com");
        req.setPassword("secret");

        assertEquals("user@mail.com", req.getEmail());
        assertEquals("secret", req.getPassword());
    }
}
