package com.mwm.bioplanta.service.portaria.expedicao;

import com.mwm.bioplanta.dto.portaria.expedicao.PortariaExpedicaoExclusaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.OrigemTransportadoraExclusao;
import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaResponseDTO;
import com.mwm.bioplanta.model.BioPortariaExpedicao;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaExpedicaoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoNaoEncontradaException;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortariaExpedicaoExclusaoService {

    private static final String TIPO_REGISTRO = "EXPEDICAO";
        private static final String STATUS_EM_ANDAMENTO = "Em andamento";

    private final PortariaRegistroRepository registroRepository;
    private final BioPortariaExpedicaoRepository expedicaoRepository;
        private final BioTransportadoraRepository transportadoraRepository;
        private final BioVeiculoTransportadoraRepository veiculoRepository;

    public PortariaExpedicaoExclusaoService(
            PortariaRegistroRepository registroRepository,
                        BioPortariaExpedicaoRepository expedicaoRepository,
                        BioTransportadoraRepository transportadoraRepository,
                        BioVeiculoTransportadoraRepository veiculoRepository) {
        this.registroRepository = registroRepository;
        this.expedicaoRepository = expedicaoRepository;
                this.transportadoraRepository = transportadoraRepository;
                this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public ExclusaoPortariaResponseDTO excluir(PortariaExpedicaoExclusaoRequestDTO request) {
                validarTipoRegistro(request.getTipoRegistro());

                Long registroId = parseRequiredId(request.getRegistroId(), "registroId");
                PortariaRegistro registro = registroRepository.findById(registroId)
                                .orElseThrow(() -> new PortariaExclusaoNaoEncontradaException("Registro principal da portaria não encontrado."));

                if (!TIPO_REGISTRO.equals(registro.getTipoRegistro())) {
                        throw new PortariaExclusaoValidacaoException("O registro informado não pertence ao domínio esperado para este endpoint.");
                }

                if (!STATUS_EM_ANDAMENTO.equals(registro.getStatus())) {
                        throw new PortariaExclusaoValidacaoException("A exclusão só é permitida para registros com status 'Em andamento'.");
                }

                Long expedicaoId = parseRequiredId(request.getExpedicaoId(), "expedicaoId");
        BioPortariaExpedicao expedicao = expedicaoRepository.findById(expedicaoId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("expedicaoId não pertence ao registro informado."));

        if (!registro.getId().equals(expedicao.getRegistroId())) {
            throw new PortariaExclusaoValidacaoException("expedicaoId não pertence ao registro informado.");
        }

                boolean excluirTransportadora = Boolean.TRUE.equals(request.getExcluirTransportadora());
                boolean excluirVeiculo = Boolean.TRUE.equals(request.getExcluirVeiculo());
                OrigemTransportadoraExclusao origem = request.getOrigemTransportadora();

                boolean transportadoraExcluida = false;
                boolean veiculoExcluido = false;

                if (origem == OrigemTransportadoraExclusao.OUTROS) {
                        Long transportadoraId = resolverIdTransporte(request.getTransportadoraId(), expedicao.getTransportadoraId(), "transportadoraId");
                        Long veiculoId = resolverIdTransporte(request.getVeiculoId(), expedicao.getVeiculoId(), "veiculoId");

                        if (excluirVeiculo && veiculoId != null) {
                                BioVeiculoTransportadora veiculo = veiculoRepository.findById(veiculoId).orElse(null);
                                if (veiculo != null) {
                                        veiculoRepository.delete(veiculo);
                                        veiculoExcluido = true;
                                }
                        }

                        if (excluirTransportadora && transportadoraId != null) {
                                BioTransportadora transportadora = transportadoraRepository.findById(transportadoraId).orElse(null);
                                if (transportadora != null) {
                                        transportadoraRepository.delete(transportadora);
                                        transportadoraExcluida = true;
                                }
                        }
                } else if (origem != OrigemTransportadoraExclusao.SELECIONADA) {
                        throw new PortariaExclusaoValidacaoException("origemTransportadora deve ser SELECIONADA ou OUTROS.");
                }

        expedicaoRepository.delete(expedicao);
        registroRepository.delete(registro);

        return ExclusaoPortariaResponseDTO.builder()
                .mensagem("Expedição excluída com sucesso.")
                .registroId(registro.getId())
                .tipoRegistro(TIPO_REGISTRO)
                .referenciaId(expedicao.getId())
                                .transportadoraExcluida(transportadoraExcluida)
                                .veiculoExcluido(veiculoExcluido)
                .agendaRealizadaExcluida(false)
                .build();
    }

        private void validarTipoRegistro(String tipoRegistro) {
                if (tipoRegistro == null || tipoRegistro.trim().isEmpty()) {
                        throw new PortariaExclusaoValidacaoException("tipoRegistro é obrigatório.");
                }

                if (!TIPO_REGISTRO.equals(tipoRegistro.trim())) {
                        throw new PortariaExclusaoValidacaoException("tipoRegistro incompatível com o endpoint chamado.");
                }
        }

        private Long parseRequiredId(String valor, String nomeCampo) {
                if (valor == null || valor.trim().isEmpty()) {
                        throw new PortariaExclusaoValidacaoException(nomeCampo + " é obrigatório.");
                }
                try {
                        return Long.valueOf(valor.trim());
                } catch (NumberFormatException ex) {
                        throw new PortariaExclusaoValidacaoException(nomeCampo + " deve ser numérico.");
                }
        }

        private Long parseOptionalId(String valor, String nomeCampo) {
                if (valor == null || valor.trim().isEmpty()) {
                        return null;
                }
                try {
                        return Long.valueOf(valor.trim());
                } catch (NumberFormatException ex) {
                        throw new PortariaExclusaoValidacaoException(nomeCampo + " deve ser numérico.");
                }
        }

        private Long resolverIdTransporte(String idInformado, Long idPersistido, String campo) {
                Long idConvertido = parseOptionalId(idInformado, campo);
                if (idConvertido != null && idPersistido != null && !idConvertido.equals(idPersistido)) {
                        throw new PortariaExclusaoValidacaoException(campo + " não pertence ao registro informado.");
                }
                return idConvertido != null ? idConvertido : idPersistido;
        }
}
