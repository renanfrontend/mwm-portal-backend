package com.mwm.bioplanta.dto.portaria.exclusao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortariaErrorResponseDTO {

    private int status;
    private String mensagem;
}