package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.VeiculoTipoResponseDTO;
import com.mwm.bioplanta.dto.VeiculoCombustivelResponseDTO;
import com.mwm.bioplanta.model.BioVeiculoTipo;
import com.mwm.bioplanta.model.BioVeiculoCombustivel;
import com.mwm.bioplanta.repository.BioVeiculoTipoRepository;
import com.mwm.bioplanta.repository.BioVeiculoCombustivelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veiculo")
@Tag(name = "Veículo", description = "Gerenciamento de tipos de veículos e combustíveis")

public class VeiculoController {

    private final BioVeiculoTipoRepository bioVeiculoTipoRepository;
    private final BioVeiculoCombustivelRepository bioVeiculoCombustivelRepository;

    public VeiculoController(BioVeiculoTipoRepository bioVeiculoTipoRepository,
                            BioVeiculoCombustivelRepository bioVeiculoCombustivelRepository) {
        this.bioVeiculoTipoRepository = bioVeiculoTipoRepository;
        this.bioVeiculoCombustivelRepository = bioVeiculoCombustivelRepository;
    }

    @GetMapping("/tipo")
    @Operation(summary = "Listar todos os tipos de veículos")
    public ResponseEntity<List<VeiculoTipoResponseDTO>> listarTipos() {
        List<VeiculoTipoResponseDTO> tipos = bioVeiculoTipoRepository.findAll()
                .stream()
                .map(this::toTipoDTO)
                .toList();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/combustivel")
    @Operation(summary = "Listar todos os tipos de combustíveis")
    public ResponseEntity<List<VeiculoCombustivelResponseDTO>> listarCombustiveis() {
        List<VeiculoCombustivelResponseDTO> combustiveis = bioVeiculoCombustivelRepository.findAll()
                .stream()
                .map(this::toCombustivelDTO)
                .toList();
        return ResponseEntity.ok(combustiveis);
    }

    private VeiculoTipoResponseDTO toTipoDTO(BioVeiculoTipo tipo) {
        return new VeiculoTipoResponseDTO(
                tipo.getId(),
                tipo.getLabel(),
                tipo.getValor()
        );
    }

    private VeiculoCombustivelResponseDTO toCombustivelDTO(BioVeiculoCombustivel combustivel) {
        return new VeiculoCombustivelResponseDTO(
                combustivel.getId(),
                combustivel.getLabel(),
                combustivel.getValor()
        );
    }
}
