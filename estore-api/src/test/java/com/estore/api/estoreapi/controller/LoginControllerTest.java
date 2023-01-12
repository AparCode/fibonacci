package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Status;


@Tag("Controller-tier")
public class LoginControllerTest {
    private LoginController loginController;
    private UserDAO mockUserDAO;
 
    @BeforeEach
    public void setupUserController(){
        mockUserDAO = mock(UserDAO.class);
        loginController = new LoginController(mockUserDAO);
    }

    @Test
    public void testAuthenticateUser() throws IOException {
        User user = mock(User.class);

        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        when(user.checkPassword(user.getPassword())).thenReturn(true);

        ResponseEntity<Status> response = loginController.authenticateUser(user);
      
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testRegisterUser() throws IOException {
        User user =  new User("King", "abc123");

        when(mockUserDAO.createUser(user)).thenReturn(user);

        ResponseEntity<Status> response = loginController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Status.valid, response.getBody());
    }

    @Test
    public void testRegisterFailed() throws IOException {
        // setup
        User testUser = new User("King", "abc123");

        when(mockUserDAO.createUser(testUser)).thenReturn(null);

        ResponseEntity<Status> response = loginController.registerUser(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Status.userAlreadyExists, response.getBody());

    }

    @Test
    public void testRegisterHandleException() throws IOException {
        // setup
        User testUser = new User("King", "abc123");

        doThrow(new IOException()).when(mockUserDAO).createUser(testUser);

         ResponseEntity<Status> response = loginController.registerUser(testUser);

         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }



}