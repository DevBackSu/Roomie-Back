package com.example.roomie.Config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.key}")
    String key;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘 설정
        config.setKeyObtentionIterations("1000"); // 암호화 키 생성 반복 횟수 설정
        config.setPoolSize("1"); // 암호화에 사용할 스레드 풀 크기 설정
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // 암호화에서 생성된 문자열 출력 형식 지정
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
