package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bio_filiada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Filiada", description = "Representa uma filiada.")
public class BioFiliada {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_filiada")
    @SequenceGenerator(name = "seq_bio_filiada", sequenceName = "seq_bio_filiada", allocationSize = 1)
    @Schema(description = "Identificador único da filiada", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bio_planta_id", nullable = false)
    @Schema(description = "Planta associada", requiredMode = Schema.RequiredMode.REQUIRED)
    private BioPlanta bioPlanta;

    @Column(name = "codigo_filiada")
    @Schema(description = "Código da filiada", example = "FIL001")
    private String codigoFiliada;

    @Schema(description = "Nome da filiada", example = "PRIMATO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Estado")
    private String estado;

    @Schema(description = "Cidade")
    private String cidade;

     @Column(name = "ativo")
     @Schema(description = "Status ativo (S/N)", example = "S")
     private String ativo = "S";

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}