package com.mwm.bioplanta.repository.portaria;

import com.mwm.bioplanta.model.BioPortariaExpedicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BioPortariaExpedicaoRepository extends JpaRepository<BioPortariaExpedicao, Long> {
    List<BioPortariaExpedicao> findByRegistroIdIn(Set<Long> registroIds);
    Optional<BioPortariaExpedicao> findByRegistroId(Long registroId);
}
