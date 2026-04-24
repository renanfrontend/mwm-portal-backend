package com.mwm.bioplanta.dto.portaria;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "PortariaEntregaInsumoResponse", description = "Resposta do registro de entrega de insumo na portaria")
public class PortariaEntregaInsumoResponseDTO {
    @Schema(description = "ID do registro de entrega de insumo")
    private Long id;
    @Schema(description = "ID do registro principal")
    private Long registroId;
    @Schema(description = "ID da transportadora")
    private Long transportadoraId;
    @Schema(description = "ID do veículo")
    private Long veiculoId;
    @Schema(description = "ID do usuário")
    private Long usuarioId;
    @Schema(description = "Data de entrada")
    private String dataEntrada;
    @Schema(description = "Horário de entrada")
    private String horarioEntrada;
    @Schema(description = "Atividade")
    private String atividade;
    @Schema(description = "Nome manual da transportadora")
    private String transportadoraManual;
    @Schema(description = "Tipo de veículo")
    private String tipoVeiculo;
    @Schema(description = "Placa")
    private String placa;
    @Schema(description = "Nome do motorista")
    private String motorista;
    @Schema(description = "CPF/Passaporte")
    private String cpfPassaporte;
    @Schema(description = "Empresa")
    private String empresa;
    @Schema(description = "Nota fiscal")
    private String notaFiscal;
    @Schema(description = "Peso inicial")
    private Double pesoInicial;
    @Schema(description = "Peso final")
    private Double pesoFinal;
    @Schema(description = "Data de saída")
    private String dataSaida;
    @Schema(description = "Horário de saída")
    private String horarioSaida;
    @Schema(description = "Observação")
    private String observacao;
    @Schema(description = "Data de criação")
    private String criadoEm;
    @Schema(description = "Data de atualização")
    private String atualizadoEm;
}