package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserTest {

    @Test
    public void testStruct() {
        int expectedId = 0;
        String expectedUsername = "John Doe";
        String expectedPassword = "StrongPassword";
        String expectedEmail = "JDoe@email.com";

        User user = new User(expectedUsername, expectedPassword);

        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
        assertTrue(user.checkPassword(expectedPassword));
    }
    
}
