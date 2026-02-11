package com.mwm.bioplanta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Contato DTO", description = "Dados de um contato")
public class ContatoDTO {

    @Schema(description = "Nome do contato", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Telefone do contato", example = "45999470460", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefone;

    @Schema(description = "Email do contato", example = "joao@transportadora.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
