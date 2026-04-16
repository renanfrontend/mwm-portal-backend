package com.mwm.bioplanta.service.agenda.validador;

import com.mwm.bioplanta.model.BioAgendaPlanejada;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.model.BioProdutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ProdutorAgendaPlanejadaValidadorTest {
    @Test
    void filtrarAgendasPorRegrasParticipacaoECertificado_remove_agenda_sem_participacao_ou_certificado() {
        // Semana de referência: 2026-03-18 (quarta)
        LocalDate referencia = LocalDate.of(2026, 3, 18);
        LocalDate domingo = referencia.with(java.time.DayOfWeek.SUNDAY);
        LocalDate sabado = domingo.plusDays(6);

        // Produtor 1: participa e certificado
        BioProdutor p1 = new BioProdutor();
        p1.setId(1L);
        p1.setDoamDejetos("S");
        p1.setCertificado("S");
        // Produtor 2: não participa
        BioProdutor p2 = new BioProdutor();
        p2.setId(2L);
        p2.setDoamDejetos("N");
        p2.setCertificado("S");
        // Produtor 3: não certificado
        BioProdutor p3 = new BioProdutor();
        p3.setId(3L);
        p3.setDoamDejetos("S");
        p3.setCertificado("N");

        BioEstabelecimento e1 = new BioEstabelecimento();
        e1.setId(101L);
        e1.setBioProdutor(p1);
        BioEstabelecimento e2 = new BioEstabelecimento();
        e2.setId(102L);
        e2.setBioProdutor(p2);
        BioEstabelecimento e3 = new BioEstabelecimento();
        e3.setId(103L);
        e3.setBioProdutor(p3);

        // Agenda: produtor 1 (válido), 2 (inválido), 3 (inválido)
        BioAgendaPlanejada a1 = new BioAgendaPlanejada();
        a1.setIdEstabelecimento(101L);
        a1.setDataAgendada(domingo.plusDays(1)); // semana atual
        BioAgendaPlanejada a2 = new BioAgendaPlanejada();
        a2.setIdEstabelecimento(102L);
        a2.setDataAgendada(domingo.plusDays(2)); // semana atual
        BioAgendaPlanejada a3 = new BioAgendaPlanejada();
        a3.setIdEstabelecimento(103L);
        a3.setDataAgendada(sabado.plusDays(7)); // semana futura
        BioAgendaPlanejada a4 = new BioAgendaPlanejada();
        a4.setIdEstabelecimento(102L);
        a4.setDataAgendada(domingo.minusDays(7)); // semana passada

        List<BioAgendaPlanejada> agendas = Arrays.asList(a1, a2, a3, a4);
        List<BioEstabelecimento> estabelecimentos = Arrays.asList(e1, e2, e3);

        List<BioAgendaPlanejada> filtrado = ProdutorAgendaPlanejadaValidador.filtrarAgendasPorRegrasParticipacaoECertificado(
                agendas, estabelecimentos, referencia);

        // Esperado: a1 (válido), a4 (semana passada, não filtra)
        Assertions.assertTrue(filtrado.contains(a1));
        Assertions.assertTrue(filtrado.contains(a4));
        Assertions.assertFalse(filtrado.contains(a2));
        Assertions.assertFalse(filtrado.contains(a3));
    }

    @Test
    void filtrarAgendasPorRegrasParticipacaoECertificado_nao_remove_se_todos_validos() {
        LocalDate referencia = LocalDate.of(2026, 3, 18);
        BioProdutor p1 = new BioProdutor();
        p1.setId(1L);
        p1.setDoamDejetos("S");
        p1.setCertificado("S");
        BioEstabelecimento e1 = new BioEstabelecimento();
        e1.setId(101L);
        e1.setBioProdutor(p1);
        BioAgendaPlanejada a1 = new BioAgendaPlanejada();
        a1.setIdEstabelecimento(101L);
        a1.setDataAgendada(referencia);
        List<BioAgendaPlanejada> agendas = Collections.singletonList(a1);
        List<BioEstabelecimento> estabelecimentos = Collections.singletonList(e1);
        List<BioAgendaPlanejada> filtrado = ProdutorAgendaPlanejadaValidador.filtrarAgendasPorRegrasParticipacaoECertificado(
                agendas, estabelecimentos, referencia);
        Assertions.assertEquals(1, filtrado.size());
        Assertions.assertTrue(filtrado.contains(a1));
    }
}
