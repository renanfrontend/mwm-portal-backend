package com.mwm.bioplanta.dto.portaria;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO para Portaria Registro
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "PortariaRegistro", description = "Registro de entrada/saída de pessoas e veículos")
public class PortariaRegistroDTO {

    @Schema(description = "ID único do registro", example = "1")
    private Long id;

    @Schema(description = "Data de entrada", example = "2026-03-24")
    private String dataEntrada;

    @Schema(description = "Hora de entrada", example = "08:00")
    private String horaEntrada;

    @Schema(description = "Tipo do registro", example = "ABASTECIMENTO", 
            allowableValues = {"ABASTECIMENTO", "ENTREGA_DEJETOS", "ENTREGA_INSUMO", "EXPEDICAO", "VISITA"})
    private String tipoRegistro;

    @Schema(description = "Status do registro", example = "Em andamento", 
            allowableValues = {"Em andamento", "Concluído"})
    private String status;

    @Schema(description = "Data de saída (opcional)", example = "2026-03-24")
    private String dataSaida;

    @Schema(description = "Hora de saída (opcional)", example = "17:00")
    private String horaSaida;

    @Schema(description = "Origem da entrada", example = "AGENDADA", 
            allowableValues = {"AGENDADA", "ESPONTANEA"})
    private String origemEntrada;

    @Schema(description = "Observações", example = "Abastecimento realizado sem problemas")
    private String observacoes;

    @Schema(description = "ID do responsável")
    private Long responsavelId;

    @Schema(description = "ID da agenda planejada")
    private Long agendaRealizadaId;

    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;

    // Dados específicos por tipo (polimorfismo)
    @Schema(description = "Dados de Abastecimento (quando tipoRegistro = ABASTECIMENTO)")
    private PortariaAbastecimentoDTO abastecimento;

    @Schema(description = "Dados de Visita (quando tipoRegistro = VISITA)")
    private PortariaVisitaDTO visita;

    @Schema(description = "Dados de Entrega de Dejetos (quando tipoRegistro = ENTREGA_DEJETOS)")
    private PortariaEntregaDejetosDTO.EntregaDejetosDTO entrega_dejetos;

    @Schema(description = "Dados de Entrega de Insumo (quando tipoRegistro = ENTREGA_INSUMO)")
    private java.util.Map<String, Object> entrega_insumo;

        @Schema(description = "Dados de Expedição (quando tipoRegistro = EXPEDICAO)")
        private PortariaExpedicaoResponseDTO expedicao;

    // Outros tipos podem ser adicionados conforme necessário
}
