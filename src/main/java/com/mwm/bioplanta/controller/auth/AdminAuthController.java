package com.mwm.bioplanta.controller.auth;

import com.mwm.bioplanta.dto.auth.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Auth", description = "Endpoints utilitários para hash e validação administrativa.")
public class AdminAuthController {
    @Value("${admin.hash}")
    private String adminHash;

    @Operation(summary = "Valida senha/frase administrativa", description = "Compara a senha/frase enviada com o hash guardado em application.properties (admin.hash). Retorna 200 se válido, 401 se inválido.")

    @PostMapping("/validate")
    public ResponseEntity<Object> validateAdmin(@RequestBody AdminPasswordRequest request) {
        try {
            if (BCrypt.checkpw(request.getPassword(), adminHash)) {
                return ResponseEntity.ok().body("Acesso liberado");
            } else {
                return ResponseEntity.status(401).body(new ErrorResponse("Senha administrativa inválida"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Hash de admin inválido ou corrompido"));
        }
    }

    @Operation(summary = "Gera hash BCrypt para senha/frase", description = "Recebe uma senha/frase e retorna o hash BCrypt correspondente. Use para cadastrar ou atualizar admin.hash.")

    @PostMapping("/hash-password")
    public ResponseEntity<AdminHashResponse> hashPassword(@RequestBody AdminPasswordRequest request) {
        String hash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        return ResponseEntity.ok().body(new AdminHashResponse(hash));
    }

    public static class AdminPasswordRequest {
        private String password;
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AdminHashResponse {
        private String hash;
        public AdminHashResponse(String hash) { this.hash = hash; }
        public String getHash() { return hash; }
    }
}
