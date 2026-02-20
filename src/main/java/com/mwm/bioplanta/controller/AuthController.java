package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.ErrorResponse;
import com.mwm.bioplanta.dto.LoginRequest;
import com.mwm.bioplanta.dto.LoginResponse;
import com.mwm.bioplanta.dto.LoginJwtResponse;
import com.mwm.bioplanta.dto.RegisterRequest;
import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de autenticação e registro")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Realiza login", description = "Autentica usuário e retorna token/sessão")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticate(request.getUsername(), request.getPassword());
            // Gera token JWT simples
            String token = com.mwm.bioplanta.util.JwtUtil.generateToken(user.getUsername());
            // Define perfil (ADMIN se username = Admin, senão USER)
            String perfil = "ADMIN";
            if (!"Admin".equalsIgnoreCase(user.getUsername())) {
                perfil = "USER";
            }
            LoginJwtResponse.UsuarioInfo usuarioInfo = new LoginJwtResponse.UsuarioInfo(
                user.getId(), user.getNome(), perfil
            );
            LoginJwtResponse resp = new LoginJwtResponse(token, usuarioInfo);
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
