package com.mwm.bioplanta.controller.auth;

import com.mwm.bioplanta.dto.auth.ErrorResponse;
import com.mwm.bioplanta.dto.auth.LoginJwtResponse;
import com.mwm.bioplanta.dto.auth.LoginRequest;
import com.mwm.bioplanta.dto.auth.LoginResponse;
import com.mwm.bioplanta.dto.auth.RegisterRequest;
import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.service.auth.UserService;
import com.mwm.bioplanta.service.auth.UsuarioPerfilPermissaoService;
import com.mwm.bioplanta.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de autenticação e registro")
public class AuthController {

    private final UserService userService;
    private final UsuarioPerfilPermissaoService usuarioPerfilPermissaoService;

    public AuthController(UserService userService, UsuarioPerfilPermissaoService usuarioPerfilPermissaoService) {
        this.userService = userService;
        this.usuarioPerfilPermissaoService = usuarioPerfilPermissaoService;
    }

    @Operation(summary = "Realiza login", description = "Autentica usuário e retorna token/sessão")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticate(request.getUsername(), request.getPassword());
            String token = JwtUtil.generateToken(user.getUsername());
            String perfil = "ADMIN";
            if (!"Admin".equalsIgnoreCase(user.getUsername())) {
                perfil = "USER";
            }
            LoginJwtResponse.UsuarioInfo usuarioInfo = new LoginJwtResponse.UsuarioInfo(
                user.getId(), user.getNome(), perfil
            );
            // Busca perfis e permissoes
            java.util.List<String> perfis = usuarioPerfilPermissaoService.getPerfis(user.getId());
            java.util.List<String> permissoes = usuarioPerfilPermissaoService.getPermissoes(user.getId());
            LoginJwtResponse resp = new LoginJwtResponse(token, usuarioInfo, perfis, permissoes);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Registra novo usuário", description = "Cria um novo usuário no sistema")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            return ResponseEntity.ok(new LoginResponse("Usuário registrado com sucesso", user.getNome()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        }
    }
}
