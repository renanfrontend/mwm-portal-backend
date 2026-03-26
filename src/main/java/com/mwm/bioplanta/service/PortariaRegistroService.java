package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.*;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.PortariaRegistroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para Portaria Registro
 * Responsável pela lógica de negócio de registros de portaria
 * 
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Slf4j
@Service
@Transactional
public class PortariaRegistroService {

    @Autowired
    private PortariaRegistroRepository portariaRegistroRepository;

    /**
     * Lista todos os registros com paginação
     * 
     * @param page página (0-indexed)
     * @param pageSize quantidade de itens por página
     * @return resposta com dados paginados
     */
    public PaginationResponseDTO<PortariaRegistroDTO> listRegistros(Integer page, Integer pageSize) {
        log.info("Listando registros de portaria - página: {}, tamanho: {}", page, pageSize);

        try {
            int pageNum = page != null ? page : 0;
            int size = pageSize != null ? pageSize : 5;
            
            Pageable pageable = PageRequest.of(pageNum, size);
            Page<PortariaRegistro> pageResult = portariaRegistroRepository.findAll(pageable);
            
            List<PortariaRegistroDTO> dtos = pageResult.getContent()
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            PaginationResponseDTO<PortariaRegistroDTO> response = new PaginationResponseDTO<>();
            response.setData(dtos);
            response.setPage(pageResult.getNumber());
            response.setPageSize(pageResult.getSize());
            response.setTotal(pageResult.getTotalElements());
            response.setTotalPages(pageResult.getTotalPages());
            response.setHasNext(pageResult.hasNext());
            response.setHasPrevious(pageResult.hasPrevious());

            log.info("Registros listados com sucesso - total: {}", pageResult.getTotalElements());
            return response;

        } catch (Exception e) {
            log.error("Erro ao listar registros de portaria", e);
            throw new RuntimeException("Erro ao buscar registros: " + e.getMessage());
        }
    }

    /**
     * Obtém um registro por ID
     * 
     * @param id ID do registro
     * @return dados do registro
     */
    public PortariaRegistroDTO getRegistroById(Long id) {
        log.info("Buscando registro de portaria por ID: {}", id);

        try {
            PortariaRegistro registro = portariaRegistroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registro não encontrado com ID: " + id));
            
            return mapToDTO(registro);

        } catch (Exception e) {
            log.error("Erro ao buscar registro com ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar registro: " + e.getMessage());
        }
    }

    /**
     * Cria um novo registro
     * 
     * @param registroDTO dados do novo registro
     * @return registro criado
     */
    public PortariaRegistroDTO createRegistro(PortariaRegistroDTO registroDTO) {
        log.info("Criando novo registro de portaria - tipo: {}", registroDTO.getTipoRegistro());

        try {
            PortariaRegistro registro = new PortariaRegistro();
            registro.setDataEntrada(LocalDate.parse(registroDTO.getDataEntrada()));
            registro.setHoraEntrada(LocalTime.parse(registroDTO.getHoraEntrada()));
            registro.setTipoRegistro(registroDTO.getTipoRegistro());
            registro.setStatus(registroDTO.getStatus() != null ? registroDTO.getStatus() : "Em andamento");
            registro.setOrigemEntrada(registroDTO.getOrigemEntrada() != null ? registroDTO.getOrigemEntrada() : "ESPONTANEA");
            registro.setObservacoes(registroDTO.getObservacoes());
            registro.setResponsavelId(registroDTO.getResponsavelId());
            registro.setCriadoEm(LocalDateTime.now());
            registro.setAtualizadoEm(LocalDateTime.now());
            
            PortariaRegistro saved = portariaRegistroRepository.save(registro);
            
            log.info("Registro de portaria criado com ID: {}", saved.getId());
            return mapToDTO(saved);

        } catch (Exception e) {
            log.error("Erro ao criar registro de portaria", e);
            throw new RuntimeException("Erro ao criar registro: " + e.getMessage());
        }
    }

    /**
     * Atualiza um registro existente
     * 
     * @param id ID do registro
     * @param registroDTO novos dados
     * @return registro atualizado
     */
    public PortariaRegistroDTO updateRegistro(Long id, PortariaRegistroDTO registroDTO) {
        log.info("Atualizando registro de portaria - ID: {}", id);

        try {
            PortariaRegistro registro = portariaRegistroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registro não encontrado com ID: " + id));
            
            if (registroDTO.getDataEntrada() != null) {
                registro.setDataEntrada(LocalDate.parse(registroDTO.getDataEntrada()));
            }
            if (registroDTO.getHoraEntrada() != null) {
                registro.setHoraEntrada(LocalTime.parse(registroDTO.getHoraEntrada()));
            }
            if (registroDTO.getStatus() != null) {
                registro.setStatus(registroDTO.getStatus());
            }
            if (registroDTO.getDataSaida() != null) {
                registro.setDataSaida(LocalDate.parse(registroDTO.getDataSaida()));
            }
            if (registroDTO.getHoraSaida() != null) {
                registro.setHoraSaida(LocalTime.parse(registroDTO.getHoraSaida()));
            }
            if (registroDTO.getObservacoes() != null) {
                registro.setObservacoes(registroDTO.getObservacoes());
            }
            
            registro.setAtualizadoEm(LocalDateTime.now());
            PortariaRegistro updated = portariaRegistroRepository.save(registro);
            
            log.info("Registro de portaria atualizado - ID: {}", updated.getId());
            return mapToDTO(updated);

        } catch (Exception e) {
            log.error("Erro ao atualizar registro com ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar registro: " + e.getMessage());
        }
    }

    /**
     * Deleta um registro
     * 
     * @param id ID do registro a deletar
     */
    public void deleteRegistro(Long id) {
        log.info("Deletando registro de portaria - ID: {}", id);

        try {
            PortariaRegistro registro = portariaRegistroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registro não encontrado com ID: " + id));
            
            portariaRegistroRepository.delete(registro);
            
            log.info("Registro de portaria deletado - ID: {}", id);

        } catch (Exception e) {
            log.error("Erro ao deletar registro com ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar registro: " + e.getMessage());
        }
    }

    /**
     * Deleta múltiplos registros
     * 
     * @param ids lista de IDs a deletar
     */
    public void deleteMultipleRegistros(List<Long> ids) {
        log.info("Deletando {} registros de portaria", ids.size());

        try {
            List<PortariaRegistro> registros = portariaRegistroRepository.findAllById(ids);
            
            if (registros.isEmpty()) {
                throw new RuntimeException("Nenhum registro encontrado para os IDs fornecidos");
            }
            
            portariaRegistroRepository.deleteAll(registros);
            
            log.info("{} registros de portaria deletados", registros.size());

        } catch (Exception e) {
            log.error("Erro ao deletar múltiplos registros", e);
            throw new RuntimeException("Erro ao deletar registros: " + e.getMessage());
        }
    }

    /**
     * Mapeia PortariaRegistro para PortariaRegistroDTO
     */
    private PortariaRegistroDTO mapToDTO(PortariaRegistro registro) {
        PortariaRegistroDTO dto = new PortariaRegistroDTO();
        dto.setId(registro.getId());
        dto.setDataEntrada(registro.getDataEntrada() != null ? registro.getDataEntrada().toString() : null);
        dto.setHoraEntrada(registro.getHoraEntrada() != null ? registro.getHoraEntrada().toString() : null);
        dto.setTipoRegistro(registro.getTipoRegistro());
        dto.setStatus(registro.getStatus());
        dto.setDataSaida(registro.getDataSaida() != null ? registro.getDataSaida().toString() : null);
        dto.setHoraSaida(registro.getHoraSaida() != null ? registro.getHoraSaida().toString() : null);
        dto.setOrigemEntrada(registro.getOrigemEntrada());
        dto.setObservacoes(registro.getObservacoes());
        dto.setResponsavelId(registro.getResponsavelId());
        dto.setAgendaRealizadaId(registro.getAgendaRealizadaId());
        dto.setCriadoEm(registro.getCriadoEm());
        dto.setAtualizadoEm(registro.getAtualizadoEm());
        return dto;
    }
}
