package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaExpedicaoRequestDTO;
import com.mwm.bioplanta.dto.portaria.PortariaExpedicaoResponseDTO;
import com.mwm.bioplanta.model.BioPortariaExpedicao;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.repository.auth.UserRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaExpedicaoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.util.CnpjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PortariaExpedicaoService {

    @Autowired
    private BioPortariaExpedicaoRepository expedicaoRepository;
    @Autowired
    private PortariaRegistroRepository registroRepository;
    @Autowired
    private BioTransportadoraRepository transportadoraRepository;
    @Autowired
    private BioVeiculoTransportadoraRepository veiculoRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PortariaExpedicaoResponseDTO registrarExpedicao(PortariaExpedicaoRequestDTO dto) {
        validarPayload(dto);
        LocalDateTime agora = LocalDateTime.now();

        PortariaExpedicaoRequestDTO.ExpedicaoDTO expedicaoDTO = dto.getExpedicao();

        PortariaRegistro registro = new PortariaRegistro();
        registro.setTipoRegistro("EXPEDICAO");
        registro.setDataEntrada(LocalDate.parse(dto.getData_entrada()));
        registro.setHoraEntrada(LocalTime.parse(dto.getHora_entrada()));
        registro.setStatus(dto.getStatus() != null ? dto.getStatus() : "Concluído");
        registro.setOrigemEntrada(dto.getOrigem_entrada() != null ? dto.getOrigem_entrada() : "ESPONTANEA");
        registro.setObservacoes(dto.getObservacoes());
        if (dto.getData_saida() != null && !dto.getData_saida().isEmpty()) {
            registro.setDataSaida(LocalDate.parse(dto.getData_saida()));
        }
        if (dto.getHora_saida() != null && !dto.getHora_saida().isEmpty()) {
            registro.setHoraSaida(LocalTime.parse(dto.getHora_saida()));
        }
        registro.setCriadoEm(agora);
        registro.setAtualizadoEm(agora);
        registro = registroRepository.save(registro);

        BioTransportadora transportadora = null;
        BioVeiculoTransportadora veiculo = null;

        if (expedicaoDTO.getTransportadora_id() != null) {
            transportadora = transportadoraRepository.findById(expedicaoDTO.getTransportadora_id()).orElse(null);
            if (expedicaoDTO.getVeiculo_id() != null) {
                veiculo = veiculoRepository.findById(expedicaoDTO.getVeiculo_id()).orElse(null);
            }
        } else if (expedicaoDTO.getTransportadora_manual() != null && !expedicaoDTO.getTransportadora_manual().isEmpty()) {
            transportadora = new BioTransportadora();
            transportadora.setNomeFantasia(expedicaoDTO.getTransportadora_manual());
            transportadora.setRazaoSocial(expedicaoDTO.getTransportadora_manual());
            transportadora.setCnpj(CnpjUtil.gerarCNPJUnico());
            transportadora.setStatus("Ativo");
            transportadora.setOrigemCadastro("EXPEDICAO");
            transportadora.setCriadoEm(agora);
            transportadora.setAtualizadoEm(agora);
            transportadora = transportadoraRepository.save(transportadora);

            if (expedicaoDTO.getPlaca_manual() != null && !expedicaoDTO.getPlaca_manual().isEmpty()) {
                veiculo = new BioVeiculoTransportadora();
                veiculo.setBioTransportadora(transportadora);
                veiculo.setTipo(expedicaoDTO.getTipo_veiculo());
                veiculo.setCapacidade("0");
                veiculo.setPlaca(expedicaoDTO.getPlaca_manual());
                veiculo.setStatus("Ativo");
                veiculo.setCriadoEm(agora);
                veiculo.setAtualizadoEm(agora);
                veiculo = veiculoRepository.save(veiculo);
            }
        }

        User usuario = null;
        try {
            usuario = userRepository.findByUsername("Admin").orElse(null);
        } catch (Exception e) {
            // usuário Admin opcional
        }

        BioPortariaExpedicao expedicao = new BioPortariaExpedicao();
        expedicao.setRegistroId(registro.getId());
        expedicao.setTransportadoraId(transportadora != null ? transportadora.getId() : null);
        expedicao.setVeiculoId(veiculo != null ? veiculo.getId() : null);
        expedicao.setUsuarioId(usuario != null ? usuario.getId() : null);
        expedicao.setDataEntrada(LocalDate.parse(dto.getData_entrada()));
        expedicao.setHorarioEntrada(LocalTime.parse(dto.getHora_entrada()));
        expedicao.setAtividade("Expedição");
        expedicao.setTransportadoraManual(expedicaoDTO.getTransportadora_manual());
        expedicao.setTipoVeiculo(expedicaoDTO.getTipo_veiculo());

        String placaFinal = null;
        if (expedicaoDTO.getTransportadora_manual() != null && !expedicaoDTO.getTransportadora_manual().isEmpty()) {
            if (expedicaoDTO.getPlaca_manual() != null && !expedicaoDTO.getPlaca_manual().isEmpty()) {
                placaFinal = expedicaoDTO.getPlaca_manual();
            } else if (expedicaoDTO.getPlaca() != null && !expedicaoDTO.getPlaca().isEmpty()) {
                placaFinal = expedicaoDTO.getPlaca();
            }
        } else if (veiculo != null) {
            placaFinal = veiculo.getPlaca();
        } else if (expedicaoDTO.getPlaca() != null && !expedicaoDTO.getPlaca().isEmpty()) {
            placaFinal = expedicaoDTO.getPlaca();
        } else if (expedicaoDTO.getPlaca_manual() != null && !expedicaoDTO.getPlaca_manual().isEmpty()) {
            placaFinal = expedicaoDTO.getPlaca_manual();
        }
        expedicao.setPlaca(placaFinal);

        expedicao.setMotorista(expedicaoDTO.getMotorista_nome());
        expedicao.setCpfPassaporte(expedicaoDTO.getCpf_motorista());
        expedicao.setNotaFiscal(expedicaoDTO.getNota_fiscal());
        expedicao.setPesoInicial(expedicaoDTO.getPeso_inicial());
        expedicao.setPesoFinal(expedicaoDTO.getPeso_final());
        if (dto.getData_saida() != null && !dto.getData_saida().isEmpty()) {
            expedicao.setDataSaida(LocalDate.parse(dto.getData_saida()));
        }
        if (dto.getHora_saida() != null && !dto.getHora_saida().isEmpty()) {
            expedicao.setHorarioSaida(LocalTime.parse(dto.getHora_saida()));
        }
        expedicao.setObservacao(dto.getObservacoes());
        expedicao.setCriadoEm(agora);
        expedicao.setAtualizadoEm(agora);
        expedicao = expedicaoRepository.save(expedicao);

        registro.setExpedicaoId(expedicao.getId());
        registroRepository.save(registro);

        PortariaExpedicaoResponseDTO resp = new PortariaExpedicaoResponseDTO();
        resp.setId(expedicao.getId());
        resp.setRegistroId(expedicao.getRegistroId());
        resp.setTransportadoraId(expedicao.getTransportadoraId());
        resp.setVeiculoId(expedicao.getVeiculoId());
        resp.setUsuarioId(expedicao.getUsuarioId());
        resp.setDataEntrada(dto.getData_entrada());
        resp.setHorarioEntrada(dto.getHora_entrada());
        resp.setAtividade("Expedição");
        resp.setTransportadoraManual(expedicao.getTransportadoraManual());
        resp.setTipoVeiculo(expedicaoDTO.getTipo_veiculo());
        resp.setPlaca(expedicao.getPlaca());
        resp.setMotorista(expedicaoDTO.getMotorista_nome());
        resp.setCpfPassaporte(expedicaoDTO.getCpf_motorista());
        resp.setNotaFiscal(expedicaoDTO.getNota_fiscal());
        resp.setPesoInicial(expedicaoDTO.getPeso_inicial());
        resp.setPesoFinal(expedicaoDTO.getPeso_final());
        resp.setDataSaida(dto.getData_saida());
        resp.setHorarioSaida(dto.getHora_saida());
        resp.setObservacao(dto.getObservacoes());
        resp.setCriadoEm(expedicao.getCriadoEm().toString());
        resp.setAtualizadoEm(expedicao.getAtualizadoEm().toString());
        return resp;
    }

    private void validarPayload(PortariaExpedicaoRequestDTO dto) {
        if (dto.getData_entrada() == null || dto.getData_entrada().isEmpty())
            throw new IllegalArgumentException("data_entrada obrigatória");
        if (dto.getHora_entrada() == null || dto.getHora_entrada().isEmpty())
            throw new IllegalArgumentException("hora_entrada obrigatória");

        PortariaExpedicaoRequestDTO.ExpedicaoDTO exp = dto.getExpedicao();
        if (exp == null)
            throw new IllegalArgumentException("dados da expedição obrigatórios");
        if (exp.getMotorista_nome() == null || exp.getMotorista_nome().isEmpty())
            throw new IllegalArgumentException("motorista obrigatório");
        if (exp.getCpf_motorista() == null || exp.getCpf_motorista().isEmpty())
            throw new IllegalArgumentException("cpf_motorista obrigatório");
        if (exp.getTipo_veiculo() == null || exp.getTipo_veiculo().isEmpty())
            throw new IllegalArgumentException("tipo_veiculo obrigatório");

        boolean temTransportadora = exp.getTransportadora_id() != null
                || (exp.getTransportadora_manual() != null && !exp.getTransportadora_manual().isEmpty());
        if (!temTransportadora)
            throw new IllegalArgumentException("transportadora obrigatória");
    }
}
