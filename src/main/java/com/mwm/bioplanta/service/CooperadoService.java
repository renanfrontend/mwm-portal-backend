package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.CooperadoCreateDTO;
import com.mwm.bioplanta.dto.ProdutorListResponseDTO;
import com.mwm.bioplanta.dto.ProdutorPageResponseDTO;
import com.mwm.bioplanta.model.*;
import com.mwm.bioplanta.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CooperadoService {

    @Autowired
    private BioFiliadaRepository bioFiliadaRepository;

    @Autowired
    private BioProdutorRepository bioProdutorRepository;

    @Autowired
    private BioEstabelecimentoRepository bioEstabelecimentoRepository;

    @Autowired
    private BioProducaoRepository bioProducaoRepository;

    @Autowired
    private BioTransportadoraRepository bioTransportadoraRepository;

    @Autowired
    private BioPlantaRepository bioPlantaRepository;

    @Transactional
    public BioEstabelecimento criarCooperado(CooperadoCreateDTO dto) {
        System.out.println("Iniciando criação de cooperado: " + dto.getNomeCooperado());
        
        // 1. Buscar BioTransportadora
        BioTransportadora transportadora = bioTransportadoraRepository.findById(dto.getTransportadoraId())
            .orElseThrow(() -> new RuntimeException("Transportadora não encontrada"));

        // 2. Buscar BioFiliada (pelo ID se fornecido, senão pelo nome da transportadora)
        BioFiliada filiada = null;
        if (dto.getFiliadaId() != null) {
            filiada = bioFiliadaRepository.findById(dto.getFiliadaId())
                .orElseThrow(() -> new RuntimeException("Filiada informada não encontrada: " + dto.getFiliadaId()));
        } else {
             // Fallback legado: tenta achar pelo nome da transportadora
             filiada = bioFiliadaRepository.findByNome(transportadora.getNomeFantasia());
        }

        if (filiada == null) {
             System.out.println("ERRO: Filiada não encontrada. ID: " + dto.getFiliadaId() + " ou Nome: " + transportadora.getNomeFantasia());
             throw new RuntimeException("Filiada não encontrada. Informe o ID da filiada ou verifique o cadastro.");
        }
        System.out.println("Filiada selecionada: " + filiada.getId() + " - " + filiada.getNome());

        // 3. Verificar ou criar BioProdutor
        BioProdutor produtor = bioProdutorRepository.findByCpfCnpj(dto.getCpfCnpj());
        if (produtor == null) {
            produtor = new BioProdutor();
            produtor.setBioFiliada(filiada);
            produtor.setNome(dto.getNomeCooperado());
            produtor.setCpfCnpj(dto.getCpfCnpj());
            produtor.setTelefonePrincipal(dto.getTelefone());
            produtor.setTipoPessoa(dto.getCpfCnpj() != null && dto.getCpfCnpj().length() > 14 ? "PJ" : "PF");
            produtor.setStatus("A");
            produtor.setDataCadastro(LocalDate.now());
            produtor.setCodigoProdutor("PROD-" + System.currentTimeMillis()); // Gerador simples provisório
            
            produtor.setCriadoEm(LocalDateTime.now());
            produtor.setAtualizadoEm(LocalDateTime.now());
            produtor = bioProdutorRepository.save(produtor);
        }

        // 3.1. Validar duplicidade de Estabelecimento (Regra de Negócio)
        if (dto.getNumEstabelecimento() != null && bioEstabelecimentoRepository.existsByNumeroEstabelecimento(dto.getNumEstabelecimento())) {
            throw new RuntimeException("Já existe um estabelecimento com o número: " + dto.getNumEstabelecimento());
        }
        if (dto.getNumPropriedade() != null && bioEstabelecimentoRepository.existsByNumeroPropriedade(dto.getNumPropriedade())) {
            throw new RuntimeException("Já existe um estabelecimento com o número de propriedade: " + dto.getNumPropriedade());
        }

        // 4. Criar BioEstabelecimento
        BioEstabelecimento estabelecimento = new BioEstabelecimento();
        estabelecimento.setBioProdutor(produtor);
        // Gerar código único para estabelecimento (Obrigatório)
        estabelecimento.setCodigoEstabelecimento("EST-" + System.currentTimeMillis()); 
        
        // Campos Obrigatórios (NOT NULL no banco)
        estabelecimento.setNumeroEstabelecimento(dto.getNumEstabelecimento() != null ? dto.getNumEstabelecimento() : "EST-PADRAO"); 
        estabelecimento.setNumeroPropriedade(dto.getNumPropriedade() != null ? dto.getNumPropriedade() : "PROP-PADRAO");
        estabelecimento.setMatricula(dto.getMatricula() != null ? dto.getMatricula().toString() : "0000");
        estabelecimento.setMunicipio(dto.getMunicipio() != null ? dto.getMunicipio() : "Não Informado");
        
        // Extrair estado ou usar padrão
        String estado = "PR"; 
        if (dto.getMunicipio() != null && dto.getMunicipio().contains("-")) {
            String[] parts = dto.getMunicipio().split("-");
            if (parts.length > 1) {
                estado = parts[1].trim().substring(0, 2);
            }
        }
        estabelecimento.setEstado(estado);
        
        // Opcionais
        estabelecimento.setNome(dto.getNomeCooperado() + " - " + (dto.getNumPropriedade() != null ? dto.getNumPropriedade() : ""));
        estabelecimento.setEndereco(dto.getLocalizacao() != null ? dto.getLocalizacao() : dto.getMunicipio());
        estabelecimento.setResponsavel(dto.getResponsavel());
        estabelecimento.setDistancia(dto.getDistancia());
        estabelecimento.setRestricoes(dto.getRestricoes());
        estabelecimento.setStatus("A");
        estabelecimento.setLatitude(dto.getLatitude());
        estabelecimento.setLongitude(dto.getLongitude());
        
        estabelecimento.setCriadoEm(LocalDateTime.now());
        estabelecimento.setAtualizadoEm(LocalDateTime.now());
        estabelecimento = bioEstabelecimentoRepository.save(estabelecimento);

        // 5. Criar BioProducao
        BioProducao producao = new BioProducao();
        producao.setBioEstabelecimento(estabelecimento);
        
        // Campos Obrigatórios (NOT NULL no banco)
        producao.setAnoSafra("2024/2025"); 
        producao.setModalidadeFase(dto.getFase() != null ? dto.getFase() : "N/A");
        
        // Opcionais
        producao.setCertificacao(dto.getCertificado() != null && dto.getCertificado().equalsIgnoreCase("Ativo") ? "S" : "N");
        producao.setDoacaoDejetos(dto.getDoamDejetos() != null && dto.getDoamDejetos().equalsIgnoreCase("Sim") ? "S" : "N");
        producao.setQuantidadeCabecas(dto.getCabecas());
        producao.setQtdLagoas(dto.getQtdLagoas());
        producao.setVolLagoas(dto.getVolLagoas());
        producao.setTecnicoResponsavel(dto.getTecnico());
        producao.setTelefoneTecnico(dto.getTelefone());  
        producao.setDataInicioProducao(LocalDate.now());
        producao.setStatus("A");
        
        producao.setCriadoEm(LocalDateTime.now());
        producao.setAtualizadoEm(LocalDateTime.now());
        bioProducaoRepository.save(producao);

        return estabelecimento;
    }

    public ProdutorPageResponseDTO listarProdutores(Long plantaId, Long filiadaId, Integer page, Integer pageSize) {
        // Validação de parâmetros de paginação
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        // Buscar estabelecimentos (produtores) com filtros
        List<BioEstabelecimento> estabelecimentos = bioEstabelecimentoRepository.findByFiliada(filiadaId);

        // Converter para DTOs
        List<ProdutorListResponseDTO> items = estabelecimentos.stream()
                .map(e -> {
                    ProdutorListResponseDTO dto = new ProdutorListResponseDTO();
                    dto.setId(e.getId());
                    dto.setNomeProdutor(e.getBioProdutor().getNome());
                    dto.setNumEstabelecimento(e.getNumeroEstabelecimento());
                    dto.setFiliada(e.getBioProdutor().getBioFiliada().getNome());
                    
                    // Buscar produção associada para campos específicos
                    BioProducao producao = e.getBioProducao() != null && !e.getBioProducao().isEmpty() 
                            ? e.getBioProducao().get(0) 
                            : null;
                    
                    if (producao != null) {
                        dto.setModalidade(producao.getModalidadeFase());
                        dto.setCabecasAlojadas(producao.getQuantidadeCabecas() != null 
                                ? producao.getQuantidadeCabecas().intValue() 
                                : 0);
                        dto.setCertificado(producao.getCertificacao() != null && producao.getCertificacao().equals("S") 
                                ? "Sim" 
                                : "Não");
                        dto.setParticipaProjeto("Sim"); // Assumindo que se existe registro, participa
                        dto.setQtdLagoas(producao.getQtdLagoas());
                        dto.setVolLagoas(producao.getVolLagoas());
                    } else {
                        dto.setModalidade("N/A");
                        dto.setCabecasAlojadas(0);
                        dto.setCertificado("Não");
                        dto.setParticipaProjeto("Não");
                        dto.setQtdLagoas(0);
                        dto.setVolLagoas("0");
                    }
                    
                    dto.setDistancia(e.getDistancia());
                    dto.setRestricoesOperacionais(e.getRestricoes());
                    
                    return dto;
                })
                .collect(Collectors.toList());

        // Aplicar paginação em memória (para dados pequenos)
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, items.size());
        
        List<ProdutorListResponseDTO> paginatedItems;
        if (start > items.size()) {
            paginatedItems = List.of();
        } else {
            paginatedItems = items.subList(start, end);
        }

        // Montar resposta
        ProdutorPageResponseDTO response = new ProdutorPageResponseDTO();
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setTotal((long) items.size());
        response.setItems(paginatedItems);

        return response;
    }

    @Transactional
    public BioEstabelecimento atualizarCooperado(Long id, CooperadoCreateDTO dto) {
        System.out.println("Iniciando atualização de cooperado ID: " + id);

        // 1. Buscar o Estabelecimento Existente
        BioEstabelecimento estabelecimento = bioEstabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + id));

        BioProdutor produtor = estabelecimento.getBioProdutor();

        // 2. Atualizar Dados do Produtor
        produtor.setNome(dto.getNomeCooperado());
        produtor.setCpfCnpj(dto.getCpfCnpj());
        produtor.setTelefonePrincipal(dto.getTelefone());
        produtor.setTipoPessoa(dto.getCpfCnpj() != null && dto.getCpfCnpj().length() > 14 ? "PJ" : "PF");
        produtor.setAtualizadoEm(LocalDateTime.now());
        
        // Se a filiada mudou
        if (dto.getFiliadaId() != null && !dto.getFiliadaId().equals(produtor.getBioFiliada().getId())) {
             BioFiliada novaFiliada = bioFiliadaRepository.findById(dto.getFiliadaId())
                .orElseThrow(() -> new RuntimeException("Nova filiada não encontrada: " + dto.getFiliadaId()));
             produtor.setBioFiliada(novaFiliada);
        }
        
        bioProdutorRepository.save(produtor);

        // 3. Atualizar Dados do Estabelecimento
        estabelecimento.setNumeroEstabelecimento(dto.getNumEstabelecimento() != null ? dto.getNumEstabelecimento() : estabelecimento.getNumeroEstabelecimento());
        estabelecimento.setNumeroPropriedade(dto.getNumPropriedade() != null ? dto.getNumPropriedade() : estabelecimento.getNumeroPropriedade());
        estabelecimento.setMatricula(dto.getMatricula() != null ? dto.getMatricula().toString() : estabelecimento.getMatricula());
        estabelecimento.setMunicipio(dto.getMunicipio() != null ? dto.getMunicipio() : estabelecimento.getMunicipio());
        
        // Atualizar estado se município mudou
        if (dto.getMunicipio() != null && dto.getMunicipio().contains("-")) {
            String[] parts = dto.getMunicipio().split("-");
            if (parts.length > 1) {
                estabelecimento.setEstado(parts[1].trim().substring(0, 2));
            }
        }
        
        estabelecimento.setNome(dto.getNomeCooperado() + " - " + (dto.getNumPropriedade() != null ? dto.getNumPropriedade() : ""));
        estabelecimento.setEndereco(dto.getLocalizacao() != null ? dto.getLocalizacao() : estabelecimento.getEndereco());
        estabelecimento.setResponsavel(dto.getResponsavel());
        estabelecimento.setDistancia(dto.getDistancia());
        estabelecimento.setRestricoes(dto.getRestricoes());
        estabelecimento.setLatitude(dto.getLatitude());
        estabelecimento.setLongitude(dto.getLongitude());
        estabelecimento.setAtualizadoEm(LocalDateTime.now());
        
        estabelecimento = bioEstabelecimentoRepository.save(estabelecimento);

        // 4. Atualizar Produção
        BioProducao producao;
        if (estabelecimento.getBioProducao() != null && !estabelecimento.getBioProducao().isEmpty()) {
            producao = estabelecimento.getBioProducao().get(0);
        } else {
            producao = new BioProducao();
            producao.setBioEstabelecimento(estabelecimento);
            producao.setCriadoEm(LocalDateTime.now());
            producao.setAnoSafra("2024/2025");
            producao.setDataInicioProducao(LocalDate.now());
            producao.setStatus("A");
        }

        producao.setModalidadeFase(dto.getFase() != null ? dto.getFase() : "N/A");
        producao.setCertificacao(dto.getCertificado() != null && dto.getCertificado().equalsIgnoreCase("Ativo") ? "S" : "N");
        producao.setDoacaoDejetos(dto.getDoamDejetos() != null && dto.getDoamDejetos().equalsIgnoreCase("Sim") ? "S" : "N");
        producao.setQuantidadeCabecas(dto.getCabecas());
        producao.setQtdLagoas(dto.getQtdLagoas());
        producao.setVolLagoas(dto.getVolLagoas());
        producao.setTecnicoResponsavel(dto.getTecnico());
        producao.setTelefoneTecnico(dto.getTelefone());
        producao.setAtualizadoEm(LocalDateTime.now());

        bioProducaoRepository.save(producao);

        return estabelecimento;
    }
}
