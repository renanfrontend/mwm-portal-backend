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

import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.service.cadastro.BioVeiculoTransportadoraCrudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-veiculos-transportadoras")
@Tag(name = "Bio Veículos Transportadoras", description = "Gerenciamento de veículos de transportadoras.")

public class BioVeiculoTransportadoraController {

    private final BioVeiculoTransportadoraCrudService bioVeiculoTransportadoraService;

    public BioVeiculoTransportadoraController(BioVeiculoTransportadoraCrudService bioVeiculoTransportadoraService) {
        this.bioVeiculoTransportadoraService = bioVeiculoTransportadoraService;
    }

    @GetMapping
    @Operation(summary = "Obter todos os veículos de transportadoras (excluindo transportadoras criadas por Entrega de Dejetos)")
    public List<BioVeiculoTransportadora> listarTodos() {
        return bioVeiculoTransportadoraService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter veículo de transportadora por ID")
    public ResponseEntity<BioVeiculoTransportadora> obterPorId(@PathVariable Long id) {
        Optional<BioVeiculoTransportadora> bioVeiculoTransportadora = bioVeiculoTransportadoraService.obterPorId(id);
        return bioVeiculoTransportadora.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo veículo de transportadora")
    public BioVeiculoTransportadora criar(@RequestBody BioVeiculoTransportadora bioVeiculoTransportadora) {
        return bioVeiculoTransportadoraService.criar(bioVeiculoTransportadora);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo de transportadora por ID")
    public ResponseEntity<BioVeiculoTransportadora> atualizar(@PathVariable Long id, @RequestBody BioVeiculoTransportadora bioVeiculoTransportadora) {
        return bioVeiculoTransportadoraService.atualizar(id, bioVeiculoTransportadora)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veículo de transportadora por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioVeiculoTransportadoraService.deletar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}