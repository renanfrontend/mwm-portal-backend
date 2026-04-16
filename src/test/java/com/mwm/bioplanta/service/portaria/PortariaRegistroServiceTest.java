package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortariaRegistroServiceTest {

    @Mock
    private PortariaRegistroRepository portariaRegistroRepository;
    @Mock
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    @Mock
    private BioPortariaAbastecimentoRepository abastecimentoRepository;
    @Mock
    private BioVeiculoTransportadoraRepository veiculoRepository;
    @Mock
    private BioTransportadoraRepository transportadoraRepository;

    @InjectMocks
    private PortariaRegistroService service;

    @Test
    void createRegistro_deveNormalizarStatusEOrigem() {
        PortariaRegistroDTO dto = new PortariaRegistroDTO();
        dto.setDataEntrada("2026-04-16");
        dto.setHoraEntrada("08:00");
        dto.setTipoRegistro("VISITA");
        dto.setStatus("concluido");
        dto.setOrigemEntrada("agendada");

        when(portariaRegistroRepository.save(org.mockito.ArgumentMatchers.any(PortariaRegistro.class))).thenAnswer(invocation -> {
            PortariaRegistro registro = invocation.getArgument(0);
            registro.setId(10L);
            return registro;
        });

        PortariaRegistroDTO response = service.createRegistro(dto);

        assertEquals("Concluído", response.getStatus());
        assertEquals("AGENDADA", response.getOrigemEntrada());
    }

    @Test
    void updateRegistro_deveAtualizarEOrigemENormalizarCampos() {
        PortariaRegistro existente = new PortariaRegistro();
        existente.setId(20L);
        existente.setTipoRegistro("VISITA");
        existente.setDataEntrada(LocalDate.of(2026, 4, 16));
        existente.setHoraEntrada(LocalTime.of(8, 0));
        existente.setStatus("Em andamento");
        existente.setOrigemEntrada("ESPONTANEA");

        PortariaRegistroDTO dto = new PortariaRegistroDTO();
        dto.setStatus("concluido");
        dto.setOrigemEntrada("agendada");

        when(portariaRegistroRepository.findById(20L)).thenReturn(Optional.of(existente));
        when(portariaRegistroRepository.save(org.mockito.ArgumentMatchers.any(PortariaRegistro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PortariaRegistroDTO response = service.updateRegistro(20L, dto);

        assertEquals("Concluído", response.getStatus());
        assertEquals("AGENDADA", response.getOrigemEntrada());
    }

    @Test
    void getRegistroById_deveRetornarPlacaPersistidaQuandoVeiculoNaoEstiverCarregado() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(30L);
        registro.setTipoRegistro("ENTREGA_DEJETOS");
        registro.setDataEntrada(LocalDate.of(2026, 4, 16));
        registro.setHoraEntrada(LocalTime.of(8, 0));
        registro.setEntregaDejetosId(40L);

        BioPortariaEntregaDejetos entrega = new BioPortariaEntregaDejetos();
        entrega.setId(40L);
        entrega.setProdutorId(null);
        entrega.setPlaca("ABC1D23");
        entrega.setPlacaManual("ABC 1D23");
        entrega.setVeiculoId(50L);

        when(portariaRegistroRepository.findById(30L)).thenReturn(Optional.of(registro));
        when(entregaDejetosRepository.findAllById(org.mockito.ArgumentMatchers.anyIterable())).thenReturn(List.of(entrega));
        when(veiculoRepository.findAllById(org.mockito.ArgumentMatchers.anyIterable())).thenReturn(List.of());

        PortariaRegistroDTO response = service.getRegistroById(30L);

        assertEquals("ABC1D23", response.getEntrega_dejetos().getPlaca());
        assertEquals("ABC 1D23", response.getEntrega_dejetos().getPlaca_manual());
        assertEquals(null, response.getEntrega_dejetos().getProdutor_id());
    }
}