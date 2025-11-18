package com.mwm.bioplanta.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.dto.DashboardDataResponseDTO;
import com.mwm.bioplanta.dto.VolumePorVeiculoResponseDTO;
import com.mwm.bioplanta.model.AbastecimentoReportItem;
import com.mwm.bioplanta.repository.AbastecimentoReportItemRepository;
import com.mwm.bioplanta.repository.AnaliseCooperadoRepository;
import com.mwm.bioplanta.repository.MetricRepository;
import com.mwm.bioplanta.repository.StockItemRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardDataController {

	@Autowired
	private MetricRepository metricRepository;
	
	@Autowired
	private StockItemRepository stockItemRepository;
	
	@Autowired
	private AnaliseCooperadoRepository analiseCooperadoRepository;
	
	@Autowired
	private AbastecimentoReportItemRepository abastecimentoReportItemRepository;
	
	@GetMapping
	public DashboardDataResponseDTO exibirDashboard() {
		DashboardDataResponseDTO dash = new DashboardDataResponseDTO();
		dash.setMetrics(metricRepository.findAll());
		dash.setStock(stockItemRepository.findAll());
		dash.setCooperativeAnalysis(analiseCooperadoRepository.findAll());
		/*
		Map<String, List<AbastecimentoReportItem>> agrupado = new HashMap<>();
		List<AbastecimentoReportItem> abasts = abastecimentoReportItemRepository.findAll();
		for (AbastecimentoReportItem item : abasts) {
			if(!agrupado.containsKey(item.getPlaca())) {
				List<AbastecimentoReportItem> itensPlaca = new ArrayList<AbastecimentoReportItem>();
				itensPlaca.add(item);
				agrupado.put(item.getPlaca(), itensPlaca);
			} else {
				List<AbastecimentoReportItem> itensPlaca = agrupado.get(item.getPlaca());
				itensPlaca.add(item);
			}
		}
		*/
		dash.setAbastecimentos(new ArrayList<VolumePorVeiculoResponseDTO>());
		return dash;
	}
}
