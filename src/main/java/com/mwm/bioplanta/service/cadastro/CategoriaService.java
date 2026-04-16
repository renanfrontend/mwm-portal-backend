package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioCategoria;
import com.mwm.bioplanta.repository.cadastro.BioCategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final BioCategoriaRepository bioCategoriaRepository;

    public CategoriaService(BioCategoriaRepository bioCategoriaRepository) {
        this.bioCategoriaRepository = bioCategoriaRepository;
    }

    public List<BioCategoria> listarTodas() {
        return bioCategoriaRepository.findAll();
    }
}