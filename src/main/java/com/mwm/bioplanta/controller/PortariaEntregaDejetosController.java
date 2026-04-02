package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.service.PortariaEntregaDejetosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portaria/entrega_de_dejetos")
@Tag(name = "Portaria - Entrega de Dejetos", description = "Cadastro de entrega de dejetos na portaria")
public class PortariaEntregaDejetosController {


    private final PortariaEntregaDejetosService entregaDejetosService;

    public PortariaEntregaDejetosController(PortariaEntregaDejetosService entregaDejetosService) {
        this.entregaDejetosService = entregaDejetosService;
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
