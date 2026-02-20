package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_config_senha")
public class PasswordConfig {
    @Id
    private Long id;

    @Column(name = "dias_validade")
    private Integer diasValidade;

    @Column(name = "tamanho_minimo")
    private Integer tamanhoMinimo;

    @Column(name = "historico_qtd")
    private Integer historicoQtd;

    @Column(length = 1)
    private String status;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Long getId() { return id; }
    public Integer getDiasValidade() { return diasValidade; }
    public Integer getTamanhoMinimo() { return tamanhoMinimo; }
    public Integer getHistoricoQtd() { return historicoQtd; }
    public String getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
