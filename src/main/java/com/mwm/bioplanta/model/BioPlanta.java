package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bio_planta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Planta", description = "Representa uma planta.")
public class BioPlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_planta")
    @SequenceGenerator(name = "seq_bio_planta", sequenceName = "seq_bio_planta", allocationSize = 1)
    @Schema(description = "Identificador único da planta", example = "1")
    private Long id;

    @Schema(description = "Nome da planta", example = "Planta Toledo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name = "codigo_interno")
    @Schema(description = "Código interno da planta", example = "0013", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigoInterno;

     @Column(name = "status")
     @Schema(description = "Status da planta (A/I)", example = "A")
     private String status = "A";

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}