package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioUnidade;
import com.mwm.bioplanta.repository.cadastro.BioUnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BioUnidadeService {

    private final BioUnidadeRepository bioUnidadeRepository;

    public BioUnidadeService(BioUnidadeRepository bioUnidadeRepository) {
        this.bioUnidadeRepository = bioUnidadeRepository;
    }

    public List<BioUnidade> listarTodos() {
        return bioUnidadeRepository.findAll();
    }

    public Optional<BioUnidade> obterPorId(Long id) {
        return bioUnidadeRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioUnidade criar(BioUnidade bioUnidade) {
        return bioUnidadeRepository.save(java.util.Objects.requireNonNull(bioUnidade));
    }

    public Optional<BioUnidade> atualizar(Long id, BioUnidade bioUnidade) {
        if (!bioUnidadeRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioUnidade.setId(id);
        return Optional.of(bioUnidadeRepository.save(bioUnidade));
    }

    public boolean deletar(Long id) {
        if (!bioUnidadeRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioUnidadeRepository.deleteById(id);
        return true;
    }
}