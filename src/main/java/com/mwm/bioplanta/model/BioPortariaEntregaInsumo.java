package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bio_portaria_entrega_insumo")
@Data
public class BioPortariaEntregaInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_portaria_entrega_insumo")
    @SequenceGenerator(name = "seq_portaria_entrega_insumo", sequenceName = "seq_portaria_entrega_insumo", allocationSize = 1)
    private Long id;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Column(name = "transportadora_id")
    private Long transportadoraId;

    @Column(name = "veiculo_id")
    private Long veiculoId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @Column(name = "horario_entrada", nullable = false)
    private LocalTime horarioEntrada;

    @Column(name = "atividade", nullable = false)
    private String atividade;

    @Column(name = "transportadora_manual")
    private String transportadoraManual;

    @Column(name = "tipo_veiculo", nullable = false)
    private String tipoVeiculo;

    @Column(name = "placa")
    private String placa;

    @Column(name = "motorista", nullable = false)
    private String motorista;

    @Column(name = "cpf_passaporte", nullable = false)
    private String cpfPassaporte;

    @Column(name = "empresa", nullable = false)
    private String empresa;

    @Column(name = "nota_fiscal", nullable = false)
    private String notaFiscal;

    @Column(name = "peso_inicial", nullable = false)
    private Double pesoInicial;

    @Column(name = "peso_final", nullable = false)
    private Double pesoFinal;

    @Column(name = "data_saida")
    private LocalDate dataSaida;

    @Column(name = "horario_saida")
    private LocalTime horarioSaida;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}