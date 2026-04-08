package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioAgendaRealizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BioAgendaRealizadaRepository extends JpaRepository<BioAgendaRealizada, Long> {
}
