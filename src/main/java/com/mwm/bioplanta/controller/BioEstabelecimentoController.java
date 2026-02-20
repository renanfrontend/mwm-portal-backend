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

import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-estabelecimentos")
@Tag(name = "Bio Estabelecimentos", description = "Gerenciamento de estabelecimentos.")

public class BioEstabelecimentoController {

    private final BioEstabelecimentoRepository bioEstabelecimentoRepository;

    public BioEstabelecimentoController(BioEstabelecimentoRepository bioEstabelecimentoRepository) {
        this.bioEstabelecimentoRepository = bioEstabelecimentoRepository;
    }

    @GetMapping
    @Operation(summary = "Obter todos os estabelecimentos")
    public List<BioEstabelecimento> listarTodos() {
        return bioEstabelecimentoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter estabelecimento por ID")
    public ResponseEntity<BioEstabelecimento> obterPorId(@PathVariable Long id) {
        Optional<BioEstabelecimento> bioEstabelecimento = bioEstabelecimentoRepository.findById(java.util.Objects.requireNonNull(id));
        return bioEstabelecimento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo estabelecimento")
    public BioEstabelecimento criar(@RequestBody BioEstabelecimento bioEstabelecimento) {
        return bioEstabelecimentoRepository.save(java.util.Objects.requireNonNull(bioEstabelecimento));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estabelecimento por ID")
    public ResponseEntity<BioEstabelecimento> atualizar(@PathVariable Long id, @RequestBody BioEstabelecimento bioEstabelecimento) {
        if (!bioEstabelecimentoRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return ResponseEntity.notFound().build();
        }
        bioEstabelecimento.setId(id);
        BioEstabelecimento atualizado = bioEstabelecimentoRepository.save(bioEstabelecimento);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar estabelecimento por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioEstabelecimentoRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return ResponseEntity.notFound().build();
        }
        bioEstabelecimentoRepository.deleteById(java.util.Objects.requireNonNull(id));
        return ResponseEntity.noContent().build();
    }
}