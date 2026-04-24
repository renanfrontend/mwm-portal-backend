package com.mwm.bioplanta.util;

import java.util.Random;

public class CnpjUtil {
    
    public static String gerarCNPJUnico() {
        Random random = new Random();
        int parte1 = random.nextInt(100);
        int parte2 = random.nextInt(1000);
        int parte3 = random.nextInt(1000);
        int parte4 = random.nextInt(10000);
        int parte5 = random.nextInt(100);
        
        return String.format("%02d.%03d.%03d/%04d-%02d", parte1, parte2, parte3, parte4, parte5);
    }
}