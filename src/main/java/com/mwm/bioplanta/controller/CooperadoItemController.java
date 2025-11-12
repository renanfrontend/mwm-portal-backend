package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.CooperadoItem;
import com.mwm.bioplanta.repository.CooperadoItemRepository;

@RestController
@RequestMapping("/api/cooperados")
public class CooperadoItemController {

	@Autowired
    private CooperadoItemRepository cooperadoItemRepository;
	
    @GetMapping
    public List<CooperadoItem> listarTodos() {
        return cooperadoItemRepository.findAll();
    }

    @PostMapping
    public CooperadoItem criarCooperadoItemRepository(@RequestBody CooperadoItem cooperadoItem) {
        return cooperadoItemRepository.save(cooperadoItem);
    }
}