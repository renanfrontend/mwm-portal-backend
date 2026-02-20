package com.mwm.bioplanta.service;

import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.model.PasswordHistory;
import com.mwm.bioplanta.repository.UserRepository;
import com.mwm.bioplanta.repository.PasswordHistoryRepository;
import com.mwm.bioplanta.util.PasswordGenerator;
import com.mwm.bioplanta.model.PasswordConfig;
import com.mwm.bioplanta.repository.PasswordConfigRepository;
import com.mwm.bioplanta.dto.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordConfigRepository passwordConfigRepository;

    public UserService(UserRepository userRepository, PasswordHistoryRepository passwordHistoryRepository, PasswordConfigRepository passwordConfigRepository) {
        this.userRepository = userRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.passwordConfigRepository = passwordConfigRepository;
    }
    
    @Transactional
    public String generateAndSetNewPassword(Long userId) {
        User user = userRepository.findById(java.util.Objects.requireNonNull(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        List<PasswordHistory> last5 = passwordHistoryRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
        String newPassword = null;
        String newHash = null;
        int tries = 0;
        boolean isUnique;
        do {
            newPassword = PasswordGenerator.generate(8);
            newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            tries++;
            isUnique = true;
            for (PasswordHistory h : last5) {
                if (BCrypt.checkpw(newPassword, h.getPasswordHash())) {
                    isUnique = false;
                    break;
                }
            }
            if (tries > 10) throw new IllegalStateException("Não foi possível gerar senha única. Tente novamente.");
        } while (!isUnique);
        // Salva senha atual no histórico
        PasswordHistory hist = new PasswordHistory();
        hist.setUserId(userId);
        hist.setPasswordHash(user.getPasswordHash());
        hist.setCreatedAt(java.time.LocalDateTime.now());
        passwordHistoryRepository.save(hist);
        // Atualiza usuário
        user.setPasswordHash(newHash);
        user.setExpiryDate(LocalDate.now().plusDays(60));
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
        // Mantém só os 5 mais recentes
        List<PasswordHistory> all = passwordHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (all.size() > 5) {
            all.stream().skip(5).forEach(passwordHistoryRepository::delete);
        }
        return newPassword;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        String hash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        user.setPasswordHash(hash);
        user.setExpiryDate(LocalDate.now().plusDays(60));
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        User saved = userRepository.save(user);
        savePasswordHistory(saved, hash);
        return saved;
    }

    // Salva senha no histórico
    private void savePasswordHistory(User user, String passwordHash) {
        com.mwm.bioplanta.model.PasswordHistory hist = new com.mwm.bioplanta.model.PasswordHistory();
        hist.setUserId(user.getId());
        hist.setPasswordHash(passwordHash);
        hist.setCreatedAt(java.time.LocalDateTime.now());
        passwordHistoryRepository.save(hist);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // LOG DE DEPURAÇÃO TEMPORÁRIO
        System.out.println("[DEBUG LOGIN] username recebido: '" + username + "'");
        System.out.println("[DEBUG LOGIN] username banco: '" + user.getUsername() + "'");
        System.out.println("[DEBUG LOGIN] hash banco: '" + user.getPasswordHash() + "'");
        boolean senhaOk = BCrypt.checkpw(password, user.getPasswordHash());
        System.out.println("[DEBUG LOGIN] senha correta? " + senhaOk);
        if (!senhaOk) {
            throw new RuntimeException("Senha incorreta");
        }
        checkPasswordExpiry(user);
        return user;
    }

    // Checa se a senha está expirada
    private void checkPasswordExpiry(User user) {
        // A senha expira se a data de validade já passou
        if (user.getExpiryDate() != null) {
            if (java.time.LocalDate.now().isAfter(user.getExpiryDate())) {
                throw new RuntimeException("Senha expirada, é necessário trocar a senha");
            }
        }
    }
}
