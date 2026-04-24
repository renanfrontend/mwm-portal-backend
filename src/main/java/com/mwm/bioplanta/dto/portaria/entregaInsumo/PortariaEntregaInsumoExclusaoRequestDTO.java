package com.mwm.bioplanta.dto.portaria.entregaInsumo;

import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaBaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "PortariaEntregaInsumoExclusaoRequest", description = "Requisição para exclusão de entrega de insumo")
public class PortariaEntregaInsumoExclusaoRequestDTO extends ExclusaoPortariaBaseRequestDTO {
    
    @Schema(description = "ID da entrega de insumo", required = true)
    private String entregaInsumoId;
}