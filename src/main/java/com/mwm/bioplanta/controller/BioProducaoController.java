package com.mwm.bioplanta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.BioProducao;
import com.mwm.bioplanta.repository.BioProducaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-producoes")
@Tag(name = "Bio Produções", description = "Gerenciamento de produções.")
@CrossOrigin("*")
public class BioProducaoController {

    @Autowired
    private BioProducaoRepository bioProducaoRepository;

    @GetMapping
    @Operation(summary = "Obter todas as produções")
    public List<BioProducao> listarTodos() {
        return bioProducaoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produção por ID")
    public ResponseEntity<BioProducao> obterPorId(@PathVariable Long id) {
        Optional<BioProducao> bioProducao = bioProducaoRepository.findById(id);
        return bioProducao.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova produção")
    public BioProducao criar(@RequestBody BioProducao bioProducao) {
        return bioProducaoRepository.save(bioProducao);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produção por ID")
    public ResponseEntity<BioProducao> atualizar(@PathVariable Long id, @RequestBody BioProducao bioProducao) {
        if (!bioProducaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioProducao.setId(id);
        BioProducao atualizado = bioProducaoRepository.save(bioProducao);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produção por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioProducaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioProducaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}