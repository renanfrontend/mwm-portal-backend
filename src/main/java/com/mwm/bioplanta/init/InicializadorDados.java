package com.mwm.bioplanta.init;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;

import com.mwm.bioplanta.model.AbastecimentoReportItem;
import com.mwm.bioplanta.repository.AbastecimentoReportItemRepository;

public class InicializadorDados implements CommandLineRunner {
	private final AbastecimentoReportItemRepository abastecimentoReportItemRepository;
	
    public InicializadorDados(AbastecimentoReportItemRepository abastecimentoReportItemRepository) {
        this.abastecimentoReportItemRepository = abastecimentoReportItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    	//abastecimentoReportItemRepository.save(new AbastecimentoReportItem(Long.parseLong("50"), "Concluído", "Primato Cooperativa Agroindustrial", "Caminhão (Ração)", "BCK-0138", "2025-09-25", "17:09:21", "17:09:21", new BigDecimal("134.56"), new BigDecimal("391396"), "vanessa", "Biometano"));
    }
}
