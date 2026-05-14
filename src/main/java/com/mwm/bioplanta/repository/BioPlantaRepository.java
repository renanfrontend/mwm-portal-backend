package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.BioPlanta;

public interface BioPlantaRepository extends JpaRepository<BioPlanta, Long> {

}