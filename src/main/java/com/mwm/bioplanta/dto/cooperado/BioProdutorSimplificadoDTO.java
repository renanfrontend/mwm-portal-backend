package com.mwm.bioplanta.dto.cooperado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado do BioProdutor para retornar em respostas aninhadas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados básicos do produtor")
public class BioProdutorSimplificadoDTO {

    @Schema(description = "ID do produtor", example = "456")
    private Long id;

    @Schema(description = "Código do produtor", example = "COD-123")
    private String codigoProdutor;

    @Schema(description = "Nome do produtor", example = "João da Silva")
    private String nome;

    @Schema(description = "CPF ou CNPJ do produtor", example = "123.456.789-00")
    private String cpfCnpj;

    @Schema(description = "Telefone principal", example = "45999999999")
    private String telefonePrincipal;

    @Schema(description = "ID da filiada", example = "1")
    private Long filiadaId;

    @Schema(description = "Nome da filiada", example = "Filiada Exemplo")
    private String filiadaNome;

    @Schema(description = "Distância em km do produtor até a planta", example = "12.50")
    private java.math.BigDecimal distanciaKm;
}
