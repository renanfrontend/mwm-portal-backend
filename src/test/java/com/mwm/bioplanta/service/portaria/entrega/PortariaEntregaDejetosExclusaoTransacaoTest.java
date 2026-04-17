package com.mwm.bioplanta.service.portaria.entrega;

import com.mwm.bioplanta.dto.portaria.entrega.PortariaEntregaDejetosExclusaoRequestDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.agenda.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class PortariaEntregaDejetosExclusaoTransacaoTest {

    @Autowired
    private PortariaEntregaDejetosExclusaoService service;

    @Autowired
    private PortariaRegistroRepository registroRepository;

    @Autowired
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;

    @MockBean
    private BioAgendaRealizadaRepository agendaRealizadaRepository;

    @Test
    void excluirDeveFazerRollbackQuandoFalhaOcorrerAposExcluirRegistroPrincipal() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setDataEntrada(LocalDate.of(2026, 4, 17));
        registro.setHoraEntrada(LocalTime.of(10, 0));
        registro.setTipoRegistro("ENTREGA_DEJETOS");
        registro.setStatus("Em andamento");
        registro = registroRepository.save(registro);

        BioPortariaEntregaDejetos entrega = new BioPortariaEntregaDejetos();
        entrega.setProdutorId(1L);
        entrega.setAgendaRealizadaId(999L);
        entrega = entregaDejetosRepository.save(entrega);

        registro.setEntregaDejetosId(entrega.getId());
        registro = registroRepository.save(registro);

        PortariaEntregaDejetosExclusaoRequestDTO request = new PortariaEntregaDejetosExclusaoRequestDTO();
        request.setRegistroId(String.valueOf(registro.getId()));
        request.setTipoRegistro("ENTREGA_DEJETOS");
        request.setEntregaDejetosId(String.valueOf(entrega.getId()));

        doThrow(new RuntimeException("falha simulada")).when(agendaRealizadaRepository).deleteById(999L);

        assertThrows(RuntimeException.class, () -> service.excluir(request));

        assertTrue(registroRepository.findById(registro.getId()).isPresent());
        assertTrue(entregaDejetosRepository.findById(entrega.getId()).isPresent());
    }
}