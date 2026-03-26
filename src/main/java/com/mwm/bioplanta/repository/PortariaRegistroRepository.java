package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.PortariaRegistro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para Portaria Registro
 * @author Antonio Marcos de Souza Santos
 * @date 24/03/2026
 */
@Repository
public interface PortariaRegistroRepository extends JpaRepository<PortariaRegistro, Long> {
    
}
