package com.example.bibliotecaalejandra;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String message;
    @SerializedName("user")
    private UserResponse user;

    public String getMessage() {
        return message;
    }

    public UserResponse getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
