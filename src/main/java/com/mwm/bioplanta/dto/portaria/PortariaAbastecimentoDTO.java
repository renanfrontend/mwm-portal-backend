package com.mwm.bioplanta.dto.portaria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para Portaria Abastecimento
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PortariaAbastecimento", description = "Dados de abastecimento/coleta com veículo")
public class PortariaAbastecimentoDTO {

    @Schema(description = "ID do abastecimento")
    private Long id;

    @Schema(description = "ID do registro principal")
    private Long registroId;

    @Schema(description = "ID do motorista")
    private Long motoristaId;

    @Schema(description = "Nome do motorista", example = "João Silva")
    private String motoristaNome;

    @Schema(description = "CPF do motorista", example = "123.456.789-00")
    private String cpfMotorista;

    @Schema(description = "ID da transportadora")
    private Long transportadoraId;

    @Schema(description = "Nome manual da transportadora (se não encontrado no cadastro)", example = "Transportadora XYZ")
    private String transportadoraManual;

    @Schema(description = "ID do veículo")
    private Long veiculoId;

    @Schema(description = "Placa do veículo (lookup ou snapshot gravado)", example = "ABC-1234")
    private String placa;

    @Schema(description = "Placa manual (se veículo não encontrado)", example = "ABC-1234")
    private String placaManual;

    @Schema(description = "Tipo de veículo", example = "Caminhão", 
            allowableValues = {"Caminhão", "Carro", "Moto"})
    private String tipoVeiculo;

    @Schema(description = "Peso inicial em kg", example = "5000")
    private Double pesoInicial;

    @Schema(description = "Peso final em kg", example = "4500")
    private Double pesoFinal;

    // Campos extras para compatibilidade frontend (snake_case)
    private Double peso_inicial;
    private Double peso_final;
}
