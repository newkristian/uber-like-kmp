package com.example.uberapp_tim9.shared;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtUtils {
    private static final Gson gson = new Gson();

    /**
     * Decodifica un JWT y devuelve sus partes (header, payload y signature)
     */
    public static JwtParts decodeJWT(String jwt) throws Exception {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token JWT inválido");
        }

        return new JwtParts(
                decodeBase64(parts[0]),
                decodeBase64(parts[1]),
                parts[2]
        );
    }

    /**
     * Extrae los claims del payload del JWT
     */
    public static JsonObject getClaims(String jwt) throws Exception {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token JWT inválido");
        }

        String payloadJson = decodeBase64(parts[1]);
        return JsonParser.parseString(payloadJson).getAsJsonObject();
    }

    /**
     * Verifica si el JWT ha expirado
     */
    public static boolean isJWTExpired(String jwt) throws Exception {
        JsonObject claims = getClaims(jwt);

        if (!claims.has("exp")) {
            return false; // Si no tiene campo exp, se considera no expirado
        }

        long exp = claims.get("exp").getAsLong();
        Date expirationDate = new Date(exp * 1000); // Convertir a milisegundos
        return expirationDate.before(new Date());
    }

    /**
     * Obtiene un claim específico del JWT
     */
    public static <T> T getClaim(String jwt, String claimName, Class<T> classOfT) throws Exception {
        JsonObject claims = getClaims(jwt);
        return gson.fromJson(claims.get(claimName), classOfT);
    }

    private static String decodeBase64(String encodedString) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(encodedString, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    /**
     * Clase para contener las partes de un JWT decodificado
     */
    public static class JwtParts {
        public final String header;
        public final String payload;
        public final String signature;

        public JwtParts(String header, String payload, String signature) {
            this.header = header;
            this.payload = payload;
            this.signature = signature;
        }
    }
}