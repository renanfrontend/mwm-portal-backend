package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BioPortariaAbastecimentoRepository extends JpaRepository<BioPortariaAbastecimento, Long> {
    // Métodos customizados se necessário
}
