package com.example.roomie.Config;

import com.example.roomie.Auth.CustomOAuth2UserService;
import com.example.roomie.Auth.LoginFailureHandler;
import com.example.roomie.Auth.LoginSuccessHandler;
import com.example.roomie.Entity.Role;
import com.example.roomie.JWT.JwtAuthenticationProcessingFilter;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity  // spring security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final LoginSuccessHandler OAuthloginSuccessHandler;
    private final LoginFailureHandler OAuthloginFailureHandler;
    private final CustomOAuth2UserService customOAuth2Service;

    // 이전에는 WebSecurityConfigurerAdatpter를 상속 받아 Override 했으나 이제는 @Bean으로 빈을 등록해서 컨테이너가 관리하도록 사용 가능
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .cors(AbstractHttpConfigurer::disable) // CORS 문제를 해결하기 위해 설정 추가
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 시큐리티에서 제공하는 기본 로그인 form 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼을 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 사용하지 않음
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // H2 콘솔에 대해 CSRF 비활성화
                .csrf(csrf -> csrf.ignoringRequestMatchers("/swagger-ui/**")) // 스웨거에 대해 CSRF 비활성화?

                // CSRF 보호 비활성화 (필요에 따라 설정 가능)
                .csrf(AbstractHttpConfigurer::disable)

                // 보안 헤더 비활성화 (필요에 따라 설정 가능)
//                .headers(headers -> headers.frameOptions().disable()) // 예: H2 콘솔 사용 시 필요
//                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // 최신 방식으로 frameOptions 설정
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // 최신 방식으로 frameOptions 설정

                // 세션을 사용하지 않음 (JWT 기반 인증을 사용할 경우 상태 없음 모드로 설정)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL별로 접근 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console", "/h2-console/**").permitAll() // 특정 리소스에 대한 접근 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Swagger 관련 경로 허용
                        .requestMatchers("/api/**").permitAll() // 기본 api 호출 시 인증 X
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated() // 나머지 요청은 인증이 필요
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(OAuthloginSuccessHandler) // 로그인 성공 시 처리할 핸들러
                        .failureHandler(OAuthloginFailureHandler) // 로그인 실패 시 처리할 핸들러
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2Service)) // 사용자 정보 서비스 설정
                );

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // React 서버 URL
        corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        corsConfiguration.addAllowedHeader("*"); // 모든 헤더 허용
        corsConfiguration.setAllowCredentials(true); // 인증 정보 포함 (쿠키, 헤더 등)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 주소 요청에 위 설정 적용
        return source;
    }

    /**
     * Jwt 인증 처리를 담당하는 JWT 인증 필터를 빈으로 등록함
     *
     * @return
     */
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }

}
