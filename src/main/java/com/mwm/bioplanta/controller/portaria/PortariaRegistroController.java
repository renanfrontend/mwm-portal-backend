package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.PaginationResponseDTO;
import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.service.portaria.PortariaRegistroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para Portaria Registro
 * Endpoints REST para CRUD de registros de portaria
 * 
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/portaria/registros")
@Tag(name = "Portaria Registro", description = "Endpoints para gerenciar registros de entrada/saída de portaria")
public class PortariaRegistroController {

    @Autowired
    private PortariaRegistroService portariaRegistroService;

    /**
     * Lista todos os registros com paginação
     * 
     * @param page página (padrão: 0)
     * @param pageSize itens por página (padrão: 5)
     * @return lista paginada de registros
     */
    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna lista de registros de portaria com paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros obtidos com sucesso",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> listRegistros(
            @Parameter(description = "Número da página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") Integer page,
            
            @Parameter(description = "Quantidade de itens por página", example = "5")
            @RequestParam(defaultValue = "5") Integer pageSize) {

        log.info("GET /api/portaria/registros - página: {}, tamanho: {}", page, pageSize);

        try {
            PaginationResponseDTO<PortariaRegistroDTO> response = portariaRegistroService.listRegistros(page, pageSize);

            Map<String, Object> body = new HashMap<>();
            body.put("data", response.getData());

            Map<String, Object> pagination = new HashMap<>();
            pagination.put("page", response.getPage());
            pagination.put("pageSize", response.getPageSize());
            pagination.put("total", response.getTotal());
            pagination.put("totalPages", response.getTotalPages());
            pagination.put("hasNext", response.getHasNext());
            pagination.put("hasPrevious", response.getHasPrevious());

            body.put("pagination", pagination);

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("Erro ao listar registros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar registros: " + e.getMessage());
        }
    }

    /**
     * Obtém um registro específico por ID
     * 
     * @param id ID do registro
     * @return dados do registro
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter registro por ID", description = "Retorna um registro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> getRegistroById(
            @Parameter(description = "ID do registro", example = "1", required = true)
            @PathVariable Long id) {

        log.info("GET /api/portaria/registros/{}", id);

        try {
            PortariaRegistroDTO registro = portariaRegistroService.getRegistroById(id);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao obter registro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar registro: " + e.getMessage());
        }
    }

    /**
     * Cria um novo registro
     * 
     * @param registroDTO dados do novo registro
     * @return registro criado com ID
     */
    @PostMapping
    @Operation(summary = "Criar novo registro", description = "Cria um novo registro de portaria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> createRegistro(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do novo registro",
                required = true,
                content = @Content(schema = @Schema(implementation = PortariaRegistroDTO.class)))
            @RequestBody PortariaRegistroDTO registroDTO) {

        log.info("POST /api/portaria/registros - tipo: {}", registroDTO.getTipoRegistro());

        try {
            PortariaRegistroDTO created = portariaRegistroService.createRegistro(registroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Erro ao criar registro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar registro: " + e.getMessage());
        }
    }

    /**
     * Atualiza um registro existente
     * 
     * @param id ID do registro
     * @param registroDTO novos dados
     * @return registro atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> updateRegistro(
            @Parameter(description = "ID do registro", example = "1", required = true)
            @PathVariable Long id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Novos dados do registro",
                required = true,
                content = @Content(schema = @Schema(implementation = PortariaRegistroDTO.class)))
            @RequestBody PortariaRegistroDTO registroDTO) {

        log.info("PUT /api/portaria/registros/{}", id);

        try {
            PortariaRegistroDTO updated = portariaRegistroService.updateRegistro(id, registroDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao atualizar registro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar registro: " + e.getMessage());
        }
    }

    /**
     * Deleta um registro
     * 
     * @param id ID do registro a deletar
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro", description = "Deleta um registro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registro deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> deleteRegistro(
            @Parameter(description = "ID do registro", example = "1", required = true)
            @PathVariable Long id) {

        log.info("DELETE /api/portaria/registros/{}", id);

        try {
            portariaRegistroService.deleteRegistro(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao deletar registro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar registro: " + e.getMessage());
        }
    }

    /**
     * Deleta múltiplos registros
     * 
     * @param ids lista de IDs a deletar
     */
    @DeleteMapping
    @Operation(summary = "Deletar múltiplos registros", description = "Deleta vários registros de uma vez")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registros deletados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Lista de IDs inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> deleteMultipleRegistros(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Lista de IDs a deletar",
                required = true,
                content = @Content(schema = @Schema(type = "array", example = "[1, 2, 3]")))
            @RequestBody List<Long> ids) {

        log.info("DELETE /api/portaria/registros - {} registros", ids.size());

        try {
            portariaRegistroService.deleteMultipleRegistros(ids);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar múltiplos registros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar registros: " + e.getMessage());
        }
    }
}
