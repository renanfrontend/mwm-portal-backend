package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bio_veiculo_transportadora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Veiculo Transportadora", description = "Representa um veículo de transportadora.")
public class BioVeiculoTransportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_veiculo_transportadora")
    @SequenceGenerator(name = "seq_veiculo_transportadora", sequenceName = "seq_veiculo_transportadora", allocationSize = 1)
    @Schema(description = "Identificador único do veículo", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transportadora_id", nullable = false)
    @Schema(description = "Transportadora associada", requiredMode = Schema.RequiredMode.REQUIRED)
    private BioTransportadora bioTransportadora;

    @Schema(description = "Tipo do veículo", example = "Caminhão Truck", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    @Schema(description = "Capacidade do veículo", example = "14ton", requiredMode = Schema.RequiredMode.REQUIRED)
    private String capacidade;

    @Schema(description = "Placa do veículo", example = "ABC1234")
    private String placa;

    @Column(name = "tipo_abastecimento")
    @Schema(description = "Tipo de abastecimento", example = "Diesel")
    private String tipoAbastecimento;

    @Schema(description = "TAG do veículo (obrigatório para Biometano)", example = "AABBCC1122334455")
    private String tag;

    @Column(name = "status", columnDefinition = "NVARCHAR(MAX)")
    @Schema(description = "Status do veículo", example = "Ativo")
    private String status = "Ativo";

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}