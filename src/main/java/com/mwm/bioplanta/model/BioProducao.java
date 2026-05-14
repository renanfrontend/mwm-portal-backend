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
@Table(name = "bio_producao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Producao", description = "Representa uma produção.")
public class BioProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_producao")
    @SequenceGenerator(name = "seq_bio_producao", sequenceName = "seq_bio_producao", allocationSize = 1)
    @Schema(description = "Identificador único da produção", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bio_estabelecimento_id", nullable = false)
    @Schema(description = "Estabelecimento associado", requiredMode = Schema.RequiredMode.REQUIRED)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private BioEstabelecimento bioEstabelecimento;

    @Column(name = "ano_safra")
    @Schema(description = "Ano da safra", example = "2024/2025")
    private String anoSafra;

    @Column(name = "modalidade_fase")
    @Schema(description = "Modalidade/Fase da produção", example = "GRSC")
    private String modalidadeFase;

     @Column(name = "certificacao")
     @Schema(description = "Possui certificação (S/N)", example = "S")
     private String certificacao;

     @Column(name = "doacao_dejetos")
     @Schema(description = "Doação de dejetos (S/N)", example = "N")
     private String doacaoDejetos;

    @Column(name = "numero_granja")
    @Schema(description = "Número da granja")
    private String numeroGranja;

    @Column(name = "quantidade_cabecas")
    @Schema(description = "Quantidade de cabeças", example = "1400")
    private Integer quantidadeCabecas;

    @Column(name = "qtd_lagoas")
    @Schema(description = "Quantidade de lagoas")
    private Integer qtdLagoas;

    @Column(name = "vol_lagoas")
    @Schema(description = "Volume das lagoas")
    private String volLagoas;

    @Column(name = "area_total")
    @Schema(description = "Área total")
    private Double areaTotal;

    @Column(name = "tecnico_responsavel")
    @Schema(description = "Técnico responsável")
    private String tecnicoResponsavel;

    @Column(name = "telefone_tecnico")
    @Schema(description = "Telefone do técnico")
    private String telefoneTecnico;

    @Column(name = "email_tecnico")
    @Schema(description = "Email do técnico")
    private String emailTecnico;

    @Column(name = "data_inicio_producao")
    @Schema(description = "Data início da produção")
    private LocalDate dataInicioProducao;

    @Column(name = "data_fim_producao")
    @Schema(description = "Data fim da produção")
    private LocalDate dataFimProducao;

    @Column(name = "observacoes")
    @Schema(description = "Observações")
    private String observacoes;

     @Column(name = "status")
     @Schema(description = "Status da produção", example = "A")
     private String status = "A";

    @Column(name = "criado_em")
    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
