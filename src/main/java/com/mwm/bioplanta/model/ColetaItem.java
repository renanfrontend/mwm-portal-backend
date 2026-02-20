package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coleta_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Coleta Item", description = "Representa um item de Coleta.")
public class ColetaItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do item de coleta", example = "1")
	private Long id;
	@Schema(description = "Cooperado da coleta", example = "Luiz Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String cooperado;
	@Schema(description = "Motorista da coleta", example = "João Neves", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String motorista;
	@Schema(description = "Tipo de veiculo da coleta", example = "Caminhão de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String tipoVeiculo;
	@Schema(description = "Placa do veiculo da coleta", example = "Caminhão de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String placa;
	@Schema(description = "Odômetro do veículo", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long odometro;
	@Schema(description = "Data prevista", example = "01/01/2025", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String dataPrevisao;
	@Schema(description = "Hora prevista", example = "HH:mm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String horaPrevisao;
	@Schema(description = "Status da coleta", example = "Pendente, Entregue, Atrasado", requiredMode = Schema.RequiredMode.REQUIRED)
	private String status;
}
