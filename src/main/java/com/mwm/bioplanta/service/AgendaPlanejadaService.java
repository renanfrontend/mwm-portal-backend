package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.AgendaPlanejadaDiaDTO;
import com.mwm.bioplanta.dto.AgendaPlanejadaDiaRequestDTO;
import com.mwm.bioplanta.dto.AgendaPlanejadaSemanaLinhaDTO;
import com.mwm.bioplanta.dto.AgendaPlanejadaSemanaResponseDTO;
import com.mwm.bioplanta.model.BioAgendaPlanejada;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.repository.BioAgendaPlanejadaRepository;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mwm.bioplanta.dto.CopiarAgendaRequestDTO;
import com.mwm.bioplanta.dto.LimparAgendaRequestDTO;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class AgendaPlanejadaService {

    private final BioAgendaPlanejadaRepository bioAgendaPlanejadaRepository;
    private final BioEstabelecimentoRepository bioEstabelecimentoRepository;

    public AgendaPlanejadaService(BioAgendaPlanejadaRepository bioAgendaPlanejadaRepository,
                                  BioEstabelecimentoRepository bioEstabelecimentoRepository) {
        this.bioAgendaPlanejadaRepository = bioAgendaPlanejadaRepository;
        this.bioEstabelecimentoRepository = bioEstabelecimentoRepository;
    }

    @Transactional(readOnly = true)
    public boolean verificarDadosSemana(Long idBioplanta, Long idFiliada, LocalDate dataInicio, LocalDate dataFim) {
        long count = bioAgendaPlanejadaRepository.countViagensReais(
                idBioplanta, idFiliada, dataInicio, dataFim);
        return count > 0;
    }

    @Transactional
    public void copiarSemana(CopiarAgendaRequestDTO dto) {
        if (dto.getDataInicioOrigem() == null || dto.getDataInicioDestino() == null) {
            throw new IllegalArgumentException("Datas de origem e destino são obrigatórias");
        }
        
        LocalDate inicioOrigem = LocalDate.parse(dto.getDataInicioOrigem());
        LocalDate inicioDestino = LocalDate.parse(dto.getDataInicioDestino());
        
        LocalDate fimOrigem = inicioOrigem.plusDays(6); // Sábado da semana origem
        LocalDate fimDestino = inicioDestino.plusDays(6);

        // 1. Busca dados da origem (filtrando por IDs selecionados se houver)
        List<Long> idsEstabelecimentos = (dto.getIdsEstabelecimentos() == null || dto.getIdsEstabelecimentos().isEmpty()) 
                ? null : dto.getIdsEstabelecimentos();
        
        List<BioAgendaPlanejada> listaOrigem = bioAgendaPlanejadaRepository.findParaCopia(
                dto.getIdBioplanta(),
                dto.getIdFiliada(),
                inicioOrigem,
                fimOrigem,
                idsEstabelecimentos
        );

        if (listaOrigem.isEmpty()) {
            log.warn("Nenhum dado encontrado para copiar na semana de origem: {}", inicioOrigem);
            return;
        }

        // 2. LIMPA TODA a semana destino EM LOTE antes de copiar (evita constraint duplicada)
        int deletados = bioAgendaPlanejadaRepository.deleteByPeriodo(
                dto.getIdBioplanta(),
                dto.getIdFiliada(),
                inicioDestino,
                fimDestino
        );
        
        log.info("Deletados {} registros no destino. Copiando {} registros da semana {} para {}", 
                deletados, listaOrigem.size(), inicioOrigem, inicioDestino);

        // 3. Copia os registros
        for (BioAgendaPlanejada origem : listaOrigem) {
            // Calcula dia correspondente na semana destino (preserva dia da semana: Seg -> Seg)
            long diasDiferenca = ChronoUnit.DAYS.between(inicioOrigem, origem.getDataAgendada());
            LocalDate novaData = inicioDestino.plusDays(diasDiferenca);
            
            // Cria e salva o novo registro
            BioAgendaPlanejada destino = new BioAgendaPlanejada();
            destino.setIdBioplanta(origem.getIdBioplanta());
            destino.setIdFiliada(origem.getIdFiliada());
            destino.setIdEstabelecimento(origem.getIdEstabelecimento());
            destino.setProdutor(origem.getProdutor());
            destino.setDistanciaKm(origem.getDistanciaKm());
            destino.setTransportadora(origem.getTransportadora());
            destino.setQtdViagens(origem.getQtdViagens());
            
            destino.setDataAgendada(novaData);
            destino.setCriadoEm(LocalDateTime.now());
            destino.setAtualizadoEm(LocalDateTime.now());
            
            bioAgendaPlanejadaRepository.save(destino);
        }
    }

    @Transactional
    public void limparSemana(LimparAgendaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload obrigatorio");
        }

        validarParametrosSemana(dto.getIdBioplanta(), dto.getIdFiliada(), dto.getDataInicio(), dto.getDataFim());

        List<Long> ids = dto.getIdsEstabelecimentos();
        if (ids != null && ids.isEmpty()) {
            ids = null;
        }

        List<BioAgendaPlanejada> registros = bioAgendaPlanejadaRepository.findParaLimpeza(
                dto.getIdBioplanta(),
                dto.getIdFiliada(),
                dto.getDataInicio(),
                dto.getDataFim(),
                ids);

        if (registros.isEmpty()) {
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        for (BioAgendaPlanejada registro : registros) {
            registro.setQtdViagens(0);
            registro.setTransportadora(null);
            registro.setAtualizadoEm(agora);
        }

        bioAgendaPlanejadaRepository.saveAll(registros);
    }

    @Transactional(readOnly = true)
    public AgendaPlanejadaSemanaResponseDTO carregarSemana(Long idBioplanta,
                                                           Long idFiliada,
                                                           LocalDate dataInicio,
                                                           LocalDate dataFim) {
        validarParametrosSemana(idBioplanta, idFiliada, dataInicio, dataFim);

        List<BioEstabelecimento> estabelecimentos = bioEstabelecimentoRepository.findByFiliada(idFiliada);
        log.info("Encontrados {} estabelecimentos para filiada {}", estabelecimentos.size(), idFiliada);
        List<BioAgendaPlanejada> registros = bioAgendaPlanejadaRepository
                .findByIdBioplantaAndIdFiliadaAndDataAgendadaBetween(idBioplanta, idFiliada, dataInicio, dataFim);

        Map<Long, Map<LocalDate, BioAgendaPlanejada>> registrosPorEstabelecimento = new HashMap<>();
        for (BioAgendaPlanejada registro : registros) {
            registrosPorEstabelecimento
                    .computeIfAbsent(registro.getIdEstabelecimento(), key -> new HashMap<>())
                    .put(registro.getDataAgendada(), registro);
        }

        List<LocalDate> dias = montarDias(dataInicio, dataFim);
        List<AgendaPlanejadaSemanaLinhaDTO> linhas = new ArrayList<>();

        for (BioEstabelecimento estabelecimento : estabelecimentos) {
            try {
                Long idEstabelecimento = estabelecimento.getId();
                Map<LocalDate, BioAgendaPlanejada> registroPorDia = registrosPorEstabelecimento.get(idEstabelecimento);

                AgendaPlanejadaSemanaLinhaDTO linha = new AgendaPlanejadaSemanaLinhaDTO();
                linha.setIdEstabelecimento(idEstabelecimento);
                
                String nomeProdutor = (estabelecimento.getBioProdutor() != null) 
                    ? estabelecimento.getBioProdutor().getNome() 
                    : "Produtor Desconhecido";
                linha.setProdutor(nomeProdutor);
                linha.setDistanciaKm(parseDistanciaKm(estabelecimento.getDistancia()));
                linha.setTransportadora(obterTransportadora(registroPorDia));
                linha.setDias(montarDiasAgenda(dias, registroPorDia));

                linhas.add(linha);
            } catch (Exception e) {
                log.error("Erro ao processar estabelecimento {}: {}", estabelecimento.getId(), e.getMessage(), e);
            }
        }

        AgendaPlanejadaSemanaResponseDTO response = new AgendaPlanejadaSemanaResponseDTO();
        response.setDataInicio(dataInicio);
        response.setDataFim(dataFim);
        response.setLinhas(linhas);
        return response;
    }

    @Transactional
    public void salvarDia(AgendaPlanejadaDiaRequestDTO dto) {
        validarParametrosDia(dto);

        Optional<BioAgendaPlanejada> existente = bioAgendaPlanejadaRepository
                .findByIdFiliadaAndIdEstabelecimentoAndDataAgendada(dto.getIdFiliada(),
                        dto.getIdEstabelecimento(),
                        dto.getDataAgendada());

        Integer qtdViagens = dto.getQtdViagens();
        if (qtdViagens != null && qtdViagens > 0) {
            BioAgendaPlanejada registro = existente.orElseGet(BioAgendaPlanejada::new);
            if (registro.getId() == null) {
                registro.setCriadoEm(LocalDateTime.now());
            }

            registro.setIdBioplanta(dto.getIdBioplanta());
            registro.setIdFiliada(dto.getIdFiliada());
            registro.setIdEstabelecimento(dto.getIdEstabelecimento());
            registro.setProdutor(dto.getProdutor());
            registro.setDistanciaKm(dto.getDistanciaKm() != null ? dto.getDistanciaKm() : 0);
            registro.setTransportadora(dto.getTransportadora());
            registro.setDataAgendada(dto.getDataAgendada());
            registro.setQtdViagens(qtdViagens);
            registro.setAtualizadoEm(LocalDateTime.now());

            bioAgendaPlanejadaRepository.save(registro);
            return;
        }

        existente.ifPresent(bioAgendaPlanejadaRepository::delete);
    }

    private void validarParametrosSemana(Long idBioplanta, Long idFiliada, LocalDate dataInicio, LocalDate dataFim) {
        if (idBioplanta == null) {
            throw new IllegalArgumentException("idBioplanta e obrigatorio");
        }
        if (idFiliada == null) {
            throw new IllegalArgumentException("idFiliada e obrigatorio");
        }
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("dataInicio e dataFim sao obrigatorios");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("dataFim nao pode ser menor que dataInicio");
        }
    }

    private void validarParametrosDia(AgendaPlanejadaDiaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload obrigatorio");
        }
        if (dto.getIdBioplanta() == null) {
            throw new IllegalArgumentException("idBioplanta e obrigatorio");
        }
        if (dto.getIdFiliada() == null) {
            throw new IllegalArgumentException("idFiliada e obrigatorio");
        }
        if (dto.getIdEstabelecimento() == null) {
            throw new IllegalArgumentException("idEstabelecimento e obrigatorio");
        }
        if (dto.getProdutor() == null || dto.getProdutor().isBlank()) {
            throw new IllegalArgumentException("produtor e obrigatorio");
        }
        if (dto.getDataAgendada() == null) {
            throw new IllegalArgumentException("dataAgendada e obrigatorio");
        }
        if (dto.getQtdViagens() == null) {
            throw new IllegalArgumentException("qtdViagens e obrigatorio");
        }
        if (dto.getQtdViagens() < 0) {
            throw new IllegalArgumentException("qtdViagens nao pode ser negativo");
        }
    }

    private List<LocalDate> montarDias(LocalDate dataInicio, LocalDate dataFim) {
        List<LocalDate> dias = new ArrayList<>();
        LocalDate dataAtual = dataInicio;
        while (!dataAtual.isAfter(dataFim)) {
            dias.add(dataAtual);
            dataAtual = dataAtual.plusDays(1);
        }
        return dias;
    }

    private List<AgendaPlanejadaDiaDTO> montarDiasAgenda(List<LocalDate> dias,
                                                         Map<LocalDate, BioAgendaPlanejada> registroPorDia) {
        List<AgendaPlanejadaDiaDTO> lista = new ArrayList<>();
        for (LocalDate dia : dias) {
            int qtd = 0;
            if (registroPorDia != null) {
                BioAgendaPlanejada registro = registroPorDia.get(dia);
                if (registro != null && registro.getQtdViagens() != null) {
                    qtd = registro.getQtdViagens();
                }
            }
            lista.add(new AgendaPlanejadaDiaDTO(dia, qtd));
        }
        return lista;
    }

    private String obterTransportadora(Map<LocalDate, BioAgendaPlanejada> registroPorDia) {
        if (registroPorDia == null) {
            return null;
        }
        for (BioAgendaPlanejada registro : registroPorDia.values()) {
            if (registro.getTransportadora() != null && !registro.getTransportadora().isBlank()) {
                return registro.getTransportadora();
            }
        }
        return null;
    }

    private Integer parseDistanciaKm(String distancia) {
        if (distancia == null || distancia.isBlank()) {
            return 0;
        }
        String apenasNumeros = distancia.replaceAll("\\D", "");
        if (apenasNumeros.isEmpty()) {
            return 0;
        }
        try {
            return Integer.valueOf(apenasNumeros);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
