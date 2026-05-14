package com.mwm.bioplanta.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.dto.TransportadoraDTO;
import com.mwm.bioplanta.dto.TransportadoraResponseDTO;
import com.mwm.bioplanta.dto.TransportadoraPageResponseDTO;
import com.mwm.bioplanta.dto.VeiculoDTO;
import com.mwm.bioplanta.service.BioTransportadoraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/logistica/transportadoras")
@Tag(name = "Transportadoras", description = "Gerenciamento de transportadoras e seus veículos.")

public class BioTransportadoraController {

    @Autowired
    private BioTransportadoraService bioTransportadoraService;

    @GetMapping
    @Operation(summary = "Listar transportadoras com paginação e busca")
    public ResponseEntity<TransportadoraPageResponseDTO> listar(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "search", required = false) String search) {
        TransportadoraPageResponseDTO response = bioTransportadoraService.listar(page, pageSize, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter transportadora por ID com seus veículos")
    public ResponseEntity<TransportadoraResponseDTO> obterPorId(@PathVariable Long id) {
        try {
            TransportadoraResponseDTO response = bioTransportadoraService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar uma nova transportadora")
    public ResponseEntity<?> criar(@RequestBody TransportadoraDTO dto) {
        try {
            TransportadoraResponseDTO response = bioTransportadoraService.criar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.toLowerCase().contains("cnpj")) {
                return ResponseEntity.badRequest().body("cnpj ja esta cadastrado.");
            }
            return ResponseEntity.badRequest().body("erro ao criar transportadora");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transportadora por ID")
    public ResponseEntity<TransportadoraResponseDTO> atualizar(@PathVariable Long id, @RequestBody TransportadoraDTO dto) {
        try {
            TransportadoraResponseDTO response = bioTransportadoraService.atualizar(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transportadora por ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            bioTransportadoraService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ============ ENDPOINTS DE SUB-RECURSO: VEÍCULOS ============

    @PostMapping("/{transportadoraId}/veiculos")
    @Operation(summary = "Adicionar veículo a uma transportadora")
    public ResponseEntity<VeiculoDTO> adicionarVeiculo(
            @PathVariable Long transportadoraId,
            @RequestBody VeiculoDTO veiculoDTO) {
        try {
            VeiculoDTO response = bioTransportadoraService.adicionarVeiculo(transportadoraId, veiculoDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{transportadoraId}/veiculos/{veiculoId}")
    @Operation(summary = "Editar veículo de uma transportadora")
    public ResponseEntity<VeiculoDTO> editarVeiculo(
            @PathVariable Long transportadoraId,
            @PathVariable Long veiculoId,
            @RequestBody VeiculoDTO veiculoDTO) {
        try {
            VeiculoDTO response = bioTransportadoraService.editarVeiculo(transportadoraId, veiculoId, veiculoDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{transportadoraId}/veiculos/{veiculoId}")
    @Operation(summary = "Remover veículo de uma transportadora")
    public ResponseEntity<Void> removerVeiculo(
            @PathVariable Long transportadoraId,
            @PathVariable Long veiculoId) {
        try {
            bioTransportadoraService.removerVeiculo(transportadoraId, veiculoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}