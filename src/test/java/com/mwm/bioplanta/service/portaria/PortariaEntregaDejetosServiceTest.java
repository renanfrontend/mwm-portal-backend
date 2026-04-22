package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.model.BioAgendaRealizada;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTipo;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.agenda.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTipoRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortariaEntregaDejetosServiceTest {

    @Mock
    private BioTransportadoraRepository transportadoraRepository;
    @Mock
    private BioVeiculoTipoRepository veiculoTipoRepository;
    @Mock
    private BioVeiculoTransportadoraRepository veiculoRepository;
    @Mock
    private PortariaRegistroRepository registroRepository;
    @Mock
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    @Mock
    private BioAgendaRealizadaRepository agendaRealizadaRepository;

    @InjectMocks
    private PortariaEntregaDejetosService service;

    @Test
    void registrarEntregaDeDejetos_devePersistirIdsGeradosParaTransportadoraEVeiculo() {
        PortariaEntregaDejetosDTO dto = new PortariaEntregaDejetosDTO();
        dto.setTipoRegistro("ENTREGA_DEJETOS");
        dto.setData_entrada("2026-04-16");
        dto.setHora_entrada("10:30");
        dto.setStatus("Concluido");
        dto.setOrigem_entrada("espontanea");

        PortariaEntregaDejetosDTO.EntregaDejetosDTO entregaDto = new PortariaEntregaDejetosDTO.EntregaDejetosDTO();
        entregaDto.setProdutor_id("23");
        entregaDto.setMotorista_nome("Joao Silva");
        entregaDto.setCpf_motorista("12345678900");
        entregaDto.setTransportadora_manual("Transportes XPTO");
        entregaDto.setPlaca_manual("ABC 1D23");
        entregaDto.setTipo_veiculo("Carreta");
        entregaDto.setPeso_inicial(20000);
        entregaDto.setPeso_final(15000);
        entregaDto.setDensidade("1025");
        dto.setEntrega_dejetos(entregaDto);

        BioTransportadora transportadoraSalva = new BioTransportadora();
        transportadoraSalva.setId(55L);
        transportadoraSalva.setNomeFantasia("Transportes XPTO");

        BioVeiculoTipo tipoSalvo = new BioVeiculoTipo();
        tipoSalvo.setId(66L);
        tipoSalvo.setLabel("Carreta");
        tipoSalvo.setValue("Carreta");

        BioVeiculoTransportadora veiculoSalvo = new BioVeiculoTransportadora();
        veiculoSalvo.setId(77L);
        veiculoSalvo.setPlaca("ABC-1D23"); // Placa já formatada, igual ao que o sistema salva
        veiculoSalvo.setBioTransportadora(transportadoraSalva);

        when(transportadoraRepository.save(org.mockito.ArgumentMatchers.any(BioTransportadora.class))).thenReturn(transportadoraSalva);
        when(veiculoTipoRepository.findAll()).thenReturn(Collections.emptyList());
        when(veiculoTipoRepository.save(org.mockito.ArgumentMatchers.any(BioVeiculoTipo.class))).thenReturn(tipoSalvo);
        when(veiculoRepository.findByBioTransportadoraId(55L)).thenReturn(Collections.emptyList());
        when(veiculoRepository.save(org.mockito.ArgumentMatchers.any(BioVeiculoTransportadora.class))).thenReturn(veiculoSalvo);
        when(registroRepository.save(org.mockito.ArgumentMatchers.any(PortariaRegistro.class))).thenAnswer(invocation -> {
            PortariaRegistro registro = invocation.getArgument(0);
            if (registro.getId() == null) {
                registro.setId(33L);
            }
            return registro;
        });
        when(entregaDejetosRepository.save(org.mockito.ArgumentMatchers.any(BioPortariaEntregaDejetos.class))).thenAnswer(invocation -> {
            BioPortariaEntregaDejetos entrega = invocation.getArgument(0);
            if (entrega.getId() == null) {
                entrega.setId(99L);
            }
            return entrega;
        });
        when(agendaRealizadaRepository.save(org.mockito.ArgumentMatchers.any(BioAgendaRealizada.class))).thenAnswer(invocation -> {
            BioAgendaRealizada agenda = invocation.getArgument(0);
            agenda.setId(111L);
            return agenda;
        });

        PortariaEntregaDejetosDTO response = service.registrarEntregaDeDejetos(dto);

        assertEquals("55", response.getEntrega_dejetos().getTransportadora_id());
        assertEquals("77", response.getEntrega_dejetos().getVeiculo_id());
        assertEquals("ABC-1D23", response.getEntrega_dejetos().getPlaca());
        assertEquals("ABC-1D23", response.getEntrega_dejetos().getPlaca_manual());
        assertEquals("Concluído", response.getStatus());
        assertEquals("ESPONTANEA", response.getOrigem_entrada());

        ArgumentCaptor<BioPortariaEntregaDejetos> entregaCaptor = ArgumentCaptor.forClass(BioPortariaEntregaDejetos.class);
        verify(entregaDejetosRepository, atLeastOnce()).save(entregaCaptor.capture());
        BioPortariaEntregaDejetos entregaPersistida = entregaCaptor.getAllValues().get(entregaCaptor.getAllValues().size() - 1);
        assertEquals(55L, entregaPersistida.getTransportadoraId());
        assertEquals(77L, entregaPersistida.getVeiculoId());
        assertEquals("ABC-1D23", entregaPersistida.getPlaca());
        assertEquals(111L, entregaPersistida.getAgendaRealizadaId());

        ArgumentCaptor<PortariaRegistro> registroCaptor = ArgumentCaptor.forClass(PortariaRegistro.class);
        verify(registroRepository, atLeastOnce()).save(registroCaptor.capture());
        PortariaRegistro registroPersistido = registroCaptor.getAllValues().get(registroCaptor.getAllValues().size() - 1);
        assertEquals(99L, registroPersistido.getEntregaDejetosId());
        assertEquals("Concluído", registroPersistido.getStatus());
        assertEquals("ESPONTANEA", registroPersistido.getOrigemEntrada());
    }
}