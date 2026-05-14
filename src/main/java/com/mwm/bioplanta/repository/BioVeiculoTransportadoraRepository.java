package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.BioTransportadora;

import java.util.List;

public interface BioVeiculoTransportadoraRepository extends JpaRepository<BioVeiculoTransportadora, Long> {

    BioVeiculoTransportadora findByTipo(String tipo);

    List<BioVeiculoTransportadora> findByBioTransportadora(BioTransportadora bioTransportadora);

    List<BioVeiculoTransportadora> findByBioTransportadoraId(Long transportadoraId);

    long countByBioTransportadoraId(Long transportadoraId);
}