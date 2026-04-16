package com.mwm.bioplanta.dto.portaria;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PortariaAbastecimentoRequest", description = "Requisição para registro de abastecimento na portaria")
public class PortariaAbastecimentoRequestDTO {
    @Schema(description = "Tipo do registro (sempre 'ABASTECIMENTO')", example = "ABASTECIMENTO", required = true)
    private String tipoRegistro;
    @Schema(description = "Data de entrada (yyyy-MM-dd)", example = "2026-04-14", required = true)
    private String data_entrada;
    @Schema(description = "Hora de entrada (HH:mm)", example = "08:30", required = true)
    private String hora_entrada;
    @Schema(description = "Data de saída (yyyy-MM-dd)", example = "2026-04-14")
    private String data_saida;
    @Schema(description = "Hora de saída (HH:mm)", example = "09:00")
    private String hora_saida;
    @Schema(description = "Observações")
    private String observacoes;
    @Schema(description = "Status", required = true)
    private String status;
    @Schema(description = "Origem da entrada", required = true)
    private String origem_entrada;
    @Schema(description = "Objeto de abastecimento", required = true)
    private AbastecimentoDTO abastecimento;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AbastecimentoDTO {
        @Schema(description = "Nome do motorista", required = true)
        private String motorista_nome;
        @Schema(description = "CPF do motorista", required = true)
        private String cpf_motorista;
        @Schema(description = "ID do motorista")
        private Long motorista_id;
        @Schema(description = "ID da transportadora")
        private Long transportadora_id;
        @Schema(description = "Nome manual da transportadora (se 'Outros')")
        private String transportadora_manual;
        @Schema(description = "ID do veículo")
        private Long veiculo_id;
        @Schema(description = "Placa do veículo para gravar também na portaria")
        private String placa;
        @Schema(description = "Placa manual (se 'Outros')")
        private String placa_manual;
        @Schema(description = "Tipo do veículo", required = true)
        private String tipo_veiculo;
        @Schema(description = "Peso inicial")
        private Double peso_inicial;
        @Schema(description = "Peso final")
        private Double peso_final;
    }
}
