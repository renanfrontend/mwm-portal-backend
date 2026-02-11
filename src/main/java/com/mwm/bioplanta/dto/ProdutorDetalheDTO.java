package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO detalhado do Produtor para exibição no formulário logística
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalhes completos do produtor para logística")
public class ProdutorDetalheDTO {

    @Schema(description = "ID do estabelecimento", example = "123")
    private Long id;

    @Schema(description = "Matrícula", example = "12345")
    private String matricula;

    @Schema(description = "Nome do estabelecimento", example = "Nome do Produtor")
    private String nome;

    @Schema(description = "Município", example = "Toledo")
    private String municipio;

    @Schema(description = "Status", example = "Ativo")
    private String status;

    @Schema(description = "Latitude", example = "-24.1234")
    private BigDecimal latitude;

    @Schema(description = "Longitude", example = "-53.1234")
    private BigDecimal longitude;

    @Schema(description = "Distância até a planta", example = "15km")
    private String distancia;

    @Schema(description = "Quantidade de lagoas", example = "2")
    private Integer qtdLagoas;

    @Schema(description = "Volume das lagoas", example = "5000")
    private String volLagoas;

    @Schema(description = "Fase/Modalidade", example = "GRSC")
    private String fase;

    @Schema(description = "Quantidade de cabeças", example = "1000")
    private Integer cabecas;

    @Schema(description = "Certificado", example = "Sim")
    private String certificado;

    @Schema(description = "Doam dejetos", example = "Sim")
    private String doamDejetos;

    @Schema(description = "Técnico responsável", example = "Nome do Técnico")
    private String tecnico;

    @Schema(description = "Responsável pelo estabelecimento", example = "Nome do Responsável")
    private String responsavel;

    @Schema(description = "Restrições operacionais", example = "Nenhuma restrição")
    private String restricoes;

    @Schema(description = "Localização (link ou descrição)", example = "Linha X")
    private String localizacao;

    @Schema(description = "Número do estabelecimento", example = "12345")
    private String numeroEstabelecimento;

    @Schema(description = "Número da propriedade", example = "67890")
    private String numeroPropriedade;

    @Schema(description = "Dados básicos do produtor")
    private BioProdutorSimplificadoDTO bioProdutor;
}
