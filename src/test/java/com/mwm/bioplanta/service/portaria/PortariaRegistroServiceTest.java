package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaRegistroDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.BioPortariaExpedicao;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaInsumoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaExpedicaoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
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
    private BioPortariaEntregaInsumoRepository entregaInsumoRepository;
    @Mock
    private BioPortariaExpedicaoRepository expedicaoRepository;
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

    @Test
    void getRegistroById_deveRetornarExpedicaoQuandoTipoForExpedicao() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(4L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setDataEntrada(LocalDate.of(2026, 5, 8));
        registro.setHoraEntrada(LocalTime.of(9, 30));

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(14L);
        expedicao.setRegistroId(4L);
        expedicao.setVeiculoId(99L);
        expedicao.setMotorista("Motorista Teste");
        expedicao.setCpfPassaporte("12345678900");
        expedicao.setAtividade("CARREGAMENTO");
        expedicao.setTipoVeiculo("TRUCK");
        expedicao.setNotaFiscal("NF-123");
        expedicao.setPlaca("ABC1D23");
        expedicao.setDataEntrada(LocalDate.of(2026, 5, 8));
        expedicao.setHorarioEntrada(LocalTime.of(9, 30));

        when(portariaRegistroRepository.findById(4L)).thenReturn(Optional.of(registro));
        when(expedicaoRepository.findByRegistroIdIn(org.mockito.ArgumentMatchers.anySet())).thenReturn(List.of(expedicao));
        when(veiculoRepository.findAllById(org.mockito.ArgumentMatchers.anyIterable())).thenReturn(List.of());

        PortariaRegistroDTO response = service.getRegistroById(4L);

        assertEquals(14L, response.getExpedicao().getId());
        assertEquals(4L, response.getExpedicao().getRegistroId());
        assertEquals("ABC1D23", response.getExpedicao().getPlaca());
        assertEquals("Motorista Teste", response.getExpedicao().getMotorista());
    }

    @Test
    void updateRegistro_deveAtualizarExpedicaoComTransportadoraSelecionada() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(50L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setExpedicaoId(500L);
        registro.setDataEntrada(LocalDate.of(2026, 5, 8));
        registro.setHoraEntrada(LocalTime.of(8, 0));

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(500L);
        expedicao.setRegistroId(50L);
        expedicao.setTransportadoraManual("Transportadora Manual Antiga");
        expedicao.setPlaca("ANT-0001");
        expedicao.setMotorista("Motorista Antigo");
        expedicao.setCpfPassaporte("00000000000");
        expedicao.setTipoVeiculo("Truck");
        expedicao.setDataEntrada(LocalDate.of(2026, 5, 8));
        expedicao.setHorarioEntrada(LocalTime.of(8, 0));
        expedicao.setCriadoEm(LocalDateTime.now().minusDays(1));
        expedicao.setAtualizadoEm(LocalDateTime.now().minusDays(1));

        PortariaRegistroDTO dto = new PortariaRegistroDTO();
        dto.setDataEntrada("2026-05-08");
        dto.setHoraEntrada("08:30");
        dto.setDataSaida("2026-05-08");
        dto.setHoraSaida("14:00");
        dto.setObservacoes("Atualizado");

        var expedicaoDTO = new com.mwm.bioplanta.dto.portaria.PortariaExpedicaoResponseDTO();
        expedicaoDTO.setMotorista("João da Silva");
        expedicaoDTO.setCpfPassaporte("12345678901");
        expedicaoDTO.setTransportadoraId(3L);
        expedicaoDTO.setVeiculoId(7L);
        expedicaoDTO.setPlaca("ABC-1234");
        expedicaoDTO.setTipoVeiculo("Caminhão");
        expedicaoDTO.setNotaFiscal("NF-00123");
        expedicaoDTO.setPesoInicial(0d);
        expedicaoDTO.setPesoFinal(0d);
        dto.setExpedicao(expedicaoDTO);

        when(portariaRegistroRepository.findById(50L)).thenReturn(Optional.of(registro));
        when(portariaRegistroRepository.save(any(PortariaRegistro.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(expedicaoRepository.findById(500L)).thenReturn(Optional.of(expedicao));
        when(expedicaoRepository.findByRegistroIdIn(anySet())).thenReturn(List.of(expedicao));
        when(veiculoRepository.findAllById(anyIterable())).thenReturn(List.of());

        PortariaRegistroDTO response = service.updateRegistro(50L, dto);

        assertEquals("João da Silva", response.getExpedicao().getMotorista());
        assertEquals("12345678901", response.getExpedicao().getCpfPassaporte());
        assertEquals(3L, response.getExpedicao().getTransportadoraId());
        assertEquals(7L, response.getExpedicao().getVeiculoId());
        assertEquals("ABC-1234", response.getExpedicao().getPlaca());
        assertNull(response.getExpedicao().getTransportadoraManual());
        verify(expedicaoRepository).save(any(BioPortariaExpedicao.class));
    }

    @Test
    void updateRegistro_deveAtualizarExpedicaoComTransportadoraManual() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(51L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setDataEntrada(LocalDate.of(2026, 5, 8));
        registro.setHoraEntrada(LocalTime.of(8, 0));

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(501L);
        expedicao.setRegistroId(51L);
        expedicao.setTransportadoraId(3L);
        expedicao.setVeiculoId(7L);
        expedicao.setPlaca("ABC-1234");
        expedicao.setMotorista("Motorista Antigo");
        expedicao.setCpfPassaporte("00000000000");
        expedicao.setTipoVeiculo("Truck");
        expedicao.setDataEntrada(LocalDate.of(2026, 5, 8));
        expedicao.setHorarioEntrada(LocalTime.of(8, 0));
        expedicao.setCriadoEm(LocalDateTime.now().minusDays(1));
        expedicao.setAtualizadoEm(LocalDateTime.now().minusDays(1));

        PortariaRegistroDTO dto = new PortariaRegistroDTO();
        dto.setDataEntrada("2026-05-08");
        dto.setHoraEntrada("08:30");

        var expedicaoDTO = new com.mwm.bioplanta.dto.portaria.PortariaExpedicaoResponseDTO();
        expedicaoDTO.setMotorista("João da Silva");
        expedicaoDTO.setCpfPassaporte("12345678901");
        expedicaoDTO.setTransportadoraManual("Transportadora XYZ Ltda");
        expedicaoDTO.setPlaca("XYZ-9999");
        expedicaoDTO.setTipoVeiculo("Caminhão");
        expedicaoDTO.setPesoInicial(0d);
        expedicaoDTO.setPesoFinal(0d);
        dto.setExpedicao(expedicaoDTO);

        when(portariaRegistroRepository.findById(51L)).thenReturn(Optional.of(registro));
        when(portariaRegistroRepository.save(any(PortariaRegistro.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(expedicaoRepository.findByRegistroId(51L)).thenReturn(Optional.of(expedicao));
        when(expedicaoRepository.findById(501L)).thenReturn(Optional.of(expedicao));
        when(expedicaoRepository.findByRegistroIdIn(anySet())).thenReturn(List.of(expedicao));
        when(veiculoRepository.findAllById(anyIterable())).thenReturn(List.of());

        PortariaRegistroDTO response = service.updateRegistro(51L, dto);

        assertEquals("João da Silva", response.getExpedicao().getMotorista());
        assertEquals("12345678901", response.getExpedicao().getCpfPassaporte());
        assertNull(response.getExpedicao().getTransportadoraId());
        assertNull(response.getExpedicao().getVeiculoId());
        assertEquals("Transportadora XYZ Ltda", response.getExpedicao().getTransportadoraManual());
        assertEquals("XYZ-9999", response.getExpedicao().getPlaca());
        verify(expedicaoRepository).save(any(BioPortariaExpedicao.class));
    }
}