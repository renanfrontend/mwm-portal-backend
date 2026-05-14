package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.PortariaItem;
import com.mwm.bioplanta.repository.PortariaItemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/portaria")
@Tag(name = "Portaria", description = "Gerenciamento da portaria.")
public class PortariaItemController {

	@Autowired
    private PortariaItemRepository portariaItemRepository;
	
    @GetMapping
    @Operation(summary = "Obter todas as entradas na portaria")
    public List<PortariaItem> listarTodos() {
        return portariaItemRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova entrada na portaria")
    public PortariaItem criarPortariaItemRepository(@RequestBody PortariaItem portariaItem) {
        return portariaItemRepository.save(portariaItem);
    }
}
