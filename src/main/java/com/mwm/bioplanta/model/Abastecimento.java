package com.mwm.bioplanta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bio_abastecimento")
@Data
public class Abastecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_abastecimento")
    @SequenceGenerator(name = "seq_bio_abastecimento", sequenceName = "seq_bio_abastecimento", allocationSize = 1)
    private Long id;

    @Column(name = "transportadora_id")
    private Long id_transportadora;

    @Column(name = "veiculo_transportadora_id")
    private Long id_veiculo_transportadora;

    @Column(name = "usuario_id")
    private Long id_usuario;

    @Column(name = "assinatura_id")
    private Long id_assinatura;

    @Column(name = "pressao_inicial")
    private Double pressao_inicial;

    @Column(name = "odometro")
    private Integer odometro;

    @Column(name = "frentista")
    private String frentista;

    @Column(name = "criado_em")
    private LocalDateTime criado_em;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizado_em;

    @Column(name = "expirado_em")
    private LocalDateTime expirado_em;

    @Column(name = "hora_inicial")
    private LocalTime hora_incial;

    @Column(name = "status")
    private String status;

    @Column(name = "tipo_execucao")
    private String tipo_execucao;

    @Column(name = "pressao_final")
    private Double pressao_final;

    @Column(name = "volume_abastecido")
    private Double volume_abastecido;

    @Column(name = "hora_final")
    private LocalTime hora_final;
}
