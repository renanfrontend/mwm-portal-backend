package com.mwm.bioplanta.dto;

public class LoginJwtResponse {
    private String token;
    private UsuarioInfo usuario;

    public LoginJwtResponse(String token, UsuarioInfo usuario) {
        this.token = token;
        this.usuario = usuario;
    }
    public String getToken() { return token; }
    public UsuarioInfo getUsuario() { return usuario; }

    public static class UsuarioInfo {
        private Long id;
        private String nome;
        private String perfil;
        public UsuarioInfo(Long id, String nome, String perfil) {
            this.id = id;
            this.nome = nome;
            this.perfil = perfil;
        }
        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getPerfil() { return perfil; }
    }
}
