package com.mwm.bioplanta.dto.abastecimento;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AbastecimentoRequest", description = "Requisição para registro de abastecimento")
public class AbastecimentoRequestDTO {

    @Schema(description = "ID único do registro", example = "1")
    private Long id;

    @Schema(description = "ID da transportadora")
    private Long id_transportadora;

    @Schema(description = "ID do veículo")
    private Long id_veiculo_transportadora;

    @Schema(description = "ID do usuário")
    private Long id_usuario;

    @Schema(description = "ID da assinatura")
    private Long id_assinatura;

    @Schema(description = "Pressão inicial em m3", example = "1000,00")
    private Double pressao_inicial;

    @Schema(description = "Odômetro em km", example = "1234567")
    private Integer odometro;

    @Schema(description = "Tag do frentista", example = "12345678901")
    private String frentista;

    @Schema(description = "Data de criação")
    private String criado_em;

    @Schema(description = "Hora inicial", example = "08:00")
    private String hora_inicial;

    @Schema(description = "Data de atualização")
    private String atualizado_em;

    @Schema(description = "Data de expiração")
    private String expirado_em;

    @Schema(description = "Status de preenchimento", example = "Andamento", allowableValues = {"Andamento", "Concluído"})
    private String status;

    @Schema(description = "Tipo de execução", example = "Manual", allowableValues = {"Manual", "Automação"})
    private String tipo_execucao;

    @Schema(description = "Pressão final em m3", example = "1000,00")
    private Double pressao_final;

    @Schema(description = "Volume abastecido em m3", example = "1000,00")
    private Double volume_abastecido;

    @Schema(description = "Hora final", example = "08:00")
    private String hora_final;
}
