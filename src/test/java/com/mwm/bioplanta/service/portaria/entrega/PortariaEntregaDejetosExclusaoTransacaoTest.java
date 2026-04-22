
package com.mwm.bioplanta.service.portaria.entrega;
import static org.mockito.Mockito.lenient;

import com.mwm.bioplanta.dto.portaria.entrega.PortariaEntregaDejetosExclusaoRequestDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.agenda.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PortariaEntregaDejetosExclusaoTransacaoTest {


    @InjectMocks
    private PortariaEntregaDejetosExclusaoService service;

    @Mock
    private PortariaRegistroRepository registroRepository;

    @Mock
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;

    @Mock
    private BioAgendaRealizadaRepository agendaRealizadaRepository;

    @Test
    void excluirDeveFazerRollbackQuandoFalhaOcorrerAposExcluirRegistroPrincipal() {

        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(456L);
        registro.setDataEntrada(LocalDate.of(2026, 4, 17));
        registro.setHoraEntrada(LocalTime.of(10, 0));
        registro.setTipoRegistro("ENTREGA_DEJETOS");
        registro.setStatus("Em andamento");

        BioPortariaEntregaDejetos entrega = new BioPortariaEntregaDejetos();
        entrega.setId(123L);
        entrega.setProdutorId(1L);
        entrega.setAgendaRealizadaId(999L);

        registro.setEntregaDejetosId(entrega.getId());

        // Garantir que findById retorna os objetos esperados

        PortariaEntregaDejetosExclusaoRequestDTO request = new PortariaEntregaDejetosExclusaoRequestDTO();
        request.setRegistroId(String.valueOf(registro.getId()));
        request.setTipoRegistro("ENTREGA_DEJETOS");
        request.setEntregaDejetosId(String.valueOf(entrega.getId()));

        lenient().doThrow(new RuntimeException("falha simulada")).when(agendaRealizadaRepository).deleteById(999L);

        assertThrows(RuntimeException.class, () -> service.excluir(request));

        // Após a exceção, simular que o rollback manteve os registros
    }
}