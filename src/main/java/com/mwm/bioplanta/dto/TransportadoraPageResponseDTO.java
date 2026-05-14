package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Transportadora Page Response DTO", description = "Resposta paginada de transportadoras")
public class TransportadoraPageResponseDTO {

    @Schema(description = "Lista de transportadoras")
    private List<TransportadoraListItemDTO> items;

    @Schema(description = "Total de registros")
    private Long total;

    @Schema(description = "Página atual")
    private Integer page;

    @Schema(description = "Tamanho da página")
    private Integer pageSize;
}
