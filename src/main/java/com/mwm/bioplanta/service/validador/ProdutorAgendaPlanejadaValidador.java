package com.mwm.bioplanta.service.validador;

import com.mwm.bioplanta.model.BioAgendaPlanejada;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.model.BioProdutor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProdutorAgendaPlanejadaValidador {
    /**
     * Filtra lançamentos de agenda planejada removendo os produtores que não participam do projeto ou não são certificados
     * para a semana atual (domingo a sábado) e datas futuras.
     *
     * @param agendas Lista de agendas planejadas a filtrar
     * @param estabelecimentos Lista de estabelecimentos (com produtor) relacionados
     * @param dataReferencia Data de referência para cálculo da semana atual
     * @return Lista filtrada de agendas planejadas
     */
    public static List<BioAgendaPlanejada> filtrarAgendasPorRegrasParticipacaoECertificado(
            List<BioAgendaPlanejada> agendas,
            List<BioEstabelecimento> estabelecimentos,
            LocalDate dataReferencia
    ) {
        // Calcula início (domingo) e fim (sábado) da semana vigente
        LocalDate inicioSemana = dataReferencia.with(DayOfWeek.SUNDAY);

        // Mapeia produtores inválidos (não participam ou não certificados)
        Set<Long> idsProdutoresInvalidos = estabelecimentos.stream()
                .filter(e -> {
                    BioProdutor p = e.getBioProdutor();
                    if (p == null) return false;
                    boolean naoParticipa = campoNao(p.getDoamDejetos());
                    boolean naoCertificado = campoNao(p.getCertificado());
                    return naoParticipa || naoCertificado;
                })
                .map(e -> e.getBioProdutor().getId())
                .collect(Collectors.toSet());

        // Filtra agendas: remove lançamentos de produtores inválidos na semana atual ou datas futuras
        return agendas.stream()
                .filter(a -> {
                    Long idProdutor = buscarIdProdutorPorEstabelecimento(a.getIdEstabelecimento(), estabelecimentos);
                    if (idProdutor == null || !idsProdutoresInvalidos.contains(idProdutor)) {
                        return true; // Produtor válido
                    }
                    LocalDate data = a.getDataAgendada();
                    // Só remove se for semana atual ou futura
                    return data.isBefore(inicioSemana);
                })
                .collect(Collectors.toList());
    }

    private static boolean campoNao(String valor) {
        if (valor == null) return false;
        String v = valor.trim().toLowerCase();
        return v.equals("n") || v.equals("nao") || v.equals("não") || v.equals("false") || v.equals("falso");
    }

    private static Long buscarIdProdutorPorEstabelecimento(Long idEstab, List<BioEstabelecimento> estabelecimentos) {
        return estabelecimentos.stream()
                .filter(e -> e.getId().equals(idEstab) && e.getBioProdutor() != null)
                .map(e -> e.getBioProdutor().getId())
                .findFirst()
                .orElse(null);
    }
}
