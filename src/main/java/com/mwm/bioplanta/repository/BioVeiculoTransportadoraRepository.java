package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.BioTransportadora;

import java.util.List;

public interface BioVeiculoTransportadoraRepository extends JpaRepository<BioVeiculoTransportadora, Long> {

    BioVeiculoTransportadora findByTipo(String tipo);

    List<BioVeiculoTransportadora> findByBioTransportadora(BioTransportadora bioTransportadora);

    List<BioVeiculoTransportadora> findByBioTransportadoraId(Long transportadoraId);

    long countByBioTransportadoraId(Long transportadoraId);

    @Query("SELECT v FROM BioVeiculoTransportadora v " +
           "JOIN FETCH v.bioTransportadora t " +
           "WHERE t.origemCadastro IS NULL OR t.origemCadastro != 'FORMULARIO_ENTREGA_DEJETOS' " +
           "ORDER BY t.nomeFantasia ASC")
    List<BioVeiculoTransportadora> findAllExcluindoEntregaDejetos();
}