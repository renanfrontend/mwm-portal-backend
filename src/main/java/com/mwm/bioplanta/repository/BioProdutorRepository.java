package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioProdutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BioProdutorRepository extends JpaRepository<BioProdutor, Long> {
        @org.springframework.data.jpa.repository.Query("SELECT p FROM BioProdutor p WHERE p.status = 'A' AND (" +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.cpfCnpj) LIKE LOWER(CONCAT('%', :search, '%')))")
        java.util.List<BioProdutor> buscarAtivos(@org.springframework.data.repository.query.Param("search") String search);

    @Query("SELECT p FROM BioProdutor p WHERE p.status = 'A' AND " +
            "FUNCTION('REPLACE', FUNCTION('REPLACE', FUNCTION('REPLACE', FUNCTION('REPLACE', p.cpfCnpj, '.', ''), '-', ''), '/', ''), ' ', '') = :cpfCnpj")
    Optional<BioProdutor> findAtivoByCpfCnpjNormalizado(@Param("cpfCnpj") String cpfCnpj);

    BioProdutor findByCpfCnpj(String cpfCnpj);
}
