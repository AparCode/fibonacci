package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.model.Status;

@RestController
@RequestMapping("login")
public class LoginController {
    private static Logger LOG = Logger.getLogger(LoginController.class.getName());
    private UserDAO userDAO;

    public LoginController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @PostMapping("/auth")
    public ResponseEntity<Status> authenticateUser(@RequestBody(required = false) User user) {
        LOG.info("POST /login/auth " + user);

        try {
            User target = userDAO.getUser(user.getUsername());
            if (target != null){
                if (target.checkPassword(user.getPassword())){ return new ResponseEntity<Status>(Status.valid, HttpStatus.OK); }
                else { return new ResponseEntity<Status>(Status.invalidPassword, HttpStatus.OK); }
            }

            return new ResponseEntity<Status>(Status.doesNotExist, HttpStatus.OK);
        } catch (IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * registers the user by creating an User instance with the given credentials
     */
    
    @PostMapping("")
    public ResponseEntity<Status> registerUser(@RequestBody User user){
        LOG.info("POST /register" + user);

        try{
            User r_user = userDAO.createUser(user);

            if(r_user != null){
                return new ResponseEntity<Status>(Status.valid, HttpStatus.CREATED);
            }
            return new ResponseEntity<Status>(Status.userAlreadyExists, HttpStatus.OK);
        }

        catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Status> changeUserInfo(@RequestBody User user){
        LOG.info("PUT /user " + user);

        try{
            User changedUser = userDAO.updateUser(user);
            if(changedUser != null)
                return new ResponseEntity<Status>(Status.valid, HttpStatus.OK);
            else
                return new ResponseEntity<Status>(Status.doesNotExist, HttpStatus.NOT_FOUND);
        }

        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

