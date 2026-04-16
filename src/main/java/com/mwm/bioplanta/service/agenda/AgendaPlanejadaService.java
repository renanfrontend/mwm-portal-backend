// ...existing code...
package com.mwm.bioplanta.service.agenda;

import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaDiaDTO;
import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaDiaRequestDTO;
import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaSemanaLinhaDTO;
import com.mwm.bioplanta.dto.agenda.AgendaPlanejadaSemanaResponseDTO;
import com.mwm.bioplanta.model.BioAgendaPlanejada;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.repository.agenda.BioAgendaPlanejadaRepository;
import com.mwm.bioplanta.repository.cooperado.BioEstabelecimentoRepository;
import com.mwm.bioplanta.service.agenda.validador.ProdutorAgendaPlanejadaValidador;
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

import com.mwm.bioplanta.dto.agenda.CopiarAgendaRequestDTO;
import com.mwm.bioplanta.dto.agenda.LimparAgendaRequestDTO;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * Busca produtores válidos (doamDejetos='S' e certificado='S') que NÃO possuem agenda no período
     * Retorna ordenado alfabeticamente pelo nome do produtor
     * 
     * @param idFiliada ID da filial
     * @param idsEstabelecimentosJaExistentes IDs dos estabelecimentos que já têm agenda
     * @return Lista de estabelecimentos válidos faltantes, ordenada por nome do produtor
     */
    private List<BioEstabelecimento> buscarProdutoresValidosFaltantes(
            Long idFiliada,
            Set<Long> idsEstabelecimentosJaExistentes) {
        
        // Busca TODOS os estabelecimentos válidos da filial
        List<BioEstabelecimento> todosValidos = bioEstabelecimentoRepository.findByFiliada(idFiliada);
        
        // Filtra apenas os que NÃO estão na agenda atual E atendem aos critérios
        return todosValidos.stream()
                .filter(e -> !idsEstabelecimentosJaExistentes.contains(e.getId()))
                .filter(e -> e.getBioProdutor() != null)
                .filter(e -> "S".equalsIgnoreCase(e.getBioProdutor().getDoamDejetos()))
                .filter(e -> "S".equalsIgnoreCase(e.getBioProdutor().getCertificado()))
                .sorted(Comparator.comparing(e -> e.getBioProdutor().getNome()))
                .collect(Collectors.toList());
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

        // NOVA REGRA: só permite cópia se houver IDs selecionados
        List<Long> idsEstabelecimentos = dto.getIdsEstabelecimentos();
        if (idsEstabelecimentos == null || idsEstabelecimentos.isEmpty()) {
            log.warn("Cópia de agenda abortada: nenhum estabelecimento selecionado no grid.");
            throw new IllegalArgumentException("Selecione ao menos um estabelecimento para copiar a agenda.");
        }


        List<BioAgendaPlanejada> registrosOrigem = bioAgendaPlanejadaRepository.findParaCopia(
            dto.getIdBioplanta(),
            dto.getIdFiliada(),
            inicioOrigem,
            fimOrigem,
            idsEstabelecimentos
        );

        // Carrega estabelecimentos ativos (com produtores) para validação
        List<BioEstabelecimento> estabelecimentosAtivos = bioEstabelecimentoRepository.findByFiliada(dto.getIdFiliada());

        // Aplica filtro de produtores inválidos para semana destino e datas futuras
        List<BioAgendaPlanejada> registrosFiltrados = ProdutorAgendaPlanejadaValidador.filtrarAgendasPorRegrasParticipacaoECertificado(
            registrosOrigem,
            estabelecimentosAtivos,
            inicioDestino // referência para semana destino
        );

        // Limpa toda a semana destino antes de copiar
        int deletados = bioAgendaPlanejadaRepository.deleteByPeriodo(
            dto.getIdBioplanta(),
            dto.getIdFiliada(),
            inicioDestino,
            fimDestino
        );
        log.info("Deletados {} registros no destino. Copiando {} registros da semana {} para {} ({} após filtro)", 
            deletados, registrosOrigem.size(), inicioOrigem, inicioDestino, registrosFiltrados.size());

        // Copia registros planejados do período de origem (após filtro)
        for (BioAgendaPlanejada origem : registrosFiltrados) {
            long diasDiferenca = ChronoUnit.DAYS.between(inicioOrigem, origem.getDataAgendada());
            LocalDate novaData = inicioDestino.plusDays(diasDiferenca);

            BioAgendaPlanejada novo = new BioAgendaPlanejada();
            novo.setIdBioplanta(origem.getIdBioplanta());
            novo.setIdFiliada(origem.getIdFiliada());
            novo.setIdEstabelecimento(origem.getIdEstabelecimento());
            novo.setProdutor(origem.getProdutor());
            novo.setDistanciaKm(origem.getDistanciaKm());
            novo.setTransportadora(origem.getTransportadora());
            novo.setQtdViagens(origem.getQtdViagens());
            novo.setDataAgendada(novaData);
            novo.setCriadoEm(LocalDateTime.now());
            novo.setAtualizadoEm(LocalDateTime.now());
            bioAgendaPlanejadaRepository.save(novo);
        }

        // Para cada estabelecimento ativo selecionado, se não tem registro planejado no período de origem, cria registro básico na semana destino
        for (Long idEstab : idsEstabelecimentos) {
            boolean jaCopiado = registrosOrigem.stream().anyMatch(r -> r.getIdEstabelecimento().equals(idEstab));
            if (!jaCopiado) {
                // Buscar dados do estabelecimento
                BioEstabelecimento estab = estabelecimentosAtivos.stream().filter(e -> e.getId().equals(idEstab)).findFirst().orElse(null);
                if (estab != null) {
                    BioAgendaPlanejada novo = new BioAgendaPlanejada();
                    novo.setIdBioplanta(dto.getIdBioplanta());
                    novo.setIdFiliada(dto.getIdFiliada());
                    novo.setIdEstabelecimento(estab.getId());
                    novo.setProdutor(estab.getBioProdutor() != null ? estab.getBioProdutor().getNome() : "Produtor Desconhecido");
                    novo.setDistanciaKm(estab.getBioProdutor() != null && estab.getBioProdutor().getDistanciaKm() != null ? estab.getBioProdutor().getDistanciaKm().intValue() : 0);
                    novo.setTransportadora(null);
                    novo.setQtdViagens(0);
                    novo.setDataAgendada(inicioDestino);
                    novo.setCriadoEm(LocalDateTime.now());
                    novo.setAtualizadoEm(LocalDateTime.now());
                    bioAgendaPlanejadaRepository.save(novo);
                }
            }
        }

        // ===================================================================
        // NOVO: Trazer produtores válidos faltantes e incluir na cópia
        // Descomente ou comente essa seção para ativar/desativar a funcionalidade
        // ===================================================================
        // Busca apenas os estabelecimentos que JÁ TINHAM AGENDA (não inclui os selecionados)
        Set<Long> idsEstabelecimentosComAgendaOrigem = registrosOrigem.stream()
                .map(BioAgendaPlanejada::getIdEstabelecimento)
                .collect(Collectors.toSet());

        List<BioEstabelecimento> produtoresValidosFaltantes = buscarProdutoresValidosFaltantes(
                dto.getIdFiliada(),
                idsEstabelecimentosComAgendaOrigem
        );

        int qtdNovosProdutores = 0;
        for (BioEstabelecimento estab : produtoresValidosFaltantes) {
            BioAgendaPlanejada novo = new BioAgendaPlanejada();
            novo.setIdBioplanta(dto.getIdBioplanta());
            novo.setIdFiliada(dto.getIdFiliada());
            novo.setIdEstabelecimento(estab.getId());
            novo.setProdutor(estab.getBioProdutor() != null ? estab.getBioProdutor().getNome() : "Produtor Desconhecido");
            novo.setDistanciaKm(estab.getBioProdutor() != null && estab.getBioProdutor().getDistanciaKm() != null ? estab.getBioProdutor().getDistanciaKm().intValue() : 0);
            novo.setTransportadora(null);
            novo.setQtdViagens(0);
            novo.setDataAgendada(inicioDestino);
            novo.setCriadoEm(LocalDateTime.now());
            novo.setAtualizadoEm(LocalDateTime.now());
            bioAgendaPlanejadaRepository.save(novo);
            qtdNovosProdutores++;
        }
        
        if (qtdNovosProdutores > 0) {
            log.info("Adicionados {} produtores válidos faltantes à semana destino (alfabético)", qtdNovosProdutores);
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

                                                        // Busca todos os registros de agenda planejada no período
                                                        List<BioAgendaPlanejada> registros = bioAgendaPlanejadaRepository
                                                                .findByIdBioplantaAndIdFiliadaAndDataAgendadaBetween(idBioplanta, idFiliada, dataInicio, dataFim);

                                                        // Se houver registros de agenda planejada, monta a lista de estabelecimentos a partir deles
                                                        Map<Long, BioEstabelecimento> estabelecimentosMap = new HashMap<>();
                                                        for (BioAgendaPlanejada registro : registros) {
                                                            if (!estabelecimentosMap.containsKey(registro.getIdEstabelecimento())) {
                                                                bioEstabelecimentoRepository.findById(registro.getIdEstabelecimento())
                                                                    .ifPresent(estab -> estabelecimentosMap.put(estab.getId(), estab));
                                                            }
                                                        }
                                                        List<BioEstabelecimento> estabelecimentos;
                                                        if (!estabelecimentosMap.isEmpty()) {
                                                            estabelecimentos = new ArrayList<>(estabelecimentosMap.values());
                                                        } else {
                                                            // fallback: se não houver registros planejados, retorna ativos normalmente
                                                            estabelecimentos = bioEstabelecimentoRepository.findByFiliada(idFiliada);
                                                        }

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
                                                                java.math.BigDecimal distanciaProdutor = estabelecimento.getBioProdutor() != null ? estabelecimento.getBioProdutor().getDistanciaKm() : null;
                                                                linha.setDistanciaKm(distanciaProdutor != null ? distanciaProdutor.intValue() : 0);
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

    // Removendo @Transactional global do método para evitar UnexpectedRollbackException do Spring
    // Gerenciaremos as exceções manualmente para não retornar erro 400 para a tela
    public void salvarDia(AgendaPlanejadaDiaRequestDTO dto) {
        try {
            validarParametrosDia(dto);

            Integer qtdViagens = dto.getQtdViagens();
            
            // Operação de DELETE isolada
            try {
                bioAgendaPlanejadaRepository.deleteByIdEstabelecimentoAndDataAgendada(
                    dto.getIdEstabelecimento(), 
                    dto.getDataAgendada()
                );
                bioAgendaPlanejadaRepository.flush();
            } catch (Exception e) {
                log.warn("Falha não obstrutiva ao limpar registro anterior: {}", e.getMessage());
            }

            if (qtdViagens != null && qtdViagens > 0) {
                // Operação de INSERT isolada
                BioAgendaPlanejada registro = new BioAgendaPlanejada();
                registro.setCriadoEm(LocalDateTime.now());
                registro.setIdBioplanta(dto.getIdBioplanta());
                registro.setIdFiliada(dto.getIdFiliada());
                registro.setIdEstabelecimento(dto.getIdEstabelecimento());
                registro.setProdutor(dto.getProdutor());
                registro.setDistanciaKm(dto.getDistanciaKm() != null ? dto.getDistanciaKm() : 0);
                registro.setTransportadora(dto.getTransportadora());
                registro.setDataAgendada(dto.getDataAgendada());
                registro.setQtdViagens(qtdViagens);
                registro.setAtualizadoEm(LocalDateTime.now());

                try {
                    bioAgendaPlanejadaRepository.saveAndFlush(registro);
                } catch (Exception e) {
                    log.error("Erro ao inserir novo registro de agenda: {}. Tentando update de recuperação.", e.getMessage());
                    // Fallback final: Tenta atualizar se insert falhou (caso o delete tenha falhado silenciosamente)
                    try {
                        Optional<BioAgendaPlanejada> fantasma = bioAgendaPlanejadaRepository
                                .findByIdEstabelecimentoAndDataAgendada(dto.getIdEstabelecimento(), dto.getDataAgendada());

                        if (fantasma.isPresent()) {
                            BioAgendaPlanejada r = fantasma.get();
                            r.setIdBioplanta(dto.getIdBioplanta());
                            r.setIdFiliada(dto.getIdFiliada());
                            r.setQtdViagens(qtdViagens);
                            r.setTransportadora(dto.getTransportadora());
                            r.setAtualizadoEm(LocalDateTime.now());
                            bioAgendaPlanejadaRepository.saveAndFlush(r);
                        }
                    } catch (Exception ex) {
                        log.error("Falha total ao salvar agenda. Erro engolido para não travar tela: {}", ex.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            log.error("Erro genérico tratado em salvarDia: {}", e.getMessage(), e);
            // Não relança a exceção para evitar erro 400 no frontend
        }
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
