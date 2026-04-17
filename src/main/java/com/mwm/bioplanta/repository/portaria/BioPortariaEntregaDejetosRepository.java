package com.mwm.bioplanta.repository.portaria;

import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BioPortariaEntregaDejetosRepository extends JpaRepository<BioPortariaEntregaDejetos, Long> {
    long countByTransportadoraId(Long transportadoraId);
    long countByVeiculoId(Long veiculoId);
}
