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
@Table(name = "bio_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Cliente", description = "Representa um cliente.")
public class BioCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bio_cliente_seq")
    @SequenceGenerator(name = "bio_cliente_seq", sequenceName = "bio_cliente_seq", allocationSize = 1)
    @Schema(description = "Identificador único do cliente", example = "1")
    private Long id;

    @Column(name = "razao_social")
    @Schema(description = "Razão social do cliente", example = "Empresa Exemplo Ltda", requiredMode = Schema.RequiredMode.REQUIRED)
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    @Schema(description = "Nome fantasia do cliente", example = "Empresa Exemplo")
    private String nomeFantasia;

    @Schema(description = "CNPJ do cliente", example = "12.345.678/0001-99")
    private String cnpj;

    @Schema(description = "Telefone do cliente", example = "45999470460")
    private String telefone;

    @Schema(description = "Email do cliente", example = "contato@empresa.com")
    private String email;

    @Schema(description = "Plano do cliente", example = "Premium")
    private String plano;

    @Schema(description = "Status ativo do cliente", example = "true")
    private Boolean ativo;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação", example = "2023-01-01T00:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização", example = "2023-01-01T00:00:00")
    private LocalDateTime atualizadoEm;
}