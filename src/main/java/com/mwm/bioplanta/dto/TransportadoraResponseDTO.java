package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Transportadora Response DTO", description = "Resposta com dados completos da transportadora")
public class TransportadoraResponseDTO {

    @Schema(description = "ID da transportadora")
    private Long id;

    @Schema(description = "Nome fantasia")
    private String nomeFantasia;

    @Schema(description = "Razão social")
    private String razaoSocial;

    @Schema(description = "CNPJ")
    private String cnpj;

    @Schema(description = "Categoria")
    private String categoria;

    @Schema(description = "Endereço")
    private String endereco;

    @Schema(description = "Cidade")
    private String cidade;

    @Schema(description = "UF")
    private String uf;

    @Schema(description = "Telefone")
    private String telefone;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Telefone comercial")
    private String telefoneComercial;

    @Schema(description = "Email comercial")
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

    @Schema(description = "Status (Ativo/Inativo)")
    private String status;

    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
