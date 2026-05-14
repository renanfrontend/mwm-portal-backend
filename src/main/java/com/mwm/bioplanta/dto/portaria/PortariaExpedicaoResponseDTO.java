package com.mwm.bioplanta.dto.portaria;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "PortariaExpedicaoResponse", description = "Resposta do registro de expedição na portaria")
public class PortariaExpedicaoResponseDTO {

    @Schema(description = "ID do registro de expedição")
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
    @JsonAlias({"placa_manual"})
    private String placa;

    @Schema(description = "Nome do motorista")
    @JsonAlias({"motorista_nome"})
    private String motorista;

    @Schema(description = "CPF/Passaporte")
    @JsonAlias({"cpf_motorista"})
    private String cpfPassaporte;

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
