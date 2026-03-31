package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.CategoriaResponseDTO;
import com.mwm.bioplanta.model.BioCategoria;
import com.mwm.bioplanta.repository.BioCategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
@Tag(name = "Categoria", description = "Gerenciamento de categorias")

public class CategoriaController {

    private final BioCategoriaRepository bioCategoriaRepository;

    public CategoriaController(BioCategoriaRepository bioCategoriaRepository) {
        this.bioCategoriaRepository = bioCategoriaRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = bioCategoriaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    private CategoriaResponseDTO toDTO(BioCategoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getLabel(),
                categoria.getValor()
        );
    }
}
