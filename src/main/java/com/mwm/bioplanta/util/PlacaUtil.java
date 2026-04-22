package com.mwm.bioplanta.util;

public class PlacaUtil {
    /**
     * Formata uma placa Mercosul (ex: ABC1D23 para ABC-1D23) se possível.
     * Se já estiver formatada, retorna igual. Se não for padrão, retorna original.
     */
    public static String formatarPlacaMercosul(String placa) {
        if (placa == null) return null;
        String p = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (p.matches("[A-Z]{3}[0-9][A-Z][0-9]{2}")) {
            // ABC1D23 → ABC-1D23
            return p.substring(0,3) + "-" + p.substring(3,4) + p.substring(4,5) + p.substring(5);
        }
        if (p.matches("[A-Z]{3}[0-9]{4}")) {
            // ABC1234 → ABC-1234
            return p.substring(0,3) + "-" + p.substring(3);
        }
        // Se já tem hífen ou não bate padrão, retorna original
        return placa.trim();
    }
}
