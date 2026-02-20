package com.mwm.bioplanta.service;

import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {
    private final UserRepository userRepository;
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (user.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Senha expirada. Solicite uma nova senha ao administrador.");
        }
        if (!BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Senha inválida");
        }
        return user;
    }
}
