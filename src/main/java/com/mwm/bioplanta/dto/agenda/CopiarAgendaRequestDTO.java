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
public class CopiarAgendaRequestDTO {
    private Long idBioplanta;
    private Long idFiliada;
    private String dataInicioOrigem;
    private String dataInicioDestino;
    private List<Long> idsEstabelecimentos;
}
