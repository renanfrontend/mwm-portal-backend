package com.mwm.bioplanta.dto.portaria.entrega;

import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaBaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PortariaEntregaDejetosExclusaoRequestDTO extends ExclusaoPortariaBaseRequestDTO {

    private String entregaDejetosId;
}