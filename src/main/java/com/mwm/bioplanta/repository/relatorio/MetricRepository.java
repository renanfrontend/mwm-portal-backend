package com.mwm.bioplanta.repository.relatorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.Metric;

public interface MetricRepository extends JpaRepository<Metric, Long>{

}
