package com.mwm.bioplanta.service.cadastro;

import com.mwm.bioplanta.dto.cadastro.FiliadaResponseDTO;
import com.mwm.bioplanta.model.BioFiliada;
import com.mwm.bioplanta.repository.cadastro.BioFiliadaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FiliadaService {

    private final BioFiliadaRepository bioFiliadaRepository;

    public FiliadaService(BioFiliadaRepository bioFiliadaRepository) {
        this.bioFiliadaRepository = bioFiliadaRepository;
    }

    /**
     * Buscar todas as filiadas ativas
     */
    public List<FiliadaResponseDTO> findAllAtivas() {
        List<BioFiliada> filiadas = bioFiliadaRepository.findByAtivo("S");
        return filiadas.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BioFiliada> listarTodas() {
        return bioFiliadaRepository.findAll();
    }

    public Optional<BioFiliada> obterPorId(Long id) {
        return bioFiliadaRepository.findById(java.util.Objects.requireNonNull(id));
    }

    public BioFiliada criar(BioFiliada bioFiliada) {
        return bioFiliadaRepository.save(java.util.Objects.requireNonNull(bioFiliada));
    }

    public Optional<BioFiliada> atualizar(Long id, BioFiliada bioFiliada) {
        if (!bioFiliadaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return Optional.empty();
        }

        bioFiliada.setId(id);
        return Optional.of(bioFiliadaRepository.save(bioFiliada));
    }

    public boolean deletar(Long id) {
        if (!bioFiliadaRepository.existsById(java.util.Objects.requireNonNull(id))) {
            return false;
        }

        bioFiliadaRepository.deleteById(id);
        return true;
    }

    /**
     * Converter entidade BioFiliada para FiliadaResponseDTO
     */
    private FiliadaResponseDTO converterParaResponseDTO(BioFiliada filiada) {
        FiliadaResponseDTO dto = new FiliadaResponseDTO();
        dto.setId(filiada.getId());
        dto.setCodigoFiliada(filiada.getCodigoFiliada());
        dto.setNome(filiada.getNome());
        dto.setEstado(filiada.getEstado());
        dto.setCidade(filiada.getCidade());
        return dto;
    }
}