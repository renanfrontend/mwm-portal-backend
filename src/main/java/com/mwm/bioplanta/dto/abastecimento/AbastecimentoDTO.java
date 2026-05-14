package com.mwm.bioplanta.dto.abastecimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Abastecimento", description = "Dados de abastecimento")
public class AbastecimentoDTO {

    @Schema(description = "ID único do registro", example = "1")
    private Long id;

    @Schema(description = "ID da transportadora")
    private Long idTransportadora;

    @Schema(description = "ID do veículo")
    private Long idVeiculoTransportadora;

    @Schema(description = "ID do usuário")
    private Long idUsuario;

    @Schema(description = "ID da assinatura")
    private Long idAssinatura;

    @Schema(description = "Pressão inicial em m3", example = "1000,00")
    private Double pressaoInicial;

    @Schema(description = "Odômetro em km", example = "1234567")
    private Integer odometro;

    @Schema(description = "Tag do frentista", example = "12345678901")
    private String frentista;

    @Schema(description = "Data de criação")
    private LocalDateTime criadoEm;

    @Schema(description = "Hora inicial", example = "08:00")
    private String horaInicial;

    @Schema(description = "Data de atualização")
    private LocalDateTime atualizadoEm;

    @Schema(description = "Data de expiração")
    private LocalDateTime expiradoEm;

    @Schema(description = "Status de preenchimento", example = "Andamento", allowableValues = {"Andamento", "Concluído"})
    private String status;

    @Schema(description = "Tipo de execução", example = "Manual", allowableValues = {"Manual", "Automação"})
    private String tipoExecucao;

    @Schema(description = "Pressão final em m3", example = "1000,00")
    private Double pressaoFinal;

    @Schema(description = "Volume abastecido em m3", example = "1000,00")
    private Double volumeAbastecido;

    @Schema(description = "Hora final", example = "08:00")
    private String horaFinal;
}
