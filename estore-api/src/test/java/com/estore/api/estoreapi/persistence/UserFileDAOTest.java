package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.User;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
    @JsonProperty
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[5];
        testUsers[0] = new User("Jack", "MyPasswd");
        testUsers[1] = new User("Angela", null);
        testUsers[2] = new User("Jack01", "MyPasswd");
        testUsers[3] = new User("John", "Password234");
        testUsers[4] = new User("Alex", "MyPasswd");

        when(mockObjectMapper.readValue(new File("null.txt"), User[].class)).thenReturn(testUsers);
        userFileDAO = new UserFileDAO("null.txt", mockObjectMapper);
    }

    @Test
    public void testGetUser() throws IOException {
        User user = userFileDAO.getUser("Angela");
        assertEquals(user, testUsers[1]);
    }

    @Test
    public void testGetNonExistentUser() throws IOException {
        User user = userFileDAO.getUser("Angela5");
        assertEquals(user, null);
    }

    @Test
    public void testGetUserByName() throws IOException{
        User user = userFileDAO.getUser("Jack");
        assertEquals(user, testUsers[0]);
    }

    @Test
    public void testGetNonExistentUserByName() throws IOException{
        User user = userFileDAO.getUser("Walter");
        assertEquals(user, null);
    }

    @Test
    public void testGetUsers() throws IOException {
        User[] usersArray  = userFileDAO.getUsers();
        ArrayList<User> users = new ArrayList<User>();
        for(User u : usersArray) {
            users.add(u);
        }

        assertEquals(users.size(), testUsers.length);
        for (int i = 0; i < testUsers.length; i++){
            assertEquals(users.contains(testUsers[i]), true);
        }
    }

    @Test 
    public void testUpdateUser() throws IOException {
        User user = new User("Jack01", "MyPasswd");
        User result = userFileDAO.updateUser(user);

        assertEquals(user, result);
    }

    @Test
    public void testUpdateNonExistingUser() throws IOException {
        User user = new User("PK", "001i0dsjka");
        User result = userFileDAO.updateUser(user);

        assertNotEquals(user, result);
    }

    @Test
    public void testCreateUser() throws IOException {
        User newUser = new User("Mark", "markkram");
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(newUser), "Unexpected Exception Thrown.");

        assertNotNull(result);
    }

    @Test
    public void testCreatePreExistingUser() throws IOException {
        User newUser = new User("Zach", "apple00");
        User result = userFileDAO.createUser(newUser);

        assertNotEquals(newUser, result);
    }

    @Test
    public void testDeleteUser() throws IOException {
        assertTrue(userFileDAO.deleteUser("Jack"));
    }

}
