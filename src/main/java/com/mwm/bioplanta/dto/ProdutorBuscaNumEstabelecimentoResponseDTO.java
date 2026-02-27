package com.mwm.bioplanta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorBuscaNumEstabelecimentoResponseDTO {
    private Long id;
    private String nome;
    private String numEstabelecimento;
    private String message;
}
