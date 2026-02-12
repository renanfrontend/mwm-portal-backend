package com.mwm.bioplanta.model;

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
@Table(name = "metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String icon;
	private String label;
	private String valor;
	private String trend;
	private String unit;
	
}
