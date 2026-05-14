package com.mwm.bioplanta.repository.auth;

import com.mwm.bioplanta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UsuarioPerfilPermissaoRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT p.nome FROM bio_usuario_perfil up JOIN bio_perfil p ON p.id = up.perfil_id WHERE up.usuario_id = :usuarioId", nativeQuery = true)
    List<String> findPerfisByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query(value = "SELECT DISTINCT pe.nome FROM bio_usuario_perfil up JOIN bio_perfil_permissao pp ON up.perfil_id = pp.perfil_id JOIN bio_permissao pe ON pe.id = pp.permissao_id WHERE up.usuario_id = :usuarioId", nativeQuery = true)
    List<String> findPermissoesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
