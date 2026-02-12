package com.mwm.bioplanta.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
@Table(name = "abastecimento_report_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbastecimentoReportItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String status;
	private String cliente;
	private String veiculo;
	private String placa;
	private String data;
	private String horaInicio;
	private String horaTermino;
	@Column(precision = 10, scale = 2)
	private BigDecimal volume;
	@Column(precision = 10, scale = 2)
	private Long odometro;
	private String usuario;
	private String produto;
	
}
