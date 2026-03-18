package com.mwm.bioplanta.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportarRelatorioRequest {
    @Schema(description = "Data inicial do período (yyyy-MM-dd)", example = "2026-03-08", required = true)
    @NotBlank
    private String dataInicial;

    @Schema(description = "Data final do período (yyyy-MM-dd)", example = "2026-03-14", required = true)
    @NotBlank
    private String dataFinal;

    @Schema(description = "Nome da transportadora (opcional)", example = "PRIMATO")
    private String transportadora;

    @Schema(description = "Formato do arquivo de exportação ('csv' ou 'pdf')", example = "csv", required = true)
    @NotBlank
    private String formato;

    @Schema(description = "Lista de dados planejados para exportação", required = true)
    @NotNull
    private List<DadoPlanejadoDTO> dadosPlanejados;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadoPlanejadoDTO {
        @Schema(description = "ID do registro", example = "1")
        private Long id;

        @Schema(description = "Número do estabelecimento", example = "123")
        private String numEstabelecimento;

        @Schema(description = "Nome do produtor", example = "João")
        private String produtor;

        @Schema(description = "Distância em KM", example = "10")
        private Integer distancia;

        @Schema(description = "Nome da transportadora", example = "PRIMATO")
        private String transportadora;

        @Schema(description = "Total de KM", example = "70")
        private Integer totalKm;

        @Schema(description = "Domingo", example = "1")
        private Integer dom;
        @Schema(description = "Segunda", example = "1")
        private Integer seg;
        @Schema(description = "Terça", example = "1")
        private Integer ter;
        @Schema(description = "Quarta", example = "1")
        private Integer qua;
        @Schema(description = "Quinta", example = "1")
        private Integer qui;
        @Schema(description = "Sexta", example = "1")
        private Integer sex;
        @Schema(description = "Sábado", example = "1")
        private Integer sab;
    }
}
