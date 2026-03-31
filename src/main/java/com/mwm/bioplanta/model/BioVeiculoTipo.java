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
@Table(name = "bio_veiculo_tipo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Veiculo Tipo", description = "Tipo de veículo")
public class BioVeiculoTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_veiculo_tipo")
    @SequenceGenerator(name = "seq_bio_veiculo_tipo", sequenceName = "seq_bio_veiculo_tipo", allocationSize = 1)
    @Schema(description = "Identificador único do tipo de veículo", example = "1")
    private Long id;

    @Column(name = "label", nullable = false)
    @Schema(description = "Label do tipo de veículo", example = "Truck")
    private String label;

    @Column(name = "valor", nullable = false)
    @Schema(description = "Valor do tipo de veículo", example = "truck")
    private String valor;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
