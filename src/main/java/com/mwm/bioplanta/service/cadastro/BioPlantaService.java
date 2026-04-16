package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioPlanta;
import com.mwm.bioplanta.repository.cadastro.BioPlantaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BioPlantaService {

    private final BioPlantaRepository bioPlantaRepository;

    public BioPlantaService(BioPlantaRepository bioPlantaRepository) {
        this.bioPlantaRepository = bioPlantaRepository;
    }

    public List<BioPlanta> listarTodos() {
        return bioPlantaRepository.findAll();
    }

    public Optional<BioPlanta> obterPorId(Long id) {
        return bioPlantaRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioPlanta criar(BioPlanta bioPlanta) {
        return bioPlantaRepository.save(java.util.Objects.requireNonNull(bioPlanta));
    }

    public Optional<BioPlanta> atualizar(Long id, BioPlanta bioPlanta) {
        if (!bioPlantaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioPlanta.setId(id);
        return Optional.of(bioPlantaRepository.save(bioPlanta));
    }

    public boolean deletar(Long id) {
        if (!bioPlantaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioPlantaRepository.deleteById(id);
        return true;
    }
}