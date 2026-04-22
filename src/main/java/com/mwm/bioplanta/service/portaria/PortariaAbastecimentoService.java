package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoDTO;
import com.mwm.bioplanta.dto.portaria.PortariaAbastecimentoRequestDTO;
import com.mwm.bioplanta.model.BioPortariaAbastecimento;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaAbastecimentoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwm.bioplanta.util.PlacaUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PortariaAbastecimentoService {
    private final BioPortariaAbastecimentoRepository abastecimentoRepository;
    private final PortariaRegistroRepository registroRepository;
    private final BioVeiculoTransportadoraRepository veiculoRepository;

    @Autowired
    public PortariaAbastecimentoService(BioPortariaAbastecimentoRepository abastecimentoRepository,
                                        PortariaRegistroRepository registroRepository,
                                        BioVeiculoTransportadoraRepository veiculoRepository) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.registroRepository = registroRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public PortariaAbastecimentoDTO registrarAbastecimento(PortariaAbastecimentoRequestDTO request) {
        validarRequest(request);
        String statusNormalizado = normalizarStatus(request.getStatus());
        String origemNormalizada = normalizarOrigem(request.getOrigem_entrada());
        LocalDateTime agora = LocalDateTime.now();

        PortariaRegistro registro = salvarRegistroPrincipal(request, statusNormalizado, origemNormalizada, agora);
        BioPortariaAbastecimento abastecimento = salvarRegistroAbastecimento(request, registro.getId(), agora);

        return montarResposta(registro, abastecimento);
    }

    private PortariaRegistro salvarRegistroPrincipal(
            PortariaAbastecimentoRequestDTO request,
            String statusNormalizado,
            String origemNormalizada,
            LocalDateTime agora) {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setTipoRegistro("ABASTECIMENTO");
        registro.setDataEntrada(LocalDate.parse(request.getData_entrada()));
        registro.setHoraEntrada(LocalTime.parse(request.getHora_entrada()));
        registro.setStatus(statusNormalizado);
        registro.setOrigemEntrada(origemNormalizada);
        registro.setObservacoes(request.getObservacoes());
        if (request.getData_saida() != null && !request.getData_saida().isEmpty()) {
            registro.setDataSaida(LocalDate.parse(request.getData_saida()));
        }
        if (request.getHora_saida() != null && !request.getHora_saida().isEmpty()) {
            registro.setHoraSaida(LocalTime.parse(request.getHora_saida()));
        }
        registro.setCriadoEm(agora);
        registro.setAtualizadoEm(agora);
        return registroRepository.save(registro);
    }

    private BioPortariaAbastecimento salvarRegistroAbastecimento(
            PortariaAbastecimentoRequestDTO request,
            Long registroId,
            LocalDateTime agora) {
        BioPortariaAbastecimento abastecimento = new BioPortariaAbastecimento();
        abastecimento.setRegistroId(registroId);
        abastecimento.setMotoristaNome(request.getAbastecimento().getMotorista_nome());
        abastecimento.setCpfMotorista(request.getAbastecimento().getCpf_motorista());
        abastecimento.setMotoristaId(request.getAbastecimento().getMotorista_id());
        abastecimento.setTipoVeiculo(request.getAbastecimento().getTipo_veiculo());
        abastecimento.setPesoInicial(request.getAbastecimento().getPeso_inicial());
        abastecimento.setPesoFinal(request.getAbastecimento().getPeso_final());
        abastecimento.setCriadoEm(agora);
        abastecimento.setAtualizadoEm(agora);

        preencherDadosTransporte(request, abastecimento);

        return abastecimentoRepository.save(abastecimento);
    }

    private void preencherDadosTransporte(PortariaAbastecimentoRequestDTO request, BioPortariaAbastecimento abastecimento) {
        if (request.getAbastecimento().getTransportadora_id() != null) {
            abastecimento.setTransportadoraId(request.getAbastecimento().getTransportadora_id());
            abastecimento.setTransportadoraManual(null);
            abastecimento.setPlacaManual(null);
            abastecimento.setVeiculoId(request.getAbastecimento().getVeiculo_id());
            // Formatar placa do veículo ao salvar
            String placaFormatada = PlacaUtil.formatarPlacaMercosul(resolverPlacaAbastecimento(request));
            abastecimento.setPlaca(placaFormatada);
            return;
        }

        abastecimento.setTransportadoraId(null);
        abastecimento.setTransportadoraManual(request.getAbastecimento().getTransportadora_manual());
        // Formatar placa manual ao salvar
        String placaManualFormatada = PlacaUtil.formatarPlacaMercosul(request.getAbastecimento().getPlaca_manual());
        abastecimento.setPlacaManual(placaManualFormatada);
        abastecimento.setVeiculoId(null);
        // Formatar placa ao salvar
        String placaFormatada = PlacaUtil.formatarPlacaMercosul(normalizarTexto(request.getAbastecimento().getPlaca()));
        abastecimento.setPlaca(placaFormatada);
    }

    private PortariaAbastecimentoDTO montarResposta(PortariaRegistro registro, BioPortariaAbastecimento abastecimento) {
        PortariaAbastecimentoDTO dto = new PortariaAbastecimentoDTO();
        dto.setId(abastecimento.getId());
        dto.setRegistroId(registro.getId());
        dto.setMotoristaId(abastecimento.getMotoristaId());
        dto.setMotoristaNome(abastecimento.getMotoristaNome());
        dto.setCpfMotorista(abastecimento.getCpfMotorista());
        dto.setTransportadoraId(abastecimento.getTransportadoraId());
        dto.setTransportadoraManual(abastecimento.getTransportadoraManual());
        dto.setVeiculoId(abastecimento.getVeiculoId());
        dto.setPlaca(abastecimento.getPlaca());
        // Sempre retornar placa manual formatada
        dto.setPlacaManual(PlacaUtil.formatarPlacaMercosul(abastecimento.getPlacaManual()));
        dto.setTipoVeiculo(abastecimento.getTipoVeiculo());
        dto.setPesoInicial(abastecimento.getPesoInicial());
        dto.setPesoFinal(abastecimento.getPesoFinal());
        return dto;
    }

    private String buscarPlacaVeiculo(Long veiculoId) {
        if (veiculoId == null) {
            return null;
        }

        BioVeiculoTransportadora veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        return veiculo != null ? veiculo.getPlaca() : null;
    }

    private String resolverPlacaAbastecimento(PortariaAbastecimentoRequestDTO request) {
        String placaInformada = normalizarTexto(request.getAbastecimento().getPlaca());
        if (placaInformada != null) {
            return placaInformada;
        }

        return normalizarTexto(buscarPlacaVeiculo(request.getAbastecimento().getVeiculo_id()));
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }

        String texto = valor.trim();
        return texto.isEmpty() ? null : texto;
    }

    private void validarRequest(PortariaAbastecimentoRequestDTO request) {
        if (request == null || request.getAbastecimento() == null)
            throw new IllegalArgumentException("Requisição ou objeto abastecimento não pode ser nulo");
        if (request.getTipoRegistro() == null || !"ABASTECIMENTO".equalsIgnoreCase(request.getTipoRegistro().trim()))
            throw new IllegalArgumentException("tipoRegistro deve ser 'ABASTECIMENTO'");
        if (request.getData_entrada() == null || request.getHora_entrada() == null || request.getStatus() == null || request.getOrigem_entrada() == null)
            throw new IllegalArgumentException("Campos data_entrada, hora_entrada, status e origem_entrada são obrigatórios");
        var ab = request.getAbastecimento();
        if (ab.getMotorista_nome() == null || ab.getCpf_motorista() == null || ab.getTipo_veiculo() == null)
            throw new IllegalArgumentException("motorista_nome, cpf_motorista e tipo_veiculo são obrigatórios em abastecimento");
        // Lógica transportadora/placa
        boolean outros = ab.getTransportadora_id() == null;
        if (outros) {
            if (ab.getTransportadora_manual() == null || ab.getPlaca_manual() == null)
                throw new IllegalArgumentException("Se transportadora_id for nulo, transportadora_manual e placa_manual são obrigatórios");
        } else {
            if (ab.getVeiculo_id() == null)
                throw new IllegalArgumentException("Se transportadora_id informado, veiculo_id é obrigatório");
        }

        normalizarStatus(request.getStatus());
        normalizarOrigem(request.getOrigem_entrada());
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

    private String normalizarOrigem(String origem) {
        if (origem == null || origem.trim().isEmpty()) {
            throw new IllegalArgumentException("origem_entrada é obrigatória e deve ser 'ESPONTANEA' ou 'AGENDADA'");
        }

        String valor = origem.trim().toUpperCase();
        if ("ESPONTANEA".equals(valor) || "AGENDADA".equals(valor)) {
            return valor;
        }

        throw new IllegalArgumentException("origem_entrada inválida. Use 'ESPONTANEA' ou 'AGENDADA'");
    }
}
