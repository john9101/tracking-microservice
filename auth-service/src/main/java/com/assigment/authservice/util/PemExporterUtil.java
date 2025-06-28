package com.assigment.authservice.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class PemExporterUtil {
    public static void exportKeyToPem(KeyPair keyPair) throws IOException {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String encodedPublicKey = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded());
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String encodedPrivateKey = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded());


        try (FileWriter writer = new FileWriter(Paths.get("public.pem").toFile())) {
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(encodedPublicKey);
            writer.write("\n-----END PUBLIC KEY-----\n");
        }

        try (FileWriter writer = new FileWriter(Paths.get("private.pem").toFile())) {
            writer.write("-----BEGIN PRIVATE KEY-----\n");
            writer.write(encodedPrivateKey);
            writer.write("\n-----END PRIVATE KEY-----\n");
        }
    }
}
