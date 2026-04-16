package com.mwm.bioplanta.controller.cadastro;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.BioProducao;
import com.mwm.bioplanta.service.cadastro.BioProducaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-producoes")
@Tag(name = "Bio Produções", description = "Gerenciamento de produções.")

public class BioProducaoController {

    private final BioProducaoService bioProducaoService;

    public BioProducaoController(BioProducaoService bioProducaoService) {
        this.bioProducaoService = bioProducaoService;
    }

    @GetMapping
    @Operation(summary = "Obter todas as produções")
    public List<BioProducao> listarTodos() {
        return bioProducaoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produção por ID")
    public ResponseEntity<BioProducao> obterPorId(@PathVariable Long id) {
        Optional<BioProducao> bioProducao = bioProducaoService.obterPorId(id);
        return bioProducao.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova produção")
    public BioProducao criar(@RequestBody BioProducao bioProducao) {
        return bioProducaoService.criar(bioProducao);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produção por ID")
    public ResponseEntity<BioProducao> atualizar(@PathVariable Long id, @RequestBody BioProducao bioProducao) {
        return bioProducaoService.atualizar(id, bioProducao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produção por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioProducaoService.deletar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}