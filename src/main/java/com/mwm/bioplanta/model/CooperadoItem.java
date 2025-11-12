package com.mwm.bioplanta.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Cooperado Item", description = "Representa um item de Cooperado.")
public class CooperadoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do item de cooperado", example = "1")
	private Long id;//TODO Renan avisar que não é String
	@Schema(description = "Número de matrícula", example = "102646", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long matricula;
	@Schema(description = "Filiado", example = "Primato", requiredMode = Schema.RequiredMode.REQUIRED)
	private String filial;
	@Schema(description = "Motorista", example = "Renato Ivan", requiredMode = Schema.RequiredMode.REQUIRED)
	private String motorista;
	@Schema(description = "Tipo de Veículo", example = "Caminhão de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String tipoVeiculo;
	@Schema(description = "Placa", example = "ABC-1D23", requiredMode = Schema.RequiredMode.REQUIRED)
	private String placa;
	@Schema(description = "Certificado", example = "Ativo", requiredMode = Schema.RequiredMode.REQUIRED)
	private String certificado;
	@Schema(description = "Doam Dejetos", example = "Sim", requiredMode = Schema.RequiredMode.REQUIRED)
	private String doamDejetos;
	@Schema(description = "Fase de produção", example = "Crechário", requiredMode = Schema.RequiredMode.REQUIRED)
	private String fase;
	@Schema(description = "CNPJ", example = "069.037.349-02", requiredMode = Schema.RequiredMode.REQUIRED)
	private String cnpj;
	@Schema(description = "Número Granja", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long numGranja;
	@Schema(description = "Quantidade de cabeças alojadas", example = "1450", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long qtdCabecas;
	@Schema(description = "Município", example = "Toledo", requiredMode = Schema.RequiredMode.REQUIRED)
	private String municipio;
	@Schema(description = "Localização", example = "https://www.google.com/maps/dir/-24.7229319,-53.8641137", requiredMode = Schema.RequiredMode.REQUIRED)
	private String localizacao;
	@Schema(description = "Latitude", example = "-24,7229319", requiredMode = Schema.RequiredMode.REQUIRED)
	private BigDecimal latitude;//TODO definir limite
	@Schema(description = "Longitude", example = "-53,8641137", requiredMode = Schema.RequiredMode.REQUIRED)
	private BigDecimal longitude;//TODO definir limite
	@Schema(description = "Técnico", example = "Daniel", requiredMode = Schema.RequiredMode.REQUIRED)
	private String tecnico;
	@Schema(description = "Telefone", example = "4533761170", requiredMode = Schema.RequiredMode.REQUIRED)
	private String telefone;
	@Schema(description = "Número da propriedade", example = "41277001602", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long numPropriedade;
	@Schema(description = "Número do estabelecimento", example = "41000592839", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long numEstabelecimento;
}
