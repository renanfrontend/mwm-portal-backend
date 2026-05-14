package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.*;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTransportadora;
import com.mwm.bioplanta.repository.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.BioVeiculoTransportadoraRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BioTransportadoraService {

    private static final String TRANSPORTADORA_NAO_ENCONTRADA = "Transportadora não encontrada com ID: ";
    private static final String BIOMETANO = "Biometano";
    private static final String VEICULO_NAO_PERTENCE = "Veículo não pertence a esta transportadora";

    private final BioTransportadoraRepository bioTransportadoraRepository;
    private final BioVeiculoTransportadoraRepository bioVeiculoTransportadoraRepository;

    public BioTransportadoraService(BioTransportadoraRepository bioTransportadoraRepository,
                                   BioVeiculoTransportadoraRepository bioVeiculoTransportadoraRepository) {
        this.bioTransportadoraRepository = bioTransportadoraRepository;
        this.bioVeiculoTransportadoraRepository = bioVeiculoTransportadoraRepository;
    }

    /**
     * Criar uma nova transportadora
     */
    @Transactional
    public TransportadoraResponseDTO criar(TransportadoraDTO dto) {
        // Validar se CNPJ já existe
        if (bioTransportadoraRepository.findByCnpj(dto.getCnpj()) != null) {
            throw new IllegalArgumentException("Já existe uma transportadora com este CNPJ: " + dto.getCnpj());
        }

        BioTransportadora transportadora = new BioTransportadora();
        preencherTransportadora(transportadora, dto);
        transportadora.setStatus("Ativo");
        transportadora.setCriadoEm(LocalDateTime.now());
        transportadora.setAtualizadoEm(LocalDateTime.now());

        transportadora = bioTransportadoraRepository.save(transportadora);

        // Salvar veículos se fornecidos
        if (dto.getVeiculos() != null && !dto.getVeiculos().isEmpty()) {
            for (VeiculoDTO veiculoDTO : dto.getVeiculos()) {
                salvarVeiculo(transportadora, veiculoDTO);
            }
        }

        return converterParaResponseDTO(transportadora);
    }

    /**
     * Atualizar transportadora existente
     */
    @Transactional
    public TransportadoraResponseDTO atualizar(Long id, TransportadoraDTO dto) {
        BioTransportadora transportadora = bioTransportadoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(TRANSPORTADORA_NAO_ENCONTRADA + id));

        preencherTransportadora(transportadora, dto);
        transportadora.setAtualizadoEm(LocalDateTime.now());

        transportadora = bioTransportadoraRepository.save(transportadora);

        // Atualizar veículos: remover os antigos e adicionar os novos
        if (dto.getVeiculos() != null) {
            List<BioVeiculoTransportadora> veiculosAntigos = bioVeiculoTransportadoraRepository.findByBioTransportadoraId(id);
            bioVeiculoTransportadoraRepository.deleteAll(veiculosAntigos);

            for (VeiculoDTO veiculoDTO : dto.getVeiculos()) {
                salvarVeiculo(transportadora, veiculoDTO);
            }
        }

        return converterParaResponseDTO(transportadora);
    }

    /**
     * Buscar transportadora por ID
     */
    public TransportadoraResponseDTO buscarPorId(Long id) {
        BioTransportadora transportadora = bioTransportadoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(TRANSPORTADORA_NAO_ENCONTRADA + id));
        return converterParaResponseDTO(transportadora);
    }

    /**
     * Listar transportadoras com paginação e busca
     */
    public TransportadoraPageResponseDTO listar(Integer page, Integer pageSize, String search) {
        page = page != null && page > 0 ? page - 1 : 0;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<BioTransportadora> resultado;
        if (search != null && !search.trim().isEmpty()) {
            resultado = bioTransportadoraRepository.buscar(search.trim(), pageable);
        } else {
            resultado = bioTransportadoraRepository.findAll(pageable);
        }

        List<TransportadoraListItemDTO> items = resultado.getContent().stream()
                .map(this::converterParaListItemDTO)
                .collect(Collectors.toList());

        TransportadoraPageResponseDTO response = new TransportadoraPageResponseDTO();
        response.setItems(items);
        response.setTotal(resultado.getTotalElements());
        response.setPage(page + 1);
        response.setPageSize(pageSize);

        return response;
    }

    /**
     * Deletar transportadora
     */
    @Transactional
    public void deletar(Long id) {
        if (!bioTransportadoraRepository.existsById(id)) {
            throw new RuntimeException("Transportadora não encontrada com ID: " + id);
        }

        // Deletar veículos associados
        List<BioVeiculoTransportadora> veiculos = bioVeiculoTransportadoraRepository.findByBioTransportadoraId(id);
        bioVeiculoTransportadoraRepository.deleteAll(veiculos);

        bioTransportadoraRepository.deleteById(id);
    }

    /**
     * Adicionar veículo a uma transportadora (sub-recurso)
     */
    @Transactional
    public VeiculoDTO adicionarVeiculo(Long transportadoraId, VeiculoDTO veiculoDTO) {
        BioTransportadora transportadora = bioTransportadoraRepository.findById(transportadoraId)
                .orElseThrow(() -> new RuntimeException("Transportadora não encontrada com ID: " + transportadoraId));

        // Validar tag obrigatória para Biometano
        // if ("Biometano".equals(veiculoDTO.getTipoAbastecimento())) {
        //     if (veiculoDTO.getTag() == null || veiculoDTO.getTag().trim().isEmpty()) {
        //         throw new RuntimeException("TAG é obrigatória quando o tipo de abastecimento é Biometano");
        //     }
        //     if (veiculoDTO.getTag().length() != 16) {
        //         throw new RuntimeException("TAG deve ter exatamente 16 caracteres");
        //     }
        // }

        BioVeiculoTransportadora veiculo = new BioVeiculoTransportadora();
        veiculo.setBioTransportadora(transportadora);
        veiculo.setTipo(veiculoDTO.getTipo());
        veiculo.setCapacidade(veiculoDTO.getCapacidade());
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setTipoAbastecimento(veiculoDTO.getTipoAbastecimento());
        // veiculo.setTag(veiculoDTO.getTag());
        veiculo.setCriadoEm(LocalDateTime.now());
        veiculo.setAtualizadoEm(LocalDateTime.now());

        veiculo = bioVeiculoTransportadoraRepository.save(veiculo);

        return converterVeiculoParaDTO(veiculo);
    }

    /**
     * Editar veículo
     */
    @Transactional
    public VeiculoDTO editarVeiculo(Long transportadoraId, Long veiculoId, VeiculoDTO veiculoDTO) {
        BioTransportadora transportadora = bioTransportadoraRepository.findById(transportadoraId)
                .orElseThrow(() -> new RuntimeException("Transportadora não encontrada com ID: " + transportadoraId));

        BioVeiculoTransportadora veiculo = bioVeiculoTransportadoraRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + veiculoId));

        // Validar se o veículo pertence à transportadora
        if (!veiculo.getBioTransportadora().getId().equals(transportadoraId)) {
            throw new RuntimeException("Veículo não pertence a esta transportadora");
        }

        // Validar tag obrigatória para Biometano
        // if ("Biometano".equals(veiculoDTO.getTipoAbastecimento())) {
        //     if (veiculoDTO.getTag() == null || veiculoDTO.getTag().trim().isEmpty()) {
        //         throw new RuntimeException("TAG é obrigatória quando o tipo de abastecimento é Biometano");
        //     }
        //     if (veiculoDTO.getTag().length() != 16) {
        //         throw new RuntimeException("TAG deve ter exatamente 16 caracteres");
        //     }
        // }

        veiculo.setTipo(veiculoDTO.getTipo());
        veiculo.setCapacidade(veiculoDTO.getCapacidade());
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setTipoAbastecimento(veiculoDTO.getTipoAbastecimento());
        // veiculo.setTag(veiculoDTO.getTag());
        veiculo.setAtualizadoEm(LocalDateTime.now());

        veiculo = bioVeiculoTransportadoraRepository.save(veiculo);

        return converterVeiculoParaDTO(veiculo);
    }

    /**
     * Remover veículo
     */
    @Transactional
    public void removerVeiculo(Long transportadoraId, Long veiculoId) {
        BioTransportadora transportadora = bioTransportadoraRepository.findById(transportadoraId)
                .orElseThrow(() -> new RuntimeException("Transportadora não encontrada com ID: " + transportadoraId));

        BioVeiculoTransportadora veiculo = bioVeiculoTransportadoraRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + veiculoId));

        // Validar se o veículo pertence à transportadora
        if (!veiculo.getBioTransportadora().getId().equals(transportadoraId)) {
            throw new RuntimeException("Veículo não pertence a esta transportadora");
        }

        bioVeiculoTransportadoraRepository.deleteById(veiculoId);
    }

    // ============ MÉTODOS AUXILIARES ============

    private void preencherTransportadora(BioTransportadora transportadora, TransportadoraDTO dto) {
        transportadora.setNomeFantasia(dto.getNomeFantasia());
        transportadora.setRazaoSocial(dto.getRazaoSocial());
        transportadora.setCnpj(dto.getCnpj());
        transportadora.setCategoria(dto.getCategoria());
        transportadora.setEndereco(dto.getEndereco());
        transportadora.setCidade(dto.getCidade());
        transportadora.setUf(dto.getUf());
        transportadora.setTelefoneComercial(dto.getTelefoneComercial());
        transportadora.setEmailComercial(dto.getEmailComercial());

        // Preencher contatos
        if (dto.getContatoPrincipal() != null) {
            transportadora.setContatoPrincipalNome(dto.getContatoPrincipal().getNome());
            transportadora.setContatoPrincipalTelefone(dto.getContatoPrincipal().getTelefone());
            transportadora.setContatoPrincipalEmail(dto.getContatoPrincipal().getEmail());
        }
    }

    private void salvarVeiculo(BioTransportadora transportadora, VeiculoDTO veiculoDTO) {
        // Validar tag obrigatória para Biometano
        // if ("Biometano".equals(veiculoDTO.getTipoAbastecimento())) {
        //     if (veiculoDTO.getTag() == null || veiculoDTO.getTag().trim().isEmpty()) {
        //         throw new RuntimeException("TAG é obrigatória quando o tipo de abastecimento é Biometano");
        //     }
        //     if (veiculoDTO.getTag().length() != 16) {
        //         throw new RuntimeException("TAG deve ter exatamente 16 caracteres");
        //     }
        // }

        BioVeiculoTransportadora veiculo = new BioVeiculoTransportadora();
        veiculo.setBioTransportadora(transportadora);
        veiculo.setTipo(veiculoDTO.getTipo());
        veiculo.setCapacidade(veiculoDTO.getCapacidade());
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setTipoAbastecimento(veiculoDTO.getTipoAbastecimento());
        // veiculo.setTag(veiculoDTO.getTag());
        veiculo.setCriadoEm(LocalDateTime.now());
        veiculo.setAtualizadoEm(LocalDateTime.now());

        bioVeiculoTransportadoraRepository.save(veiculo);
    }

    private TransportadoraResponseDTO converterParaResponseDTO(BioTransportadora transportadora) {
        TransportadoraResponseDTO dto = new TransportadoraResponseDTO();
        dto.setId(transportadora.getId());
        dto.setNomeFantasia(transportadora.getNomeFantasia());
        dto.setRazaoSocial(transportadora.getRazaoSocial());
        dto.setCnpj(transportadora.getCnpj());
        dto.setCategoria(transportadora.getCategoria());
        dto.setEndereco(transportadora.getEndereco());
        dto.setCidade(transportadora.getCidade());
        dto.setUf(transportadora.getUf());
        dto.setTelefoneComercial(transportadora.getTelefoneComercial());
        dto.setEmailComercial(transportadora.getEmailComercial());
        dto.setStatus(transportadora.getStatus());
        dto.setCriadoEm(transportadora.getCriadoEm());
        dto.setAtualizadoEm(transportadora.getAtualizadoEm());

        // Converter contatos
        if (transportadora.getContatoPrincipalNome() != null) {
            dto.setContatoPrincipal(new ContatoDTO(
                    transportadora.getContatoPrincipalNome(),
                    transportadora.getContatoPrincipalTelefone(),
                    transportadora.getContatoPrincipalEmail()
            ));
        }

        // Carregar veículos associados
        List<BioVeiculoTransportadora> veiculos = bioVeiculoTransportadoraRepository.findByBioTransportadoraId(transportadora.getId());
        dto.setVeiculos(veiculos.stream()
                .map(this::converterVeiculoParaDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private TransportadoraListItemDTO converterParaListItemDTO(BioTransportadora transportadora) {
        TransportadoraListItemDTO dto = new TransportadoraListItemDTO();
        dto.setId(transportadora.getId());
        dto.setNomeFantasia(transportadora.getNomeFantasia());
        dto.setRazaoSocial(transportadora.getRazaoSocial());
        dto.setCnpj(transportadora.getCnpj());
        dto.setTelefoneComercial(transportadora.getTelefoneComercial());
        dto.setEmailComercial(transportadora.getEmailComercial());
        dto.setEndereco(transportadora.getEndereco());
        dto.setCidade(transportadora.getCidade());
        dto.setUf(transportadora.getUf());
        dto.setStatus(transportadora.getStatus());

        // Contar veículos
        Long quantidadeVeiculos = bioVeiculoTransportadoraRepository.countByBioTransportadoraId(transportadora.getId());
        dto.setQuantidadeVeiculos(quantidadeVeiculos);

        return dto;
    }

    private VeiculoDTO converterVeiculoParaDTO(BioVeiculoTransportadora veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setTipo(veiculo.getTipo());
        dto.setCapacidade(veiculo.getCapacidade());
        dto.setPlaca(veiculo.getPlaca());
        dto.setTipoAbastecimento(veiculo.getTipoAbastecimento());
        // dto.setTag(veiculo.getTag());
        return dto;
    }
}
