package com.mwm.bioplanta.dto.abastecimento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.mwm.bioplanta.dto.cadastro.TransportadoraListItemDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Abastecimento Page Response DTO", description = "Resposta paginada de abastecimentos")
public class AbastecimentoPageResponseDTO {

    @Schema(description = "Lista de abastecimentos")
    private List<TransportadoraListItemDTO> items;
}
