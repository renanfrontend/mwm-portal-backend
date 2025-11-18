package com.mwm.bioplanta.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.dto.VolumePorDiaResponseDTO;
import com.mwm.bioplanta.dto.VolumePorMesResponseDTO;
import com.mwm.bioplanta.model.AbastecimentoReportItem;
import com.mwm.bioplanta.repository.AbastecimentoReportItemRepository;

@RestController
@RequestMapping("/api/abastecimentos")
public class AbastecimentoReportItemController {

	@Autowired
    private AbastecimentoReportItemRepository abastecimentoReportItemRepository;
	
    @GetMapping
    public List<AbastecimentoReportItem> listarTodos() {
        return abastecimentoReportItemRepository.findAll();
    }
    
    @GetMapping("/report")
    public List<AbastecimentoReportItem> listarPorPeriodo(@RequestParam("startDate") String dataInicial, @RequestParam("endDate") String dataFinal) {
    	return abastecimentoReportItemRepository.findAll();
    }

    @PostMapping
    public AbastecimentoReportItem criarAbastecimentoReportItemRepository(@RequestBody AbastecimentoReportItem abastecimentoReportItem) {
        return abastecimentoReportItemRepository.save(abastecimentoReportItem);
    }
    
    @GetMapping("/volume-por-mes")
    public List<VolumePorMesResponseDTO> listarVolumePorMes() {
    	List<AbastecimentoReportItem> todos = abastecimentoReportItemRepository.findAll();
    	BigDecimal volumeTotal = new BigDecimal(0);
    	for (AbastecimentoReportItem item : todos) {
    		volumeTotal = volumeTotal.add(item.getVolume());
    	}
    	List<VolumePorMesResponseDTO> volumes = new ArrayList<VolumePorMesResponseDTO>();
    	VolumePorMesResponseDTO setembro = new VolumePorMesResponseDTO();
    	setembro.setName("Setembro");
    	setembro.setVolume(volumeTotal);
    	volumes.add(setembro);
    	return volumes;
    }
    
    @GetMapping("/volume-por-dia")
    public List<VolumePorDiaResponseDTO> listarVolumePorDia(@RequestParam("startDate") String dataInicial, @RequestParam("endDate") String dataFinal) {
    	List<AbastecimentoReportItem> todos = abastecimentoReportItemRepository.findAll();
    	List<VolumePorDiaResponseDTO> volumes = new ArrayList<VolumePorDiaResponseDTO>();
    	for (AbastecimentoReportItem item : todos) {
    		VolumePorDiaResponseDTO dia = new VolumePorDiaResponseDTO();
    		dia.setData(item.getData());
    		dia.setVolumeTotal(item.getVolume());
    		volumes.add(dia);
    	}
    	return volumes;
    }
    
    @PostMapping("/report")
    public AbastecimentoReportItem criarAbastecimentoReportItemReport(@RequestBody AbastecimentoReportItem abastecimentoReportItem) {
    	return abastecimentoReportItemRepository.save(abastecimentoReportItem);
    }
}
