package com.mwm.bioplanta.dto.portaria;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PortariaEntregaInsumoRequest", description = "Requisição para registro de entrega de insumo na portaria")
public class PortariaEntregaInsumoRequestDTO {
    @Schema(description = "Tipo do registro (sempre 'ENTREGA_INSUMO')", example = "ENTREGA_INSUMO")
    private String tipoRegistro;
    @Schema(description = "Data de entrada (yyyy-MM-dd)", example = "2026-04-23")
    private String data_entrada;
    @Schema(description = "Hora de entrada (HH:mm)", example = "08:30")
    private String hora_entrada;
    @Schema(description = "Data de saída (yyyy-MM-dd)", example = "2026-04-23")
    private String data_saida;
    @Schema(description = "Hora de saída (HH:mm)", example = "09:15")
    private String hora_saida;
    @Schema(description = "Observações")
    private String observacoes;
    @Schema(description = "Status", example = "Em andamento")
    private String status;
    @Schema(description = "Origem da entrada", example = "ESPONTANEA")
    private String origem_entrada;
    @Schema(description = "Dados da entrega de insumo")
    private EntregaInsumoDTO entrega_insumo;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EntregaInsumoDTO {
        @Schema(description = "Nome do motorista")
        private String motorista_nome;
        @Schema(description = "CPF do motorista")
        private String cpf_motorista;
        @Schema(description = "ID do motorista")
        private Long motoristamotorista_id;
        @Schema(description = "ID da transportadora")
        private Long transportadora_id;
        @Schema(description = "Nome manual da transportadora (se 'Outros')")
        private String transportadora_manual;
        @Schema(description = "ID do veículo")
        private Long veiculo_id;
        @Schema(description = "Placa do veículo")
        private String placa;
        @Schema(description = "Placa manual (se 'Outros')")
        private String placa_manual;
        @Schema(description = "Tipo do veículo")
        private String tipo_veiculo;
        @Schema(description = "Peso inicial")
        private Double peso_inicial;
        @Schema(description = "Peso final")
        private Double peso_final;
        @Schema(description = "Empresa")
        private String empresa;
        @Schema(description = "Nota fiscal")
        private String nota_fiscal;
    }
}