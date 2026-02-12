package com.mwm.bioplanta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.BioUnidade;
import com.mwm.bioplanta.repository.BioUnidadeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/biounidades")
@Tag(name = "Bio Unidades", description = "Gerenciamento de unidades bio.")

public class BioUnidadeController {

    @Autowired
    private BioUnidadeRepository bioUnidadeRepository;
    @GetMapping

    @Operation(summary = "Obter todas as unidades bio")
    public List<BioUnidade> listarTodos() {
        return bioUnidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter unidade bio por ID")
    public ResponseEntity<BioUnidade> obterPorId(@PathVariable Long id) {
        Optional<BioUnidade> bioUnidade = bioUnidadeRepository.findById(id);
        return bioUnidade.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova unidade bio")
    public BioUnidade criar(@RequestBody BioUnidade bioUnidade) {
        return bioUnidadeRepository.save(bioUnidade);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade bio por ID")
    public ResponseEntity<BioUnidade> atualizar(@PathVariable Long id, @RequestBody BioUnidade bioUnidade) {
        if (!bioUnidadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioUnidade.setId(id);
        BioUnidade atualizado = bioUnidadeRepository.save(bioUnidade);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar unidade bio por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioUnidadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioUnidadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}