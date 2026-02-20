package com.mwm.bioplanta.controller;

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
import com.mwm.bioplanta.repository.BioFiliadaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-filiadas")
@Tag(name = "Bio Filiadas", description = "Gerenciamento de filiada.")

public class BioFiliadaController {

    private final BioFiliadaRepository bioFiliadaRepository;

    public BioFiliadaController(BioFiliadaRepository bioFiliadaRepository) {
        this.bioFiliadaRepository = bioFiliadaRepository;
    }

    @GetMapping
    @Operation(summary = "Obter todas as filiada")
    public List<BioFiliada> listarTodos() {
        return bioFiliadaRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter filiada por ID")
    public ResponseEntity<BioFiliada> obterPorId(@PathVariable Long id) {
        Optional<BioFiliada> bioFiliada = bioFiliadaRepository.findById(java.util.Objects.requireNonNull(id));
        return bioFiliada.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova filiada")
    public BioFiliada criar(@RequestBody BioFiliada bioFiliada) {
        return bioFiliadaRepository.save(java.util.Objects.requireNonNull(bioFiliada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar filiada por ID")
    public ResponseEntity<BioFiliada> atualizar(@PathVariable Long id, @RequestBody BioFiliada bioFiliada) {
        if (!bioFiliadaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return ResponseEntity.notFound().build();
        }
        bioFiliada.setId(id);
        BioFiliada atualizado = bioFiliadaRepository.save(bioFiliada);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar filiada por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioFiliadaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return ResponseEntity.notFound().build();
        }
        bioFiliadaRepository.deleteById(java.util.Objects.requireNonNull(id));
        return ResponseEntity.noContent().build();
    }
}