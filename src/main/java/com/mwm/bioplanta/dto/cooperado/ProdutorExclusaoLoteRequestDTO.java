package com.mwm.bioplanta.dto.cooperado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorExclusaoLoteRequestDTO {
    private List<Long> estabelecimentoIds;
}
