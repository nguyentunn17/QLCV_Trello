package com.example.backend_qlcv.payload;

import java.util.List;

public class AuthResponse {
    private String type = "Bearer";
    private Long id;
    private String username;

    private String email;
    private String accessToken;

    private List<String> roles;

    public AuthResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.accessToken = accessToken;
        this.roles = roles;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}