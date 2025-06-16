package com.example.uberapp_tim9.model.dtos;

public class TokenDTO {
    String accessToken;
    String refreshToken;

    public TokenDTO(String s, String s1) {
        this.accessToken = s;
        this.refreshToken = s1;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}