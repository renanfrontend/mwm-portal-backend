package com.mwm.bioplanta.repository.portaria;

import com.mwm.bioplanta.model.BioPortariaEntregaInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BioPortariaEntregaInsumoRepository extends JpaRepository<BioPortariaEntregaInsumo, Long> {
    List<BioPortariaEntregaInsumo> findByRegistroIdIn(java.util.Set<Long> registroIds);
    Optional<BioPortariaEntregaInsumo> findByRegistroId(Long registroId);
}