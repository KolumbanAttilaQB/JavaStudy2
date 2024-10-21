package com.example.javastudy.Data;

public class UserModel {

    private final int id;
    private final String username;
    private final String email;
    private final String image;
    private final String accessToken;
    private final String refreshToken;

    public UserModel(int id, String userName, String email, String image, String accessToken, String refreshToken) {
        this.id = id;
        this.username = userName;
        this.email = email;
        this.image = image;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserModel getUserData() {
        return new UserModel(id, username, email, image, accessToken, refreshToken);
    }

    public String getUserName() {
        return  username;
    }
}

