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

import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-estabelecimentos")
@Tag(name = "Bio Estabelecimentos", description = "Gerenciamento de estabelecimentos.")
@CrossOrigin("*")
public class BioEstabelecimentoController {

    @Autowired
    private BioEstabelecimentoRepository bioEstabelecimentoRepository;

    @GetMapping
    @Operation(summary = "Obter todos os estabelecimentos")
    public List<BioEstabelecimento> listarTodos() {
        return bioEstabelecimentoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter estabelecimento por ID")
    public ResponseEntity<BioEstabelecimento> obterPorId(@PathVariable Long id) {
        Optional<BioEstabelecimento> bioEstabelecimento = bioEstabelecimentoRepository.findById(id);
        return bioEstabelecimento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo estabelecimento")
    public BioEstabelecimento criar(@RequestBody BioEstabelecimento bioEstabelecimento) {
        return bioEstabelecimentoRepository.save(bioEstabelecimento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estabelecimento por ID")
    public ResponseEntity<BioEstabelecimento> atualizar(@PathVariable Long id, @RequestBody BioEstabelecimento bioEstabelecimento) {
        if (!bioEstabelecimentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioEstabelecimento.setId(id);
        BioEstabelecimento atualizado = bioEstabelecimentoRepository.save(bioEstabelecimento);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar estabelecimento por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioEstabelecimentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bioEstabelecimentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}