package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_portaria_entrega_dejetos")
@Data
public class BioPortariaEntregaDejetos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_portaria_entrega_dejetos")
    @SequenceGenerator(name = "seq_portaria_entrega_dejetos", sequenceName = "seq_portaria_entrega_dejetos", allocationSize = 1)
    private Long id;

    @Column(name = "abastecimento_id", nullable = false, unique = true)
    private Long abastecimentoId;

    @Column(name = "agenda_realizada_id")
    private Long agendaRealizadaId;

    @Column(name = "produtor_id", nullable = false)
    private Long produtorId;

    @Column(name = "densidade")
    private String densidade;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
