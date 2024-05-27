package com.example.backend_qlcv.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class LoginRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String salt;

    private boolean remember;
}