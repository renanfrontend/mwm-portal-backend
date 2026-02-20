package com.mwm.bioplanta.dto;

public class LoginResponse {
    private String message;
    private String nome;
    public LoginResponse(String message, String nome) {
        this.message = message;
        this.nome = nome;
    }
    public String getMessage() { return message; }
    public String getNome() { return nome; }
}
