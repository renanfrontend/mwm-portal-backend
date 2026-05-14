package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.FiliadaResponseDTO;
import com.mwm.bioplanta.model.BioFiliada;
import com.mwm.bioplanta.repository.BioFiliadaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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