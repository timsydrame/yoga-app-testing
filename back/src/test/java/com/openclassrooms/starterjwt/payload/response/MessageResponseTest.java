package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void shouldSetAndGetMessage() {
        MessageResponse resp = new MessageResponse("ok");
        assertEquals("ok", resp.getMessage());

        resp.setMessage("updated");
        assertEquals("updated", resp.getMessage());
    }
}
