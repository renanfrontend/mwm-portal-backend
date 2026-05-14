package com.mwm.bioplanta.service.auth;

import com.mwm.bioplanta.repository.auth.UsuarioPerfilPermissaoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioPerfilPermissaoService {
    private final UsuarioPerfilPermissaoRepository repository;

    public UsuarioPerfilPermissaoService(UsuarioPerfilPermissaoRepository repository) {
        this.repository = repository;
    }

    public List<String> getPerfis(Long usuarioId) {
        return repository.findPerfisByUsuarioId(usuarioId);
    }

    public List<String> getPermissoes(Long usuarioId) {
        return repository.findPermissoesByUsuarioId(usuarioId);
    }
}
