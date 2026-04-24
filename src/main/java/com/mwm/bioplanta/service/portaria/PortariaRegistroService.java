package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PaginationResponseDTO;
import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoDTO;
import com.mwm.bioplanta.dto.portaria.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.BioPortariaEntregaInsumo;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaInsumoRepository;
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
    private BioPortariaEntregaInsumoRepository entregaInsumoRepository;
    
    @Autowired
    private BioVeiculoTransportadoraRepository veiculoRepository;
    
    @Autowired
    private BioTransportadoraRepository transportadoraRepository;

    @Autowired
    private PortariaEntregaDejetosService entregaDejetosService;

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

            LocalDate dataEntradaAnterior = registro.getDataEntrada();
            LocalTime horaEntradaAnterior = registro.getHoraEntrada();

            aplicarAtualizacoesRegistro(registro, registroDTO);
            registro.setAtualizadoEm(LocalDateTime.now());
            PortariaRegistro updated = portariaRegistroRepository.save(registro);
            
            atualizarDensidadeEntregaDejetos(updated, registroDTO);
            atualizarEntregaInsumo(updated, registroDTO);
            atualizarAbastecimento(updated, registroDTO);

            if ("ENTREGA_DEJETOS".equals(registro.getTipoRegistro()) 
                && registroDTO.getDataEntrada() != null
                && (registro.getEntregaDejetosId() != null)) {
                LocalDate novaData = LocalDate.parse(registroDTO.getDataEntrada());
                LocalTime novaHora = registroDTO.getHoraEntrada() != null 
                    ? LocalTime.parse(registroDTO.getHoraEntrada()) 
                    : horaEntradaAnterior;
                entregaDejetosService.atualizarAgendaRealizadaData(
                    registro.getEntregaDejetosId(), novaData, novaHora);
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

         if ("ENTREGA_INSUMO".equals(registro.getTipoRegistro())) {
             BioPortariaEntregaInsumo entregaInsumo = dadosRelacionados.entregasInsumoPorRegistroId().get(registro.getId());
             if (entregaInsumo != null) {
                 dto.setEntrega_insumo(criarEntregaInsumoDTO(entregaInsumo, dadosRelacionados.veiculosPorId()));
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

    private Map<Long, BioPortariaEntregaInsumo> carregarEntregasInsumo(List<PortariaRegistro> registros) {
        Set<Long> registroIds = registros.stream()
                .filter(registro -> "ENTREGA_INSUMO".equals(registro.getTipoRegistro()))
                .map(PortariaRegistro::getId)
                .collect(Collectors.toSet());

        if (registroIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return entregaInsumoRepository.findByRegistroIdIn(registroIds).stream()
                .collect(Collectors.toMap(BioPortariaEntregaInsumo::getRegistroId, Function.identity()));
    }

            private Map<Long, BioVeiculoTransportadora> carregarVeiculos(
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
            Map<Long, BioPortariaEntregaDejetos> entregasPorId,
            Map<Long, BioPortariaEntregaInsumo> entregasInsumoPorRegistroId) {
        Set<Long> veiculoIds = java.util.stream.Stream.concat(
                    java.util.stream.Stream.concat(
                        abastecimentosPorRegistroId.values().stream().map(BioPortariaAbastecimento::getVeiculoId),
                        entregasPorId.values().stream().map(BioPortariaEntregaDejetos::getVeiculoId)),
                    entregasInsumoPorRegistroId.values().stream().map(BioPortariaEntregaInsumo::getVeiculoId))
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
                || registroDTO.getEntrega_dejetos() == null) {
            return;
        }

        var entregaDejetos = entregaDejetosRepository.findById(registro.getEntregaDejetosId()).orElse(null);
        if (entregaDejetos != null) {
            var dto = registroDTO.getEntrega_dejetos();
            boolean alterou = false;
            
            if (dto.getPeso_inicial() != null) {
                entregaDejetos.setPesoInicial(dto.getPeso_inicial().doubleValue());
                log.info("Salvando peso inicial: {}", dto.getPeso_inicial());
                alterou = true;
            }
            if (dto.getPeso_final() != null) {
                entregaDejetos.setPesoFinal(dto.getPeso_final().doubleValue());
                log.info("Salvando peso final: {}", dto.getPeso_final());
                alterou = true;
            }
            if (dto.getDensidade() != null) {
                entregaDejetos.setDensidade(dto.getDensidade());
                log.info("Salvando densidade: {}", dto.getDensidade());
                alterou = true;
            }
            
            if (alterou) {
                entregaDejetos.setAtualizadoEm(LocalDateTime.now());
                entregaDejetosRepository.save(entregaDejetos);
                entregaDejetosRepository.flush();
            }
        }
    }

    private void atualizarEntregaInsumo(PortariaRegistro registro, PortariaRegistroDTO registroDTO) {
        log.info("ENTREGA_INSUMO check - tipo: {}, insumoId: {}, entrega_insumo: {}",
                registro.getTipoRegistro(), registro.getEntregaInsumoId(), registroDTO.getEntrega_insumo());

        if (!"ENTREGA_INSUMO".equals(registro.getTipoRegistro())
                || registroDTO.getEntrega_insumo() == null) {
            return;
        }

        // Se não tem ID, busca pelo registroId
        Long entregaInsumoId = registro.getEntregaInsumoId();
        if (entregaInsumoId == null) {
            entregaInsumoId = entregaInsumoRepository.findByRegistroId(registro.getId())
                    .map(BioPortariaEntregaInsumo::getId).orElse(null);
        }
        
        if (entregaInsumoId == null) {
            log.info("EntregaInsumo não encontrada para registro ID: {}", registro.getId());
            return;
        }

        var entregaInsumo = entregaInsumoRepository.findById(entregaInsumoId).orElse(null);
        if (entregaInsumo != null) {
            var dto = registroDTO.getEntrega_insumo();
            boolean alterou = false;
            
            Object pesoInicialObj = dto.get("peso_inicial");
            if (pesoInicialObj != null) {
                Double pesoInicial = toDouble(pesoInicialObj);
                if (pesoInicial != null) {
                    entregaInsumo.setPesoInicial(pesoInicial);
                    log.info("Salvando peso inicial: {}", pesoInicial);
                    alterou = true;
                }
            }
            Object pesoFinalObj = dto.get("peso_final");
            if (pesoFinalObj != null) {
                Double pesoFinal = toDouble(pesoFinalObj);
                if (pesoFinal != null) {
                    entregaInsumo.setPesoFinal(pesoFinal);
                    log.info("Salvando peso final: {}", pesoFinal);
                    alterou = true;
                }
            }
            
            if (alterou) {
                entregaInsumo.setAtualizadoEm(LocalDateTime.now());
                entregaInsumoRepository.save(entregaInsumo);
                entregaInsumoRepository.flush();
            }
        }
    }

    private void atualizarAbastecimento(PortariaRegistro registro, PortariaRegistroDTO registroDTO) {
        log.info("ABASTECIMENTO check - tipo: {}, abastecimentoId: {}, abastecimento: {}",
                registro.getTipoRegistro(), registro.getAbastecimentoId(), registroDTO.getAbastecimento());
        log.info("DEBUG - registro.getAbastecimentoId(): {}, registro.getId(): {}", registro.getAbastecimentoId(), registro.getId());

        if (!"ABASTECIMENTO".equals(registro.getTipoRegistro())
                || registroDTO.getAbastecimento() == null) {
            log.info("ABASTECIMENTO - condição de entrada não atendida");
            return;
        }

        Long abastecimentoId = registro.getAbastecimentoId();
        if (abastecimentoId == null) {
            log.info("Buscando abastecimento por registroId: {}", registro.getId());
            abastecimentoId = abastecimentoRepository.findByRegistroId(registro.getId())
                    .map(BioPortariaAbastecimento::getId).orElse(null);
            log.info("AbastecimentoId encontrado: {}", abastecimentoId);
        }
        
        if (abastecimentoId == null) {
            log.info("Abastecimento não encontrado para registro ID: {}", registro.getId());
            return;
        }

        var abastecimento = abastecimentoRepository.findById(abastecimentoId).orElse(null);
        if (abastecimento != null) {
            var dto = registroDTO.getAbastecimento();
            boolean alterou = false;
            
            if (dto.getPeso_inicial() != null) {
                abastecimento.setPesoInicial(dto.getPeso_inicial());
                log.info("Salvando peso inicial: {}", dto.getPeso_inicial());
                alterou = true;
            }
            if (dto.getPeso_final() != null) {
                abastecimento.setPesoFinal(dto.getPeso_final());
                log.info("Salvando peso final: {}", dto.getPeso_final());
                alterou = true;
            }
            
            if (alterou) {
                abastecimento.setAtualizadoEm(LocalDateTime.now());
                abastecimentoRepository.save(abastecimento);
                abastecimentoRepository.flush();
            }
        }
    }

    private Double toDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private DadosRelacionadosPortaria carregarDadosRelacionados(List<PortariaRegistro> registros) {
        Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId = carregarAbastecimentos(registros);
        Map<Long, BioPortariaEntregaDejetos> entregasPorId = carregarEntregas(registros);
        Map<Long, BioPortariaEntregaInsumo> entregasInsumoPorRegistroId = carregarEntregasInsumo(registros);
        Map<Long, BioVeiculoTransportadora> veiculosPorId = carregarVeiculos(abastecimentosPorRegistroId, entregasPorId, entregasInsumoPorRegistroId);
        return new DadosRelacionadosPortaria(abastecimentosPorRegistroId, entregasPorId, entregasInsumoPorRegistroId, veiculosPorId);
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
        
        // Campos snake_case para compatibilidade frontend
        abastecimentoDTO.setPeso_inicial(abastecimento.getPesoInicial());
        abastecimentoDTO.setPeso_final(abastecimento.getPesoFinal());
        
        return abastecimentoDTO;
    }

    private PortariaEntregaDejetosDTO.EntregaDejetosDTO criarEntregaDejetosDTO(
            BioPortariaEntregaDejetos entregaDejetos,
            Map<Long, BioVeiculoTransportadora> veiculosPorId) {
        PortariaEntregaDejetosDTO.EntregaDejetosDTO entregaDTO = new PortariaEntregaDejetosDTO.EntregaDejetosDTO();
        // Preencher o campo id
        entregaDTO.setId(entregaDejetos.getId() != null ? String.valueOf(entregaDejetos.getId()) : null);
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

    private Map<String, Object> criarEntregaInsumoDTO(BioPortariaEntregaInsumo entrega, Map<Long, BioVeiculoTransportadora> veiculosPorId) {
        Map<String, Object> entregaDTO = new java.util.HashMap<>();
        entregaDTO.put("id", entrega.getId());
        entregaDTO.put("motorista_nome", entrega.getMotorista());
        entregaDTO.put("cpf_motorista", entrega.getCpfPassaporte());
        entregaDTO.put("empresa", entrega.getEmpresa());
        entregaDTO.put("nota_fiscal", entrega.getNotaFiscal());
        entregaDTO.put("peso_inicial", entrega.getPesoInicial());
        entregaDTO.put("peso_final", entrega.getPesoFinal());
        entregaDTO.put("tipo_veiculo", entrega.getTipoVeiculo());
        entregaDTO.put("transportadora_manual", entrega.getTransportadoraManual());
        entregaDTO.put("placa_manual", entrega.getPlaca());
        entregaDTO.put("observacao", entrega.getObservacao());
        entregaDTO.put("veiculo_id", entrega.getVeiculoId());
        
        if (entrega.getDataSaida() != null) {
            entregaDTO.put("data_saida", entrega.getDataSaida().toString());
        }
        if (entrega.getHorarioSaida() != null) {
            entregaDTO.put("hora_saida", entrega.getHorarioSaida().toString());
        }

        if (entrega.getPlaca() != null && !entrega.getPlaca().isBlank()) {
            entregaDTO.put("placa", entrega.getPlaca());
        } else if (entrega.getVeiculoId() != null) {
            var veiculo = veiculosPorId.get(entrega.getVeiculoId());
            if (veiculo != null) {
                if (veiculo.getPlaca() != null) {
                    entregaDTO.put("placa", veiculo.getPlaca());
                }
                if (veiculo.getBioTransportadora() != null) {
                    entregaDTO.put("transportadora_id", veiculo.getBioTransportadora().getId());
                    entregaDTO.put("transportadora_nome", veiculo.getBioTransportadora().getNomeFantasia());
                }
            }
        }

        return entregaDTO;
    }

    private record DadosRelacionadosPortaria(
            Map<Long, BioPortariaAbastecimento> abastecimentosPorRegistroId,
            Map<Long, BioPortariaEntregaDejetos> entregasPorId,
            Map<Long, BioPortariaEntregaInsumo> entregasInsumoPorRegistroId,
            Map<Long, BioVeiculoTransportadora> veiculosPorId) {
    }
}
