package com.mwm.bioplanta.dto.portaria.exclusao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExclusaoPortariaResponseDTO {

    private String mensagem;
    private Long registroId;
    private String tipoRegistro;
    private Long referenciaId;
    private boolean agendaRealizadaExcluida;
    private boolean transportadoraExcluida;
    private boolean veiculoExcluido;
}