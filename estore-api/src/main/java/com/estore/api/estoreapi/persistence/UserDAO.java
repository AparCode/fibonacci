package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.User;


/**
 * Defines the interface for User Persistance
 * 
 * @author Nate Mount
 */
public interface UserDAO {
    
    User getUser(String username) throws IOException;

    User[] getUsers() throws IOException;

    User updateUser(User user) throws IOException;

    User createUser(User user) throws IOException;

    boolean deleteUser(String username) throws IOException;

}
