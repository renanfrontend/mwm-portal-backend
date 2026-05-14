package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.FaturamentoItem;
import com.mwm.bioplanta.repository.FaturamentoItemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/faturamentos")
@Tag(name = "Faturamento", description = "Gerenciamento de faturamentos")
public class FaturamentoItemController {

	@Autowired
    private FaturamentoItemRepository faturamentoItemRepository;
	
    @GetMapping
    @Operation(summary = "Obter todas as agendas")
    public List<FaturamentoItem> listarTodos() {
        return faturamentoItemRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova agenda")
    public FaturamentoItem criarAgendaDataRepository(@RequestBody FaturamentoItem faturamentoItem) {
        return faturamentoItemRepository.save(faturamentoItem);
    }
}

