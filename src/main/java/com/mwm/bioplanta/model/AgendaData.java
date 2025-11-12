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
@Schema(name = "Agenda Data", description = "Representa a agenda da semana.")
public class AgendaData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do item de agenda", example = "1")
	private Long id;
	@Schema(description = "Nome do cooperado", example = "Luiz Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
	private String cooperado;
	@Schema(description = "Quantidade de visitas agendada para Segunda-feira", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long seg;
	@Schema(description = "Quantidade de visitas agendada para Terça-feira", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long ter;
	@Schema(description = "Quantidade de visitas agendada para Quarta-feira", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long qua;
	@Schema(description = "Quantidade de visitas agendada para Quinta-feira", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long qui;
	@Schema(description = "Quantidade de visitas agendada para Sexta-feira", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long sex;
	@Schema(description = "Quantidade total", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long qtd;
	@Schema(description = "Odômetro", example = "10000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long km;
	@Schema(description = "Transportadora responsável", example = "Primato", requiredMode = Schema.RequiredMode.REQUIRED)
	private String transportadora;
	@Schema(description = "Status da agenda", example = "Realizado, Planejado", requiredMode = Schema.RequiredMode.REQUIRED)
	private String status;
}
