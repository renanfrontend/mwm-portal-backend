package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.model.BioCliente;
import com.mwm.bioplanta.repository.cadastro.BioClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BioClienteService {

    private final BioClienteRepository bioClienteRepository;

    public BioClienteService(BioClienteRepository bioClienteRepository) {
        this.bioClienteRepository = bioClienteRepository;
    }

    public List<BioCliente> listarTodos() {
        return bioClienteRepository.findAll();
    }

    public Optional<BioCliente> obterPorId(Long id) {
        return bioClienteRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioCliente criar(BioCliente bioCliente) {
        return bioClienteRepository.save(java.util.Objects.requireNonNull(bioCliente));
    }

    public Optional<BioCliente> atualizar(Long id, BioCliente bioCliente) {
        if (!bioClienteRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioCliente.setId(id);
        return Optional.of(bioClienteRepository.save(bioCliente));
    }

    public boolean deletar(Long id) {
        if (!bioClienteRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioClienteRepository.deleteById(id);
        return true;
    }
}