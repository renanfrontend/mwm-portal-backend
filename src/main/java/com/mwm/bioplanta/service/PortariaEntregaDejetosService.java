package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.PortariaEntregaDejetosDTO;
import com.mwm.bioplanta.model.*;
import com.mwm.bioplanta.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class PortariaEntregaDejetosService {
    private final BioTransportadoraRepository transportadoraRepository;
    private final BioVeiculoTipoRepository veiculoTipoRepository;
    private final BioVeiculoTransportadoraRepository veiculoRepository;
    private final PortariaRegistroRepository registroRepository;
    private final BioPortariaEntregaDejetosRepository entregaDejetosRepository;
    private final BioAgendaRealizadaRepository agendaRealizadaRepository;

    public PortariaEntregaDejetosService(
            BioTransportadoraRepository transportadoraRepository,
            BioVeiculoTipoRepository veiculoTipoRepository,
            BioVeiculoTransportadoraRepository veiculoRepository,
            PortariaRegistroRepository registroRepository,
            BioPortariaEntregaDejetosRepository entregaDejetosRepository,
            BioAgendaRealizadaRepository agendaRealizadaRepository) {
        this.transportadoraRepository = transportadoraRepository;
        this.veiculoTipoRepository = veiculoTipoRepository;
        this.veiculoRepository = veiculoRepository;
        this.registroRepository = registroRepository;
        this.entregaDejetosRepository = entregaDejetosRepository;
        this.agendaRealizadaRepository = agendaRealizadaRepository;
    }

      @Transactional
      public PortariaEntregaDejetosDTO registrarEntregaDeDejetos(PortariaEntregaDejetosDTO dto) {
          BioTransportadora transportadora = getOrCreateTransportadora(dto);
          BioVeiculoTipo veiculoTipo = getOrCreateVeiculoTipo(dto);
          BioVeiculoTransportadora veiculo = getOrCreateVeiculo(dto, transportadora, veiculoTipo);
          PortariaRegistro registro = criarRegistro(dto);
          BioPortariaEntregaDejetos entregaDejetos = criarEntregaDejetos(dto, transportadora, veiculo);
          
          // Criar registro em bio_agenda_realizada com os dados da entrega
          Long agendaRealizadaId = criarAgendaRealizada(entregaDejetos.getId(), dto, transportadora);
          entregaDejetos.setAgendaRealizadaId(agendaRealizadaId);
          entregaDejetosRepository.save(entregaDejetos);
          
          registro.setEntregaDejetosId(entregaDejetos.getId());
          registroRepository.save(registro);

        // Retornar DTO preenchido com IDs gerados (apenas campos principais e entrega_dejetos)
        PortariaEntregaDejetosDTO response = new PortariaEntregaDejetosDTO();
        response.setTipoRegistro(dto.getTipoRegistro());
        response.setData_entrada(registro.getDataEntrada() != null ? registro.getDataEntrada().toString() : null);
        response.setHora_entrada(registro.getHoraEntrada() != null ? registro.getHoraEntrada().toString() : null);
        response.setData_saida(dto.getData_saida());
        response.setHora_saida(dto.getHora_saida());
        response.setObservacoes(registro.getObservacoes());
        response.setStatus(registro.getStatus());
        response.setOrigem_entrada(registro.getOrigemEntrada());

         PortariaEntregaDejetosDTO.EntregaDejetosDTO entrega = new PortariaEntregaDejetosDTO.EntregaDejetosDTO();
         if (dto.getEntrega_dejetos() != null) {
             entrega.setProdutor_id(dto.getEntrega_dejetos().getProdutor_id());
             entrega.setMotorista_nome(dto.getEntrega_dejetos().getMotorista_nome());
             entrega.setCpf_motorista(dto.getEntrega_dejetos().getCpf_motorista());
             entrega.setMotorista_id(dto.getEntrega_dejetos().getMotorista_id());
              entrega.setTransportadora_id(transportadora != null ? String.valueOf(transportadora.getId()) : dto.getEntrega_dejetos().getTransportadora_id());
              entrega.setTransportadora_manual(dto.getEntrega_dejetos().getTransportadora_manual());
              entrega.setVeiculo_id(veiculo != null ? String.valueOf(veiculo.getId()) : dto.getEntrega_dejetos().getVeiculo_id());
              entrega.setPlaca(dto.getEntrega_dejetos().getPlaca());
              entrega.setPlaca_manual(dto.getEntrega_dejetos().getPlaca_manual());
             entrega.setTipo_veiculo(veiculoTipo != null ? veiculoTipo.getLabel() : dto.getEntrega_dejetos().getTipo_veiculo());
             entrega.setPeso_inicial(dto.getEntrega_dejetos().getPeso_inicial());
             entrega.setPeso_final(dto.getEntrega_dejetos().getPeso_final());
             entrega.setDensidade(dto.getEntrega_dejetos().getDensidade());
         }
         // Retornar o ID da entrega_dejetos que foi gerado no banco
         entrega.setId(String.valueOf(entregaDejetos.getId()));
         response.setEntrega_dejetos(entrega);
        return response;
    }

    private BioTransportadora getOrCreateTransportadora(PortariaEntregaDejetosDTO dto) {
        BioTransportadora transportadora = null;
        PortariaEntregaDejetosDTO.EntregaDejetosDTO ent = dto.getEntrega_dejetos();
        
        String nomeTransportadora = null;
        
        // Prioridade 1: Se tem transportadora_manual, usar ele
        if (ent != null && ent.getTransportadora_manual() != null && !ent.getTransportadora_manual().trim().isEmpty()) {
            nomeTransportadora = ent.getTransportadora_manual().trim();
        }
        // Prioridade 2: Se tem transportadora_id, procurar por ID
        else if (ent != null && ent.getTransportadora_id() != null) {
            try {
                transportadora = transportadoraRepository.findById(Long.valueOf(ent.getTransportadora_id())).orElse(null);
            } catch (NumberFormatException ignored) {}
            
            if (transportadora != null) {
                return transportadora;
            }
        }
        
        // Se chegou aqui e não tem nomeTransportadora, criar padrão
        if (nomeTransportadora == null || nomeTransportadora.isEmpty()) {
            nomeTransportadora = "OUTRA";
        }
        
        // Criar nova transportadora com o nome definido
        transportadora = new BioTransportadora();
        transportadora.setNomeFantasia(nomeTransportadora);
        transportadora.setRazaoSocial(nomeTransportadora);
        // Gerar CNPJ único (aleatório para registros manuais)
        transportadora.setCnpj(gerarCNPJUnico());
        transportadora.setCidade("");
        transportadora.setUf("");
        transportadora.setEndereco("");
        transportadora.setCategoria("");
        transportadora.setTelefoneComercial("");
        transportadora.setEmailComercial("");
        transportadora.setStatus("Ativo");
        transportadora.setOrigemCadastro("FORMULARIO_ENTREGA_DEJETOS");
        transportadora.setCriadoEm(LocalDateTime.now());
        transportadora.setAtualizadoEm(LocalDateTime.now());
        transportadora = transportadoraRepository.save(transportadora);
        
        return transportadora;
    }

    private BioVeiculoTipo getOrCreateVeiculoTipo(PortariaEntregaDejetosDTO dto) {
        BioVeiculoTipo veiculoTipo = null;
        PortariaEntregaDejetosDTO.EntregaDejetosDTO ent = dto.getEntrega_dejetos();
        if (ent != null && ent.getTipo_veiculo() != null && !ent.getTipo_veiculo().trim().isEmpty()) {
            veiculoTipo = veiculoTipoRepository.findAll().stream()
                    .filter(t -> t.getLabel().equalsIgnoreCase(ent.getTipo_veiculo()) || t.getValue().equalsIgnoreCase(ent.getTipo_veiculo()))
                    .findFirst().orElse(null);
            if (veiculoTipo == null) {
                veiculoTipo = new BioVeiculoTipo();
                veiculoTipo.setLabel(ent.getTipo_veiculo());
                veiculoTipo.setValue(ent.getTipo_veiculo());
                veiculoTipo.setCriadoEm(LocalDateTime.now());
                veiculoTipo.setAtualizadoEm(LocalDateTime.now());
                veiculoTipo = veiculoTipoRepository.save(veiculoTipo);
            }
        }
        return veiculoTipo;
    }

    private BioVeiculoTransportadora getOrCreateVeiculo(PortariaEntregaDejetosDTO dto, BioTransportadora transportadora, BioVeiculoTipo veiculoTipo) {
        BioVeiculoTransportadora veiculo = null;
        PortariaEntregaDejetosDTO.EntregaDejetosDTO ent = dto.getEntrega_dejetos();
        
        // Prioridade 1: Se tem placa_manual e transportadora, procurar/criar por placa
        if (ent != null && ent.getPlaca_manual() != null && transportadora != null && !ent.getPlaca_manual().trim().isEmpty()) {
            String placaManual = ent.getPlaca_manual().replaceAll("\\s", "").toUpperCase();
            
            // Procurar na transportadora específica
            veiculo = veiculoRepository.findByBioTransportadoraId(transportadora.getId()).stream()
                    .filter(v -> v.getPlaca() != null && v.getPlaca().replaceAll("\\s","").equalsIgnoreCase(placaManual))
                    .findFirst()
                    .orElse(null);
            
            // Se não encontrou, criar novo
            if (veiculo == null) {
                veiculo = new BioVeiculoTransportadora();
                veiculo.setBioTransportadora(transportadora);
                veiculo.setTipo(veiculoTipo != null ? veiculoTipo.getLabel() : ent.getTipo_veiculo());
                veiculo.setCapacidade("0"); // Capacidade padrão: "0"
                veiculo.setPlaca(placaManual);
                veiculo.setTipoAbastecimento(""); // Tipo abastecimento padrão: vazio
                veiculo.setStatus("Ativo");
                veiculo.setCriadoEm(LocalDateTime.now());
                veiculo.setAtualizadoEm(LocalDateTime.now());
                veiculo = veiculoRepository.save(veiculo);
            }
        }
        // Prioridade 2: Se não tem placa_manual mas tem veiculo_id, procurar por ID
        else if (ent != null && ent.getVeiculo_id() != null) {
            try {
                veiculo = veiculoRepository.findById(Long.valueOf(ent.getVeiculo_id())).orElse(null);
            } catch (NumberFormatException ignored) {}
        }
        
        return veiculo;
    }

    private PortariaRegistro criarRegistro(PortariaEntregaDejetosDTO dto) {
        PortariaRegistro registro = new PortariaRegistro();
        registro.setDataEntrada(dto.getData_entrada() != null ? LocalDate.parse(dto.getData_entrada()) : LocalDate.now());
        registro.setHoraEntrada(dto.getHora_entrada() != null ? LocalTime.parse(dto.getHora_entrada()) : LocalTime.now());
        registro.setTipoRegistro("ENTREGA_DEJETOS");
        registro.setStatus(dto.getStatus() != null ? dto.getStatus() : "Em andamento");
        registro.setOrigemEntrada(dto.getOrigem_entrada() != null ? dto.getOrigem_entrada() : "ESPONTANEA");
        registro.setObservacoes(dto.getObservacoes());
        registro.setResponsavelId(null);
        registro.setCriadoEm(LocalDateTime.now());
        registro.setAtualizadoEm(LocalDateTime.now());
        return registroRepository.save(registro);
    }

       private BioPortariaEntregaDejetos criarEntregaDejetos(PortariaEntregaDejetosDTO dto, BioTransportadora transportadora, BioVeiculoTransportadora veiculo) {
           BioPortariaEntregaDejetos entrega = new BioPortariaEntregaDejetos();
           PortariaEntregaDejetosDTO.EntregaDejetosDTO ent = dto.getEntrega_dejetos();
           // Nota: abastecimentoId foi removido - agora usamos entrega_dejetos_id na PortariaRegistro
            entrega.setAgendaRealizadaId(null);
            entrega.setProdutorId(ent != null ? (ent.getProdutor_id() != null ? Long.valueOf(ent.getProdutor_id()) : null) : null);
            entrega.setMotoristaNome(ent != null ? ent.getMotorista_nome() : null);
            entrega.setCpfMotorista(ent != null ? ent.getCpf_motorista() : null);
            entrega.setMotoristaId(ent != null && ent.getMotorista_id() != null ? Long.valueOf(ent.getMotorista_id()) : null);
            entrega.setTransportadoraId(ent != null && ent.getTransportadora_id() != null ? Long.valueOf(ent.getTransportadora_id()) : null);
            entrega.setTransportadoraManual(ent != null ? ent.getTransportadora_manual() : null);
             entrega.setVeiculoId(ent != null && ent.getVeiculo_id() != null ? tryParseLong(ent.getVeiculo_id()) : null);
             entrega.setPlaca(ent != null ? ent.getPlaca() : null);
             entrega.setPlacaManual(ent != null && (ent.getTransportadora_manual() != null || ent.getVeiculo_id() == null) ? ent.getPlaca_manual() : null);
             entrega.setTipoVeiculo(ent != null ? ent.getTipo_veiculo() : null);
            entrega.setPesoInicial(ent != null && ent.getPeso_inicial() != null ? ent.getPeso_inicial().doubleValue() : null);
            entrega.setPesoFinal(ent != null && ent.getPeso_final() != null ? ent.getPeso_final().doubleValue() : null);
            entrega.setDensidade(ent != null ? ent.getDensidade() : null);
            entrega.setCriadoEm(LocalDateTime.now());
            entrega.setAtualizadoEm(LocalDateTime.now());
           return entregaDejetosRepository.save(entrega);
       }

      /**
       * Obter entrega de dejetos por ID
       */
      public BioPortariaEntregaDejetos obterEntregaDeDejetos(Long id) {
          return entregaDejetosRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Entrega de dejetos não encontrada com ID: " + id));
      }

      /**
       * ============================================================================
       * MÉTODO PRIVADO: Criar Agenda Realizada
       * ============================================================================
       * Responsabilidade: Gravar os dados da entrega de dejetos na tabela 
       *                   bio_agenda_realizada após o registro ser salvo
       * 
       * Recebe:
       *   - entregaDejetosId: ID do registro que foi salvo em bio_portaria_entrega_dejetos
       *   - dto: DTO com os dados que vieram do POST endpoint
       *   - transportadora: Objeto da transportadora (criada ou obtida)
       * 
       * Retorna:
       *   - Long: ID da agenda_realizada que foi gerada no banco
       * 
       * Fluxo:
       *   1. Cria novo objeto BioAgendaRealizada
       *   2. Preenche com dados do POST e da transportadora
       *   3. Seta o entregaDejetosId (relacionamento com entrega_dejetos)
       *   4. Salva no banco
       *   5. Retorna o ID gerado
       * 
       * Dados salvos em bio_agenda_realizada:
       *   - produtor_id: Do DTO (entrega_dejetos.produtor_id)
       *   - data_real: Data de hoje (LocalDate.now())
       *   - transportadora_nome: Do objeto transportadora obtido/criado
       *   - quantidade_veiculos: Padrão = 1
       *   - status: Padrão = "REALIZADO"
       *   - entrega_dejetos_id: ID da entrega que foi salva (relacionamento)
       *   - criado_em / atualizado_em: Data/hora atual
       */
       private Long criarAgendaRealizada(Long entregaDejetosId, PortariaEntregaDejetosDTO dto, BioTransportadora transportadora) {
           BioAgendaRealizada agendaRealizada = new BioAgendaRealizada();
           PortariaEntregaDejetosDTO.EntregaDejetosDTO ent = dto.getEntrega_dejetos();
           
           // Extrair produtor_id do DTO
           agendaRealizada.setProdutorId(ent != null && ent.getProdutor_id() != null ? Long.valueOf(ent.getProdutor_id()) : null);
           
           // Pega data_entrada e hora_entrada do DTO e junta em um LocalDateTime
           LocalDate dataEntrada = dto.getData_entrada() != null ? LocalDate.parse(dto.getData_entrada()) : LocalDate.now();
           LocalTime horaEntrada = dto.getHora_entrada() != null ? LocalTime.parse(dto.getHora_entrada()) : LocalTime.now();
           agendaRealizada.setDataReal(dataEntrada.atTime(horaEntrada));
          
          // Obtém o nome da transportadora (criada ou obtida)
          agendaRealizada.setTransportadoraNome(transportadora != null ? transportadora.getNomeFantasia() : null);
          
          // Quantidade de veículos padrão = 1
          agendaRealizada.setQuantidadeVeiculos(1);
          
          // Status padrão para agenda realizada
          agendaRealizada.setStatus("REALIZADO");
          
          // Relacionamento com a entrega_dejetos que foi salva
          agendaRealizada.setEntregaDejetosId(entregaDejetosId);
          
          // Datas de auditoria
          agendaRealizada.setCriadoEm(LocalDateTime.now());
          agendaRealizada.setAtualizadoEm(LocalDateTime.now());
          
          // Salva na tabela bio_agenda_realizada e retorna o ID gerado
          BioAgendaRealizada saved = agendaRealizadaRepository.save(agendaRealizada);
          return saved.getId();
      }

    /**
     * Gera um CNPJ único (aleatório) para registros manuais de transportadora
     * Formato: XX.XXX.XXX/XXXX-XX
     */
    private String gerarCNPJUnico() {
        java.util.Random random = new java.util.Random();
        int parte1 = random.nextInt(100);
        int parte2 = random.nextInt(1000);
        int parte3 = random.nextInt(1000);
        int parte4 = random.nextInt(10000);
        int parte5 = random.nextInt(100);
        
         return String.format("%02d.%03d.%03d/%04d-%02d", parte1, parte2, parte3, parte4, parte5);
     }

     private Long tryParseLong(String value) {
         try {
             return Long.valueOf(value);
         } catch (NumberFormatException e) {
             return null;
         }
     }
 }
