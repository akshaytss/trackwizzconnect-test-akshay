package app.trackwizz.connect.util;

import app.trackwizz.connect.constant.KeyLoaderConstants;
import app.trackwizz.connect.constant.common.MessageConstants;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class KeyLoader {
    private static String getPrivateKey() {
        try {
            return new String(
                    Objects.requireNonNull(KeyLoader.class.getClassLoader().getResourceAsStream(KeyLoaderConstants.PRIVATE_KEY_FILENAME)).readAllBytes()
            );
        } catch (Exception e) {
            return "";
        }
    }

    private static String getPublicKey() {
        try {
            return new String(
                    Objects.requireNonNull(KeyLoader.class.getClassLoader().getResourceAsStream(KeyLoaderConstants.PUBLIC_KEY_FILENAME)).readAllBytes()
            );
        } catch (Exception e) {
            return "";
        }
    }

    public static PrivateKey loadPrivateKey() {
        try {
            String privateKeyContent = getPrivateKey();
            String keyContent = privateKeyContent
                    .replace(KeyLoaderConstants.BEGIN_PRIVATE_KEY, MessageConstants.EMPTY)
                    .replace(KeyLoaderConstants.END_PRIVATE_KEY, MessageConstants.EMPTY)
                    .replaceAll("\\s", MessageConstants.EMPTY);
            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyLoaderConstants.KEY_ALGORITHM);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            return null;
        }
    }

    public static PublicKey loadPublicKey() {
        try {
            String publicKeyContent = getPublicKey();
            String keyContent = publicKeyContent
                    .replace(KeyLoaderConstants.BEGIN_PUBLIC_KEY, MessageConstants.EMPTY)
                    .replace(KeyLoaderConstants.END_PUBLIC_KEY, MessageConstants.EMPTY)
                    .replaceAll("\\s", MessageConstants.EMPTY);
            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyLoaderConstants.KEY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            return null;
        }
    }
}
