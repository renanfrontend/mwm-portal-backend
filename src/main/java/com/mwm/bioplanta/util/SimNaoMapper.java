package com.mwm.bioplanta.util;

public final class SimNaoMapper {

    private SimNaoMapper() {
    }

    public static String toDescricao(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }

        return switch (valor.trim().toUpperCase()) {
            case "S" -> "Sim";
            case "N" -> "Não";
            default -> "";
        };
    }

    public static String toBanco(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return switch (valor.trim().toUpperCase()) {
            case "SIM", "S" -> "S";
            case "NÃO", "NAO", "N" -> "N";
            default -> null;
        };
    }
}
