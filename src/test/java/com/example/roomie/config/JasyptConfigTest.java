package com.example.roomie.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptConfigTest {

    @Test
    void jasypt() {
        String secret = "ansumin";

        System.out.println(secret + " : " + jasyptEncoding(secret));
    }

    private String jasyptEncoding(String secret) {
        String key = "roomie";
        StandardPBEStringEncryptor pbeEncry = new StandardPBEStringEncryptor();
        pbeEncry.setAlgorithm("PBEWithMD5AndDES");
        pbeEncry.setPassword(key);
        return pbeEncry.encrypt(secret);
    }

}