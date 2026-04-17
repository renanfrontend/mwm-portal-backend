package com.mwm.bioplanta.service.portaria.exclusao;

import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;

public record ExclusaoTransporteContexto(
        BioTransportadora transportadora,
        BioVeiculoTransportadora veiculo,
        boolean excluirTransportadora,
        boolean excluirVeiculo) {

    public static ExclusaoTransporteContexto vazio() {
        return new ExclusaoTransporteContexto(null, null, false, false);
    }
}