
package com.mwm.bioplanta.controller.agenda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaDiaRequestDTO;
import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaSemanaResponseDTO;
import com.mwm.bioplanta.dto.agenda.CopiarAgendaRequestDTO;
import com.mwm.bioplanta.dto.agenda.LimparAgendaRequestDTO;
import com.mwm.bioplanta.service.agenda.AgendaPlanejadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/agenda/planejado")
@Tag(name = "Agenda Planejada", description = "Gerenciamento da agenda planejada.")

public class AgendaPlanejadaController {


    private static final Logger logger = LoggerFactory.getLogger(AgendaPlanejadaController.class);

    private final AgendaPlanejadaService agendaPlanejadaService;

    public AgendaPlanejadaController(AgendaPlanejadaService agendaPlanejadaService) {
        this.agendaPlanejadaService = agendaPlanejadaService;
    }

    @GetMapping("/test")
    @Operation(summary = "Teste se o controller está carregado")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("AgendaPlanejadaController funcionando");
    }

    @GetMapping("/semana")
    @Operation(summary = "Carregar agenda planejada da semana")
    public ResponseEntity<AgendaPlanejadaSemanaResponseDTO> carregarSemana(
            @RequestParam Long idBioplanta,
            @RequestParam Long idFiliada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            logger.info("Iniciando carregarSemana: idBioplanta={}, idFiliada={}", idBioplanta, idFiliada);
            AgendaPlanejadaSemanaResponseDTO response = agendaPlanejadaService
                    .carregarSemana(idBioplanta, idFiliada, dataInicio, dataFim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro em carregarSemana: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/dia")
    @Operation(summary = "Salvar agenda planejada do dia")
    public ResponseEntity<Void> salvarDia(@RequestBody AgendaPlanejadaDiaRequestDTO dto) {
        try {
            agendaPlanejadaService.salvarDia(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro em salvarDia: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/copiar")
    @Operation(summary = "Copiar planejamento de uma semana para outra")
    public ResponseEntity<Void> copiarSemana(@RequestBody CopiarAgendaRequestDTO dto) {
        try {
            agendaPlanejadaService.copiarSemana(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro em copiarSemana: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/limpar-semana")
    @Operation(summary = "Limpar planejamento da semana")
    public ResponseEntity<Void> limparSemana(@RequestBody LimparAgendaRequestDTO dto) {
        try {
            agendaPlanejadaService.limparSemana(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erro em limparSemana: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/existe-dados")
    @Operation(summary = "Verificar se existem dados para a semana (trava de navegação)")
    public ResponseEntity<Boolean> verificarExistenciaDados(
            @RequestParam Long idBioplanta,
            @RequestParam Long idFiliada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            boolean existe = agendaPlanejadaService.verificarDadosSemana(idBioplanta, idFiliada, dataInicio, dataFim);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            logger.error("Erro em verificarExistenciaDados: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build(); // ou retornar false? Melhor badRequest se erro tecnico
        }
    }
}
