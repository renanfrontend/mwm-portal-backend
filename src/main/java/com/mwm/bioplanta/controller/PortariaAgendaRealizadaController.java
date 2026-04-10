package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.AgendaRealizadaSemanalDTO;
import com.mwm.bioplanta.service.PortariaAgendaRealizadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ============================================================================
 * CONTROLLER: Portaria Agenda Realizada
 * ============================================================================
 * Responsabilidade: Gerenciar endpoints relacionados à agenda realizada
 * 
 * Endpoint:
 * GET /api/portaria/agenda-realizada/semanal?dataInicio=2026-04-05&dataFim=2026-04-11
 * 
 * Retorna: Grid/Relatório de agenda realizada por semana
 * 
 * @author Sistema de Portaria
 * @date 2026-04-09
 */
@RestController
@RequestMapping("/api/portaria/agenda-realizada")
@Tag(name = "Portaria - Agenda Realizada", description = "Relatórios de agenda realizada")
public class PortariaAgendaRealizadaController {

    private final PortariaAgendaRealizadaService agendaRealizadaService;

    public PortariaAgendaRealizadaController(PortariaAgendaRealizadaService agendaRealizadaService) {
        this.agendaRealizadaService = agendaRealizadaService;
    }

    /**
     * ============================================================================
     * ENDPOINT: Gerar Agenda Realizada Semanal
     * ============================================================================
     * Responsabilidade: Retornar grid/relatório de agenda realizada para uma semana
     * 
     * Método: GET
     * URL: /api/portaria/agenda-realizada/semanal
     * 
     * Parâmetros:
     *   - dataInicio: Data inicial do período (formato: yyyy-MM-dd)
     *   - dataFim: Data final do período (formato: yyyy-MM-dd)
     * 
     * Exemplo:
     *   GET /api/portaria/agenda-realizada/semanal?dataInicio=2026-04-05&dataFim=2026-04-11
     * 
     * Retorno:
     *   - Status 200: Lista de linhas do grid (uma por produtor)
     *   - Status 400: Erro de validação de datas
     *   - Status 500: Erro interno do servidor
     * 
     * Estrutura do retorno:
     * [
     *   {
     *     "codigoProdutor": "1772470242918",
     *     "nomeProduto": "ADEMIR JOSE ENGELSING",
     *     "distanciaKm": 24.0,
     *     "transportadoraNome": "PRIMATO",
     *     "domingo": 0,
     *     "segunda": 3,
     *     "terca": 3,
     *     "quarta": 0,
     *     "quinta": 0,
     *     "sexta": 3,
     *     "sabado": 0,
     *     "totalEntregas": 9,
     *     "totalKm": 216.0
     *   }
     * ]
     */
    @GetMapping("/semanal")
    @Operation(
        summary = "Gerar Agenda Realizada Semanal",
        description = "Retorna um grid/relatório de agenda realizada para uma semana específica, " +
                      "contando entregas de dejetos por dia e por produtor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Relatório gerado com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = AgendaRealizadaSemanalDTO.class))
            )
        ),
        @ApiResponse(responseCode = "400", description = "Erro na validação das datas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Object> gerarAgendaRealizadaSemanal(
            @Parameter(
                description = "Data inicial do período (formato: yyyy-MM-dd)",
                example = "2026-04-05",
                required = true
            )
            @RequestParam String dataInicio,
            
            @Parameter(
                description = "Data final do período (formato: yyyy-MM-dd)",
                example = "2026-04-11",
                required = true
            )
            @RequestParam String dataFim) {
        try {
            // Converter strings para LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inicio = LocalDate.parse(dataInicio, formatter);
            LocalDate fim = LocalDate.parse(dataFim, formatter);
            
            // Validar se dataInicio é menor que dataFim
            if (inicio.isAfter(fim)) {
                return ResponseEntity.badRequest().body("dataInicio não pode ser maior que dataFim");
            }
            
            // Chamar o serviço para gerar o relatório
            List<AgendaRealizadaSemanalDTO> resultado = agendaRealizadaService.gerarAgendaRealizadaSemanal(inicio, fim);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            // Erro ao parsear as datas ou erro interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
