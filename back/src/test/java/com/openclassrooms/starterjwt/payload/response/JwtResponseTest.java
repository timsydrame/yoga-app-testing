package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void constructor_shouldSetFields_andDefaultTypeBearer() {
        JwtResponse resp = new JwtResponse(
                "token123",
                1L,
                "user@mail.com",
                "Fatou",
                "Drame",
                true
        );

        assertEquals("token123", resp.getToken());
        assertEquals("Bearer", resp.getType()); // default
        assertEquals(1L, resp.getId());
        assertEquals("user@mail.com", resp.getUsername());
        assertEquals("Fatou", resp.getFirstName());
        assertEquals("Drame", resp.getLastName());
        assertTrue(resp.getAdmin());
    }

    @Test
    void setters_shouldWork() {
        JwtResponse resp = new JwtResponse("t", 1L, "u", "f", "l", false);

        resp.setToken("newToken");
        resp.setType("Bearer");
        resp.setAdmin(true);

        assertEquals("newToken", resp.getToken());
        assertEquals("Bearer", resp.getType());
        assertTrue(resp.getAdmin());
    }
}
