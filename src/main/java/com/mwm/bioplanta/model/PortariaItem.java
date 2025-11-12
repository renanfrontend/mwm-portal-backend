package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Portaria Item", description = "Representa uma entrada da portaria.")
public class PortariaItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do item de portaria", example = "1")
	private Long id;
	@Schema(description = "Categoria da entrada", example = "Entregas, Abastecimentos, Coletas, Visitas", requiredMode = Schema.RequiredMode.REQUIRED)
	private String categoria;
	@Schema(description = "Data da entrada", example = "01/01/2025", requiredMode = Schema.RequiredMode.REQUIRED)
	private String data;
	@Schema(description = "Hora da entrada", example = "HH:mm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String horario;
	@Schema(description = "Empresa", example = "Primato", requiredMode = Schema.RequiredMode.REQUIRED)
	private String empresa;
	@Schema(description = "Motorista da coleta", example = "João Neves", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String motorista;
	@Schema(description = "Tipo de veiculo da coleta", example = "Caminhão de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String tipoVeiculo;
	@Schema(description = "Placa do veiculo da coleta", example = "Caminhão de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String placa;
	@Schema(description = "Atividade realizada", example = "Entrega de dejetos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String atividade;
	@Schema(description = "Status da entrada", example = "Concluído, Pendente", requiredMode = Schema.RequiredMode.REQUIRED)
	private String status;
}

