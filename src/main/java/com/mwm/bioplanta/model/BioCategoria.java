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
@Table(name = "bio_categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Categoria", description = "Categoria de transportadora")
public class BioCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_categoria")
    @SequenceGenerator(name = "seq_bio_categoria", sequenceName = "seq_bio_categoria", allocationSize = 1)
    @Schema(description = "Identificador único da categoria", example = "1")
    private Long id;

    @Column(name = "label", nullable = false)
    @Schema(description = "Label da categoria", example = "Logística")
    private String label;

    @Column(name = "\"value\"", nullable = false)
    @Schema(description = "Valor da categoria", example = "Logística")
    private String value;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
