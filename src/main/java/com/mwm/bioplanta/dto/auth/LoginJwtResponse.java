package com.mwm.bioplanta.dto.auth;

import java.util.List;

public class LoginJwtResponse {
    private String token;
    private UsuarioInfo usuario;
    private List<String> perfis;
    private List<String> permissoes;

    public LoginJwtResponse(String token, UsuarioInfo usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public LoginJwtResponse(String token, UsuarioInfo usuario, List<String> perfis, List<String> permissoes) {
        this.token = token;
        this.usuario = usuario;
        this.perfis = perfis;
        this.permissoes = permissoes;
    }

    public String getToken() { return token; }
    public UsuarioInfo getUsuario() { return usuario; }
    public List<String> getPerfis() { return perfis; }
    public List<String> getPermissoes() { return permissoes; }

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
