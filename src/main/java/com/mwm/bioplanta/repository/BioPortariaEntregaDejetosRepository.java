package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BioPortariaEntregaDejetosRepository extends JpaRepository<BioPortariaEntregaDejetos, Long> {
    // Métodos customizados se necessário
}
