package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.ExportarRelatorioRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

public interface RelatorioExportacaoService {
    ByteArrayResource exportar(ExportarRelatorioRequest request, MediaType mediaType);
    String getFileName(ExportarRelatorioRequest request);
}
