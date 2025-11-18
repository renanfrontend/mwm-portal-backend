package com.mwm.bioplanta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.QualidadeDejetosItem;
import com.mwm.bioplanta.repository.QualidadeDejetosItemRepository;

@RestController
@RequestMapping("/api/qualidade-dejetos")
public class QualidadeDejetosItemController {

	@Autowired
	private QualidadeDejetosItemRepository qualidadeDejetosItemRepository;
	
	@GetMapping
	public List<QualidadeDejetosItem> listarTodos() {
		return qualidadeDejetosItemRepository.findAll();
	}
	
	@PostMapping
	public QualidadeDejetosItem criarQualidadeDejetosItem(@RequestBody QualidadeDejetosItem qualidadeDejetosItem) {
		return qualidadeDejetosItemRepository.save(qualidadeDejetosItem);
	}
}
