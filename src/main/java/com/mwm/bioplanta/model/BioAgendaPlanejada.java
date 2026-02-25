package com.mwm.bioplanta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_agenda_planejada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Bio Agenda Planejada", description = "Representa uma agenda planejada por produtor e data.")
public class BioAgendaPlanejada {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bio_agenda_planejada")
    @SequenceGenerator(name = "seq_bio_agenda_planejada", sequenceName = "seq_bio_agenda_planejada", allocationSize = 1)
    @Schema(description = "Identificador unico do item de agenda planejada", example = "1")
    private Long id;

    @Column(name = "id_bioplanta", nullable = false)
    @Schema(description = "Identificador da bioplanta", example = "1")
    private Long idBioplanta;

    @Column(name = "id_filiada", nullable = false)
    @Schema(description = "Identificador da filiada", example = "1")
    private Long idFiliada;

    @Column(name = "id_estabelecimento", nullable = false)
    @Schema(description = "Identificador do estabelecimento", example = "1")
    private Long idEstabelecimento;

    @Column(name = "produtor", nullable = false)
    @Schema(description = "Nome do produtor", example = "JOAO DA SILVA")
    private String produtor;

    @Column(name = "distancia_km", nullable = false)
    @Schema(description = "Distancia em km", example = "35")
    private Integer distanciaKm;



    @Column(name = "transportadora")
    @Schema(description = "Transportadora responsavel", example = "Primato")
    private String transportadora;

    @Column(name = "data_agendada", nullable = false)
    @Schema(description = "Data agendada", example = "2026-02-09")
    private LocalDate dataAgendada;

    @Column(name = "qtd_viagens", nullable = false)
    @Schema(description = "Quantidade de viagens", example = "2")
    private Integer qtdViagens;

    @Column(name = "criado_em")
    @Schema(description = "Data de criacao", example = "2026-02-07T10:00:00")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @Schema(description = "Data de atualizacao", example = "2026-02-07T10:00:00")
    private LocalDateTime atualizadoEm;
}
