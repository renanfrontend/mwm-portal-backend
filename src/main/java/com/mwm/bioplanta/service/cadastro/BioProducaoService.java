package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioProducao;
import com.mwm.bioplanta.repository.cadastro.BioProducaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BioProducaoService {

    private final BioProducaoRepository bioProducaoRepository;

    public BioProducaoService(BioProducaoRepository bioProducaoRepository) {
        this.bioProducaoRepository = bioProducaoRepository;
    }

    public List<BioProducao> listarTodos() {
        return bioProducaoRepository.findAll();
    }

    public Optional<BioProducao> obterPorId(Long id) {
        return bioProducaoRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioProducao criar(BioProducao bioProducao) {
        return bioProducaoRepository.save(java.util.Objects.requireNonNull(bioProducao));
    }

    public Optional<BioProducao> atualizar(Long id, BioProducao bioProducao) {
        if (!bioProducaoRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioProducao.setId(id);
        return Optional.of(bioProducaoRepository.save(bioProducao));
    }

    public boolean deletar(Long id) {
        if (!bioProducaoRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioProducaoRepository.deleteById(id);
        return true;
    }
}