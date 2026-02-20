package com.mwm.bioplanta.util;

import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET = "bioplanta_secret";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    public static String generateToken(String subject) {
        long now = System.currentTimeMillis();
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", subject, now / 1000, (now + EXPIRATION) / 1000);
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
        String signature = hmacSha256(header + "." + payloadB64, SECRET);
        return header + "." + payloadB64 + "." + signature;
    }

    private static String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao assinar JWT", e);
        }
    }
}
