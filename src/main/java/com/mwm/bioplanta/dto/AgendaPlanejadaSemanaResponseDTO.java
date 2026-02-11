package com.mwm.bioplanta.dto;

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
public class AgendaPlanejadaSemanaResponseDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<AgendaPlanejadaSemanaLinhaDTO> linhas;
}
