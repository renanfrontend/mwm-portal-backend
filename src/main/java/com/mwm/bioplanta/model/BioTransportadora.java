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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transportadora")
    @SequenceGenerator(name = "seq_transportadora", sequenceName = "seq_transportadora", allocationSize = 1)
    @Column(name = "id")
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



    @Column(name = "telefone_comercial")
    @Schema(description = "Telefone comercial alternativo", example = "(45) 3333-4444")
    private String telefoneComercial;

    @Column(name = "email_comercial")
    @Schema(description = "Email comercial alternativo", example = "contato@xyz.com.br")
    private String emailComercial;

    @Column(name = "cidade")
    @Schema(description = "Cidade da transportadora", example = "Toledo")
    private String cidade;

    @Column(name = "uf")
    @Schema(description = "UF da transportadora", example = "PR")
    private String uf;

    @Column(name = "endereco")
    @Schema(description = "Endereço da transportadora", example = "Rua Exemplo, 123")
    private String endereco;

    @Column(name = "categoria")
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

    @Column(name = "status", columnDefinition = "NVARCHAR(MAX)")
    @Schema(description = "Status da transportadora", example = "Ativo")
    private String status = "Ativo";

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}