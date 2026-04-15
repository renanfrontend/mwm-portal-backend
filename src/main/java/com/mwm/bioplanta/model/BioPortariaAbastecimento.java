package com.mwm.bioplanta.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_portaria_abastecimento")
@Data
public class BioPortariaAbastecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_portaria_abastecimento")
    @SequenceGenerator(name = "seq_portaria_abastecimento", sequenceName = "seq_portaria_abastecimento", allocationSize = 1)
    private Long id;

    @Column(name = "registro_id", nullable = false, unique = true)
    private Long registroId;

    @Column(name = "transportadora_id")
    private Long transportadoraId;

    @Column(name = "transportadora_manual")
    private String transportadoraManual;

    @Column(name = "veiculo_id")
    private Long veiculoId;

    @Column(name = "placa_manual")
    private String placaManual;

    @Column(name = "placa")
    private String placa;

    @Column(name = "tipo_veiculo")
    private String tipoVeiculo;

    @Column(name = "motorista_id")
    private Long motoristaId;

    @Column(name = "motorista_nome")
    private String motoristaNome;

    @Column(name = "cpf_motorista")
    private String cpfMotorista;

    @Column(name = "peso_inicial")
    private Double pesoInicial;

    @Column(name = "peso_final")
    private Double pesoFinal;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
