package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayloadRequestTest {

    @Test
    void loginRequest_shouldSetAndGet() {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@mail.com");
        req.setPassword("pwd");

        assertEquals("a@mail.com", req.getEmail());
        assertEquals("pwd", req.getPassword());
    }

    @Test
    void signupRequest_shouldSetAndGet() {
        SignupRequest req = new SignupRequest();
        req.setEmail("a@mail.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("password123");

        assertEquals("a@mail.com", req.getEmail());
        assertEquals("John", req.getFirstName());
        assertEquals("Doe", req.getLastName());
        assertEquals("password123", req.getPassword());
    }
}
