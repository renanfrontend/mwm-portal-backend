package com.mwm.bioplanta.service.portaria.expedicao;

import com.mwm.bioplanta.dto.portaria.exclusao.OrigemTransportadoraExclusao;
import com.mwm.bioplanta.dto.portaria.expedicao.PortariaExpedicaoExclusaoRequestDTO;
import com.mwm.bioplanta.model.BioPortariaExpedicao;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaExpedicaoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortariaExpedicaoExclusaoServiceTest {

    @Mock
    private PortariaRegistroRepository registroRepository;
    @Mock
    private BioPortariaExpedicaoRepository expedicaoRepository;
    @Mock
    private BioTransportadoraRepository transportadoraRepository;
    @Mock
    private BioVeiculoTransportadoraRepository veiculoRepository;

    @InjectMocks
    private PortariaExpedicaoExclusaoService service;

    @Test
    void excluir_deveExcluirApenasRegistroEExpedicaoQuandoOrigemSelecionada() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(1L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setStatus("Em andamento");

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(2L);
        expedicao.setRegistroId(1L);
        expedicao.setTransportadoraId(3L);
        expedicao.setVeiculoId(7L);

        PortariaExpedicaoExclusaoRequestDTO request = new PortariaExpedicaoExclusaoRequestDTO();
        request.setRegistroId("1");
        request.setTipoRegistro("EXPEDICAO");
        request.setExpedicaoId("2");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.SELECIONADA);
        request.setExcluirTransportadora(true);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(expedicaoRepository.findById(2L)).thenReturn(Optional.of(expedicao));

        var response = service.excluir(request);

        assertFalse(response.isTransportadoraExcluida());
        assertFalse(response.isVeiculoExcluido());
        verify(expedicaoRepository).delete(expedicao);
        verify(registroRepository).delete(registro);
        verify(transportadoraRepository, never()).delete(any(BioTransportadora.class));
        verify(veiculoRepository, never()).delete(any(BioVeiculoTransportadora.class));
    }

    @Test
    void excluir_deveExcluirAuxiliaresQuandoOrigemOutrosEFlagsAtivas() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(10L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setStatus("Em andamento");

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(20L);
        expedicao.setRegistroId(10L);
        expedicao.setTransportadoraId(30L);
        expedicao.setVeiculoId(40L);

        BioTransportadora transportadora = new BioTransportadora();
        transportadora.setId(30L);
        BioVeiculoTransportadora veiculo = new BioVeiculoTransportadora();
        veiculo.setId(40L);

        PortariaExpedicaoExclusaoRequestDTO request = new PortariaExpedicaoExclusaoRequestDTO();
        request.setRegistroId("10");
        request.setTipoRegistro("EXPEDICAO");
        request.setExpedicaoId("20");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(true);
        request.setExcluirVeiculo(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(expedicaoRepository.findById(20L)).thenReturn(Optional.of(expedicao));
        when(transportadoraRepository.findById(30L)).thenReturn(Optional.of(transportadora));
        when(veiculoRepository.findById(40L)).thenReturn(Optional.of(veiculo));

        var response = service.excluir(request);

        assertEquals(10L, response.getRegistroId());
        assertEquals(20L, response.getReferenciaId());
        assertEquals(true, response.isTransportadoraExcluida());
        assertEquals(true, response.isVeiculoExcluido());
        verify(veiculoRepository).delete(veiculo);
        verify(transportadoraRepository).delete(transportadora);
        verify(expedicaoRepository).delete(expedicao);
        verify(registroRepository).delete(registro);
    }

    @Test
    void excluir_deveFalharQuandoOrigemOutrosEIdsNaoConferem() {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setId(10L);
        registro.setTipoRegistro("EXPEDICAO");
        registro.setStatus("Em andamento");

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setId(20L);
        expedicao.setRegistroId(10L);
        expedicao.setTransportadoraId(30L);

        PortariaExpedicaoExclusaoRequestDTO request = new PortariaExpedicaoExclusaoRequestDTO();
        request.setRegistroId("10");
        request.setTipoRegistro("EXPEDICAO");
        request.setExpedicaoId("20");
        request.setTransportadoraId("999");
        request.setOrigemTransportadora(OrigemTransportadoraExclusao.OUTROS);
        request.setExcluirTransportadora(true);

        when(registroRepository.findById(10L)).thenReturn(Optional.of(registro));
        when(expedicaoRepository.findById(20L)).thenReturn(Optional.of(expedicao));

        PortariaExclusaoValidacaoException ex = assertThrows(
                PortariaExclusaoValidacaoException.class,
                () -> service.excluir(request));

        assertEquals("transportadoraId não pertence ao registro informado.", ex.getMessage());
    }
}
