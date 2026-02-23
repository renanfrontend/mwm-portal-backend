package com.mwm.bioplanta.repository;

import com.mwm.bioplanta.model.BioAgendaPlanejada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

public interface BioAgendaPlanejadaRepository extends JpaRepository<BioAgendaPlanejada, Long> {
    
    @Modifying
    @Query("DELETE FROM BioAgendaPlanejada b WHERE b.idEstabelecimento = :idEstabelecimento AND b.dataAgendada = :dataAgendada")
    void deleteByIdEstabelecimentoAndDataAgendada(Long idEstabelecimento, LocalDate dataAgendada);
    
    Optional<BioAgendaPlanejada> findByIdEstabelecimentoAndDataAgendada(Long idEstabelecimento, LocalDate dataAgendada);


    List<BioAgendaPlanejada> findByIdBioplantaAndIdFiliadaAndDataAgendadaBetween(Long idBioplanta,
                                                                                Long idFiliada,
                                                                                LocalDate dataInicio,
                                                                                LocalDate dataFim);

    @Query("SELECT COUNT(b) FROM BioAgendaPlanejada b WHERE " +
           "b.idBioplanta = :idBioplanta AND " +
           "b.idFiliada = :idFiliada AND " +
           "b.dataAgendada BETWEEN :dataInicio AND :dataFim AND " +
           "b.qtdViagens > 0")
    long countViagensReais(@Param("idBioplanta") Long idBioplanta,
                           @Param("idFiliada") Long idFiliada,
                           @Param("dataInicio") LocalDate dataInicio,
                           @Param("dataFim") LocalDate dataFim);

    @Query("SELECT b FROM BioAgendaPlanejada b WHERE " +
           "b.idBioplanta = :idBioplanta AND " +
           "b.idFiliada = :idFiliada AND " +
           "b.dataAgendada BETWEEN :dataInicio AND :dataFim AND " +
           "(:ids IS NULL OR b.idEstabelecimento IN :ids)")
    List<BioAgendaPlanejada> findParaCopia(@Param("idBioplanta") Long idBioplanta,
                                           @Param("idFiliada") Long idFiliada,
                                           @Param("dataInicio") LocalDate dataInicio,
                                           @Param("dataFim") LocalDate dataFim,
                                           @Param("ids") List<Long> ids);

    @Query("SELECT b FROM BioAgendaPlanejada b WHERE " +
           "b.idBioplanta = :idBioplanta AND " +
           "b.idFiliada = :idFiliada AND " +
           "b.dataAgendada BETWEEN :dataInicio AND :dataFim AND " +
           "(:ids IS NULL OR b.idEstabelecimento IN :ids)")
    List<BioAgendaPlanejada> findParaLimpeza(@Param("idBioplanta") Long idBioplanta,
                                             @Param("idFiliada") Long idFiliada,
                                             @Param("dataInicio") LocalDate dataInicio,
                                             @Param("dataFim") LocalDate dataFim,
                                             @Param("ids") List<Long> ids);

    @Modifying
    @Query("DELETE FROM BioAgendaPlanejada b WHERE " +
           "b.idBioplanta = :idBioplanta AND " +
           "b.idFiliada = :idFiliada AND " +
           "b.dataAgendada BETWEEN :dataInicio AND :dataFim")
    int deleteByPeriodo(@Param("idBioplanta") Long idBioplanta,
                        @Param("idFiliada") Long idFiliada,
                        @Param("dataInicio") LocalDate dataInicio,
                        @Param("dataFim") LocalDate dataFim);
}
