package com.mwm.bioplanta.dto.cooperado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorBuscaResponseDTO {
    private Long id;
    private String nomeProdutor;
    private String numEstabelecimento;
    private String cpfCnpj;
}
