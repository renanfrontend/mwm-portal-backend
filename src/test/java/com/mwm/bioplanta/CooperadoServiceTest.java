package com.mwm.bioplanta;

import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.model.BioProdutor;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;
import com.mwm.bioplanta.repository.BioFiliadaRepository;
import com.mwm.bioplanta.repository.BioPlantaRepository;
import com.mwm.bioplanta.repository.BioProducaoRepository;
import com.mwm.bioplanta.repository.BioProdutorRepository;
import com.mwm.bioplanta.repository.BioTransportadoraRepository;
import com.mwm.bioplanta.service.CooperadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CooperadoServiceTest {

    @Mock
    private BioFiliadaRepository bioFiliadaRepository;
    @Mock
    private BioProdutorRepository bioProdutorRepository;
    @Mock
    private BioEstabelecimentoRepository bioEstabelecimentoRepository;
    @Mock
    private BioProducaoRepository bioProducaoRepository;
    @Mock
    private BioTransportadoraRepository bioTransportadoraRepository;
    @Mock
    private BioPlantaRepository bioPlantaRepository;

    @InjectMocks
    private CooperadoService cooperadoService;

    @Test
    void inativarProdutor_deveInativarProdutorEEstabelecimento() {
        Long idProdutor = 89L;

        BioProdutor produtor = new BioProdutor();
        produtor.setId(idProdutor);
        produtor.setStatus("A");

        BioEstabelecimento estabelecimento = new BioEstabelecimento();
        estabelecimento.setId(101L);
        estabelecimento.setStatus("A");
        estabelecimento.setBioProdutor(produtor);

        when(bioProdutorRepository.findById(idProdutor)).thenReturn(Optional.of(produtor));
        when(bioEstabelecimentoRepository.findByBioProdutorId(idProdutor)).thenReturn(List.of(estabelecimento));

        cooperadoService.inativarProdutor(idProdutor);

        assertEquals("I", produtor.getStatus());
        assertEquals("I", estabelecimento.getStatus());
        verify(bioProdutorRepository).save(produtor);
        verify(bioEstabelecimentoRepository).saveAll(List.of(estabelecimento));
    }

    @Test
    void inativarProdutor_deveFalharQuandoProdutorNaoExiste() {
        Long idProdutor = 89L;
        when(bioProdutorRepository.findById(idProdutor)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cooperadoService.inativarProdutor(idProdutor));

        assertEquals("Produtor não encontrado com ID: 89", ex.getMessage());
    }
}
