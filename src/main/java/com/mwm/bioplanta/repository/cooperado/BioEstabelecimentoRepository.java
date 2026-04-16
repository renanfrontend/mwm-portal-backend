package com.mwm.bioplanta.repository.cooperado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mwm.bioplanta.model.BioEstabelecimento;
import java.util.List;

public interface BioEstabelecimentoRepository extends JpaRepository<BioEstabelecimento, Long> {
    boolean existsByNumeroEstabelecimento(String numeroEstabelecimento);
    boolean existsByNumeroPropriedade(String numeroPropriedade);
    long countByBioProdutorIdAndStatus(Long bioProdutorId, String status);
    java.util.Optional<BioEstabelecimento> findFirstByNumeroEstabelecimento(String numeroEstabelecimento);
    java.util.Optional<BioEstabelecimento> findFirstByBioProdutorIdOrderByIdAsc(Long bioProdutorId);
    List<BioEstabelecimento> findByBioProdutorId(Long bioProdutorId);
    
        @Query("SELECT e FROM BioEstabelecimento e " +
            "JOIN FETCH e.bioProdutor p " +
            "JOIN p.bioFiliada f " +
            "WHERE (:filiadaId IS NULL OR f.id = :filiadaId) " +
            "AND e.status = 'A' " +
            "AND p.status = 'A'")
        List<BioEstabelecimento> findByFiliada(@Param("filiadaId") Long filiadaId);

        @Query("SELECT e FROM BioEstabelecimento e " +
            "JOIN FETCH e.bioProdutor p " +
            "JOIN p.bioFiliada f " +
            "JOIN f.bioPlanta bp " +
            "WHERE f.id = :filiadaId " +
            "AND bp.id = :plantaId " +
            "AND e.status = 'A' " +
            "AND p.status = 'A'")
        List<BioEstabelecimento> findByPlantaAndFiliada(@Param("plantaId") Long plantaId,
                                                        @Param("filiadaId") Long filiadaId);
}