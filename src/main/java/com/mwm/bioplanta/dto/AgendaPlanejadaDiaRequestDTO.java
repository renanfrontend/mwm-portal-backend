package com.mwm.bioplanta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaPlanejadaDiaRequestDTO {
    private Long idBioplanta;
    private Long idFiliada;
    private Long idEstabelecimento;
    private String produtor;
    private Integer distanciaKm;
    private String transportadora;
    private LocalDate dataAgendada;
    private Integer qtdViagens;
}
