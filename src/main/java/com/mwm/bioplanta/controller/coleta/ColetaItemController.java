package com.mwm.bioplanta.controller.coleta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mwm.bioplanta.model.ColetaItem;
import com.mwm.bioplanta.repository.coleta.ColetaItemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/coletas")
@Tag(name = "Coletas", description = "Gerenciamento de coletas.")
public class ColetaItemController {

	@Autowired
    private ColetaItemRepository coletaItemRepository;
	
    @GetMapping
    @Operation(summary = "Obter todas as coletas")
    public List<ColetaItem> listarTodos() {
        return coletaItemRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar uma nova coleta")
    public ColetaItem criarColetaItemRepository(@RequestBody ColetaItem coletaItem) {
        return coletaItemRepository.save(coletaItem);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ColetaItem> atualizarColetaItem(@PathVariable Long id, @RequestBody ColetaItem coletaItem) {
    	ColetaItem coletaItemExistente = coletaItemRepository.findById(id).get();//TODO melhorar caso não exista o id pois esse pode lançar NoSuchElementException 
    	
    	if (coletaItemExistente == null) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	coletaItem.setId(coletaItemExistente.getId());
    	ColetaItem coletaItemAtualizado = coletaItemRepository.save(coletaItem);
    	return ResponseEntity.ok(coletaItemAtualizado);
    }
}
