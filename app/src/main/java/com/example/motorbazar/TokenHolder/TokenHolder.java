package com.example.motorbazar.TokenHolder;

public class TokenHolder {
    public static String userToken;

    public static boolean setToken(String token) {
        userToken = token;
        return true;
    }
}
