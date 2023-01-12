package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.model.User;

@Component
public class UserFileDAO implements UserDAO{

    Map<String, User> users;

    private ObjectMapper objectMapper;

    private String filename;

    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private User[] getUserArray(String username) {

        ArrayList<User> userArrayList = new ArrayList<>();
        for (User user : users.values()){ 
            if (username == null || user.getUsername().equals(username)) { userArrayList.add(user); }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;

    }

    private boolean save() throws IOException {
        User[] userArray = getUsers();

        objectMapper.writeValue(new File(filename), userArray);

        return true;
    }

    private boolean load()  throws IOException {
        users = new TreeMap<>();

        User[] userArray = objectMapper.readValue(new File(filename), User[].class);

        for (User user : userArray){
            users.put(user.getUsername(), user);
        }
        return true;
    }

    public User getUser(String username){
        synchronized (users){
            synchronized(users){
                User[] users = getUserArray(username);
                return users.length == 0 ? null : users[0];
            }
        }
    }


    public User[] getUsers() throws IOException {
        synchronized (users){
            return getUserArray(null);
        }
    }

    public User updateUser(User user) throws IOException {
        synchronized (users){
            if (users.containsKey(user.getUsername()) == false) return null;
            users.put(user.getUsername(), user);
            save();
            return user;
        }
    }

    public User createUser(User user) throws IOException {
        synchronized (users){
            for (User existing : getUsers()){ if (existing.getUsername().equals(user.getUsername())) return null; } // Check for existing user

            User newUser = new User(user.getUsername(), user.getPassword());

            users.put(newUser.getUsername(), newUser);
            save();

            return newUser;
        }
    }

    public boolean deleteUser(String username) throws IOException {
        synchronized (users){
            if (users.containsKey(username)) {
                users.remove(username);
                return save();
            } else return false;
        }
    }
}

