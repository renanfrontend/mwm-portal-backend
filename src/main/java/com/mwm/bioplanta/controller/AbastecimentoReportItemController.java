package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
