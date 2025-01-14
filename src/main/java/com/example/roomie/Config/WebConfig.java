package com.example.roomie.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // React 서버 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type", "Set-Cookie") // 허용할 헤더
                .exposedHeaders("Set-Cookie", "Authorization", "Content-Type") // 클라에서 읽을 수 있는 헤더
                .allowCredentials(true);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // React 서버 URL
//        corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
//        corsConfiguration.addAllowedHeader("*"); // 모든 헤더 허용
//        corsConfiguration.addExposedHeader("Auth"); // Access Token 헤더
//        corsConfiguration.addExposedHeader("Auth-refresh"); // Refresh Token 헤더
//        corsConfiguration.setAllowCredentials(true); // 인증 정보 포함 (쿠키, 헤더 등)
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }
}
