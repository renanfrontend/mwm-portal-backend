package com.mwm.bioplanta.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PortariaEntregaDejetosDTO {
    // Dados principais do registro
    private String tipoRegistro;
    private String data_entrada;
    private String hora_entrada;
    private String data_saida;
    private String hora_saida;
    private String observacoes;
    private String status;
    private String origem_entrada;

    // Objeto entrega_dejetos (aninhado)
    private EntregaDejetosDTO entrega_dejetos;

      @Data
      public static class EntregaDejetosDTO {
          private String id;
          private String produtor_id;
          private String motorista_nome;
          private String cpf_motorista;
          private String motorista_id;
          private String transportadora_id;
          private String transportadora_manual;
          private String veiculo_id;
          private String placa;
          private String placa_manual;
          private String tipo_veiculo;
          private Integer peso_inicial;
          private Integer peso_final;
          private String densidade;
      }
}
