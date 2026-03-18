package com.mwm.bioplanta.controller;

import com.mwm.bioplanta.dto.ExportarRelatorioRequest;
import com.mwm.bioplanta.service.RelatorioExportacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Exportação de relatórios planejados")
public class RelatorioExportacaoController {

    private final RelatorioExportacaoService relatorioExportacaoService;

    @Autowired
    public RelatorioExportacaoController(RelatorioExportacaoService relatorioExportacaoService) {
        this.relatorioExportacaoService = relatorioExportacaoService;
    }

    @Operation(summary = "Exporta relatório planejado em CSV ou PDF")
    @PostMapping("/exportar")
    public ResponseEntity<ByteArrayResource> exportarRelatorio(@Valid @RequestBody ExportarRelatorioRequest request) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if ("pdf".equalsIgnoreCase(request.getFormato())) {
            mediaType = MediaType.APPLICATION_PDF;
        } else if ("csv".equalsIgnoreCase(request.getFormato())) {
            mediaType = MediaType.parseMediaType("text/csv");
        }
        ByteArrayResource resource = relatorioExportacaoService.exportar(request, mediaType);
        String fileName = relatorioExportacaoService.getFileName(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(mediaType)
                .body(resource);
    }

    @Operation(summary = "Exporta relatório planejado em PDF")
    @PostMapping("/exportar-pdf")
    public ResponseEntity<ByteArrayResource> exportarPdf(@Valid @RequestBody ExportarRelatorioRequest request) {
        ByteArrayResource resource = relatorioExportacaoService.exportar(request, MediaType.APPLICATION_PDF);
        String fileName = relatorioExportacaoService.getFileName(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
