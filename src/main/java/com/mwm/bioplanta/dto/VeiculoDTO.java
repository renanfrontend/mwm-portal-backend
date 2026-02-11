package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Veículo DTO", description = "Dados de um veículo de transportadora")
public class VeiculoDTO {

    @Schema(description = "Tipo do veículo", example = "Caminhão Truck", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    @Schema(description = "Capacidade do veículo", example = "16000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String capacidade;

    @Schema(description = "Placa do veículo", example = "ABC-1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String placa;

    @Schema(description = "Tipo de abastecimento", example = "Diesel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoAbastecimento;
}
