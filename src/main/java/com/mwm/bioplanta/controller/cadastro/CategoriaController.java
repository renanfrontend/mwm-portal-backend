package com.mwm.bioplanta.controller.cadastro;

import com.mwm.bioplanta.dto.cadastro.CategoriaResponseDTO;
import com.mwm.bioplanta.model.BioCategoria;
import com.mwm.bioplanta.service.cadastro.CategoriaService;
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

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(categorias);
    }

    private CategoriaResponseDTO toDTO(BioCategoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getLabel(),
                categoria.getValue()
        );
    }
}
