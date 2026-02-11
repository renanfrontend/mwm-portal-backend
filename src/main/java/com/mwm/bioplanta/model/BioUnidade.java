package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_unidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Unidade", description = "Representa uma unidade bio.")
public class BioUnidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bio_unidade_seq")
    @SequenceGenerator(name = "bio_unidade_seq", sequenceName = "bio_unidade_seq", allocationSize = 1)
    @Schema(description = "Identificador único da unidade", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Nome da unidade", example = "Toledo - PR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(length = 255)
    @Schema(description = "Município da unidade", example = "Toledo")
    private String municipio;

    @Column(length = 2)
    @Schema(description = "Estado da unidade", example = "PR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estado;

    @Column(name = "tecnico_responsavel", length = 255)
    @Schema(description = "Técnico responsável", example = "João Silva")
    private String tecnicoResponsavel;

    @Column(name = "telefone_tecnico", length = 20)
    @Schema(description = "Telefone do técnico", example = "(45) 99999-9999")
    private String telefoneTecnico;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00")
    private LocalDateTime atualizadoEm;
}