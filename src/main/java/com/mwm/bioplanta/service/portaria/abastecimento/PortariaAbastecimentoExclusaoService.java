package com.mwm.bioplanta.service.portaria.abastecimento;

import com.mwm.bioplanta.dto.portaria.abastecimento.PortariaAbastecimentoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.ExclusaoTransporteContexto;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaExclusaoValidacaoService;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaTransporteExclusaoService;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortariaAbastecimentoExclusaoService {

    private static final String TIPO_REGISTRO = "ABASTECIMENTO";

    private final PortariaExclusaoValidacaoService validacaoService;
    private final PortariaTransporteExclusaoService transporteExclusaoService;
    private final PortariaRegistroRepository registroRepository;
    private final BioPortariaAbastecimentoRepository abastecimentoRepository;

    public PortariaAbastecimentoExclusaoService(
            PortariaExclusaoValidacaoService validacaoService,
            PortariaTransporteExclusaoService transporteExclusaoService,
            PortariaRegistroRepository registroRepository,
            BioPortariaAbastecimentoRepository abastecimentoRepository) {
        this.validacaoService = validacaoService;
        this.transporteExclusaoService = transporteExclusaoService;
        this.registroRepository = registroRepository;
        this.abastecimentoRepository = abastecimentoRepository;
    }

    @Transactional
    public ExclusaoPortariaResponseDTO excluir(PortariaAbastecimentoExclusaoRequestDTO request) {
        PortariaRegistro registro = validacaoService.carregarRegistroParaExclusao(
                request.getRegistroId(),
                request.getTipoRegistro(),
                TIPO_REGISTRO);

        Long abastecimentoId = validacaoService.parseRequiredId(request.getAbastecimentoId(), "abastecimentoId");
        BioPortariaAbastecimento abastecimento = abastecimentoRepository.findById(abastecimentoId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("abastecimentoId não pertence ao registro informado."));

        if (!registro.getId().equals(abastecimento.getRegistroId())) {
            throw new PortariaExclusaoValidacaoException("abastecimentoId não pertence ao registro informado.");
        }

        ExclusaoTransporteContexto transporte = transporteExclusaoService.prepararExclusao(
                request,
                abastecimento.getTransportadoraId(),
                abastecimento.getVeiculoId());

        abastecimentoRepository.delete(abastecimento);
        registroRepository.delete(registro);
        transporteExclusaoService.excluirCadastrosManuais(transporte);

        return ExclusaoPortariaResponseDTO.builder()
                .mensagem("Abastecimento excluído com sucesso.")
                .registroId(registro.getId())
                .tipoRegistro(TIPO_REGISTRO)
                .referenciaId(abastecimento.getId())
                .transportadoraExcluida(transporte.excluirTransportadora())
                .veiculoExcluido(transporte.excluirVeiculo())
                .agendaRealizadaExcluida(false)
                .build();
    }
}