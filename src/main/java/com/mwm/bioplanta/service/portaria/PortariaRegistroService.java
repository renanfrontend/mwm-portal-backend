package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PaginationResponseDTO;
import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoDTO;
import com.mwm.bioplanta.dto.portaria.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
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
            Pageable pageable = criarPageable(page, pageSize);
            Page<PortariaRegistro> pageResult = portariaRegistroRepository.findAll(pageable);

            DadosRelacionadosPortaria dadosRelacionados = carregarDadosRelacionados(pageResult.getContent());

            List<PortariaRegistroDTO> dtos = pageResult.getContent()
                    .stream()
                .map(registro -> mapToDTO(registro, dadosRelacionados))
                    .collect(Collectors.toList());

            PaginationResponseDTO<PortariaRegistroDTO> response = montarRespostaPaginada(pageResult, dtos);

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
            PortariaRegistro registro = buscarRegistroPorId(id);
            
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
            PortariaRegistro registro = criarEntidadeRegistro(registroDTO);
            
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
            PortariaRegistro registro = buscarRegistroPorId(id);

            aplicarAtualizacoesRegistro(registro, registroDTO);
            registro.setAtualizadoEm(LocalDateTime.now());
            PortariaRegistro updated = portariaRegistroRepository.save(registro);
            
            atualizarDensidadeEntregaDejetos(updated, registroDTO);
            
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
            PortariaRegistro registro = buscarRegistroPorId(id);
            
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
        DadosRelacionadosPortaria dadosRelacionados = carregarDadosRelacionados(List.of(registro));
        return mapToDTO(registro, dadosRelacionados);
    }

    private PortariaRegistroDTO mapToDTO(PortariaRegistro registro, DadosRelacionadosPortaria dadosRelacionados) {
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
             BioPortariaAbastecimento abastecimento = dadosRelacionados.abastecimentosPorRegistroId().get(registro.getId());
             if (abastecimento != null) {
                 dto.setAbastecimento(criarAbastecimentoDTO(abastecimento, dadosRelacionados.veiculosPorId()));
             }
         }

         if ("ENTREGA_DEJETOS".equals(registro.getTipoRegistro()) && registro.getEntregaDejetosId() != null) {
             var entregaDejetos = dadosRelacionados.entregasPorId().get(registro.getEntregaDejetosId());
             if (entregaDejetos != null) {
                 dto.setEntrega_dejetos(criarEntregaDejetosDTO(entregaDejetos, dadosRelacionados.veiculosPorId()));
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

    private Map<Long, BioPortariaEntregaDejetos> carregarEntregas(List<PortariaRegistro> registros) {
        Set<Long> entregaIds = registros.stream()
                .filter(registro -> "ENTREGA_DEJETOS".equals(registro.getTipoRegistro()))
                .map(PortariaRegistro::getEntregaDejetosId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        if (entregaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return entregaDejetosRepository.findAllById(entregaIds).stream()
                .collect(Collectors.toMap(BioPortariaEntregaDejetos::getId, Function.identity()));
    }

            private Map<Long, BioVeiculoTransportadora> carregarVeiculos(
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
                Map<Long, BioPortariaEntregaDejetos> entregasPorId) {
        Set<Long> veiculoIds = java.util.stream.Stream.concat(
                    abastecimentosPorRegistroId.values().stream().map(BioPortariaAbastecimento::getVeiculoId),
                    entregasPorId.values().stream().map(BioPortariaEntregaDejetos::getVeiculoId))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        if (veiculoIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return veiculoRepository.findAllById(veiculoIds).stream()
                .collect(Collectors.toMap(BioVeiculoTransportadora::getId, Function.identity()));
    }

    private Pageable criarPageable(Integer page, Integer pageSize) {
        int pageNum = page != null ? page : 0;
        int size = pageSize != null ? pageSize : 5;
        return PageRequest.of(pageNum, size,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "dataEntrada", "horaEntrada"));
    }

    private PaginationResponseDTO<PortariaRegistroDTO> montarRespostaPaginada(Page<PortariaRegistro> pageResult, List<PortariaRegistroDTO> dtos) {
        PaginationResponseDTO<PortariaRegistroDTO> response = new PaginationResponseDTO<>();
        response.setData(dtos);
        response.setPage(pageResult.getNumber());
        response.setPageSize(pageResult.getSize());
        response.setTotal(pageResult.getTotalElements());
        response.setTotalPages(pageResult.getTotalPages());
        response.setHasNext(pageResult.hasNext());
        response.setHasPrevious(pageResult.hasPrevious());
        return response;
    }

    private PortariaRegistro buscarRegistroPorId(Long id) {
        return portariaRegistroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado com ID: " + id));
    }

    private PortariaRegistro criarEntidadeRegistro(PortariaRegistroDTO registroDTO) {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setDataEntrada(LocalDate.parse(registroDTO.getDataEntrada()));
        registro.setHoraEntrada(LocalTime.parse(registroDTO.getHoraEntrada()));
        registro.setTipoRegistro(registroDTO.getTipoRegistro());
        registro.setStatus(normalizarStatus(registroDTO.getStatus()));
        registro.setOrigemEntrada(normalizarOrigem(registroDTO.getOrigemEntrada()));
        registro.setObservacoes(registroDTO.getObservacoes());
        registro.setResponsavelId(registroDTO.getResponsavelId());
        registro.setCriadoEm(LocalDateTime.now());
        registro.setAtualizadoEm(LocalDateTime.now());
        return registro;
    }

    private void aplicarAtualizacoesRegistro(PortariaRegistro registro, PortariaRegistroDTO registroDTO) {
        if (registroDTO.getDataEntrada() != null) {
            registro.setDataEntrada(LocalDate.parse(registroDTO.getDataEntrada()));
        }
        if (registroDTO.getHoraEntrada() != null) {
            registro.setHoraEntrada(LocalTime.parse(registroDTO.getHoraEntrada()));
        }
        if (registroDTO.getStatus() != null) {
            registro.setStatus(normalizarStatus(registroDTO.getStatus()));
        }
        if (registroDTO.getDataSaida() != null) {
            registro.setDataSaida(LocalDate.parse(registroDTO.getDataSaida()));
        }
        if (registroDTO.getHoraSaida() != null) {
            registro.setHoraSaida(LocalTime.parse(registroDTO.getHoraSaida()));
        }
        if (registroDTO.getOrigemEntrada() != null) {
            registro.setOrigemEntrada(normalizarOrigem(registroDTO.getOrigemEntrada()));
        }
        if (registroDTO.getObservacoes() != null) {
            registro.setObservacoes(registroDTO.getObservacoes());
        }
    }

    private String normalizarStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "Em andamento";
        }

        String valor = status.trim();
        if ("Em andamento".equalsIgnoreCase(valor)) {
            return "Em andamento";
        }
        if ("Concluído".equalsIgnoreCase(valor) || "Concluido".equalsIgnoreCase(valor)) {
            return "Concluído";
        }

        return valor;
    }

    private String normalizarOrigem(String origem) {
        if (origem == null || origem.trim().isEmpty()) {
            return "ESPONTANEA";
        }

        String valor = origem.trim().toUpperCase();
        if ("ESPONTANEA".equals(valor) || "AGENDADA".equals(valor)) {
            return valor;
        }

        return valor;
    }

    private void atualizarDensidadeEntregaDejetos(PortariaRegistro registro, PortariaRegistroDTO registroDTO) {
        log.info("ENTREGA_DEJETOS check - tipo: {}, dejetoId: {}, entrega_dejetos: {}",
                registro.getTipoRegistro(), registro.getEntregaDejetosId(), registroDTO.getEntrega_dejetos());

        if (!"ENTREGA_DEJETOS".equals(registro.getTipoRegistro())
                || registro.getEntregaDejetosId() == null
                || registroDTO.getEntrega_dejetos() == null
                || registroDTO.getEntrega_dejetos().getDensidade() == null) {
            return;
        }

        log.info("Salvando densidade: {}", registroDTO.getEntrega_dejetos().getDensidade());

        var entregaDejetos = entregaDejetosRepository.findById(registro.getEntregaDejetosId()).orElse(null);
        if (entregaDejetos != null) {
            entregaDejetos.setDensidade(registroDTO.getEntrega_dejetos().getDensidade());
            entregaDejetos.setAtualizadoEm(LocalDateTime.now());
            entregaDejetosRepository.save(entregaDejetos);
            entregaDejetosRepository.flush();
        }
    }

    private DadosRelacionadosPortaria carregarDadosRelacionados(List<PortariaRegistro> registros) {
        Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId = carregarAbastecimentos(registros);
        Map<Long, BioPortariaEntregaDejetos> entregasPorId = carregarEntregas(registros);
        Map<Long, BioVeiculoTransportadora> veiculosPorId = carregarVeiculos(abastecimentosPorRegistroId, entregasPorId);
        return new DadosRelacionadosPortaria(abastecimentosPorRegistroId, entregasPorId, veiculosPorId);
    }

    private PortariaAbastecimentoDTO criarAbastecimentoDTO(
            BioPortariaAbastecimento abastecimento,
            Map<Long, BioVeiculoTransportadora> veiculosPorId) {
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
        return abastecimentoDTO;
    }

    private PortariaEntregaDejetosDTO.EntregaDejetosDTO criarEntregaDejetosDTO(
            BioPortariaEntregaDejetos entregaDejetos,
            Map<Long, BioVeiculoTransportadora> veiculosPorId) {
        PortariaEntregaDejetosDTO.EntregaDejetosDTO entregaDTO = new PortariaEntregaDejetosDTO.EntregaDejetosDTO();
        entregaDTO.setProdutor_id(entregaDejetos.getProdutorId() != null ? String.valueOf(entregaDejetos.getProdutorId()) : null);
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

        if (entregaDejetos.getPlaca() != null && !entregaDejetos.getPlaca().isBlank()) {
            entregaDTO.setPlaca(entregaDejetos.getPlaca());
        }

        if (entregaDejetos.getVeiculoId() != null) {
            var veiculo = veiculosPorId.get(entregaDejetos.getVeiculoId());
            if (veiculo != null && veiculo.getPlaca() != null) {
                entregaDTO.setPlaca(veiculo.getPlaca());
            }
        }

        if (entregaDejetos.getPlacaManual() != null && !entregaDejetos.getPlacaManual().isEmpty()) {
            entregaDTO.setPlaca_manual(entregaDejetos.getPlacaManual());
        }

        return entregaDTO;
    }

    private record DadosRelacionadosPortaria(
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
            Map<Long, BioPortariaEntregaDejetos> entregasPorId,
            Map<Long, BioVeiculoTransportadora> veiculosPorId) {
    }
}
