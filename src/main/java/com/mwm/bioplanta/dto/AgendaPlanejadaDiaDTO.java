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
public class AgendaPlanejadaDiaDTO {
    private LocalDate dataAgendada;
    private Integer qtdViagens;
}
