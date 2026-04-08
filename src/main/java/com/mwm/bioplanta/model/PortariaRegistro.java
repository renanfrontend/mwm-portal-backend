package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Model para Portaria Registro (Tabela Base)
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Entity
@Table(name = "bio_portaria_registro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortariaRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_portaria_registro")
    @SequenceGenerator(name = "seq_portaria_registro", sequenceName = "seq_portaria_registro", allocationSize = 1)
    private Long id;

    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @Column(name = "hora_entrada", nullable = false)
    private LocalTime horaEntrada;

    @Column(name = "tipo_registro", nullable = false, length = 30)
    private String tipoRegistro;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "data_saida")
    private LocalDate dataSaida;

    @Column(name = "hora_saida")
    private LocalTime horaSaida;

    @Column(name = "origem_entrada", length = 20)
    private String origemEntrada;

    @Column(name = "agenda_realizada_id")
    private Long agendaRealizadaId;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "entrega_dejetos_id")
    private Long entregaDejetosId;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
