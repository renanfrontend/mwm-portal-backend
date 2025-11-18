package com.mwm.bioplanta.dto;

import java.util.List;

import com.mwm.bioplanta.model.AbastecimentoReportItem;
import com.mwm.bioplanta.model.AnaliseCooperado;
import com.mwm.bioplanta.model.Metric;
import com.mwm.bioplanta.model.StockItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataResponseDTO {
	private List<Metric> metrics;
	private List<StockItem> stock;
	private List<AnaliseCooperado> cooperativeAnalysis;
	private List<VolumePorVeiculoResponseDTO> abastecimentos;
}
