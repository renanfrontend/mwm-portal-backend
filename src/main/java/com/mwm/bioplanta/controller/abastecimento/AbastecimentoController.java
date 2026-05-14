package com.mwm.bioplanta.controller.abastecimento;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.dto.abastecimento.AbastecimentoDTO;
import com.mwm.bioplanta.dto.abastecimento.AbastecimentoRequestDTO;
import com.mwm.bioplanta.model.Abastecimento;
import com.mwm.bioplanta.service.abastecimento.AbastecimentoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/abastecimento")
@Tag(name = "Abastecimento", description = "Cadastro de abastecimento")
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    private static final Logger logger = LoggerFactory.getLogger(AbastecimentoController.class);

    public AbastecimentoController(AbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @GetMapping
    @Operation(summary = "Obter todas as entradas de abastecimento")
    public List<Abastecimento> listarTodos() {
        return abastecimentoService.listarTodos();
    }

    @PostMapping
    @Operation(
        summary = "Registrar abastecimento",
        description = "Cria um registro de abastecimento com todas as validações e regras de negócio.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplos de requisição para abastecimento",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Abastecimento cadastrado",
                        value = "{\n  \"id_transportadora\": 1,\n \"id_veiculo_transportadora\": 1,\n \"id_usuario\": 1,\n \"id_assinatura\": 1,\n \"pressao_inicial\": 1234.12,\n \"odometro\": 1234567,\n \"frentista\": \"12345678910\",\n  \"criado_em\": \"2026-04-15\",\n  \"hora_inicial\": \"08:07\",\n \"atualizado_em\": \"2026-04-15\",\n \"expirado_em\": \"2026-04-15\",\n  \"status\": \"Em andamento\",\n \"tipo_execucao\": \"Manual\",\n \"pressao_final\": 1234.1,\n \"volume_abastecido\": 1234.12,\n  \"hora_inicial\": \"08:07\"\n}"
                    )
                }
            )
        )
    )
    public ResponseEntity<?> registrarAbastecimento(@RequestBody AbastecimentoRequestDTO request) {
        try {
            logger.info("request registrarAbastecimento: " + request);
            AbastecimentoDTO response = abastecimentoService.registrarAbastecimento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao registrar abastecimento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar abastecimento: " + e.getMessage());
        }
    }
}
