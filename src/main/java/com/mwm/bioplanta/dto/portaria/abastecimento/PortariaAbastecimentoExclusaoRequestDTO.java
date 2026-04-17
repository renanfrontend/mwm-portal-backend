package com.mwm.bioplanta.dto.portaria.abastecimento;

import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaBaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PortariaAbastecimentoExclusaoRequestDTO extends ExclusaoPortariaBaseRequestDTO {

    private String abastecimentoId;
}