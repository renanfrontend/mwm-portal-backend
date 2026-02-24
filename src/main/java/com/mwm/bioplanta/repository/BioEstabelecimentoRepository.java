package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mwm.bioplanta.model.BioEstabelecimento;
import java.util.List;

public interface BioEstabelecimentoRepository extends JpaRepository<BioEstabelecimento, Long> {
    boolean existsByNumeroEstabelecimento(String numeroEstabelecimento);
    boolean existsByNumeroPropriedade(String numeroPropriedade);
    
        @Query("SELECT e FROM BioEstabelecimento e " +
            "JOIN FETCH e.bioProdutor p " +
            "JOIN p.bioFiliada f " +
            "WHERE (:filiadaId IS NULL OR f.id = :filiadaId) " +
            "AND p.status = 'A'")
        List<BioEstabelecimento> findByFiliada(@Param("filiadaId") Long filiadaId);
}