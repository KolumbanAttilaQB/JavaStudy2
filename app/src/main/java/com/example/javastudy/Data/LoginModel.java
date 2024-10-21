package com.example.javastudy.Data;

public class LoginModel {
    private String username;
    private String password;
    private Integer expiresInMins;

    public LoginModel(String username, String password, Integer expiresInMins) {
        this.username = username;
        this.password = password;
        this.expiresInMins = expiresInMins; // Optional
    }
}
