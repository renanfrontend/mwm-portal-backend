package com.mwm.bioplanta.dto.auth;

public class PasswordResponse {
    private String password;
    public PasswordResponse(String password) { this.password = password; }
    public String getPassword() { return password; }
}
