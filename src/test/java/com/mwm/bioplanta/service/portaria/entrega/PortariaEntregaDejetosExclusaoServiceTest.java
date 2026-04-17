package com.mwm.bioplanta.service.portaria.entrega;

import com.mwm.bioplanta.dto.portaria.entrega.PortariaEntregaDejetosExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.OrigemTransportadoraExclusao;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.agenda.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaExclusaoValidacaoService;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaTransporteExclusaoService;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortariaEntregaDejetosExclusaoServiceTest {

    @Mock
    private PortariaRegistroRepository registroRepository;
    @Mock
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    @Mock
    private BioPortariaAbastecimentoRepository abastecimentoRepository;
    @Mock
    private BioTransportadoraRepository transportadoraRepository;
    @Mock
    private BioVeiculoTransportadoraRepository veiculoRepository;
    @Mock
    private BioAgendaRealizadaRepository agendaRealizadaRepository;

    private PortariaEntregaDejetosExclusaoService service;

    @BeforeEach
    void setUp() {
        PortariaExclusaoValidacaoService validacaoService = new PortariaExclusaoValidacaoService(registroRepository);
        PortariaTransporteExclusaoService transporteService = new PortariaTransporteExclusaoService(
                validacaoService,
                transportadoraRepository,
                veiculoRepository,
                abastecimentoRepository,
                entregaDejetosRepository);
        service = new PortariaEntregaDejetosExclusaoService(
                validacaoService,
                transporteService,
                registroRepository,
                entregaDejetosRepository,
                agendaRealizadaRepository);
    }

    @Test
    void excluirDeveRemoverRegistroEEntregaSemAgendaQuandoTransportadoraForSelecionada() {
        PortariaRegistro registro = criarRegistro(10L, "Em andamento", 20L);
        BioPortariaEntregaDejetos entrega = criarEntrega(20L, null, 30L, 40L);
        PortariaEntregaDejetosExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.SELECIONADA);
        request.setExcluirTransportadora(false);
        request.setExcluirVeiculo(false);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(entregaDejetosRepository.findById(20L)).thenReturn(Optional.of(entrega));

        ExclusaoPortariaResponseDTO response = service.excluir(request);

        InOrder inOrder = inOrder(registroRepository, entregaDejetosRepository);
        inOrder.verify(registroRepository).delete(registro);
        inOrder.verify(entregaDejetosRepository).delete(entrega);
        verify(agendaRealizadaRepository, never()).deleteById(org.mockito.ArgumentMatchers.anyLong());

        assertFalse(response.isAgendaRealizadaExcluida());
        assertFalse(response.isTransportadoraExcluida());
        assertFalse(response.isVeiculoExcluido());
    }

    @Test
    void excluirDeveRemoverAgendaETransporteManualQuandoFluxoForOutros() {
        PortariaRegistro registro = criarRegistro(10L, "Em andamento", 20L);
        BioPortariaEntregaDejetos entrega = criarEntrega(20L, 99L, 30L, 40L);
        BioTransportadora transportadora = criarTransportadora(30L, "FORMULARIO_ENTREGA_DEJETOS");
        BioVeiculoTransportadora veiculo = criarVeiculo(40L, transportadora);
        PortariaEntregaDejetosExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(true);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(entregaDejetosRepository.findById(20L)).thenReturn(Optional.of(entrega));
        when(transportadoraRepository.findById(30L)).thenReturn(Optional.of(transportadora));
        when(veiculoRepository.findById(40L)).thenReturn(Optional.of(veiculo));
        when(abastecimentoRepository.countByTransportadoraId(30L)).thenReturn(0L);
        when(entregaDejetosRepository.countByTransportadoraId(30L)).thenReturn(1L);
        when(abastecimentoRepository.countByVeiculoId(40L)).thenReturn(0L);
        when(entregaDejetosRepository.countByVeiculoId(40L)).thenReturn(1L);
        when(veiculoRepository.countByBioTransportadoraId(30L)).thenReturn(1L);

        ExclusaoPortariaResponseDTO response = service.excluir(request);

        InOrder inOrder = inOrder(registroRepository, agendaRealizadaRepository, entregaDejetosRepository, veiculoRepository, transportadoraRepository);
        inOrder.verify(registroRepository).delete(registro);
        inOrder.verify(agendaRealizadaRepository).deleteById(99L);
        inOrder.verify(entregaDejetosRepository).delete(entrega);
        inOrder.verify(veiculoRepository).delete(veiculo);
        inOrder.verify(transportadoraRepository).delete(transportadora);

        assertTrue(response.isAgendaRealizadaExcluida());
        assertTrue(response.isTransportadoraExcluida());
        assertTrue(response.isVeiculoExcluido());
    }

    @Test
    void excluirDeveRejeitarQuandoStatusNaoForEmAndamento() {
        PortariaRegistro registro = criarRegistro(10L, "Concluído", 20L);
        PortariaEntregaDejetosExclusaoRequestDTO request = criarRequestBase();

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));

        PortariaExclusaoValidacaoException exception = assertThrows(
                PortariaExclusaoValidacaoException.class,
                () -> service.excluir(request));

        assertEquals("A exclusão só é permitida para registros com status 'Em andamento'.", exception.getMessage());
    }

    @Test
    void excluirDeveFalharQuandoEntregaNaoPertencerAoRegistro() {
        PortariaRegistro registro = criarRegistro(10L, "Em andamento", 999L);
        BioPortariaEntregaDejetos entrega = criarEntrega(20L, null, 30L, 40L);
        PortariaEntregaDejetosExclusaoRequestDTO request = criarRequestBase();

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(entregaDejetosRepository.findById(20L)).thenReturn(Optional.of(entrega));

        assertThrows(PortariaExclusaoValidacaoException.class, () -> service.excluir(request));
        verify(registroRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    private PortariaEntregaDejetosExclusaoRequestDTO criarRequestBase() {
        PortariaEntregaDejetosExclusaoRequestDTO request = new PortariaEntregaDejetosExclusaoRequestDTO();
        request.setRegistroId("10");
        request.setTipoRegistro("ENTREGA_DEJETOS");
        request.setEntregaDejetosId("20");
        return request;
    }

    private PortariaRegistro criarRegistro(Long id, String status, Long entregaId) {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(id);
        registro.setTipoRegistro("ENTREGA_DEJETOS");
        registro.setStatus(status);
        registro.setEntregaDejetosId(entregaId);
        return registro;
    }

    private BioPortariaEntregaDejetos criarEntrega(Long id, Long agendaRealizadaId, Long transportadoraId, Long veiculoId) {
        BioPortariaEntregaDejetos entrega = new BioPortariaEntregaDejetos();
        entrega.setId(id);
        entrega.setAgendaRealizadaId(agendaRealizadaId);
        entrega.setTransportadoraId(transportadoraId);
        entrega.setVeiculoId(veiculoId);
        return entrega;
    }

    private BioTransportadora criarTransportadora(Long id, String origemCadastro) {
        BioTransportadora transportadora = new BioTransportadora();
        transportadora.setId(id);
        transportadora.setOrigemCadastro(origemCadastro);
        return transportadora;
    }

    private BioVeiculoTransportadora criarVeiculo(Long id, BioTransportadora transportadora) {
        BioVeiculoTransportadora veiculo = new BioVeiculoTransportadora();
        veiculo.setId(id);
        veiculo.setBioTransportadora(transportadora);
        return veiculo;
    }
}