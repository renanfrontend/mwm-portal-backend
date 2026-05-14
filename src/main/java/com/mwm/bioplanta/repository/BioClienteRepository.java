package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.BioCliente;

public interface BioClienteRepository extends JpaRepository<BioCliente, Long> {

}