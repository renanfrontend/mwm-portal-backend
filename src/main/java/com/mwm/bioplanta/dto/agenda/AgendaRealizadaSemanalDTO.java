package com.mwm.bioplanta.dto.agenda;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * DTO: Agenda Realizada Semanal
 * ============================================================================
 * Responsabilidade: Representar uma linha do grid/relatório de agenda realizada
 * 
 * Contém:
 * - Dados do produtor (código, nome, distância)
 * - Nome da transportadora
 * - Contagens de entrega para cada dia da semana (Dom-Sáb)
 * - Total de KM calculado
 * 
 * @author Sistema de Portaria
 * @date 2026-04-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRealizadaSemanalDTO {
    // Dados do Produtor
    private String numeroEstabelecimento; // numero_estabelecimento da bio_estabelecimento
    private String nomeProduto;           // nome da bio_produtor
    private Double distanciaKm;           // distancia_km da bio_produtor
    
    // Dados da Transportadora
    private String transportadoraNome;  // nome_fantasia da bio_transportadora
    
    // Contagem por dia da semana (quantidade de entregas naquele dia)
    private Integer domingo;            // Contagem domingo (05/04)
    private Integer segunda;            // Contagem segunda (06/04)
    private Integer terca;              // Contagem terça (07/04)
    private Integer quarta;             // Contagem quarta (08/04)
    private Integer quinta;             // Contagem quinta (09/04)
    private Integer sexta;              // Contagem sexta (10/04)
    private Integer sabado;             // Contagem sábado (11/04)
    
    // Totalizadores
    private Integer totalEntregas;      // Soma de todas as entregas na semana
    private Double totalKm;             // distanciaKm * totalEntregas
}
