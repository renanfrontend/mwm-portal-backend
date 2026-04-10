package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.AgendaRealizadaSemanalDTO;
import com.mwm.bioplanta.model.BioAgendaRealizada;
import com.mwm.bioplanta.model.BioProdutor;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.repository.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.BioProdutorRepository;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * SERVICE: Portaria Agenda Realizada
 * ============================================================================
 * Responsabilidade: Gerar relatório de agenda realizada por semana
 * 
 * Lógica:
 * 1. Busca registros em bio_agenda_realizada dentro do período
 * 2. Agrupa por produtor
 * 3. Conta entregas por dia da semana
 * 4. Calcula totalizadores (total de entregas, total de KM)
 * 5. Monta estrutura do grid/relatório
 * 
 * @author Sistema de Portaria
 * @date 2026-04-09
 */
@Service
public class PortariaAgendaRealizadaService {
    private final BioAgendaRealizadaRepository agendaRealizadaRepository;
    private final BioProdutorRepository produtorRepository;
    private final BioEstabelecimentoRepository estabelecimentoRepository;

    public PortariaAgendaRealizadaService(
            BioAgendaRealizadaRepository agendaRealizadaRepository,
            BioProdutorRepository produtorRepository,
            BioEstabelecimentoRepository estabelecimentoRepository) {
        this.agendaRealizadaRepository = agendaRealizadaRepository;
        this.produtorRepository = produtorRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    /**
     * ============================================================================
     * MÉTODO: Gerar Agenda Realizada Semanal
     * ============================================================================
     * Responsabilidade: Criar relatório de agenda realizada para uma semana específica
     * 
     * Recebe:
     *   - dataInicio: Primeira data do período (ex: 2026-04-05)
     *   - dataFim: Última data do período (ex: 2026-04-11)
     * 
     * Retorna:
     *   - List<AgendaRealizadaSemanalDTO>: Uma linha por produtor com contagens diárias
     * 
     * Fluxo:
     * 1. Buscar todos os registros em bio_agenda_realizada entre as datas
     * 2. Agrupar por produtor_id
     * 3. Para cada produtor:
     *    - Contar quantas entregas em cada dia da semana
     *    - Buscar dados do produtor em bio_produtor
     *    - Montar linha do grid com contagens
     *    - Calcular total de KM (distancia_km * total de entregas)
     * 4. Retornar lista de DTOs montada
     */
    public List<AgendaRealizadaSemanalDTO> gerarAgendaRealizadaSemanal(LocalDate dataInicio, LocalDate dataFim) {
        // PASSO 1: Buscar todos os registros em bio_agenda_realizada dentro do período
        List<BioAgendaRealizada> agendas = agendaRealizadaRepository.findByDataRealBetween(
            dataInicio.atStartOfDay(), 
            dataFim.atTime(23, 59, 59)
        );
        
        // PASSO 2: Agrupar por produtor_id
        Map<Long, List<BioAgendaRealizada>> agendasPorProdutor = agendas.stream()
                .collect(Collectors.groupingBy(BioAgendaRealizada::getProdutorId));
        
        // PASSO 3: Montar o grid com uma linha por produtor
        List<AgendaRealizadaSemanalDTO> resultado = new ArrayList<>();
        
        for (Map.Entry<Long, List<BioAgendaRealizada>> entry : agendasPorProdutor.entrySet()) {
            Long produtorId = entry.getKey();
            List<BioAgendaRealizada> agendasDoProdutor = entry.getValue();
            
            // Buscar dados do produtor
            BioProdutor produtor = produtorRepository.findById(produtorId).orElse(null);
            
            if (produtor == null) {
                continue; // Pular se produtor não encontrado
            }
            
            // Buscar estabelecimento para obter numero_estabelecimento
            // Um produtor pode ter múltiplos estabelecimentos, pega o primeiro ativo ou o primeiro
            BioEstabelecimento estabelecimento = estabelecimentoRepository
                    .findFirstByBioProdutorIdOrderByIdAsc(produtorId)
                    .orElse(null);
            
            // Contar entregas por dia da semana
            Integer domingo = contarEntregasPorDia(agendasDoProdutor, dataInicio, 0);      // Domingo
            Integer segunda = contarEntregasPorDia(agendasDoProdutor, dataInicio, 1);      // Segunda
            Integer terca = contarEntregasPorDia(agendasDoProdutor, dataInicio, 2);        // Terça
            Integer quarta = contarEntregasPorDia(agendasDoProdutor, dataInicio, 3);       // Quarta
            Integer quinta = contarEntregasPorDia(agendasDoProdutor, dataInicio, 4);       // Quinta
            Integer sexta = contarEntregasPorDia(agendasDoProdutor, dataInicio, 5);        // Sexta
            Integer sabado = contarEntregasPorDia(agendasDoProdutor, dataInicio, 6);       // Sábado
            
            // Calcular total de entregas na semana
            Integer totalEntregas = domingo + segunda + terca + quarta + quinta + sexta + sabado;
            
            // Calcular total de KM (distancia_km * total de entregas)
            // distancia_km é BigDecimal, converter para Double
            Double totalKm = produtor.getDistanciaKm() != null ? 
                    produtor.getDistanciaKm().doubleValue() * totalEntregas : 0.0;
            
            // Montar a linha do grid
            AgendaRealizadaSemanalDTO linha = new AgendaRealizadaSemanalDTO();
            // Usar numero_estabelecimento ao invés de codigo_produtor
            linha.setNumeroEstabelecimento(estabelecimento != null ? estabelecimento.getNumeroEstabelecimento() : "");
            linha.setNomeProduto(produtor.getNome());
            linha.setDistanciaKm(produtor.getDistanciaKm() != null ? produtor.getDistanciaKm().doubleValue() : null);
            linha.setTransportadoraNome(obterTransportadoraPrincipal(agendasDoProdutor));
            linha.setDomingo(domingo);
            linha.setSegunda(segunda);
            linha.setTerca(terca);
            linha.setQuarta(quarta);
            linha.setQuinta(quinta);
            linha.setSexta(sexta);
            linha.setSabado(sabado);
            linha.setTotalEntregas(totalEntregas);
            linha.setTotalKm(totalKm);
            
            resultado.add(linha);
        }
        
        return resultado;
    }
    
    /**
     * ============================================================================
     * MÉTODO AUXILIAR: Contar Entregas por Dia
     * ============================================================================
     * Responsabilidade: Contar quantas entregas ocorreram em um dia específico
     * 
     * Recebe:
     *   - agendas: Lista de agendas do produtor
     *   - dataInicio: Data inicial da semana
     *   - diasOffset: Deslocamento do dia (0=domingo, 1=segunda, ..., 6=sábado)
     * 
     * Retorna:
     *   - Integer: Quantidade de entregas naquele dia
     */
    private Integer contarEntregasPorDia(List<BioAgendaRealizada> agendas, LocalDate dataInicio, int diasOffset) {
        LocalDate dataDia = dataInicio.plusDays(diasOffset);
        
        return (int) agendas.stream()
                .filter(a -> a.getDataReal().toLocalDate().equals(dataDia))
                .count();
    }
    
    /**
     * ============================================================================
     * MÉTODO AUXILIAR: Obter Transportadora Principal
     * ============================================================================
     * Responsabilidade: Retornar o nome da transportadora mais frequente ou a primeira
     * 
     * Recebe:
     *   - agendas: Lista de agendas do produtor
     * 
     * Retorna:
     *   - String: Nome da transportadora principal
     */
    private String obterTransportadoraPrincipal(List<BioAgendaRealizada> agendas) {
        if (agendas == null || agendas.isEmpty()) {
            return "";
        }
        
        // Retornar a transportadora da primeira agenda (ou pode ser a mais frequente)
        return agendas.get(0).getTransportadoraNome();
    }
}
