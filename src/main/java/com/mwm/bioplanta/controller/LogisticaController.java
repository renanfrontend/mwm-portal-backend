package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.CooperadoCreateDTO;
import com.mwm.bioplanta.dto.ProdutorDetalheDTO;
import com.mwm.bioplanta.dto.ProdutorPageResponseDTO;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.service.CooperadoService;
import com.mwm.bioplanta.service.ProdutorDetalheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logistica")
@Tag(name = "Logística", description = "Gerenciamento de produtores e logística.")

public class LogisticaController {

    @Autowired
    private CooperadoService cooperadoService;

    @Autowired
    private ProdutorDetalheService produtorDetalheService;

    @GetMapping("/test")
    @Operation(summary = "Teste se o controller está carregado")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("LogisticaController funcionando");
    }

    @GetMapping("/produtores")
    @Operation(summary = "Listar produtores com paginação e filtros")
    public ResponseEntity<ProdutorPageResponseDTO> listarProdutores(
            @RequestParam(required = false) Long plantaId,
            @RequestParam(required = false) Long filiadaId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            ProdutorPageResponseDTO response = cooperadoService.listarProdutores(plantaId, filiadaId, page, pageSize);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/produtores")
    @Operation(summary = "Criar um novo cooperado com entidades relacionadas")
    public ResponseEntity<?> criarCooperado(@RequestBody CooperadoCreateDTO dto) {
        try {
            BioEstabelecimento estabelecimento = cooperadoService.criarCooperado(dto);
            return ResponseEntity.ok(estabelecimento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/produtores/{id}")
    @Operation(summary = "Buscar detalhes completos de um produtor por ID")
    public ResponseEntity<ProdutorDetalheDTO> buscarDetalheProdutorPorId(@PathVariable Long id) {
        try {
            return produtorDetalheService.buscarDetalhePorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/produtores/{id}")
    @Operation(summary = "Atualizar um cooperado existente")
    public ResponseEntity<?> atualizarCooperado(@PathVariable Long id, @RequestBody CooperadoCreateDTO dto) {
        System.out.println("🔄 Recebendo PUT Produtor ID: " + id);
        System.out.println("📍 Lat recebida: " + dto.getLatitude());
        System.out.println("📍 Long recebida: " + dto.getLongitude());
        try {
            BioEstabelecimento estabelecimento = cooperadoService.atualizarCooperado(id, dto);
            return ResponseEntity.ok(estabelecimento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
