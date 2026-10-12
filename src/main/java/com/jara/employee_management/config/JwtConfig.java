package com.jara.employee_management.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

    @Bean
    KeyPair keyPair(
            @Value("${app.security.jwt.private-key-location}") Resource privateKeyResource,
            @Value("${app.security.jwt.public-key-location}") Resource publicKeyResource)
            throws IOException, GeneralSecurityException {

        RSAPrivateKey privateKey = loadPrivateKey(privateKeyResource);

        RSAPublicKey publicKey = loadPublicKey(publicKeyResource);

        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    JwtEncoder jwtEncoder(KeyPair keyPair) {

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return NimbusJwtEncoder
                .withKeyPair(publicKey, privateKey)
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(KeyPair keyPair) {

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(
                        "employee-management-api"));

        return decoder;
    }

    private RSAPrivateKey loadPrivateKey(Resource resource)
            throws IOException, GeneralSecurityException {

        byte[] keyBytes = readPem(resource);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        return (RSAPrivateKey) KeyFactory
                .getInstance("RSA")
                .generatePrivate(keySpec);
    }

    private RSAPublicKey loadPublicKey(Resource resource)
            throws IOException, GeneralSecurityException {

        byte[] keyBytes = readPem(resource);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        return (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(keySpec);
    }

    private byte[] readPem(Resource resource) throws IOException {

        String pem;

        try (InputStream input = resource.getInputStream()) {
            pem = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8);
        }

        String base64 = pem
                .replaceAll("-----BEGIN [A-Z ]+-----", "")
                .replaceAll("-----END [A-Z ]+-----", "")
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(base64);
    }
}

/*
 * import java.security.KeyPair;
 * import java.security.KeyPairGenerator;
 * import java.security.NoSuchAlgorithmException;
 * import java.security.interfaces.RSAPrivateKey;
 * import java.security.interfaces.RSAPublicKey;
 * 
 * import org.springframework.context.annotation.Bean;
 * import org.springframework.context.annotation.Configuration;
 * import org.springframework.security.oauth2.jwt.JwtDecoder;
 * import org.springframework.security.oauth2.jwt.JwtEncoder;
 * import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
 * import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
 * 
 * @Configuration
 * public class JwtConfig {
 * 
 * @Bean
 * KeyPair keyPair() throws NoSuchAlgorithmException {
 * 
 * KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
 * 
 * generator.initialize(2048);
 * 
 * return generator.generateKeyPair();
 * }
 * 
 * @Bean
 * JwtEncoder jwtEncoder(KeyPair keyPair) {
 * 
 * RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
 * 
 * RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
 * 
 * return NimbusJwtEncoder
 * .withKeyPair(publicKey, privateKey)
 * .build();
 * }
 * 
 * @Bean
 * JwtDecoder jwtDecoder(KeyPair keyPair) {
 * 
 * RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
 * 
 * return NimbusJwtDecoder
 * .withPublicKey(publicKey)
 * .build();
 * }
 * 
 * }
 */