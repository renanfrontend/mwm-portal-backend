package com.mwm.bioplanta.repository.cadastro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mwm.bioplanta.model.BioTransportadora;

public interface BioTransportadoraRepository extends JpaRepository<BioTransportadora, Long> {
    @Query("SELECT t FROM BioTransportadora t WHERE t.status = 'Ativo' AND t.origemCadastro = 'FORMULARIO_LOGISTICA'")
    Page<BioTransportadora> findAllAtivas(Pageable pageable);

    BioTransportadora findByNomeFantasia(String nomeFantasia);

    BioTransportadora findByCnpj(String cnpj);

    @Query("SELECT t FROM BioTransportadora t WHERE t.status = 'Ativo' AND t.origemCadastro = 'FORMULARIO_LOGISTICA' AND (" +
            "LOWER(t.nomeFantasia) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.razaoSocial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "t.cnpj LIKE CONCAT('%', :search, '%'))")
    Page<BioTransportadora> buscar(@Param("search") String search, Pageable pageable);

    @Override
    @org.springframework.lang.NonNull
    Page<BioTransportadora> findAll(@org.springframework.lang.NonNull Pageable pageable);
}