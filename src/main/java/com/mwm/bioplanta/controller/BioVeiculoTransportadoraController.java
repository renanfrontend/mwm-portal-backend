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

import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.repository.BioVeiculoTransportadoraRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-veiculos-transportadoras")
@Tag(name = "Bio Veículos Transportadoras", description = "Gerenciamento de veículos de transportadoras.")

public class BioVeiculoTransportadoraController {

    @Autowired
    private BioVeiculoTransportadoraRepository bioVeiculoTransportadoraRepository;

    @GetMapping
    @Operation(summary = "Obter todos os veículos de transportadoras")
    public List<BioVeiculoTransportadora> listarTodos() {
        return bioVeiculoTransportadoraRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter veículo de transportadora por ID")
    public ResponseEntity<BioVeiculoTransportadora> obterPorId(@PathVariable Long id) {
        Optional<BioVeiculoTransportadora> bioVeiculoTransportadora = bioVeiculoTransportadoraRepository.findById(id);
        return bioVeiculoTransportadora.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo veículo de transportadora")
    public BioVeiculoTransportadora criar(@RequestBody BioVeiculoTransportadora bioVeiculoTransportadora) {
        return bioVeiculoTransportadoraRepository.save(bioVeiculoTransportadora);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo de transportadora por ID")
    public ResponseEntity<BioVeiculoTransportadora> atualizar(@PathVariable Long id, @RequestBody BioVeiculoTransportadora bioVeiculoTransportadora) {
        if (!bioVeiculoTransportadoraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioVeiculoTransportadora.setId(id);
        BioVeiculoTransportadora atualizado = bioVeiculoTransportadoraRepository.save(bioVeiculoTransportadora);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veículo de transportadora por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioVeiculoTransportadoraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioVeiculoTransportadoraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}