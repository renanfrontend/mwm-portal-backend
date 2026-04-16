package com.mwm.bioplanta.controller.cadastro;

import com.mwm.bioplanta.dto.cadastro.FiliadaResponseDTO;
import com.mwm.bioplanta.service.cadastro.FiliadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/filiadas")
@Tag(name = "Filiadas", description = "Gerenciamento de filiadas (unidades/plantas)")

public class FiliadaController {

    private final FiliadaService filiadaService;

    public FiliadaController(FiliadaService filiadaService) {
        this.filiadaService = filiadaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as filiadas ativas")
    public ResponseEntity<List<FiliadaResponseDTO>> listarFiliadasAtivas() {
        List<FiliadaResponseDTO> filiadas = filiadaService.findAllAtivas();
        return ResponseEntity.ok(filiadas);
    }
}