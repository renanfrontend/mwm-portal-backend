package com.mwm.bioplanta.service.portaria.entrega;

import com.mwm.bioplanta.dto.portaria.entrega.PortariaEntregaDejetosExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaDejetos;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.agenda.BioAgendaRealizadaRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.ExclusaoTransporteContexto;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaExclusaoValidacaoService;
import com.mwm.bioplanta.service.portaria.exclusao.PortariaTransporteExclusaoService;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortariaEntregaDejetosExclusaoService {

    private static final String TIPO_REGISTRO = "ENTREGA_DEJETOS";

    private final PortariaExclusaoValidacaoService validacaoService;
    private final PortariaTransporteExclusaoService transporteExclusaoService;
    private final PortariaRegistroRepository registroRepository;
    private final BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    private final BioAgendaRealizadaRepository agendaRealizadaRepository;

    public PortariaEntregaDejetosExclusaoService(
            PortariaExclusaoValidacaoService validacaoService,
            PortariaTransporteExclusaoService transporteExclusaoService,
            PortariaRegistroRepository registroRepository,
            BioPortariaEntregaDejetosRepository entregaDejetosRepository,
            BioAgendaRealizadaRepository agendaRealizadaRepository) {
        this.validacaoService = validacaoService;
        this.transporteExclusaoService = transporteExclusaoService;
        this.registroRepository = registroRepository;
        this.entregaDejetosRepository = entregaDejetosRepository;
        this.agendaRealizadaRepository = agendaRealizadaRepository;
    }

    @Transactional
    public ExclusaoPortariaResponseDTO excluir(PortariaEntregaDejetosExclusaoRequestDTO request) {
        PortariaRegistro registro = validacaoService.carregarRegistroParaExclusao(
                request.getRegistroId(),
                request.getTipoRegistro(),
                TIPO_REGISTRO);

        Long entregaDejetosId = validacaoService.parseRequiredId(request.getEntregaDejetosId(), "entregaDejetosId");
        BioPortariaEntregaDejetos entregaDejetos = entregaDejetosRepository.findById(entregaDejetosId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("entregaDejetosId não pertence ao registro informado."));

        if (!entregaDejetos.getId().equals(registro.getEntregaDejetosId())) {
            throw new PortariaExclusaoValidacaoException("entregaDejetosId não pertence ao registro informado.");
        }

        ExclusaoTransporteContexto transporte = transporteExclusaoService.prepararExclusao(
                request,
                entregaDejetos.getTransportadoraId(),
                entregaDejetos.getVeiculoId());

        boolean agendaRealizadaExcluida = entregaDejetos.getAgendaRealizadaId() != null;

        registroRepository.delete(registro);
        if (agendaRealizadaExcluida) {
            agendaRealizadaRepository.deleteById(entregaDejetos.getAgendaRealizadaId());
        }
        entregaDejetosRepository.delete(entregaDejetos);
        transporteExclusaoService.excluirCadastrosManuais(transporte);

        return ExclusaoPortariaResponseDTO.builder()
                .mensagem("Entrega de dejetos excluída com sucesso.")
                .registroId(registro.getId())
                .tipoRegistro(TIPO_REGISTRO)
                .referenciaId(entregaDejetos.getId())
                .agendaRealizadaExcluida(agendaRealizadaExcluida)
                .transportadoraExcluida(transporte.excluirTransportadora())
                .veiculoExcluido(transporte.excluirVeiculo())
                .build();
    }
}