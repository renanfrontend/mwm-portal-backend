package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.dto.portaria.PortariaEntregaInsumoRequestDTO;
import com.mwm.bioplanta.dto.portaria.PortariaEntregaInsumoResponseDTO;
import com.mwm.bioplanta.model.BioPortariaEntregaInsumo;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.model.User;
import com.mwm.bioplanta.repository.auth.UserRepository;
import com.mwm.bioplanta.repository.portaria.BioPortariaEntregaInsumoRepository;
import com.mwm.bioplanta.repository.portaria.PortariaRegistroRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTransportadoraRepository;
import com.mwm.bioplanta.model.PortariaRegistro;
import com.mwm.bioplanta.util.CnpjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PortariaEntregaInsumoService {
    @Autowired
    private BioPortariaEntregaInsumoRepository entregaInsumoRepository;
    @Autowired
    private PortariaRegistroRepository registroRepository;
    @Autowired
    private BioTransportadoraRepository transportadoraRepository;
    @Autowired
    private BioVeiculoTransportadoraRepository veiculoRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PortariaEntregaInsumoResponseDTO registrarEntregaInsumo(PortariaEntregaInsumoRequestDTO dto) {
        validarPayload(dto);
        LocalDateTime agora = LocalDateTime.now();
        
        PortariaEntregaInsumoRequestDTO.EntregaInsumoDTO entregaInsumo = dto.getEntrega_insumo();

        PortariaRegistro registro = new PortariaRegistro();
        registro.setTipoRegistro("ENTREGA_INSUMO");
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
        
        if (entregaInsumo.getTransportadora_id() != null) {
            transportadora = transportadoraRepository.findById(entregaInsumo.getTransportadora_id()).orElse(null);
            if (entregaInsumo.getVeiculo_id() != null) {
                veiculo = veiculoRepository.findById(entregaInsumo.getVeiculo_id()).orElse(null);
            }
        } else if (entregaInsumo.getTransportadora_manual() != null && !entregaInsumo.getTransportadora_manual().isEmpty()) {
            transportadora = new BioTransportadora();
            transportadora.setNomeFantasia(entregaInsumo.getTransportadora_manual());
            transportadora.setRazaoSocial(entregaInsumo.getTransportadora_manual());
            transportadora.setCnpj(CnpjUtil.gerarCNPJUnico());
            transportadora.setStatus("Ativo");
            transportadora.setOrigemCadastro("ENTREGA_INSUMO");
            transportadora.setCriadoEm(agora);
            transportadora.setAtualizadoEm(agora);
            transportadora = transportadoraRepository.save(transportadora);
            
            if (entregaInsumo.getPlaca_manual() != null && !entregaInsumo.getPlaca_manual().isEmpty()) {
                veiculo = new BioVeiculoTransportadora();
                veiculo.setBioTransportadora(transportadora);
                veiculo.setTipo(entregaInsumo.getTipo_veiculo());
                veiculo.setCapacidade("0");
                veiculo.setPlaca(entregaInsumo.getPlaca_manual());
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
        }

        BioPortariaEntregaInsumo entrega = new BioPortariaEntregaInsumo();
        entrega.setRegistroId(registro.getId());
        entrega.setTransportadoraId(transportadora != null ? transportadora.getId() : null);
        entrega.setVeiculoId(veiculo != null ? veiculo.getId() : null);
        entrega.setUsuarioId(usuario != null ? usuario.getId() : null);
        entrega.setDataEntrada(LocalDate.parse(dto.getData_entrada()));
        entrega.setHorarioEntrada(LocalTime.parse(dto.getHora_entrada()));
        entrega.setAtividade("Entrega de Insumo");
        entrega.setTransportadoraManual(entregaInsumo.getTransportadora_manual());
        entrega.setTipoVeiculo(entregaInsumo.getTipo_veiculo());
        
        String placaFinal = null;
        if (entregaInsumo.getTransportadora_manual() != null && !entregaInsumo.getTransportadora_manual().isEmpty()) {
            if (entregaInsumo.getPlaca_manual() != null && !entregaInsumo.getPlaca_manual().isEmpty()) {
                placaFinal = entregaInsumo.getPlaca_manual();
            } else if (entregaInsumo.getPlaca() != null && !entregaInsumo.getPlaca().isEmpty()) {
                placaFinal = entregaInsumo.getPlaca();
            }
        } else if (veiculo != null) {
            placaFinal = veiculo.getPlaca();
        } else if (entregaInsumo.getPlaca() != null && !entregaInsumo.getPlaca().isEmpty()) {
            placaFinal = entregaInsumo.getPlaca();
        } else if (entregaInsumo.getPlaca_manual() != null && !entregaInsumo.getPlaca_manual().isEmpty()) {
            placaFinal = entregaInsumo.getPlaca_manual();
        }
        entrega.setPlaca(placaFinal);
        
        entrega.setMotorista(entregaInsumo.getMotorista_nome());
        entrega.setCpfPassaporte(entregaInsumo.getCpf_motorista());
        entrega.setEmpresa(entregaInsumo.getEmpresa());
        entrega.setNotaFiscal(entregaInsumo.getNota_fiscal());
        entrega.setPesoInicial(entregaInsumo.getPeso_inicial());
        entrega.setPesoFinal(entregaInsumo.getPeso_final());
        if (dto.getData_saida() != null && !dto.getData_saida().isEmpty()) {
            entrega.setDataSaida(LocalDate.parse(dto.getData_saida()));
        }
        if (dto.getHora_saida() != null && !dto.getHora_saida().isEmpty()) {
            entrega.setHorarioSaida(LocalTime.parse(dto.getHora_saida()));
        }
        entrega.setObservacao(dto.getObservacoes());
        entrega.setCriadoEm(agora);
        entrega.setAtualizadoEm(agora);
        entrega = entregaInsumoRepository.save(entrega);

        PortariaEntregaInsumoResponseDTO resp = new PortariaEntregaInsumoResponseDTO();
        resp.setId(entrega.getId());
        resp.setRegistroId(entrega.getRegistroId());
        resp.setTransportadoraId(entrega.getTransportadoraId());
        resp.setVeiculoId(entrega.getVeiculoId());
        resp.setUsuarioId(entrega.getUsuarioId());
        resp.setDataEntrada(dto.getData_entrada());
        resp.setHorarioEntrada(dto.getHora_entrada());
        resp.setAtividade("Entrega de Insumo");
        resp.setTransportadoraManual(entrega.getTransportadoraManual());
        resp.setTipoVeiculo(entregaInsumo.getTipo_veiculo());
        resp.setPlaca(entrega.getPlaca());
        resp.setMotorista(entregaInsumo.getMotorista_nome());
        resp.setCpfPassaporte(entregaInsumo.getCpf_motorista());
        resp.setEmpresa(entregaInsumo.getEmpresa());
        resp.setNotaFiscal(entregaInsumo.getNota_fiscal());
        resp.setPesoInicial(entregaInsumo.getPeso_inicial());
        resp.setPesoFinal(entregaInsumo.getPeso_final());
        resp.setDataSaida(dto.getData_saida());
        resp.setHorarioSaida(dto.getHora_saida());
        resp.setObservacao(dto.getObservacoes());
        resp.setCriadoEm(entrega.getCriadoEm().toString());
        resp.setAtualizadoEm(entrega.getAtualizadoEm().toString());
        return resp;
    }

    private void validarPayload(PortariaEntregaInsumoRequestDTO dto) {
        if (dto.getData_entrada() == null || dto.getData_entrada().isEmpty()) throw new IllegalArgumentException("data_entrada obrigatória");
        if (dto.getHora_entrada() == null || dto.getHora_entrada().isEmpty()) throw new IllegalArgumentException("hora_entrada obrigatória");
        
        PortariaEntregaInsumoRequestDTO.EntregaInsumoDTO entregaInsumo = dto.getEntrega_insumo();
        if (entregaInsumo == null) throw new IllegalArgumentException("dados da entrega de insumo obrigatórios");
        
        if (entregaInsumo.getMotorista_nome() == null || entregaInsumo.getMotorista_nome().isEmpty()) throw new IllegalArgumentException("motorista obrigatório");
        if (entregaInsumo.getCpf_motorista() == null || entregaInsumo.getCpf_motorista().isEmpty()) throw new IllegalArgumentException("cpf_passaporte obrigatório");
        if (entregaInsumo.getTipo_veiculo() == null || entregaInsumo.getTipo_veiculo().isEmpty()) throw new IllegalArgumentException("tipo_veiculo obrigatório");
        if (entregaInsumo.getEmpresa() == null || entregaInsumo.getEmpresa().isEmpty()) throw new IllegalArgumentException("empresa obrigatória");
        if (entregaInsumo.getNota_fiscal() == null || entregaInsumo.getNota_fiscal().isEmpty()) throw new IllegalArgumentException("nota_fiscal obrigatória");
        if (entregaInsumo.getPeso_inicial() == null) throw new IllegalArgumentException("peso_inicial obrigatório");
        if (entregaInsumo.getPeso_final() == null) throw new IllegalArgumentException("peso_final obrigatório");
        
        boolean temTransportadora = entregaInsumo.getTransportadora_id() != null 
            || (entregaInsumo.getTransportadora_manual() != null && !entregaInsumo.getTransportadora_manual().isEmpty());
        if (!temTransportadora) throw new IllegalArgumentException("transportadora obrigatória");
    }
}