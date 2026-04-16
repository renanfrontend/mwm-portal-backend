package com.mwm.bioplanta.dto.portaria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO para Portaria Visita
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PortariaVisita", description = "Dados de visita a estabelecimento")
public class PortariaVisitaDTO {

    @Schema(description = "ID da visita")
    private Long id;

    @Schema(description = "ID do registro principal")
    private Long registroId;

    @Schema(description = "ID do visitante")
    private Long visitanteId;

    @Schema(description = "Nome do visitante", example = "Maria Santos")
    private String visitanteNome;

    @Schema(description = "Documento do visitante (CPF ou Passaporte)", example = "123.456.789-00")
    private String documentoVisitante;

    @Schema(description = "Tipo de documento", example = "CPF", allowableValues = {"CPF", "Passaporte"})
    private String tipoDocumento;

    @Schema(description = "ID do motivo da visita")
    private Long motivoVisitaId;

    @Schema(description = "Nome do motivo da visita (lookup)", example = "Inspeção técnica")
    private String motivoVisitaNome;

    @Schema(description = "Motivo manual (se não encontrado no cadastro)", example = "Visita técnica")
    private String motivoManual;

    @Schema(description = "ID do veículo")
    private Long veiculoId;

    @Schema(description = "Placa manual (se veículo não encontrado)", example = "ABC-1234")
    private String placaManual;

    @Schema(description = "Placa do veículo (lookup)", example = "ABC-1234")
    private String placa;

    @Schema(description = "Tipo de veículo", example = "Carro", 
            allowableValues = {"Caminhão", "Carro", "Moto"})
    private String tipoVeiculo;

    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
