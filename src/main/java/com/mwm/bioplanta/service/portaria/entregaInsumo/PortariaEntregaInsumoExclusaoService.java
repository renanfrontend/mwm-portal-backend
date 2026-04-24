package com.mwm.bioplanta.service.portaria.entregaInsumo;

import com.mwm.bioplanta.dto.portaria.entregaInsumo.PortariaEntregaInsumoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaInsumo;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaInsumoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.ExclusaoTransporteContexto;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaExclusaoValidacaoService;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaTransporteExclusaoService;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortariaEntregaInsumoExclusaoService {

    private static final String TIPO_REGISTRO = "ENTREGA_INSUMO";

    private final PortariaExclusaoValidacaoService validacaoService;
    private final PortariaTransporteExclusaoService transporteExclusaoService;
    private final PortariaRegistroRepository registroRepository;
    private final BioPortariaEntregaInsumoRepository entregaInsumoRepository;

    public PortariaEntregaInsumoExclusaoService(
            PortariaExclusaoValidacaoService validacaoService,
            PortariaTransporteExclusaoService transporteExclusaoService,
            PortariaRegistroRepository registroRepository,
            BioPortariaEntregaInsumoRepository entregaInsumoRepository) {
        this.validacaoService = validacaoService;
        this.transporteExclusaoService = transporteExclusaoService;
        this.registroRepository = registroRepository;
        this.entregaInsumoRepository = entregaInsumoRepository;
    }

    @Transactional
    public ExclusaoPortariaResponseDTO excluir(PortariaEntregaInsumoExclusaoRequestDTO request) {
        PortariaRegistro registro = validacaoService.carregarRegistroParaExclusao(
                request.getRegistroId(),
                request.getTipoRegistro(),
                TIPO_REGISTRO);

        Long entregaInsumoId = validacaoService.parseRequiredId(request.getEntregaInsumoId(), "entregaInsumoId");
        BioPortariaEntregaInsumo entregaInsumo = entregaInsumoRepository.findById(entregaInsumoId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("entregaInsumoId não pertence ao registro informado."));

        if (!registro.getId().equals(entregaInsumo.getRegistroId())) {
            throw new PortariaExclusaoValidacaoException("entregaInsumoId não pertence ao registro informado.");
        }

        ExclusaoTransporteContexto transporte = transporteExclusaoService.prepararExclusao(
                request,
                entregaInsumo.getTransportadoraId(),
                entregaInsumo.getVeiculoId());

        entregaInsumoRepository.delete(entregaInsumo);
        registroRepository.delete(registro);
        transporteExclusaoService.excluirCadastrosManuais(transporte);

        return ExclusaoPortariaResponseDTO.builder()
                .mensagem("Entrega de insumo excluída com sucesso.")
                .registroId(registro.getId())
                .tipoRegistro(TIPO_REGISTRO)
                .referenciaId(entregaInsumo.getId())
                .transportadoraExcluida(transporte.excluirTransportadora())
                .veiculoExcluido(transporte.excluirVeiculo())
                .agendaRealizadaExcluida(false)
                .build();
    }
}