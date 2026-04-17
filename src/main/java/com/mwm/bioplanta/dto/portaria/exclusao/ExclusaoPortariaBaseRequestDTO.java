package com.mwm.bioplanta.dto.portaria.exclusao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ExclusaoPortariaBaseRequestDTO {

    private String registroId;
    private String tipoRegistro;
    private String transportadoraId;
    private String veiculoId;
    private String transportadoraManual;
    private String placaManual;
    private OrigemTransportadoraExclusao origemTransportadora;
    private Boolean excluirTransportadora;
    private Boolean excluirVeiculo;
}