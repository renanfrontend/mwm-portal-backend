package com.mwm.bioplanta.dto.cooperado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorPageResponseDTO {
    private Integer page;
    private Integer pageSize;
    private Long total;
    private List<ProdutorListResponseDTO> items;
}
