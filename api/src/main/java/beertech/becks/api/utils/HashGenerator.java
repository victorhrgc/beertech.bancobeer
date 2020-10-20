package beertech.becks.api.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

public class HashGenerator {

    public static String generatorHash(String frase, String algoritmo) {
        try {
            MessageDigest md = getInstance(algoritmo);
            md.update(frase.getBytes());
            return stringHexa(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int highPart = ((bytes[i] >> 4) & 0xf) << 4;
            int lowPart = bytes[i] & 0xf;
            if (highPart == 0) s.append('0');
            s.append(Integer.toHexString(highPart | lowPart));
        }
        return s.toString();
    }

    public static String getStringHash(Long newIdCurrentBuilder) {
        return newIdCurrentBuilder.toString();
    }

}
