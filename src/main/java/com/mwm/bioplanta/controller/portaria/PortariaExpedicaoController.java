package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaExpedicaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.PortariaExpedicaoResponseDTO;
import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.dto.portaria.expedicao.PortariaExpedicaoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.service.portaria.PortariaExpedicaoService;
import com.mwm.bioplanta.service.portaria.PortariaRegistroService;
import com.mwm.bioplanta.service.portaria.expedicao.PortariaExpedicaoExclusaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/api/bio-portaria-expedicao")
@Tag(name = "Portaria - Expedição", description = "Cadastro de expedição na portaria")
public class PortariaExpedicaoController {

    private final PortariaExpedicaoService expedicaoService;
    private final PortariaExpedicaoExclusaoService expedicaoExclusaoService;
    private final PortariaRegistroService registroService;

    @Autowired
    public PortariaExpedicaoController(
            PortariaExpedicaoService expedicaoService,
            PortariaExpedicaoExclusaoService expedicaoExclusaoService,
            PortariaRegistroService registroService) {
        this.expedicaoService = expedicaoService;
        this.expedicaoExclusaoService = expedicaoExclusaoService;
        this.registroService = registroService;
    }

    @PostMapping
    @Operation(
        summary = "Registrar expedição na portaria",
        description = "Cria um registro de expedição na portaria. transportadora_id e veiculo_id são opcionais — quando 'Outros', usar transportadora_manual e placa_manual.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplos de requisição para expedição",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Transportadora cadastrada",
                        value = "{\n  \"tipoRegistro\": \"EXPEDICAO\",\n  \"data_entrada\": \"2026-05-07\",\n  \"hora_entrada\": \"08:30\",\n  \"data_saida\": \"2026-05-07\",\n  \"hora_saida\": \"12:00\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"expedicao\": {\n    \"motorista_nome\": \"João Silva\",\n    \"cpf_motorista\": \"123.456.789-00\",\n    \"transportadora_id\": 5,\n    \"veiculo_id\": 12,\n    \"placa\": \"ABC1D23\",\n    \"tipo_veiculo\": \"Caminhão\",\n    \"nota_fiscal\": \"NF-001\",\n    \"peso_inicial\": 10.500,\n    \"peso_final\": 8.200\n  }\n}"
                    ),
                    @ExampleObject(
                        name = "Outros (manual)",
                        value = "{\n  \"tipoRegistro\": \"EXPEDICAO\",\n  \"data_entrada\": \"2026-05-07\",\n  \"hora_entrada\": \"08:30\",\n  \"status\": \"Em andamento\",\n  \"origem_entrada\": \"ESPONTANEA\",\n  \"observacoes\": \"\",\n  \"expedicao\": {\n    \"motorista_nome\": \"Carlos Souza\",\n    \"cpf_motorista\": \"987.654.321-00\",\n    \"transportadora_manual\": \"Transportes Silva\",\n    \"placa_manual\": \"XYZ9B76\",\n    \"tipo_veiculo\": \"Carreta\"\n  }\n}"
                    )
                }
            )
        )
    )
    public ResponseEntity<?> registrarExpedicao(@RequestBody PortariaExpedicaoRequestDTO request) {
        try {
            PortariaExpedicaoResponseDTO response = expedicaoService.registrarExpedicao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar expedição: " + e.getMessage());
        }
    }

    @PostMapping("/excluir")
    @Operation(
        summary = "Excluir expedição da portaria",
        description = "Exclui o registro principal da portaria, a expedição associada e, quando aplicável, os cadastros manuais de transporte."
    )
    public ResponseEntity<ExclusaoPortariaResponseDTO> excluirExpedicao(
            @RequestBody PortariaExpedicaoExclusaoRequestDTO request) {
        return ResponseEntity.ok(expedicaoExclusaoService.excluir(request));
    }

    @GetMapping("/registro/{registroId}")
    @Operation(
        summary = "Buscar expedição por registro",
        description = "Retorna o registro completo da portaria com o bloco de expedição populado, buscando pelo ID do registro."
    )
    public ResponseEntity<?> buscarPorRegistroId(@PathVariable Long registroId) {
        try {
            PortariaRegistroDTO registro = registroService.getRegistroById(registroId);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar expedição: " + e.getMessage());
        }
    }
}
