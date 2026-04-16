package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoDTO;
import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoRequestDTO;
import com.mwm.bioplanta.service.portaria.PortariaAbastecimentoService;
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
@RequestMapping("/api/portaria/abastecimento")
@Tag(name = "Portaria - Abastecimento", description = "Cadastro de abastecimento na portaria")
public class PortariaAbastecimentoController {

    private final PortariaAbastecimentoService abastecimentoService;

    @Autowired
    public PortariaAbastecimentoController(PortariaAbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @PostMapping
    @Operation(
        summary = "Registrar abastecimento na portaria",
        description = "Cria um registro de abastecimento na portaria com todas as validações e regras de negócio.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplos de requisição para abastecimento",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Transportadora cadastrada",
                        value = "{\n  \"tipoRegistro\": \"ABASTECIMENTO\",\n  \"data_entrada\": \"2026-04-15\",\n  \"hora_entrada\": \"08:07\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"abastecimento\": {\n    \"motorista_nome\": \"marcos\",\n    \"cpf_motorista\": \"07602015525\",\n    \"motorista_id\": null,\n    \"transportadora_id\": 23,\n    \"transportadora_manual\": null,\n    \"veiculo_id\": 128,\n    \"placa\": \"ABC-1D23\",\n    \"placa_manual\": null,\n    \"tipo_veiculo\": \"Carreta\",\n    \"peso_inicial\": 0,\n    \"peso_final\": 0\n  }\n}"
                    ),
                    @ExampleObject(
                        name = "Outros",
                        value = "{\n  \"tipoRegistro\": \"ABASTECIMENTO\",\n  \"data_entrada\": \"2026-04-15\",\n  \"hora_entrada\": \"08:07\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"abastecimento\": {\n    \"motorista_nome\": \"marcos\",\n    \"cpf_motorista\": \"07602015525\",\n    \"motorista_id\": null,\n    \"transportadora_id\": null,\n    \"transportadora_manual\": \"Transportes XPTO\",\n    \"veiculo_id\": null,\n    \"placa_manual\": \"FSR-8I99\",\n    \"tipo_veiculo\": \"Carreta\",\n    \"peso_inicial\": 0,\n    \"peso_final\": 0\n  }\n}"
                    )
                }
            )
        )
    )
    public ResponseEntity<?> registrarAbastecimento(@RequestBody PortariaAbastecimentoRequestDTO request) {
        try {
            PortariaAbastecimentoDTO response = abastecimentoService.registrarAbastecimento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar abastecimento: " + e.getMessage());
        }
    }
}
