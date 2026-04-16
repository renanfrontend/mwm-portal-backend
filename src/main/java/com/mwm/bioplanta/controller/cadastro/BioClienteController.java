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

import com.mwm.bioplanta.model.BioCliente;
import com.mwm.bioplanta.service.cadastro.BioClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bio-clientes")
@Tag(name = "Bio Clientes", description = "Gerenciamento de clientes.")

public class BioClienteController {

    private final BioClienteService bioClienteService;

    public BioClienteController(BioClienteService bioClienteService) {
        this.bioClienteService = bioClienteService;
    }

    @GetMapping
    @Operation(summary = "Obter todos os clientes")
    public List<BioCliente> listarTodos() {
        return bioClienteService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter cliente por ID")
    public ResponseEntity<BioCliente> obterPorId(@PathVariable Long id) {
        Optional<BioCliente> bioCliente = bioClienteService.obterPorId(id);
        return bioCliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo cliente")
    public BioCliente criar(@RequestBody BioCliente bioCliente) {
        return bioClienteService.criar(bioCliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente por ID")
    public ResponseEntity<BioCliente> atualizar(@PathVariable Long id, @RequestBody BioCliente bioCliente) {
        return bioClienteService.atualizar(id, bioCliente)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!bioClienteService.deletar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}