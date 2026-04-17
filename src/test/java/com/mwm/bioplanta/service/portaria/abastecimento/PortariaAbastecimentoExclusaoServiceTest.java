package com.mwm.bioplanta.service.portaria.abastecimento;

import com.mwm.bioplanta.dto.portaria.abastecimento.PortariaAbastecimentoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.OrigemTransportadoraExclusao;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaExclusaoValidacaoService;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaTransporteExclusaoService;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoConflitoException;
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
class PortariaAbastecimentoExclusaoServiceTest {

    @Mock
    private PortariaRegistroRepository registroRepository;
    @Mock
    private BioPortariaAbastecimentoRepository abastecimentoRepository;
    @Mock
    private BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    @Mock
    private BioTransportadoraRepository transportadoraRepository;
    @Mock
    private BioVeiculoTransportadoraRepository veiculoRepository;

    private PortariaAbastecimentoExclusaoService service;

    @BeforeEach
    void setUp() {
        PortariaExclusaoValidacaoService validacaoService = new PortariaExclusaoValidacaoService(registroRepository);
        PortariaTransporteExclusaoService transporteService = new PortariaTransporteExclusaoService(
                validacaoService,
                transportadoraRepository,
                veiculoRepository,
                abastecimentoRepository,
                entregaDejetosRepository);
        service = new PortariaAbastecimentoExclusaoService(
                validacaoService,
                transporteService,
                registroRepository,
                abastecimentoRepository);
    }

    @Test
    void excluirDeveRemoverApenasRegistroEAbastecimentoQuandoTransportadoraForSelecionada() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Em andamento", null);
        BioPortariaAbastecimento abastecimento = criarAbastecimento(20L, 10L, 30L, 40L);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.SELECIONADA);
        request.setExcluirTransportadora(false);
        request.setExcluirVeiculo(false);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(abastecimentoRepository.findById(20L)).thenReturn(Optional.of(abastecimento));

        ExclusaoPortariaResponseDTO response = service.excluir(request);

        InOrder inOrder = inOrder(abastecimentoRepository, registroRepository);
        inOrder.verify(abastecimentoRepository).delete(abastecimento);
        inOrder.verify(registroRepository).delete(registro);
        verify(veiculoRepository, never()).delete(org.mockito.ArgumentMatchers.any());
        verify(transportadoraRepository, never()).delete(org.mockito.ArgumentMatchers.any());

        assertEquals(10L, response.getRegistroId());
        assertEquals(20L, response.getReferenciaId());
        assertFalse(response.isTransportadoraExcluida());
        assertFalse(response.isVeiculoExcluido());
    }

    @Test
    void excluirDeveRemoverCadastrosManuaisQuandoFluxoForOutros() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Em andamento", null);
        BioPortariaAbastecimento abastecimento = criarAbastecimento(20L, 10L, 30L, 40L);
        BioTransportadora transportadora = criarTransportadora(30L, "FORMULARIO_ENTREGA_DEJETOS");
        BioVeiculoTransportadora veiculo = criarVeiculo(40L, transportadora);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(true);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(abastecimentoRepository.findById(20L)).thenReturn(Optional.of(abastecimento));
        when(transportadoraRepository.findById(30L)).thenReturn(Optional.of(transportadora));
        when(veiculoRepository.findById(40L)).thenReturn(Optional.of(veiculo));
        when(abastecimentoRepository.countByTransportadoraId(30L)).thenReturn(1L);
        when(entregaDejetosRepository.countByTransportadoraId(30L)).thenReturn(0L);
        when(abastecimentoRepository.countByVeiculoId(40L)).thenReturn(1L);
        when(entregaDejetosRepository.countByVeiculoId(40L)).thenReturn(0L);
        when(veiculoRepository.countByBioTransportadoraId(30L)).thenReturn(1L);

        ExclusaoPortariaResponseDTO response = service.excluir(request);

        InOrder inOrder = inOrder(abastecimentoRepository, registroRepository, veiculoRepository, transportadoraRepository);
        inOrder.verify(abastecimentoRepository).delete(abastecimento);
        inOrder.verify(registroRepository).delete(registro);
        inOrder.verify(veiculoRepository).delete(veiculo);
        inOrder.verify(transportadoraRepository).delete(transportadora);

        assertTrue(response.isTransportadoraExcluida());
        assertTrue(response.isVeiculoExcluido());
    }

    @Test
    void excluirDeveRejeitarQuandoStatusNaoForEmAndamento() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Concluído", null);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));

        PortariaExclusaoValidacaoException exception = assertThrows(
                PortariaExclusaoValidacaoException.class,
                () -> service.excluir(request));

        assertEquals("A exclusão só é permitida para registros com status 'Em andamento'.", exception.getMessage());
        verify(abastecimentoRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void excluirDeveRetornarConflitoQuandoTransportadoraEstiverCompartilhada() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Em andamento", null);
        BioPortariaAbastecimento abastecimento = criarAbastecimento(20L, 10L, 30L, 40L);
        BioTransportadora transportadora = criarTransportadora(30L, "FORMULARIO_ENTREGA_DEJETOS");
        BioVeiculoTransportadora veiculo = criarVeiculo(40L, transportadora);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(true);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(abastecimentoRepository.findById(20L)).thenReturn(Optional.of(abastecimento));
        when(transportadoraRepository.findById(30L)).thenReturn(Optional.of(transportadora));
        when(veiculoRepository.findById(40L)).thenReturn(Optional.of(veiculo));
        when(abastecimentoRepository.countByVeiculoId(40L)).thenReturn(1L);
        when(entregaDejetosRepository.countByVeiculoId(40L)).thenReturn(0L);
        when(abastecimentoRepository.countByTransportadoraId(30L)).thenReturn(2L);
        when(entregaDejetosRepository.countByTransportadoraId(30L)).thenReturn(0L);

        assertThrows(PortariaExclusaoConflitoException.class, () -> service.excluir(request));
        verify(abastecimentoRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void excluirDeveRetornarConflitoQuandoVeiculoEstiverCompartilhado() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Em andamento", null);
        BioPortariaAbastecimento abastecimento = criarAbastecimento(20L, 10L, 30L, 40L);
        BioTransportadora transportadora = criarTransportadora(30L, "FORMULARIO_ENTREGA_DEJETOS");
        BioVeiculoTransportadora veiculo = criarVeiculo(40L, transportadora);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("30");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(false);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(abastecimentoRepository.findById(20L)).thenReturn(Optional.of(abastecimento));
        when(transportadoraRepository.findById(30L)).thenReturn(Optional.of(transportadora));
        when(veiculoRepository.findById(40L)).thenReturn(Optional.of(veiculo));
        when(abastecimentoRepository.countByVeiculoId(40L)).thenReturn(2L);
        when(entregaDejetosRepository.countByVeiculoId(40L)).thenReturn(0L);

        assertThrows(PortariaExclusaoConflitoException.class, () -> service.excluir(request));
        verify(veiculoRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void excluirDeveFalharQuandoIdsNaoPertenceremAoRegistro() {
        PortariaRegistro registro = criarRegistro(10L, "ABASTECIMENTO", "Em andamento", null);
        BioPortariaAbastecimento abastecimento = criarAbastecimento(20L, 10L, 30L, 40L);
        PortariaAbastecimentoExclusaoRequestDTO request = criarRequestBase();
        request.setTransportadoraId("999");
        request.setVeiculoId("40");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.SELECIONADA);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(abastecimentoRepository.findById(20L)).thenReturn(Optional.of(abastecimento));

        assertThrows(PortariaExclusaoValidacaoException.class, () -> service.excluir(request));
        verify(abastecimentoRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    private PortariaAbastecimentoExclusaoRequestDTO criarRequestBase() {
        PortariaAbastecimentoExclusaoRequestDTO request = new PortariaAbastecimentoExclusaoRequestDTO();
        request.setRegistroId("10");
        request.setTipoRegistro("ABASTECIMENTO");
        request.setAbastecimentoId("20");
        return request;
    }

    private PortariaRegistro criarRegistro(Long id, String tipoRegistro, String status, Long entregaDejetosId) {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(id);
        registro.setTipoRegistro(tipoRegistro);
        registro.setStatus(status);
        registro.setEntregaDejetosId(entregaDejetosId);
        return registro;
    }

    private BioPortariaAbastecimento criarAbastecimento(Long id, Long registroId, Long transportadoraId, Long veiculoId) {
        BioPortariaAbastecimento abastecimento = new BioPortariaAbastecimento();
        abastecimento.setId(id);
        abastecimento.setRegistroId(registroId);
        abastecimento.setTransportadoraId(transportadoraId);
        abastecimento.setVeiculoId(veiculoId);
        return abastecimento;
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