package com.mwm.bioplanta.service.portaria.exclusao;

import com.mwm.bioplanta.dto.portaria.exclusao.ExclusaoPortariaBaseRequestDTO;
import com.mwm.bioplanta.dto.portaria.exclusao.OrigemTransportadoraExclusao;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaDejetosRepository;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoConflitoException;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;

@Service
public class PortariaTransporteExclusaoService {

    private static final String ORIGEM_CADASTRO_MESTRE = "FORMULARIO_LOGISTICA";

    private final PortariaExclusaoValidacaoService validacaoService;
    private final BioTransportadoraRepository transportadoraRepository;
    private final BioVeiculoTransportadoraRepository veiculoRepository;
    private final BioPortariaAbastecimentoRepository abastecimentoRepository;
    private final BioPortariaEntregaDejetosRepository entregaDejetosRepository;

    public PortariaTransporteExclusaoService(
            PortariaExclusaoValidacaoService validacaoService,
            BioTransportadoraRepository transportadoraRepository,
            BioVeiculoTransportadoraRepository veiculoRepository,
            BioPortariaAbastecimentoRepository abastecimentoRepository,
            BioPortariaEntregaDejetosRepository entregaDejetosRepository) {
        this.validacaoService = validacaoService;
        this.transportadoraRepository = transportadoraRepository;
        this.veiculoRepository = veiculoRepository;
        this.abastecimentoRepository = abastecimentoRepository;
        this.entregaDejetosRepository = entregaDejetosRepository;
    }

    public void validarContextoTransporte(ExclusaoPortariaBaseRequestDTO request, Long transportadoraIdPersistida, Long veiculoIdPersistido) {
        Long transportadoraIdInformada = validacaoService.parseOptionalId(request.getTransportadoraId(), "transportadoraId");
        Long veiculoIdInformado = validacaoService.parseOptionalId(request.getVeiculoId(), "veiculoId");

        validacaoService.validarIdsCompativeis(
                transportadoraIdInformada,
                transportadoraIdPersistida,
                "transportadoraId não pertence ao registro informado.");
        validacaoService.validarIdsCompativeis(
                veiculoIdInformado,
                veiculoIdPersistido,
                "veiculoId não pertence ao registro informado.");

        boolean excluirTransportadora = validacaoService.flagAtiva(request.getExcluirTransportadora());
        boolean excluirVeiculo = validacaoService.flagAtiva(request.getExcluirVeiculo());
        if ((excluirTransportadora || excluirVeiculo) && request.getOrigemTransportadora() != OrigemTransportadoraExclusao.OUTROS) {
            throw new PortariaExclusaoValidacaoException(
                    "A exclusão de transportadora ou veículo só é permitida para registros do fluxo manual 'Outros'.");
        }
    }

    public ExclusaoTransporteContexto prepararExclusao(ExclusaoPortariaBaseRequestDTO request, Long transportadoraIdPersistida, Long veiculoIdPersistido) {
        validarContextoTransporte(request, transportadoraIdPersistida, veiculoIdPersistido);

        boolean excluirTransportadora = validacaoService.flagAtiva(request.getExcluirTransportadora());
        boolean excluirVeiculo = validacaoService.flagAtiva(request.getExcluirVeiculo());
        if (!excluirTransportadora && !excluirVeiculo) {
            return ExclusaoTransporteContexto.vazio();
        }

        Long transportadoraId = resolverId(request.getTransportadoraId(), transportadoraIdPersistida, "transportadoraId");
        BioTransportadora transportadora = carregarTransportadoraManual(transportadoraId);

        BioVeiculoTransportadora veiculo = null;
        if (excluirVeiculo) {
            Long veiculoId = resolverId(request.getVeiculoId(), veiculoIdPersistido, "veiculoId");
            veiculo = carregarVeiculoManual(veiculoId, transportadoraId);
            validarUsoExclusivoVeiculo(veiculo.getId());
        }

        if (excluirTransportadora) {
            validarUsoExclusivoTransportadora(transportadora.getId());
            validarSemOutrosVeiculosAssociados(transportadora.getId(), veiculo, excluirVeiculo);
        }

        return new ExclusaoTransporteContexto(transportadora, veiculo, excluirTransportadora, excluirVeiculo);
    }

    public void excluirCadastrosManuais(ExclusaoTransporteContexto contexto) {
        if (contexto.excluirVeiculo() && contexto.veiculo() != null) {
            veiculoRepository.delete(contexto.veiculo());
        }

        if (contexto.excluirTransportadora() && contexto.transportadora() != null) {
            transportadoraRepository.delete(contexto.transportadora());
        }
    }

    private Long resolverId(String valorInformado, Long valorPersistido, String nomeCampo) {
        Long valorConvertido = validacaoService.parseOptionalId(valorInformado, nomeCampo);
        Long valorFinal = valorConvertido != null ? valorConvertido : valorPersistido;
        if (valorFinal == null) {
            throw new PortariaExclusaoValidacaoException(nomeCampo + " é obrigatório para excluir cadastro manual associado.");
        }
        return valorFinal;
    }

    private BioTransportadora carregarTransportadoraManual(Long transportadoraId) {
        BioTransportadora transportadora = transportadoraRepository.findById(transportadoraId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("transportadoraId não encontrado."));

        if (ORIGEM_CADASTRO_MESTRE.equals(transportadora.getOrigemCadastro())) {
            throw new PortariaExclusaoValidacaoException(
                    "A transportadora informada pertence ao cadastro mestre e não pode ser excluída por este fluxo.");
        }

        return transportadora;
    }

    private BioVeiculoTransportadora carregarVeiculoManual(Long veiculoId, Long transportadoraId) {
        BioVeiculoTransportadora veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new PortariaExclusaoValidacaoException("veiculoId não encontrado."));

        if (!veiculo.getBioTransportadora().getId().equals(transportadoraId)) {
            throw new PortariaExclusaoValidacaoException("veiculoId não pertence à transportadora informada para este processo.");
        }

        if (ORIGEM_CADASTRO_MESTRE.equals(veiculo.getBioTransportadora().getOrigemCadastro())) {
            throw new PortariaExclusaoValidacaoException(
                    "O veículo informado pertence a uma transportadora do cadastro mestre e não pode ser excluído por este fluxo.");
        }

        return veiculo;
    }

    private void validarUsoExclusivoTransportadora(Long transportadoraId) {
        long totalUsos = abastecimentoRepository.countByTransportadoraId(transportadoraId)
                + entregaDejetosRepository.countByTransportadoraId(transportadoraId);

        if (totalUsos == 0) {
            throw new PortariaExclusaoValidacaoException("A transportadora informada não está vinculada ao processo esperado.");
        }

        if (totalUsos > 1) {
            throw new PortariaExclusaoConflitoException(
                    "A transportadora manual está sendo usada por outro processo e não pode ser excluída.");
        }
    }

    private void validarUsoExclusivoVeiculo(Long veiculoId) {
        long totalUsos = abastecimentoRepository.countByVeiculoId(veiculoId)
                + entregaDejetosRepository.countByVeiculoId(veiculoId);

        if (totalUsos == 0) {
            throw new PortariaExclusaoValidacaoException("O veículo informado não está vinculado ao processo esperado.");
        }

        if (totalUsos > 1) {
            throw new PortariaExclusaoConflitoException(
                    "O veículo manual está sendo usado por outro processo e não pode ser excluído.");
        }
    }

    private void validarSemOutrosVeiculosAssociados(Long transportadoraId, BioVeiculoTransportadora veiculoAtual, boolean excluirVeiculo) {
        long quantidadeVeiculos = veiculoRepository.countByBioTransportadoraId(transportadoraId);
        long veiculosRemanescentes = quantidadeVeiculos;

        if (excluirVeiculo && veiculoAtual != null && veiculoAtual.getBioTransportadora().getId().equals(transportadoraId)) {
            veiculosRemanescentes -= 1;
        }

        if (veiculosRemanescentes > 0) {
            throw new PortariaExclusaoConflitoException(
                    "A transportadora manual possui outros veículos associados e não pode ser excluída.");
        }
    }
}