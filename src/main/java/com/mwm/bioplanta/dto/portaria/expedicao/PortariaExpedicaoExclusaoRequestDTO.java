package com.mwm.bioplanta.dto.portaria.expedicao;

import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaBaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "PortariaExpedicaoExclusaoRequest", description = "Requisição para exclusão de expedição")
public class PortariaExpedicaoExclusaoRequestDTO extends ExclusaoPortariaBaseRequestDTO {

    @Schema(description = "ID da expedição", required = true)
    private String expedicaoId;
}
