package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_produtor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Produtor", description = "Representa um produtor.")
public class BioProdutor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_produtor")
    @SequenceGenerator(name = "seq_bio_produtor", sequenceName = "seq_bio_produtor", allocationSize = 1)
    @Schema(description = "Identificador único do produtor", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bio_filiada_id", nullable = false)
    @Schema(description = "Filiada associada", requiredMode = Schema.RequiredMode.REQUIRED)
    private BioFiliada bioFiliada;

    @Column(name = "codigo_produtor")
    @Schema(description = "Código do produtor", example = "PROD001")
    private String codigoProdutor;

    @Schema(description = "Nome do produtor", example = "JOAO DA SILVA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name = "cpf_cnpj", unique = true)
    @Schema(description = "CPF ou CNPJ do produtor", example = "123.456.789-00")
    private String cpfCnpj;

    @Column(name = "telefone_principal")
    @Schema(description = "Telefone principal")
    private String telefonePrincipal;

    @Column(name = "telefone_secundario")
    @Schema(description = "Telefone secundário")
    private String telefoneSecundario;

    @Schema(description = "Email")
    private String email;

    @Column(name = "tipo_pessoa")
    @Schema(description = "Tipo de pessoa (PF/PJ)")
    private String tipoPessoa;


    @Column(name = "status")
    @Schema(description = "Status do produtor (A/I)")
    private String status;

    @Column(name = "distancia_km", precision = 8, scale = 2)
    @Schema(description = "Distância em km do produtor até a planta", example = "12.50")
    private java.math.BigDecimal distanciaKm;

    @Column(name = "data_cadastro")
    @Schema(description = "Data de cadastro")
    private LocalDate dataCadastro;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
        @Column(name = "certificado")
        @Schema(description = "Produtor certificado (N/S)", example = "N")
        private String certificado;
        @Column(name = "doam_dejetos")
        @Schema(description = "Produtor doa dejetos (S/N)", example = "S")
        private String doamDejetos;
}
