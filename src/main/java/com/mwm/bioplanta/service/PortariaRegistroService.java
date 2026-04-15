package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.*;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.BioTransportadoraRepository;
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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

    @Autowired
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;

    @Autowired
    private BioPortariaAbastecimentoRepository abastecimentoRepository;
    
    @Autowired
    private BioVeiculoTransportadoraRepository veiculoRepository;
    
    @Autowired
    private BioTransportadoraRepository transportadoraRepository;

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

            Pageable pageable = PageRequest.of(pageNum, size, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "dataEntrada", "horaEntrada"));
            Page<PortariaRegistro> pageResult = portariaRegistroRepository.findAll(pageable);

                Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId = carregarAbastecimentos(pageResult.getContent());
                Map<Long, com.mwm.bioplanta.model.BioPortariaEntregaDejetos> entregasPorId = carregarEntregas(pageResult.getContent());
                Map<Long, com.mwm.bioplanta.model.BioVeiculoTransportadora> veiculosPorId = carregarVeiculos(abastecimentosPorRegistroId, entregasPorId);

            List<PortariaRegistroDTO> dtos = pageResult.getContent()
                    .stream()
                    .map(registro -> mapToDTO(registro, abastecimentosPorRegistroId, entregasPorId, veiculosPorId))
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
        log.info("📥 RegistroDTO recebido: entrega_dejetos={}, tipo={}", registroDTO.getEntrega_dejetos(), registroDTO.getTipoRegistro());

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
            
            // Salvar densidade em entrega_dejetos se aplicável
            log.info("ENTREGA_DEJETOS check - tipo: {}, dejetoId: {}, entrega_dejetos: {}", 
                updated.getTipoRegistro(), updated.getEntregaDejetosId(), registroDTO.getEntrega_dejetos());
            if ("ENTREGA_DEJETOS".equals(updated.getTipoRegistro()) && 
                updated.getEntregaDejetosId() != null &&
                registroDTO.getEntrega_dejetos() != null &&
                registroDTO.getEntrega_dejetos().getDensidade() != null) {
                log.info("Salvando densidade: {}", registroDTO.getEntrega_dejetos().getDensidade());
                
                var entregaDejetos = entregaDejetosRepository.findById(updated.getEntregaDejetosId()).orElse(null);
                if (entregaDejetos != null) {
                    entregaDejetos.setDensidade(registroDTO.getEntrega_dejetos().getDensidade());
                    entregaDejetos.setAtualizadoEm(LocalDateTime.now());
                    entregaDejetosRepository.save(entregaDejetos);
                    entregaDejetosRepository.flush();
                }
            }
            
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
     * Busca dados relacionados de acordo com o tipo de registro
     */
    private PortariaRegistroDTO mapToDTO(PortariaRegistro registro) {
        Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId = "ABASTECIMENTO".equals(registro.getTipoRegistro())
            ? abastecimentoRepository.findByRegistroId(registro.getId())
                .map(abastecimento -> Collections.singletonMap(registro.getId(), abastecimento))
                .orElseGet(Collections::emptyMap)
            : Collections.emptyMap();

        Map<Long, com.mwm.bioplanta.model.BioPortariaEntregaDejetos> entregasPorId = "ENTREGA_DEJETOS".equals(registro.getTipoRegistro())
            && registro.getEntregaDejetosId() != null
            ? entregaDejetosRepository.findById(registro.getEntregaDejetosId())
                .map(entrega -> Collections.singletonMap(registro.getEntregaDejetosId(), entrega))
                .orElseGet(Collections::emptyMap)
            : Collections.emptyMap();

        Map<Long, com.mwm.bioplanta.model.BioVeiculoTransportadora> veiculosPorId = carregarVeiculos(abastecimentosPorRegistroId, entregasPorId);

        return mapToDTO(registro, abastecimentosPorRegistroId, entregasPorId, veiculosPorId);
        }

        private PortariaRegistroDTO mapToDTO(
            PortariaRegistro registro,
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
            Map<Long, com.mwm.bioplanta.model.BioPortariaEntregaDejetos> entregasPorId,
            Map<Long, com.mwm.bioplanta.model.BioVeiculoTransportadora> veiculosPorId) {
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
        
         // Buscar dados relacionados de acordo com o tipo de registro
         if ("ABASTECIMENTO".equals(registro.getTipoRegistro())) {
             BioPortariaAbastecimento abastecimento = abastecimentosPorRegistroId.get(registro.getId());
             if (abastecimento != null) {
                 PortariaAbastecimentoDTO abastecimentoDTO = new PortariaAbastecimentoDTO();
                 abastecimentoDTO.setId(abastecimento.getId());
                 abastecimentoDTO.setRegistroId(abastecimento.getRegistroId());
                 abastecimentoDTO.setMotoristaId(abastecimento.getMotoristaId());
                 abastecimentoDTO.setMotoristaNome(abastecimento.getMotoristaNome());
                 abastecimentoDTO.setCpfMotorista(abastecimento.getCpfMotorista());
                 abastecimentoDTO.setTransportadoraId(abastecimento.getTransportadoraId());
                 abastecimentoDTO.setTransportadoraManual(abastecimento.getTransportadoraManual());
                 abastecimentoDTO.setVeiculoId(abastecimento.getVeiculoId());
                 if (abastecimento.getPlaca() != null && !abastecimento.getPlaca().isBlank()) {
                     abastecimentoDTO.setPlaca(abastecimento.getPlaca());
                 } else if (abastecimento.getVeiculoId() != null) {
                     var veiculo = veiculosPorId.get(abastecimento.getVeiculoId());
                     if (veiculo != null) {
                         abastecimentoDTO.setPlaca(veiculo.getPlaca());
                     }
                 }
                 abastecimentoDTO.setPlacaManual(abastecimento.getPlacaManual());
                 abastecimentoDTO.setTipoVeiculo(abastecimento.getTipoVeiculo());
                 abastecimentoDTO.setPesoInicial(abastecimento.getPesoInicial());
                 abastecimentoDTO.setPesoFinal(abastecimento.getPesoFinal());
                 dto.setAbastecimento(abastecimentoDTO);
             }
         }

         if ("ENTREGA_DEJETOS".equals(registro.getTipoRegistro()) && registro.getEntregaDejetosId() != null) {
             var entregaDejetos = entregasPorId.get(registro.getEntregaDejetosId());
             if (entregaDejetos != null) {
                 PortariaEntregaDejetosDTO.EntregaDejetosDTO entregaDTO = new PortariaEntregaDejetosDTO.EntregaDejetosDTO();
                 entregaDTO.setProdutor_id(String.valueOf(entregaDejetos.getProdutorId()));
                 entregaDTO.setMotorista_nome(entregaDejetos.getMotoristaNome());
                 entregaDTO.setCpf_motorista(entregaDejetos.getCpfMotorista());
                 entregaDTO.setMotorista_id(entregaDejetos.getMotoristaId() != null ? String.valueOf(entregaDejetos.getMotoristaId()) : null);
                 entregaDTO.setTransportadora_id(entregaDejetos.getTransportadoraId() != null ? String.valueOf(entregaDejetos.getTransportadoraId()) : null);
                 entregaDTO.setTransportadora_manual(entregaDejetos.getTransportadoraManual());
                 entregaDTO.setVeiculo_id(entregaDejetos.getVeiculoId() != null ? String.valueOf(entregaDejetos.getVeiculoId()) : null);
                 entregaDTO.setTipo_veiculo(entregaDejetos.getTipoVeiculo());
                 entregaDTO.setPeso_inicial(entregaDejetos.getPesoInicial() != null ? entregaDejetos.getPesoInicial().intValue() : null);
                 entregaDTO.setPeso_final(entregaDejetos.getPesoFinal() != null ? entregaDejetos.getPesoFinal().intValue() : null);
                 entregaDTO.setDensidade(entregaDejetos.getDensidade());
                 
                 // Fluxo 1: Seleção normal - obter placa do veículo
                 if (entregaDejetos.getVeiculoId() != null) {
                     var veiculo = veiculosPorId.get(entregaDejetos.getVeiculoId());
                     if (veiculo != null && veiculo.getPlaca() != null) {
                         entregaDTO.setPlaca(veiculo.getPlaca());
                     }
                 }
                 
                 // Fluxo 2: Digitação manual - retornar placa_manual
                 if (entregaDejetos.getPlacaManual() != null && !entregaDejetos.getPlacaManual().isEmpty()) {
                     entregaDTO.setPlaca_manual(entregaDejetos.getPlacaManual());
                 }
                 
                 dto.setEntrega_dejetos(entregaDTO);
             }
        }
        
        return dto;
    }

    private Map<Long, BioPortariaAbastecimento> carregarAbastecimentos(List<PortariaRegistro> registros) {
        Set<Long> registroIds = registros.stream()
                .filter(registro -> "ABASTECIMENTO".equals(registro.getTipoRegistro()))
                .map(PortariaRegistro::getId)
                .collect(Collectors.toSet());

        if (registroIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return abastecimentoRepository.findByRegistroIdIn(registroIds).stream()
                .collect(Collectors.toMap(BioPortariaAbastecimento::getRegistroId, Function.identity()));
    }

    private Map<Long, com.mwm.bioplanta.model.BioPortariaEntregaDejetos> carregarEntregas(List<PortariaRegistro> registros) {
        Set<Long> entregaIds = registros.stream()
                .filter(registro -> "ENTREGA_DEJETOS".equals(registro.getTipoRegistro()))
                .map(PortariaRegistro::getEntregaDejetosId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        if (entregaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return entregaDejetosRepository.findAllById(entregaIds).stream()
                .collect(Collectors.toMap(com.mwm.bioplanta.model.BioPortariaEntregaDejetos::getId, Function.identity()));
    }

    private Map<Long, com.mwm.bioplanta.model.BioVeiculoTransportadora> carregarVeiculos(
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
            Map<Long, com.mwm.bioplanta.model.BioPortariaEntregaDejetos> entregasPorId) {
        Set<Long> veiculoIds = java.util.stream.Stream.concat(
                    abastecimentosPorRegistroId.values().stream().map(BioPortariaAbastecimento::getVeiculoId),
                    entregasPorId.values().stream().map(com.mwm.bioplanta.model.BioPortariaEntregaDejetos::getVeiculoId))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        if (veiculoIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return veiculoRepository.findAllById(veiculoIds).stream()
                .collect(Collectors.toMap(com.mwm.bioplanta.model.BioVeiculoTransportadora::getId, Function.identity()));
    }
}
