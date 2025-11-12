package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.AgendaData;
import com.mwm.bioplanta.repository.AgendaDataRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/agenda")
@Tag(name = "Agendas", description = "Gerenciamento de agendas")
public class AgendaDataController {

	@Autowired
    private AgendaDataRepository agendaDataRepository;
	
    @GetMapping
    @Operation(summary = "Obter todas as agendas")
    public List<AgendaData> listarTodos() {
        return agendaDataRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova agenda")
    public AgendaData criarAgendaDataRepository(@RequestBody AgendaData agendaData) {
        return agendaDataRepository.save(agendaData);
    }
}
