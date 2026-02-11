package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bio_estabelecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Estabelecimento", description = "Representa um estabelecimento.")
public class BioEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_estabelecimento")
    @SequenceGenerator(name = "seq_bio_estabelecimento", sequenceName = "seq_bio_estabelecimento", allocationSize = 1)
    @Schema(description = "Identificador único do estabelecimento", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bio_produtor_id", nullable = false)
    @Schema(description = "Produtor associado", requiredMode = Schema.RequiredMode.REQUIRED)
    private BioProdutor bioProdutor;

    @OneToMany(mappedBy = "bioEstabelecimento", fetch = FetchType.LAZY)
    @Schema(description = "Produções associadas ao estabelecimento")
    private List<BioProducao> bioProducao;

    @Column(name = "codigo_estabelecimento")
    @Schema(description = "Código interno do estabelecimento", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigoEstabelecimento;

    @Column(name = "numero_estabelecimento", unique = true)
    @Schema(description = "Número do estabelecimento", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroEstabelecimento;

    @Column(name = "numero_propriedade")
    @Schema(description = "Número da propriedade")
    private String numeroPropriedade;

    @Schema(description = "Matrícula", example = "MAT-001")
    private String matricula;

    @Column(name = "nome_propriedade")
    @Schema(description = "Nome do estabelecimento")
    private String nome;

    @Column(name = "responsavel")
    @Schema(description = "Nome do responsável")
    private String responsavel;

    @Schema(description = "Endereço")
    private String endereco;
    
    // Telefone removido pois não existe na tabela bio_estabelecimento
    // @Schema(description = "Telefone")
    // private String telefone;

    // Campos migrados de BioPropriedade que fazem sentido estar no local físico (Estabelecimento)
    @Schema(description = "Município")
    private String municipio;

     @Column(name = "estado")
     @Schema(description = "Estado")
     private String estado;

    @Schema(description = "CEP")
    private String cep;

    @Column(name = "localizacao_link")
    @Schema(description = "Link de localização")
    private String localizacaoLink;

     @Column(name = "status")
     @Schema(description = "Status (A/I)")
     private String status = "Ativo";

    @Column(precision = 18, scale = 10)
    @Schema(description = "Latitude")
    private java.math.BigDecimal latitude;

    @Column(precision = 18, scale = 10)
    @Schema(description = "Longitude")
    private java.math.BigDecimal longitude;

    @Column(name = "distancia")
    @Schema(description = "Distância até a planta")
    private String distancia;

    @Column(name = "restricoes")
    @Schema(description = "Restrições operacionais")
    private String restricoes;

    @Column(name = "criado_em")
    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;
}
