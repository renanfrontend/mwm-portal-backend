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

import com.mwm.bioplanta.model.BioFiliada;
import com.mwm.bioplanta.service.cadastro.FiliadaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-filiadas")
@Tag(name = "Bio Filiadas", description = "Gerenciamento de filiada.")

public class BioFiliadaController {

    private final FiliadaService filiadaService;

    public BioFiliadaController(FiliadaService filiadaService) {
        this.filiadaService = filiadaService;
    }

    @GetMapping
    @Operation(summary = "Obter todas as filiada")
    public List<BioFiliada> listarTodos() {
        return filiadaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter filiada por ID")
    public ResponseEntity<BioFiliada> obterPorId(@PathVariable Long id) {
        Optional<BioFiliada> bioFiliada = filiadaService.obterPorId(id);
        return bioFiliada.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova filiada")
    public BioFiliada criar(@RequestBody BioFiliada bioFiliada) {
        return filiadaService.criar(bioFiliada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar filiada por ID")
    public ResponseEntity<BioFiliada> atualizar(@PathVariable Long id, @RequestBody BioFiliada bioFiliada) {
        return filiadaService.atualizar(id, bioFiliada)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar filiada por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!filiadaService.deletar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}