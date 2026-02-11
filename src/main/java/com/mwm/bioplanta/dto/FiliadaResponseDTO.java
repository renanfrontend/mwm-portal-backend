package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Filiada Response DTO", description = "Resposta com dados da filiada")
public class FiliadaResponseDTO {

    @Schema(description = "ID da filiada")
    private Long id;

    @Schema(description = "Código da filiada")
    private String codigoFiliada;

    @Schema(description = "Nome da filiada")
    private String nome;

    @Schema(description = "Estado")
    private String estado;

    @Schema(description = "Cidade")
    private String cidade;
}