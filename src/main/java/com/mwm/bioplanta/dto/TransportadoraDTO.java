package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Transportadora DTO", description = "Dados de uma transportadora para criação/atualização")
public class TransportadoraDTO {

    @Schema(description = "Nome fantasia", example = "Transportadora XYZ Ltda", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeFantasia;

    @Schema(description = "Razão social", example = "XYZ Logística e Transportes SA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String razaoSocial;

    @Schema(description = "CNPJ", example = "12.345.678/0001-99", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnpj;

    @Schema(description = "Categoria", example = "Logística Geral")
    private String categoria;

    @Schema(description = "Endereço", example = "Rua das Flores, 123, Centro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endereco;

    @Schema(description = "Cidade", example = "Toledo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cidade;

    @Schema(description = "UF", example = "PR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uf;

    @Schema(description = "Telefone comercial", example = "(45) 3333-4444", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefoneComercial;

    @Schema(description = "Email comercial", example = "contato@xyz.com.br", requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailComercial;

    @Schema(description = "Contato principal")
    private ContatoDTO contatoPrincipal;

    @Schema(description = "Contato comercial")
    private ContatoDTO contatoComercial;

    @Schema(description = "Contato financeiro")
    private ContatoDTO contatoFinanceiro;

    @Schema(description = "Contato jurídico")
    private ContatoDTO contatoJuridico;

    @Schema(description = "Lista de veículos")
    private List<VeiculoDTO> veiculos;
}
