package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.BioFiliada;
import java.util.List;

public interface BioFiliadaRepository extends JpaRepository<BioFiliada, Long> {

    BioFiliada findByNome(String nome);
    
    List<BioFiliada> findByAtivo(String ativo);
}