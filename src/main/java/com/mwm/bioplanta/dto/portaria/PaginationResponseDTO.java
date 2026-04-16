package com.mwm.bioplanta.dto.portaria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO para resposta com paginação
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PaginationResponse", description = "Resposta com dados paginados")
public class PaginationResponseDTO<T> {

    @Schema(description = "Dados da página atual")
    private List<T> data;

    @Schema(description = "Página atual (0-indexed)", example = "0")
    private Integer page;

    @Schema(description = "Quantidade de itens por página", example = "5")
    private Integer pageSize;

    @Schema(description = "Total de itens", example = "100")
    private Long total;

    @Schema(description = "Total de páginas", example = "20")
    private Integer totalPages;

    @Schema(description = "Tem próxima página?", example = "true")
    private Boolean hasNext;

    @Schema(description = "Tem página anterior?", example = "false")
    private Boolean hasPrevious;
}
