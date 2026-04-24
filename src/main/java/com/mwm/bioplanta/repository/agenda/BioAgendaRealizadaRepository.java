package com.mwm.bioplanta.repository.agenda;

import com.mwm.bioplanta.model.BioAgendaRealizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BioAgendaRealizadaRepository extends JpaRepository<BioAgendaRealizada, Long> {
    /**
     * Buscar agendas realizadas entre duas datas
     * 
     * @param dataInicio Data inicial do período (com hora)
     * @param dataFim Data final do período (com hora)
     * @return Lista de agendas realizadas dentro do período
     */
    List<BioAgendaRealizada> findByDataRealBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    Optional<BioAgendaRealizada> findByEntregaDejetosId(Long entregaDejetosId);
}
