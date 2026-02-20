package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperadoCreateDTO {

    @Schema(description = "ID da filiada", example = "1")
    private Long filiadaId;

    @Schema(description = "Matrícula do cooperado", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer matricula;

    @Schema(description = "ID da transportadora selecionada", example = "1")
    private Long transportadoraId;

    @Schema(description = "ID do tipo de veículo", example = "1")
    private Long tipoVeiculoId;

    @Schema(description = "Nome do cooperado/estabelecimento", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeCooperado;

    @Schema(description = "Nome do responsável (caso não seja o produtor)", example = "Maria Souza")
    private String responsavel;

    @Schema(description = "CPF ou CNPJ", example = "123.456.789-00")
    private String cpfCnpj;

    @Schema(description = "Placa do veículo", example = "ABC-1234")
    private String placa;

    @Schema(description = "Certificado", example = "Ativo", allowableValues = {"Ativo", "Inativo"})
    private String certificado;

    @Schema(description = "Doam dejetos", example = "Sim", allowableValues = {"Sim", "Não"})
    private String doamDejetos;

    @Schema(description = "Quantidade de lagoas", example = "2")
    private Integer qtdLagoas;

    @Schema(description = "Volume total das lagoas (m³)", example = "5000")
    private String volLagoas;

    @Schema(description = "Fase dos dejetos", example = "GRSC", allowableValues = {"Term. Frimesa", "GRSC", "Crechário", "UPD"})
    private String fase;

    @Schema(description = "Cabeças alojadas", example = "5000")
    private Integer cabecas;

    @Schema(description = "Técnico responsável", example = "Dr. José")
    private String tecnico;

    @Schema(description = "Telefone", example = "45999470460")
    private String telefone;

    @Schema(description = "Nº da propriedade", example = "PROP001")
    private String numPropriedade;

    @Schema(description = "Nº do estabelecimento", example = "EST001")
    private String numEstabelecimento;

    @Schema(description = "Município", example = "Toledo - PR")
    private String municipio;

    @Schema(description = "Localização/Endereço", example = "Linha São Paulo")
    private String localizacao;

    @Schema(description = "Latitude", example = "-24.725626")
    private java.math.BigDecimal latitude;

    @Schema(description = "Longitude", example = "-53.741378")
    private java.math.BigDecimal longitude;

    @Schema(description = "Distância em km do produtor até a planta", example = "12.50")
    private java.math.BigDecimal distanciaKm;

    @Schema(description = "Restrições operacionais", example = "Caminhão não entra com chuva")
    private String restricoes;
}
