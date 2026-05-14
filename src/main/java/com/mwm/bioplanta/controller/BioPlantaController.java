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

import com.mwm.bioplanta.model.BioPlanta;
import com.mwm.bioplanta.repository.BioPlantaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-plantas")
@Tag(name = "Bio Plantas", description = "Gerenciamento de plantas.")

public class BioPlantaController {

    @Autowired
    private BioPlantaRepository bioPlantaRepository;

    @GetMapping
    @Operation(summary = "Obter todas as plantas")
    public List<BioPlanta> listarTodos() {
        return bioPlantaRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter planta por ID")
    public ResponseEntity<BioPlanta> obterPorId(@PathVariable Long id) {
        Optional<BioPlanta> bioPlanta = bioPlantaRepository.findById(id);
        return bioPlanta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova planta")
    public BioPlanta criar(@RequestBody BioPlanta bioPlanta) {
        return bioPlantaRepository.save(bioPlanta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar planta por ID")
    public ResponseEntity<BioPlanta> atualizar(@PathVariable Long id, @RequestBody BioPlanta bioPlanta) {
        if (!bioPlantaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioPlanta.setId(id);
        BioPlanta atualizado = bioPlantaRepository.save(bioPlanta);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar planta por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioPlantaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioPlantaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}