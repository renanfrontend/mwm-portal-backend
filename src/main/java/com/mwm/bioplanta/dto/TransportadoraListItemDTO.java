package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Transportadora List Item DTO", description = "Item na listagem de transportadoras")
public class TransportadoraListItemDTO {

    @Schema(description = "ID da transportadora")
    private Long id;

    @Schema(description = "Nome fantasia")
    private String nomeFantasia;

    @Schema(description = "Razão social")
    private String razaoSocial;

    @Schema(description = "CNPJ")
    private String cnpj;

    @Schema(description = "Telefone comercial")
    private String telefoneComercial;

    @Schema(description = "Email comercial")
    private String emailComercial;

    @Schema(description = "Endereço")
    private String endereco;

    @Schema(description = "Cidade")
    private String cidade;

    @Schema(description = "UF")
    private String uf;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Quantidade de veículos")
    private Long quantidadeVeiculos;
}
