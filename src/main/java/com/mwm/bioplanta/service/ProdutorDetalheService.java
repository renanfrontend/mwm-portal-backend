package com.mwm.bioplanta.service;

import com.mwm.bioplanta.dto.BioProdutorSimplificadoDTO;
import com.mwm.bioplanta.dto.ProdutorDetalheDTO;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.model.BioProducao;
import com.mwm.bioplanta.model.BioProdutor;
import com.mwm.bioplanta.repository.BioEstabelecimentoRepository;
import com.mwm.bioplanta.util.SimNaoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;

/**
 * Service para buscar detalhes completos de um produtor
 * Seguindo princípio Single Responsibility (SOLID)
 */
@Service
public class ProdutorDetalheService {

    private final BioEstabelecimentoRepository estabelecimentoRepository;

    public ProdutorDetalheService(BioEstabelecimentoRepository estabelecimentoRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    /**
     * Busca detalhes completos de um produtor pelo ID do estabelecimento
     * @param id ID do estabelecimento
     * @return DTO com dados completos ou Optional.empty() se não encontrado
     */
    @Transactional(readOnly = true)
    public Optional<ProdutorDetalheDTO> buscarDetalhePorId(Long id) {
        Optional<BioEstabelecimento> estabelecimentoOpt = estabelecimentoRepository
                .findById(java.util.Objects.requireNonNull(id));
        
        if (estabelecimentoOpt.isEmpty()) {
            return Optional.empty();
        }

        BioEstabelecimento estabelecimento = estabelecimentoOpt.get();
        
        // Mapear dados do estabelecimento
        ProdutorDetalheDTO dto = ProdutorDetalheDTO.builder()
            .id(estabelecimento.getId())
                .matricula(estabelecimento.getMatricula())
                .nome(estabelecimento.getNome())
                .municipio(estabelecimento.getMunicipio())
                .status(mapearStatus(estabelecimento.getStatus()))
                .latitude(estabelecimento.getLatitude())
                .longitude(estabelecimento.getLongitude())
                .distancia(estabelecimento.getDistancia())
                .distanciaKm(estabelecimento.getBioProdutor() != null ? estabelecimento.getBioProdutor().getDistanciaKm() : null)
                .responsavel(estabelecimento.getResponsavel())
                .restricoes(estabelecimento.getRestricoes())
                .localizacao(estabelecimento.getEndereco()) // Ajustado para pegar o endereço (localização) corretamente
                .numeroEstabelecimento(estabelecimento.getNumeroEstabelecimento())
                .numeroPropriedade(estabelecimento.getNumeroPropriedade())
                .build();

        // Buscar dados da produção mais recente (se existir)
        if (estabelecimento.getBioProducao() != null && !estabelecimento.getBioProducao().isEmpty()) {
            BioProducao producaoRecente = estabelecimento.getBioProducao().stream()
                    .max(Comparator.comparing(p -> p.getCriadoEm() != null ? p.getCriadoEm() : java.time.LocalDateTime.MIN))
                    .orElse(null);

            if (producaoRecente != null) {
                dto.setQtdLagoas(producaoRecente.getQtdLagoas());
                dto.setVolLagoas(producaoRecente.getVolLagoas());
                dto.setFase(producaoRecente.getModalidadeFase());
                dto.setCabecas(producaoRecente.getQuantidadeCabecas());
                dto.setCertificado(SimNaoMapper.toDescricao(producaoRecente.getCertificacao()));
                dto.setDoamDejetos(SimNaoMapper.toDescricao(producaoRecente.getDoacaoDejetos()));
                dto.setTecnico(producaoRecente.getTecnicoResponsavel());
            }
        }

        // Mapear dados do produtor
        BioProdutor produtor = estabelecimento.getBioProdutor();
        if (produtor != null) {
            BioProdutorSimplificadoDTO produtorDTO = new BioProdutorSimplificadoDTO();
            produtorDTO.setId(produtor.getId());
            produtorDTO.setCodigoProdutor(produtor.getCodigoProdutor());
            produtorDTO.setNome(produtor.getNome());
            produtorDTO.setCpfCnpj(produtor.getCpfCnpj());
            produtorDTO.setTelefonePrincipal(produtor.getTelefonePrincipal());
            produtorDTO.setDistanciaKm(produtor.getDistanciaKm());
            
            if (produtor.getBioFiliada() != null) {
                produtorDTO.setFiliadaId(produtor.getBioFiliada().getId());
                produtorDTO.setFiliadaNome(produtor.getBioFiliada().getNome());
            }
            
            dto.setBioProdutor(produtorDTO);
        }

        return Optional.of(dto);
    }

    /**
     * Mapeia status do banco (A/I) para formato legível
     */
    private String mapearStatus(String status) {
        if (status == null) return null;
        return switch (status.toUpperCase()) {
            case "A" -> "Ativo";
            case "I" -> "Inativo";
            default -> status;
        };
    }

}
