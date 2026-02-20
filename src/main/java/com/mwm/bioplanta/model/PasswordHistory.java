package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_historico_senha")
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_historico_senha")
    @SequenceGenerator(name = "seq_bio_historico_senha", sequenceName = "seq_bio_historico_senha", allocationSize = 1)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long userId;

    @Column(name = "senha_hash", nullable = false, length = 64)
    private String passwordHash;

    @Column(name = "criado_em")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
