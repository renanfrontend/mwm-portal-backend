package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bio_transportadora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Transportadora", description = "Representa uma transportadora.")
public class BioTransportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bio_transportadora_seq")
    @SequenceGenerator(name = "bio_transportadora_seq", sequenceName = "bio_transportadora_seq", allocationSize = 1)
    @Schema(description = "Identificador único da transportadora", example = "1")
    private Long id;

    @Column(name = "nome_fantasia")
    @Schema(description = "Nome fantasia da transportadora", example = "Transportadora Exemplo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeFantasia;

    @Column(name = "razao_social")
    @Schema(description = "Razão social da transportadora", example = "Transportadora Exemplo Ltda", requiredMode = Schema.RequiredMode.REQUIRED)
    private String razaoSocial;

    @Schema(description = "CNPJ da transportadora", example = "12.345.678/0001-99", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnpj;

    @Column(name = "telefone")
    @Schema(description = "Telefone comercial da transportadora", example = "45999470460")
    private String telefoneComercial;

    @Column(name = "email")
    @Schema(description = "Email comercial da transportadora", example = "contato@transportadora.com")
    private String emailComercial;

    @Schema(description = "Cidade da transportadora", example = "Toledo")
    private String cidade;

    @Schema(description = "UF da transportadora", example = "PR")
    private String uf;

    @Schema(description = "Endereço da transportadora", example = "Rua Exemplo, 123")
    private String endereco;

    @Schema(description = "Categoria da transportadora", example = "Logística Geral")
    private String categoria;

    @Column(name = "contato_principal_nome")
    @Schema(description = "Nome do contato principal", example = "João Silva")
    private String contatoPrincipalNome;

    @Column(name = "contato_principal_telefone")
    @Schema(description = "Telefone do contato principal", example = "45999470460")
    private String contatoPrincipalTelefone;

    @Column(name = "contato_principal_email")
    @Schema(description = "Email do contato principal", example = "joao@transportadora.com")
    private String contatoPrincipalEmail;

    @Column(name = "contato_comercial_nome")
    @Schema(description = "Nome do contato comercial", example = "Maria Santos")
    private String contatoComercialNome;

    @Column(name = "contato_comercial_telefone")
    @Schema(description = "Telefone do contato comercial", example = "45999470461")
    private String contatoComercialTelefone;

    @Column(name = "contato_comercial_email")
    @Schema(description = "Email do contato comercial", example = "maria@transportadora.com")
    private String contatoComercialEmail;

    @Column(name = "contato_financeiro_nome")
    @Schema(description = "Nome do contato financeiro", example = "Pedro Oliveira")
    private String contatoFinanceiroNome;

    @Column(name = "contato_financeiro_telefone")
    @Schema(description = "Telefone do contato financeiro", example = "45999470462")
    private String contatoFinanceiroTelefone;

    @Column(name = "contato_financeiro_email")
    @Schema(description = "Email do contato financeiro", example = "pedro@transportadora.com")
    private String contatoFinanceiroEmail;

    @Column(name = "contato_juridico_nome")
    @Schema(description = "Nome do contato jurídico", example = "Ana Costa")
    private String contatoJuridicoNome;

    @Column(name = "contato_juridico_telefone")
    @Schema(description = "Telefone do contato jurídico", example = "45999470463")
    private String contatoJuridicoTelefone;

    @Column(name = "contato_juridico_email")
    @Schema(description = "Email do contato jurídico", example = "ana@transportadora.com")
    private String contatoJuridicoEmail;

    @Schema(description = "Status da transportadora", example = "Ativo")
    private String status;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}