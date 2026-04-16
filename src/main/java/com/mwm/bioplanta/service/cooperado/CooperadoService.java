package com.mwm.bioplanta.service.cooperado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mwm.bioplanta.dto.cooperado.CooperadoCreateDTO;
import com.mwm.bioplanta.dto.cooperado.ProdutorBuscaNumEstabelecimentoResponseDTO;
import com.mwm.bioplanta.dto.cooperado.ProdutorBuscaResponseDTO;
import com.mwm.bioplanta.dto.cooperado.ProdutorListResponseDTO;
import com.mwm.bioplanta.dto.cooperado.ProdutorPageResponseDTO;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.model.BioFiliada;
import com.mwm.bioplanta.model.BioProducao;
import com.mwm.bioplanta.model.BioProdutor;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.repository.cadastro.BioFiliadaRepository;
import com.mwm.bioplanta.repository.cadastro.BioPlantaRepository;
import com.mwm.bioplanta.repository.cadastro.BioProducaoRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cooperado.BioEstabelecimentoRepository;
import com.mwm.bioplanta.repository.cooperado.BioProdutorRepository;
import com.mwm.bioplanta.util.SimNaoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CooperadoService {
    @Transactional
    public void inativarEstabelecimentos(List<Long> estabelecimentoIds) {
        if (estabelecimentoIds == null || estabelecimentoIds.isEmpty()) {
            throw new RuntimeException("Informe ao menos um estabelecimento para inativação.");
        }

        for (Long idEstabelecimento : estabelecimentoIds) {
            inativarEstabelecimento(idEstabelecimento);
        }
    }

    @Transactional
    public void inativarEstabelecimento(Long idEstabelecimento) {
        BioEstabelecimento estabelecimento = bioEstabelecimentoRepository.findById(idEstabelecimento)
            .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + idEstabelecimento));

        estabelecimento.setStatus("I");
        estabelecimento.setAtualizadoEm(LocalDateTime.now());
        bioEstabelecimentoRepository.save(estabelecimento);

        BioProdutor produtor = estabelecimento.getBioProdutor();
        if (produtor == null) {
            return;
        }

        long ativos = bioEstabelecimentoRepository.countByBioProdutorIdAndStatus(produtor.getId(), "A");
        if (ativos == 0) {
            produtor.setStatus("I");
            produtor.setAtualizadoEm(LocalDateTime.now());
            bioProdutorRepository.save(produtor);
        }
    }

    @Transactional
    public void inativarProdutor(Long idProdutor) {
        BioProdutor produtor = bioProdutorRepository.findById(idProdutor)
            .orElseThrow(() -> new RuntimeException("Produtor não encontrado com ID: " + idProdutor));

        produtor.setStatus("I");
        produtor.setAtualizadoEm(LocalDateTime.now());
        bioProdutorRepository.save(produtor);

        List<BioEstabelecimento> estabelecimentos = bioEstabelecimentoRepository.findByBioProdutorId(idProdutor);
        for (BioEstabelecimento estabelecimento : estabelecimentos) {
            estabelecimento.setStatus("I");
            estabelecimento.setAtualizadoEm(LocalDateTime.now());
        }
        bioEstabelecimentoRepository.saveAll(estabelecimentos);
    }

    private static final Logger logger = LoggerFactory.getLogger(CooperadoService.class);


    private final BioFiliadaRepository bioFiliadaRepository;
    private final BioProdutorRepository bioProdutorRepository;
    private final BioEstabelecimentoRepository bioEstabelecimentoRepository;
    private final BioProducaoRepository bioProducaoRepository;
    private final BioTransportadoraRepository bioTransportadoraRepository;
    private final BioPlantaRepository bioPlantaRepository;

    public CooperadoService(
        BioFiliadaRepository bioFiliadaRepository,
        BioProdutorRepository bioProdutorRepository,
        BioEstabelecimentoRepository bioEstabelecimentoRepository,
        BioProducaoRepository bioProducaoRepository,
        BioTransportadoraRepository bioTransportadoraRepository,
        BioPlantaRepository bioPlantaRepository
    ) {
        this.bioFiliadaRepository = bioFiliadaRepository;
        this.bioProdutorRepository = bioProdutorRepository;
        this.bioEstabelecimentoRepository = bioEstabelecimentoRepository;
        this.bioProducaoRepository = bioProducaoRepository;
        this.bioTransportadoraRepository = bioTransportadoraRepository;
        this.bioPlantaRepository = bioPlantaRepository;
    }

    @Transactional
    public BioEstabelecimento criarCooperado(CooperadoCreateDTO dto) {
        logger.info("Iniciando criação de cooperado: {}", dto.getNomeCooperado());
        String certificadoBanco = SimNaoMapper.toBanco(dto.getCertificado());
        String doamDejetosBanco = SimNaoMapper.toBanco(dto.getDoamDejetos());
        BioTransportadora transportadora = null;
        BioFiliada filiada = buscarFiliada(dto, transportadora);
        logger.info("Filiada selecionada: {} - {}", filiada.getId(), filiada.getNome());
        BioProdutor produtor = buscarOuCriarProdutor(dto, filiada, certificadoBanco, doamDejetosBanco);
        validarDuplicidadeEstabelecimento(dto);
        BioEstabelecimento estabelecimento = criarEstabelecimento(dto, produtor);
        criarProducao(dto, estabelecimento, certificadoBanco, doamDejetosBanco);
        return estabelecimento;
    }

    @Transactional(readOnly = true)
    public Optional<ProdutorBuscaResponseDTO> buscarProdutorPorCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null || cpfCnpj.isBlank()) {
            return Optional.empty();
        }

        String cpfCnpjNormalizado = cpfCnpj.replaceAll("\\D", "");
        if (cpfCnpjNormalizado.isBlank()) {
            return Optional.empty();
        }

        Optional<BioProdutor> produtorOpt = bioProdutorRepository.findAtivoByCpfCnpjNormalizado(cpfCnpjNormalizado);
        if (produtorOpt.isEmpty()) {
            return Optional.empty();
        }

        BioProdutor produtor = produtorOpt.get();
        String numEstabelecimento = bioEstabelecimentoRepository
                .findFirstByBioProdutorIdOrderByIdAsc(produtor.getId())
                .map(BioEstabelecimento::getNumeroEstabelecimento)
                .orElse(null);

        ProdutorBuscaResponseDTO dto = new ProdutorBuscaResponseDTO(
                produtor.getId(),
                produtor.getNome(),
                numEstabelecimento,
                cpfCnpjNormalizado
        );

        return Optional.of(dto);
    }

    @Transactional(readOnly = true)
    public Optional<ProdutorBuscaNumEstabelecimentoResponseDTO> buscarProdutorPorNumeroEstabelecimento(String numEstabelecimento) {
        if (numEstabelecimento == null || numEstabelecimento.isBlank()) {
            return Optional.empty();
        }

        return bioEstabelecimentoRepository.findFirstByNumeroEstabelecimento(numEstabelecimento)
                .map(estabelecimento -> {
                    Long produtorId = estabelecimento.getBioProdutor() != null ? estabelecimento.getBioProdutor().getId() : null;
                    String nomeProdutor = estabelecimento.getBioProdutor() != null ? estabelecimento.getBioProdutor().getNome() : null;
                    return new ProdutorBuscaNumEstabelecimentoResponseDTO(
                            produtorId,
                            nomeProdutor,
                            estabelecimento.getNumeroEstabelecimento(),
                            "N. de estabelecimento já existe."
                    );
                });
    }

    @Transactional(readOnly = true)
    public ProdutorPageResponseDTO listarProdutores(Long plantaId, Long filiadaId, Integer page, Integer pageSize) {
        // Validação de parâmetros de paginação
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        if (plantaId == null || filiadaId == null) {
            ProdutorPageResponseDTO response = new ProdutorPageResponseDTO();
            response.setPage(page);
            response.setPageSize(pageSize);
            response.setTotal(0L);
            response.setItems(List.of());
            return response;
        }

        // Buscar estabelecimentos (produtores) com filtros
        List<BioEstabelecimento> estabelecimentos = bioEstabelecimentoRepository.findByPlantaAndFiliada(plantaId, filiadaId);

        // Converter para DTOs
        List<ProdutorListResponseDTO> items = estabelecimentos.stream()
                .map(e -> {
                    ProdutorListResponseDTO dto = new ProdutorListResponseDTO();
                    Long produtorId = e.getBioProdutor() != null ? e.getBioProdutor().getId() : null;
                    dto.setId(produtorId);
                    dto.setProdutorId(produtorId);
                    dto.setEstabelecimentoId(e.getId());
                    
                    if (e.getBioProdutor() != null) {
                        dto.setNomeProdutor(e.getBioProdutor().getNome());
                        if (e.getBioProdutor().getBioFiliada() != null) {
                            dto.setFiliada(e.getBioProdutor().getBioFiliada().getNome());
                        } else {
                            dto.setFiliada("N/A");
                        }
                    } else {
                        dto.setNomeProdutor("N/A");
                        dto.setFiliada("N/A");
                    }
                    
                    dto.setNumEstabelecimento(e.getNumeroEstabelecimento());
                    
                    // Buscar produção associada para campos específicos
                    BioProducao producao = null;
                    if (e.getBioProducao() != null && !e.getBioProducao().isEmpty()) {
                         producao = e.getBioProducao().get(0);
                    }
                    
                        if (producao != null) {
                            dto.setModalidade(producao.getModalidadeFase());
                            dto.setCabecasAlojadas(producao.getQuantidadeCabecas() != null 
                                    ? producao.getQuantidadeCabecas().intValue() 
                                    : 0);
                                dto.setCertificado(SimNaoMapper.toDescricao(producao.getCertificacao()));
                                dto.setDoamDejetos(SimNaoMapper.toDescricao(producao.getDoacaoDejetos()));
                            dto.setQtdLagoas(producao.getQtdLagoas());
                            dto.setVolLagoas(producao.getVolLagoas());
                    } else {
                        dto.setModalidade("N/A");
                        dto.setCabecasAlojadas(0);
                        dto.setCertificado("");
                            dto.setDoamDejetos("");
                            dto.setQtdLagoas(0);
                            dto.setVolLagoas("0");
                    }
                    
                    // Lógica robusta para definir distância
                    String distanciaFinal = "-";
                    String distEst = e.getDistancia();
                    java.math.BigDecimal distProd = e.getBioProdutor() != null ? e.getBioProdutor().getDistanciaKm() : null;

                    // LOG EXTREMO para depurar
                    logger.info("DEBUG PRODUTOR ID {}: Nome={}, DistanciaEstab={}, DistanciaProdutor={}", 
                        e.getId(), 
                        e.getBioProdutor() != null ? e.getBioProdutor().getNome() : "SEM PRODUTOR",
                        distEst, 
                        distProd
                    );

                    // Prioriza BioEstabelecimento se tiver valor válido
                    if (distEst != null && !distEst.trim().isEmpty() && !distEst.trim().equals("-") && !distEst.trim().equals("0")) {
                        distanciaFinal = distEst;
                    } 
                    // Fallback para BioProdutor
                    else if (distProd != null) {
                        distanciaFinal = distProd.toString() + " Km"; // Adiciona " Km" para ficar igual ao padrão visual
                    }
                    
                    dto.setDistancia(distanciaFinal);
                    dto.setRestricoesOperacionais(e.getRestricoes());
                    
                    return dto;
                })
                .collect(Collectors.toList());

        // DEBUG: Imprimir no log o que está sendo retornado
        if (!items.isEmpty()) {
            logger.info("DEBUG - Exemplo de ProdutorDTO enviado: Nome={}, Distancia={}", 
                items.get(0).getNomeProdutor(), items.get(0).getDistancia());
        }

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
        logger.info("Iniciando atualização de cooperado ID: {}", id);

        BioEstabelecimento estabelecimento = bioEstabelecimentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + id));

        BioProdutor produtor = estabelecimento.getBioProdutor();
        String certificadoBanco = SimNaoMapper.toBanco(dto.getCertificado());
        String doamDejetosBanco = SimNaoMapper.toBanco(dto.getDoamDejetos());
        atualizarProdutor(produtor, dto, certificadoBanco, doamDejetosBanco);
        bioProdutorRepository.save(produtor);

        atualizarEstabelecimento(estabelecimento, produtor, dto);
        estabelecimento = bioEstabelecimentoRepository.save(estabelecimento);

        BioProducao producao = buscarOuCriarProducao(estabelecimento);
        atualizarProducao(producao, dto, certificadoBanco, doamDejetosBanco);
        bioProducaoRepository.save(producao);
        return estabelecimento;
    }

    private BioFiliada buscarFiliada(CooperadoCreateDTO dto, BioTransportadora transportadora) {
        BioFiliada filiada = null;
        if (dto.getFiliadaId() != null) {
            filiada = bioFiliadaRepository.findById(dto.getFiliadaId())
                .orElseThrow(() -> new RuntimeException("Filiada informada não encontrada: " + dto.getFiliadaId()));
        } else if (transportadora != null) {
            filiada = bioFiliadaRepository.findByNome(transportadora.getNomeFantasia());
        }

        if (filiada == null) {
            logger.error("ERRO: Filiada não encontrada. ID: {} ou Nome: {}", dto.getFiliadaId(), obterNomeTransportadora(transportadora));
            throw new RuntimeException("Filiada não encontrada. Informe o ID da filiada ou verifique o cadastro.");
        }

        return filiada;
    }

    private BioProdutor buscarOuCriarProdutor(CooperadoCreateDTO dto, BioFiliada filiada, String certificadoBanco, String doamDejetosBanco) {
        BioProdutor produtor = bioProdutorRepository.findByCpfCnpj(dto.getCpfCnpj());
        if (produtor == null) {
            produtor = new BioProdutor();
            produtor.setBioFiliada(filiada);
            produtor.setNome(dto.getNomeCooperado());
            produtor.setCpfCnpj(dto.getCpfCnpj());
            produtor.setTipoPessoa(determinarTipoPessoa(dto.getCpfCnpj()));
            produtor.setStatus("A");
            produtor.setDataCadastro(LocalDate.now());
            produtor.setCodigoProdutor(dto.getCodigoProdutor() != null ? dto.getCodigoProdutor() : "");
            produtor.setCriadoEm(LocalDateTime.now());
            produtor.setAtualizadoEm(LocalDateTime.now());
            produtor.setDistanciaKm(dto.getDistanciaKm());
            produtor.setCertificado(certificadoBanco);
            produtor.setDoamDejetos(doamDejetosBanco);
            return bioProdutorRepository.save(produtor);
        }

        if (dto.getDistanciaKm() != null) {
            produtor.setDistanciaKm(dto.getDistanciaKm());
            bioProdutorRepository.save(produtor);
        }

        return produtor;
    }

    private void validarDuplicidadeEstabelecimento(CooperadoCreateDTO dto) {
        if (dto.getNumEstabelecimento() != null && bioEstabelecimentoRepository.existsByNumeroEstabelecimento(dto.getNumEstabelecimento())) {
            throw new RuntimeException("Já existe um estabelecimento com o número: " + dto.getNumEstabelecimento());
        }
        if (dto.getNumPropriedade() != null && bioEstabelecimentoRepository.existsByNumeroPropriedade(dto.getNumPropriedade())) {
            throw new RuntimeException("Já existe um estabelecimento com o número de propriedade: " + dto.getNumPropriedade());
        }
    }

    private BioEstabelecimento criarEstabelecimento(CooperadoCreateDTO dto, BioProdutor produtor) {
        BioEstabelecimento estabelecimento = new BioEstabelecimento();
        estabelecimento.setBioProdutor(produtor);
        estabelecimento.setCodigoEstabelecimento(dto.getCodigoEstabelecimento() != null ? dto.getCodigoEstabelecimento() : "");
        estabelecimento.setNumeroEstabelecimento(dto.getNumEstabelecimento() != null ? dto.getNumEstabelecimento() : "EST-PADRAO");
        estabelecimento.setNumeroPropriedade(dto.getNumPropriedade() != null ? dto.getNumPropriedade() : "PROP-PADRAO");
        estabelecimento.setMatricula(dto.getMatricula() != null ? dto.getMatricula().toString() : "0000");
        estabelecimento.setMunicipio(dto.getMunicipio() != null ? dto.getMunicipio() : "Não Informado");
        estabelecimento.setEstado(extrairEstado(dto.getMunicipio()));
        estabelecimento.setNome(dto.getNomeCooperado() + " - " + (dto.getNumPropriedade() != null ? dto.getNumPropriedade() : ""));
        estabelecimento.setEndereco(dto.getLocalizacao() != null ? dto.getLocalizacao() : dto.getMunicipio());
        estabelecimento.setResponsavel(dto.getResponsavel());

        if (dto.getDistanciaKm() != null) {
            estabelecimento.setDistancia(dto.getDistanciaKm().toString() + " Km");
            produtor.setDistanciaKm(dto.getDistanciaKm());
        }

        estabelecimento.setRestricoes(dto.getRestricoes());
        estabelecimento.setStatus("A");
        estabelecimento.setLatitude(dto.getLatitude());
        estabelecimento.setLongitude(dto.getLongitude());
        estabelecimento.setCriadoEm(LocalDateTime.now());
        estabelecimento.setAtualizadoEm(LocalDateTime.now());
        return bioEstabelecimentoRepository.save(estabelecimento);
    }

    private void criarProducao(CooperadoCreateDTO dto, BioEstabelecimento estabelecimento, String certificadoBanco, String doamDejetosBanco) {
        BioProducao producao = new BioProducao();
        producao.setBioEstabelecimento(estabelecimento);
        producao.setAnoSafra("2024/2025");
        producao.setModalidadeFase(dto.getFase() != null ? dto.getFase() : "N/A");
        producao.setCertificacao(certificadoBanco);
        producao.setDoacaoDejetos(doamDejetosBanco);
        producao.setQuantidadeCabecas(dto.getCabecas());
        producao.setQtdLagoas(dto.getQtdLagoas());
        producao.setVolLagoas(dto.getVolLagoas());
        producao.setTecnicoResponsavel(dto.getTecnico());
        producao.setDataInicioProducao(LocalDate.now());
        producao.setStatus("A");
        producao.setCriadoEm(LocalDateTime.now());
        producao.setAtualizadoEm(LocalDateTime.now());
        bioProducaoRepository.save(producao);
    }

    private void atualizarProdutor(BioProdutor produtor, CooperadoCreateDTO dto, String certificadoBanco, String doamDejetosBanco) {
        produtor.setNome(dto.getNomeCooperado());
        produtor.setCpfCnpj(dto.getCpfCnpj());
        produtor.setTipoPessoa(determinarTipoPessoa(dto.getCpfCnpj()));
        produtor.setAtualizadoEm(LocalDateTime.now());
        produtor.setCertificado(certificadoBanco);
        produtor.setDoamDejetos(doamDejetosBanco);

        if (dto.getCodigoProdutor() != null) {
            produtor.setCodigoProdutor(dto.getCodigoProdutor());
        }

        if (dto.getFiliadaId() != null && !dto.getFiliadaId().equals(produtor.getBioFiliada().getId())) {
            BioFiliada novaFiliada = bioFiliadaRepository.findById(dto.getFiliadaId())
                .orElseThrow(() -> new RuntimeException("Nova filiada não encontrada: " + dto.getFiliadaId()));
            produtor.setBioFiliada(novaFiliada);
        }
    }

    private void atualizarEstabelecimento(BioEstabelecimento estabelecimento, BioProdutor produtor, CooperadoCreateDTO dto) {
        estabelecimento.setNumeroEstabelecimento(dto.getNumEstabelecimento() != null ? dto.getNumEstabelecimento() : estabelecimento.getNumeroEstabelecimento());
        estabelecimento.setNumeroPropriedade(dto.getNumPropriedade() != null ? dto.getNumPropriedade() : estabelecimento.getNumeroPropriedade());
        estabelecimento.setMatricula(dto.getMatricula() != null ? dto.getMatricula().toString() : estabelecimento.getMatricula());
        estabelecimento.setMunicipio(dto.getMunicipio() != null ? dto.getMunicipio() : estabelecimento.getMunicipio());

        if (dto.getCodigoEstabelecimento() != null) {
            estabelecimento.setCodigoEstabelecimento(dto.getCodigoEstabelecimento());
        }

        if (dto.getMunicipio() != null && dto.getMunicipio().contains("-")) {
            estabelecimento.setEstado(extrairEstado(dto.getMunicipio()));
        }

        estabelecimento.setNome(dto.getNomeCooperado() + " - " + (dto.getNumPropriedade() != null ? dto.getNumPropriedade() : ""));
        estabelecimento.setEndereco(dto.getLocalizacao() != null ? dto.getLocalizacao() : estabelecimento.getEndereco());
        estabelecimento.setResponsavel(dto.getResponsavel());
        estabelecimento.setRestricoes(dto.getRestricoes());
        estabelecimento.setLatitude(dto.getLatitude());
        estabelecimento.setLongitude(dto.getLongitude());

        if (dto.getDistanciaKm() != null) {
            estabelecimento.setDistancia(dto.getDistanciaKm().toString() + " Km");
            produtor.setDistanciaKm(dto.getDistanciaKm());
        }

        estabelecimento.setAtualizadoEm(LocalDateTime.now());
    }

    private BioProducao buscarOuCriarProducao(BioEstabelecimento estabelecimento) {
        if (estabelecimento.getBioProducao() != null && !estabelecimento.getBioProducao().isEmpty()) {
            return estabelecimento.getBioProducao().get(0);
        }

        BioProducao producao = new BioProducao();
        producao.setBioEstabelecimento(estabelecimento);
        producao.setCriadoEm(LocalDateTime.now());
        producao.setAnoSafra("2024/2025");
        producao.setDataInicioProducao(LocalDate.now());
        producao.setStatus("A");
        return producao;
    }

    private void atualizarProducao(BioProducao producao, CooperadoCreateDTO dto, String certificadoBanco, String doamDejetosBanco) {
        producao.setModalidadeFase(dto.getFase() != null ? dto.getFase() : "N/A");
        producao.setCertificacao(certificadoBanco);
        producao.setDoacaoDejetos(doamDejetosBanco);
        producao.setQuantidadeCabecas(dto.getCabecas());
        producao.setQtdLagoas(dto.getQtdLagoas());
        producao.setVolLagoas(dto.getVolLagoas());
        producao.setTecnicoResponsavel(dto.getTecnico());
        producao.setAtualizadoEm(LocalDateTime.now());
    }

    private String determinarTipoPessoa(String cpfCnpj) {
        String cpfCnpjNum = cpfCnpj != null ? cpfCnpj.replaceAll("\\D", "") : "";
        if (cpfCnpjNum.length() == 11) {
            return "PF";
        }
        if (cpfCnpjNum.length() == 14) {
            return "PJ";
        }
        return "";
    }

    private String extrairEstado(String municipio) {
        String estado = "PR";
        if (municipio != null && municipio.contains("-")) {
            String[] parts = municipio.split("-");
            if (parts.length > 1 && parts[1].trim().length() >= 2) {
                estado = parts[1].trim().substring(0, 2);
            }
        }
        return estado;
    }

    private String obterNomeTransportadora(BioTransportadora transportadora) {
        return transportadora != null ? transportadora.getNomeFantasia() : null;
    }
}
