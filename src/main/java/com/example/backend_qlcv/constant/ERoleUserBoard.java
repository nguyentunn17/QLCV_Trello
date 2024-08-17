package com.example.backend_qlcv.constant;

public enum ERoleUserBoard {
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String role;

    ERoleUserBoard(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

