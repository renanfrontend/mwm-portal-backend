package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_agenda_realizada")
@Data
public class BioAgendaRealizada {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agenda_realizada")
    @SequenceGenerator(name = "seq_agenda_realizada", sequenceName = "seq_agenda_realizada", allocationSize = 1)
    private Long id;

    @Column(name = "agenda_planejada_id")
    private Long agendaPlanejadaId;

    @Column(name = "produtor_id", nullable = false)
    private Long produtorId;

    @Column(name = "data_real", nullable = false)
    private LocalDate dataReal;

    @Column(name = "transportadora_nome")
    private String transportadoraNome;

    @Column(name = "quantidade_veiculos", nullable = false)
    private Integer quantidadeVeiculos = 1;

    @Column(name = "status")
    private String status = "REALIZADO";

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
