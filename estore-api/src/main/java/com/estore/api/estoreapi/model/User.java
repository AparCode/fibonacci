package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    // Indicate external property names
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;

    public User(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public boolean checkPassword(String password) {return this.password.equals(password);}

    @Override
    public String toString() { return String.format("User {username=%s, password=%s}", username, password); }

}
