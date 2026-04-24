package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaEntregaInsumoRequestDTO;
import com.mwm.bioplanta.dto.portaria.PortariaEntregaInsumoResponseDTO;
import com.mwm.bioplanta.dto.portaria.entregaInsumo.PortariaEntregaInsumoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.service.portaria.PortariaEntregaInsumoService;
import com.mwm.bioplanta.service.portaria.entregaInsumo.PortariaEntregaInsumoExclusaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bio-portaria-entrega-insumo")
@Tag(name = "Portaria - Entrega de Insumo", description = "Cadastro de entrega de insumo na portaria")
public class PortariaEntregaInsumoController {

    private final PortariaEntregaInsumoService entregaInsumoService;
    private final PortariaEntregaInsumoExclusaoService entregaInsumoExclusaoService;

    @Autowired
    public PortariaEntregaInsumoController(
            PortariaEntregaInsumoService entregaInsumoService,
            PortariaEntregaInsumoExclusaoService entregaInsumoExclusaoService) {
        this.entregaInsumoService = entregaInsumoService;
        this.entregaInsumoExclusaoService = entregaInsumoExclusaoService;
    }

    @PostMapping
    @Operation(
        summary = "Registrar entrega de insumo na portaria",
        description = "Cria um registro de entrega de insumo na portaria com todas as validações e regras de negócio.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplos de requisição para entrega de insumo",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Transportadora cadastrada",
                        value = "{\n  \"tipoRegistro\": \"ENTREGA_INSUMO\",\n  \"data_entrada\": \"2026-04-23\",\n  \"hora_entrada\": \"08:07\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"entrega_insumo\": {\n    \"motorista_nome\": \"João Silva\",\n    \"cpf_motorista\": \"12345678901\",\n    \"motorista_id\": null,\n    \"transportadora_id\": 23,\n    \"transportadora_manual\": null,\n    \"veiculo_id\": 128,\n    \"placa\": \"ABC-1234\",\n    \"placa_manual\": null,\n    \"tipo_veiculo\": \"Carreta\",\n    \"peso_inicial\": 10000,\n    \"peso_final\": 20000,\n    \"empresa\": \"Empresa XYZ\",\n    \"nota_fiscal\": \"12345\"\n  }\n}"
                    ),
                    @ExampleObject(
                        name = "Outros",
                        value = "{\n  \"tipoRegistro\": \"ENTREGA_INSUMO\",\n  \"data_entrada\": \"2026-04-23\",\n  \"hora_entrada\": \"08:07\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"entrega_insumo\": {\n    \"motorista_nome\": \"João Silva\",\n    \"cpf_motorista\": \"12345678901\",\n    \"motorista_id\": null,\n    \"transportadora_id\": null,\n    \"transportadora_manual\": \"Transportes XPTO\",\n    \"veiculo_id\": null,\n    \"placa\": null,\n    \"placa_manual\": \"XYZ-5678\",\n    \"tipo_veiculo\": \"Caminhão\",\n    \"peso_inicial\": 10000,\n    \"peso_final\": 20000,\n    \"empresa\": \"Empresa XYZ\",\n    \"nota_fiscal\": \"12345\"\n  }\n}"
                    )
                }
            )
        )
    )
    public ResponseEntity<?> registrarEntregaInsumo(@RequestBody PortariaEntregaInsumoRequestDTO request) {
        try {
            PortariaEntregaInsumoResponseDTO response = entregaInsumoService.registrarEntregaInsumo(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar entrega de insumo: " + e.getMessage());
        }
    }

    @PostMapping("/excluir")
    @Operation(
        summary = "Excluir entrega de insumo da portaria",
        description = "Exclui o registro principal da portaria, a entrega de insumo associada e, quando aplicável, os cadastros manuais de transporte."
    )
    public ResponseEntity<ExclusaoPortariaResponseDTO> excluirEntregaInsumo(
            @RequestBody PortariaEntregaInsumoExclusaoRequestDTO request) {
        return ResponseEntity.ok(entregaInsumoExclusaoService.excluir(request));
    }
}