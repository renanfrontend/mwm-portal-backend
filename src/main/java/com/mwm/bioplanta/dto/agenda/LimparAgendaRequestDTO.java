package com.mwm.bioplanta.dto.agenda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LimparAgendaRequestDTO {
    private Long idBioplanta;
    private Long idFiliada;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<Long> idsEstabelecimentos;
}
