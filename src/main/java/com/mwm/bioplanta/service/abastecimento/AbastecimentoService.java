package com.mwm.bioplanta.service.abastecimento;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mwm.bioplanta.dto.abastecimento.AbastecimentoDTO;
import com.mwm.bioplanta.dto.abastecimento.AbastecimentoRequestDTO;
import com.mwm.bioplanta.model.Abastecimento;
import com.mwm.bioplanta.repository.abastecimento.AbastecimentoRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;

    @Autowired
    public AbastecimentoService(AbastecimentoRepository abastecimentoRepository) {
        this.abastecimentoRepository = abastecimentoRepository;
    }

    public List<Abastecimento> listarTodos() {
        return abastecimentoRepository.findAll();
    }

    @Transactional
    public AbastecimentoDTO registrarAbastecimento(AbastecimentoRequestDTO request) {
        validarRequest(request);
        String statusNormalizado = normalizarStatus(request.getStatus());
        LocalDateTime agora = LocalDateTime.now();

        Abastecimento abastecimento = salvarRegistroAbastecimento(request, statusNormalizado, agora);
        return montarResposta(abastecimento);
    }

    private void validarRequest(AbastecimentoRequestDTO request) {
        if (request == null)
            throw new IllegalArgumentException("Requisição não pode ser nulo");
        request.setId_transportadora(null);
        request.setId_veiculo_transportadora(null);
        request.setId_usuario(null);
        request.setId_assinatura(null);
        request.setPressao_inicial(null);
        request.setOdometro(null);
        //frentista NOT NULL
        //criado_em NOT NULL
        request.setAtualizado_em(null);
        request.setExpirado_em(null);
        //hora_inicial NOT NULL
        //status NOT NULL
        //tipo_execucao NOT NULL
        request.setPressao_final(null);
        request.setVolume_abastecido(null);
        request.setHora_final(null);
    }

    private String normalizarStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("status é obrigatório e deve ser 'Em andamento' ou 'Concluído'");
        }

        String valor = status.trim();
        if ("Em andamento".equalsIgnoreCase(valor)) {
            return "Em andamento";
        }
        if ("Concluído".equalsIgnoreCase(valor) || "Concluido".equalsIgnoreCase(valor)) {
            return "Concluído";
        }

        throw new IllegalArgumentException("status inválido. Use 'Em andamento' ou 'Concluído'");
    }

    private Abastecimento salvarRegistroAbastecimento(
        AbastecimentoRequestDTO request,
        String statusNormalizado,
        LocalDateTime agora) {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setId_transportadora(request.getId_transportadora());
        abastecimento.setId_veiculo_transportadora(request.getId_veiculo_transportadora());
        abastecimento.setId_usuario(request.getId_usuario());
        abastecimento.setId_assinatura(request.getId_assinatura());
        abastecimento.setPressao_inicial(request.getPressao_inicial());
        abastecimento.setOdometro(request.getOdometro());
        abastecimento.setFrentista(request.getFrentista());
        abastecimento.setCriado_em(agora);
        abastecimento.setHora_incial(LocalTime.now());
        abastecimento.setAtualizado_em(agora);
        abastecimento.setExpirado_em(agora);
        abastecimento.setStatus(statusNormalizado);
        abastecimento.setTipo_execucao(request.getTipo_execucao());
        abastecimento.setPressao_final(request.getPressao_final());
        abastecimento.setVolume_abastecido(request.getVolume_abastecido());
        abastecimento.setHora_final(LocalTime.now());
        return abastecimentoRepository.save(abastecimento);
    }

    private AbastecimentoDTO montarResposta(Abastecimento abastecimento) {
        AbastecimentoDTO dto = new AbastecimentoDTO();
        dto.setId(abastecimento.getId());
        dto.setIdTransportadora(abastecimento.getId_transportadora());
        dto.setIdVeiculoTransportadora(abastecimento.getId_veiculo_transportadora());
        dto.setIdUsuario(abastecimento.getId_usuario());
        dto.setIdAssinatura(abastecimento.getId_assinatura());
        dto.setPressaoInicial(abastecimento.getPressao_inicial());
        dto.setOdometro(abastecimento.getOdometro());
        dto.setFrentista(abastecimento.getFrentista());
        dto.setCriadoEm(abastecimento.getCriado_em());
        dto.setHoraInicial(abastecimento.getHora_incial().toString());
        dto.setAtualizadoEm(abastecimento.getAtualizado_em());
        dto.setExpiradoEm(abastecimento.getExpirado_em());
        dto.setStatus(abastecimento.getStatus());
        dto.setTipoExecucao(abastecimento.getTipo_execucao());
        dto.setPressaoFinal(abastecimento.getPressao_final());
        dto.setVolumeAbastecido(abastecimento.getVolume_abastecido());
        dto.setHoraFinal(abastecimento.getHora_final().toString());
        return dto;
    }
}
