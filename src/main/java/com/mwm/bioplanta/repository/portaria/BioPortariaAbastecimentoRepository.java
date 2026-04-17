package com.mwm.bioplanta.repository.portaria;

import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BioPortariaAbastecimentoRepository extends JpaRepository<BioPortariaAbastecimento, Long> {
    Optional<BioPortariaAbastecimento> findByRegistroId(Long registroId);
    List<BioPortariaAbastecimento> findByRegistroIdIn(Collection<Long> registroIds);
    long countByTransportadoraId(Long transportadoraId);
    long countByVeiculoId(Long veiculoId);
}
