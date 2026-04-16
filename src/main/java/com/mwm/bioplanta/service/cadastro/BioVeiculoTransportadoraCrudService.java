package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BioVeiculoTransportadoraCrudService {

    private final BioVeiculoTransportadoraRepository bioVeiculoTransportadoraRepository;

    public BioVeiculoTransportadoraCrudService(BioVeiculoTransportadoraRepository bioVeiculoTransportadoraRepository) {
        this.bioVeiculoTransportadoraRepository = bioVeiculoTransportadoraRepository;
    }

    public List<BioVeiculoTransportadora> listarTodos() {
        return bioVeiculoTransportadoraRepository.findAllExcluindoEntregaDejetos();
    }

    public Optional<BioVeiculoTransportadora> obterPorId(Long id) {
        return bioVeiculoTransportadoraRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioVeiculoTransportadora criar(BioVeiculoTransportadora bioVeiculoTransportadora) {
        return bioVeiculoTransportadoraRepository.save(java.util.Objects.requireNonNull(bioVeiculoTransportadora));
    }

    public Optional<BioVeiculoTransportadora> atualizar(Long id, BioVeiculoTransportadora bioVeiculoTransportadora) {
        if (!bioVeiculoTransportadoraRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioVeiculoTransportadora.setId(id);
        return Optional.of(bioVeiculoTransportadoraRepository.save(bioVeiculoTransportadora));
    }

    public boolean deletar(Long id) {
        if (!bioVeiculoTransportadoraRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioVeiculoTransportadoraRepository.deleteById(id);
        return true;
    }
}