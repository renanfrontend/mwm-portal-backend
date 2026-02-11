package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioProdutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BioProdutorRepository extends JpaRepository<BioProdutor, Long> {
    BioProdutor findByCpfCnpj(String cpfCnpj);
}
