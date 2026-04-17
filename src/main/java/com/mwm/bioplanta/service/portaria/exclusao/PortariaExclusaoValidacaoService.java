package com.mwm.bioplanta.service.portaria.exclusao;

import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoNaoEncontradaException;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.stereotype.Service;

@Service
public class PortariaExclusaoValidacaoService {

    private static final String STATUS_EM_ANDAMENTO = "Em andamento";

    private final PortariaRegistroRepository registroRepository;

    public PortariaExclusaoValidacaoService(PortariaRegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public PortariaRegistro carregarRegistroParaExclusao(String registroId, String tipoRegistroInformado, String tipoEsperado) {
        validarTipoRegistro(tipoRegistroInformado, tipoEsperado);

        Long registroIdConvertido = parseRequiredId(registroId, "registroId");
        PortariaRegistro registro = registroRepository.findById(registroIdConvertido)
                .orElseThrow(() -> new PortariaExclusaoNaoEncontradaException("Registro principal da portaria não encontrado."));

        if (!tipoEsperado.equals(registro.getTipoRegistro())) {
            throw new PortariaExclusaoValidacaoException("O registro informado não pertence ao domínio esperado para este endpoint.");
        }

        if (!STATUS_EM_ANDAMENTO.equals(registro.getStatus())) {
            throw new PortariaExclusaoValidacaoException("A exclusão só é permitida para registros com status 'Em andamento'.");
        }

        return registro;
    }

    public void validarTipoRegistro(String tipoRegistroInformado, String tipoEsperado) {
        if (tipoRegistroInformado == null || tipoRegistroInformado.trim().isEmpty()) {
            throw new PortariaExclusaoValidacaoException("tipoRegistro é obrigatório.");
        }

        if (!tipoEsperado.equals(tipoRegistroInformado.trim())) {
            throw new PortariaExclusaoValidacaoException("tipoRegistro incompatível com o endpoint chamado.");
        }
    }

    public Long parseRequiredId(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new PortariaExclusaoValidacaoException(nomeCampo + " é obrigatório.");
        }
        return parseLong(valor, nomeCampo);
    }

    public Long parseOptionalId(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return parseLong(valor, nomeCampo);
    }

    public boolean flagAtiva(Boolean valor) {
        return Boolean.TRUE.equals(valor);
    }

    public void validarIdsCompativeis(Long idInformado, Long idPersistido, String mensagem) {
        if (idInformado != null && !idInformado.equals(idPersistido)) {
            throw new PortariaExclusaoValidacaoException(mensagem);
        }
    }

    public void validarCondicao(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new PortariaExclusaoValidacaoException(mensagem);
        }
    }

    private Long parseLong(String valor, String nomeCampo) {
        try {
            return Long.valueOf(valor.trim());
        } catch (NumberFormatException ex) {
            throw new PortariaExclusaoValidacaoException(nomeCampo + " deve ser numérico.");
        }
    }
}