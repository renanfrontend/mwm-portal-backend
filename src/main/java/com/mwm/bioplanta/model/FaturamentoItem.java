package com.mwm.bioplanta.model;

import java.math.BigDecimal;

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
@Schema(name = "Faturamento Item", description = "Representa um item de Faturamento.")
public class FaturamentoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Identificador único do item de faturamento", example = "1")
	private Long id;
	@Schema(description = "Mês da fatura", example = "Janeiro", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;
	@Schema(description = "Faturamento do mês", example = "2637.99", requiredMode = Schema.RequiredMode.REQUIRED)
	private BigDecimal faturamento;
	@Schema(description = "Rótulo", example = "3.50", requiredMode = Schema.RequiredMode.REQUIRED)
	private String label;
}
