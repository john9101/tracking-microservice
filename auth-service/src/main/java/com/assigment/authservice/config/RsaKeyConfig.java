package com.assigment.authservice.config;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Configuration
public class RsaKeyConfig {

    @Value("${jwt.public-key}")
    private String publicKey;

    @Value("${jwt.private-key}")
    private String privateKey;

//    @Bean
//    public KeyPair keyPair() throws NoSuchAlgorithmException, IOException {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        PemExporter.exportKeyToPem(keyPair);
//        return keyPair;
//    }

//    @Bean
//    public RSAKey rsaKey(KeyPair keyPair) {
//        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
//                .privateKey((RSAPrivateKey) keyPair.getPrivate())
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }

    @Bean
    public RSAKey rsaKey() {
        return new RSAKey
                .Builder(publicKey())
                .privateKey(privateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private RSAPublicKey publicKey() {
        try {
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse public key", e);
        }
    }

    public RSAPrivateKey privateKey() {
        try {
            byte[] decoded = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse private key", e);
        }
    }
}
