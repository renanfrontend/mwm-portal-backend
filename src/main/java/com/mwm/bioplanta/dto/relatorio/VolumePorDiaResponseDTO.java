package com.mwm.bioplanta.dto.relatorio;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolumePorDiaResponseDTO {
	private String data;
	private BigDecimal volumeTotal;
}
