package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.service.portaria.PortariaEntregaDejetosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portaria/entrega_de_dejetos")
@Tag(name = "Portaria - Entrega de Dejetos", description = "Cadastro de entrega de dejetos na portaria")
public class PortariaEntregaDejetosController {


    private final PortariaEntregaDejetosService entregaDejetosService;

    public PortariaEntregaDejetosController(PortariaEntregaDejetosService entregaDejetosService) {
        this.entregaDejetosService = entregaDejetosService;
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter entrega de dejetos por ID",
        description = "Retorna os dados completos de uma entrega de dejetos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PortariaEntregaDejetosDTO.class))),
        @ApiResponse(responseCode = "404", description = "Entrega não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Object> obterEntregaDeDejetos(
            @Parameter(description = "ID da entrega de dejetos", example = "37", required = true)
            @PathVariable Long id) {
        try {
            var result = entregaDejetosService.obterEntregaDeDejetos(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(
        summary = "Registrar entrega de dejetos",
        description = "Cria todos os registros necessários para entrega de dejetos na portaria",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplo de requisição para entrega de dejetos",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n  \"tipoRegistro\": \"Entrega de dejetos\",\n  \"data_entrada\": \"2026-04-01\",\n  \"hora_entrada\": \"10:42\",\n  \"data_saida\": \"2026-04-01\",\n  \"hora_saida\": \"14:30\",\n  \"observacoes\": \"Observações gerais\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"entrega_dejetos\": {\n    \"produtor_id\": \"23\",\n    \"motorista_nome\": \"João Silva\",\n    \"cpf_motorista\": \"123.456.789-00\",\n    \"motorista_id\": \"123\",\n    \"transportadora_id\": \"24\",\n    \"transportadora_manual\": null,\n    \"veiculo_id\": \"97\",\n    \"placa_manual\": \"BBW 8C54\",\n    \"tipo_veiculo\": \"Carreta\",\n    \"peso_inicial\": 20000,\n    \"peso_final\": 15000,\n    \"densidade\": \"1025\"\n  }\n}"
                )
            )
        )
    )
    public ResponseEntity<Object> registrarEntregaDeDejetos(@RequestBody PortariaEntregaDejetosDTO dto) {
        try {
            var result = entregaDejetosService.registrarEntregaDeDejetos(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
