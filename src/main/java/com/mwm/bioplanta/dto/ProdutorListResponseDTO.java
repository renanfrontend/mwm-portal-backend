package com.mwm.bioplanta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorListResponseDTO {
    private Long id;
    private Long produtorId;
    private Long estabelecimentoId;
    private String nomeProdutor;
    private String numEstabelecimento;
    private String filiada;
    private String modalidade;
    private Integer cabecasAlojadas;
    private String distancia;
    private String certificado;
    private String doamDejetos;
    private Integer qtdLagoas;
    private String volLagoas;
    private String restricoesOperacionais;
}
