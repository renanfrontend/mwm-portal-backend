package com.mwm.bioplanta.service.relatorio;

import com.mwm.bioplanta.dto.relatorio.ExportarRelatorioRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

public interface RelatorioExportacaoService {
    ByteArrayResource exportar(ExportarRelatorioRequest request, MediaType mediaType);
    String getFileName(ExportarRelatorioRequest request);
}
