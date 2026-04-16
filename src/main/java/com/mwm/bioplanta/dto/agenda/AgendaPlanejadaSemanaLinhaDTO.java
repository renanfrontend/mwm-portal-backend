package com.mwm.bioplanta.dto.agenda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaPlanejadaSemanaLinhaDTO {
    private Long idEstabelecimento;
    private String produtor;
    private Integer distanciaKm;
    private String transportadora;
    private List<AgendaPlanejadaDiaDTO> dias;
}
